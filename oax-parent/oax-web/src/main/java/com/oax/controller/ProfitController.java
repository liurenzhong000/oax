package com.oax.controller;

import com.google.gson.JsonParser;
import com.oax.Constant;
import com.oax.common.ResultResponse;

import com.oax.entity.front.*;
import com.oax.exception.VoException;

import com.oax.service.*;


import com.oax.vo.RushToBuyVO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/lockPosition")
public class ProfitController {
    private final Logger mLogger = LoggerFactory.getLogger(ProfitController.class);

    @Autowired
    private I18nMessageService I18nMessageService;

    @Autowired
    private UserCoinService userCoinService;


    @Autowired
    private MovesayMoneyActiveService movesayMoneyActiveService;

    @Autowired
    private MovesayMoneyActiveListService movesayMoneyActiveListService;


    @Autowired
    private MoveOperateLogService moveOperateLogService;

    @Autowired
    private  PromoteFundService promoteFundService;



    /**
     * 余利宝首页
     *
     * @param
     * @param request
     * @return
     */
    /**/
    @RequestMapping("/index/list")
    public ResultResponse getIndex(HttpServletRequest request) throws VoException {
        String lang = request.getHeader(Constant.api_header_lang);
        List<MovesayMoneyActive> movesayMoneyActive = new ArrayList<MovesayMoneyActive>();
        try {
            movesayMoneyActive = movesayMoneyActiveService.selectAllRecord();
            if (movesayMoneyActive == null)
                return new ResultResponse(false, "无法获取活动");

        } catch (Exception e) {
            return new ResultResponse(false, I18nMessageService.getMsg(10101, lang));
        }
        return new ResultResponse(true, movesayMoneyActive);
    }

    /**
     * 个人币信息
     *
     * @param vo
     * @param request
     * @return
     */
    /**/
    @RequestMapping("/personalCurrency")
    public ResultResponse getpersonalCurrency(@RequestBody String vo, HttpServletRequest request) throws VoException {
        String lang = request.getHeader(Constant.api_header_lang);
        String userId = request.getHeader(Constant.api_header_userId);
        if (StringUtils.isBlank(vo)) {
            return new ResultResponse(false, I18nMessageService.getMsg(10102, lang));
        }
        String activeId = new JsonParser().parse(vo).getAsJsonObject().get("activeId").getAsString();

        BigDecimal userMoney = new BigDecimal("0");
        BigDecimal balance = new BigDecimal("0");
        Map<String, Object> map = new HashMap();
        List<MovesayMoneyActiveList> movesayMoneyActiveLists = new ArrayList<MovesayMoneyActiveList>();
        try {
            MovesayMoneyActive movesayMoneyActive = movesayMoneyActiveService.selectByPrimaryKey(Integer.parseInt(activeId));
            if (movesayMoneyActive == null)
                return new ResultResponse(false, "活动id不存在");
            UserCoin userCoin = userCoinService.queryBalanceInfoByUserId(Integer.parseInt(userId), movesayMoneyActive.getMarketId());
            if (userCoin != null) {
                balance = userCoin.getBanlance();
            }
            movesayMoneyActiveLists = movesayMoneyActiveListService.selectRecordByUserId(Integer.parseInt(activeId), Integer.parseInt(userId));

            for (int i = 0; i < movesayMoneyActiveLists.size(); i++) {
                userMoney = userMoney.add(movesayMoneyActiveLists.get(i).getJoinMoney());
            }
            /*活动已锁额度*/
            map.put("join_money", movesayMoneyActive.getJoinMoney());
            /*活动总额度*/
            map.put("money", movesayMoneyActive.getMoney());
            /*剩余额度*/
            map.put("limit_remain", movesayMoneyActive.getMoney().subtract(movesayMoneyActive.getJoinMoney()));
            /*个人已抢购额度*/
            map.put("PurchaseAmount", userMoney);

            /*个人剩余抢购额度*/
            map.put("limit_min", movesayMoneyActive.getLimitMax().subtract(userMoney));

            /*账户余额*/
            map.put("accountBalance", balance);

            map.put("join_num", movesayMoneyActive.getJoinNum());

        } catch (Exception e) {
            return new ResultResponse(false, I18nMessageService.getMsg(10101, lang));
        }
        return new ResultResponse(true, map);
    }


    /**
     * 静态余利宝立即抢购
     *
     * @param
     * @param request
     * @return
     */
    /**/
    @RequestMapping("/rushToBuy")
    public ResultResponse rushToBuy(@RequestBody @Valid RushToBuyVO vo, BindingResult results, HttpServletRequest request) throws VoException {
        String lang = request.getHeader(Constant.api_header_lang);
        if (results.hasErrors())
            return new ResultResponse(false, results.getFieldError().getDefaultMessage());
        BigDecimal userMoney = new BigDecimal("0");
        try {
            MovesayMoneyActive movesayMoneyActive = movesayMoneyActiveService.selectByPrimaryKey(vo.getActiveId());

            if (movesayMoneyActive == null) {
                return new ResultResponse(false, "该活动不存在");
            }

            if (vo.getJoinMoney().compareTo(movesayMoneyActive.getLimitMin()) == -1) {
                return new ResultResponse(false, "输入额度小于目前个人起购额度");
            }

            /*个人抢购上限额度*/
            BigDecimal limit_max = movesayMoneyActive.getLimitMax();

            List<MovesayMoneyActiveList> movesayMoneyActiveLists = movesayMoneyActiveListService.selectRecordByUserId(vo.getActiveId(), vo.getUserId());

            /*个人参与金额*/
            for (int i = 0; i < movesayMoneyActiveLists.size(); i++) {
                userMoney = userMoney.add(movesayMoneyActiveLists.get(i).getJoinMoney());
            }

            if (userMoney.compareTo(limit_max) == 0 || userMoney.compareTo(limit_max) == 1) {
                return new ResultResponse(false, "个人抢购额度已达上限");
            }

            /*个人抢购剩余额度*/
            BigDecimal remainMoney = limit_max.subtract(userMoney);

            if (vo.getJoinMoney().compareTo(remainMoney) == 1) {
                return new ResultResponse(false, "输入额度大于目前个人可抢购额度");
            }

            if (movesayMoneyActive.getJoinMoney().compareTo(movesayMoneyActive.getMoney()) == 0 || movesayMoneyActive.getJoinMoney().compareTo(movesayMoneyActive.getMoney()) == 1) {
                return new ResultResponse(false, "项目总额度已达上限");
            }

            /*活动抢购剩余额度*/
            BigDecimal remainActive = movesayMoneyActive.getMoney().subtract(movesayMoneyActive.getJoinMoney());

            if (vo.getJoinMoney().compareTo(remainActive) == 1) {
                return new ResultResponse(false, "输入额度大于目前项目可抢购额度");
            }

            UserCoin userCoin = userCoinService.queryBalanceInfoByUserId(vo.getUserId(), movesayMoneyActive.getMarketId());

            if (userCoin == null) {
                return new ResultResponse(false, "用户资产账户不存在");
            }
            /*检验账户余额*/
            if (vo.getJoinMoney().compareTo(userCoin.getBanlance()) == 1) {
                return new ResultResponse(false, I18nMessageService.getMsg(10041, lang));
            }

            movesayMoneyActiveListService.insertStaticMovesayMoney(vo);

        } catch (Exception e) {
            return new ResultResponse(false, I18nMessageService.getMsg(10101, lang));
        }
        return new ResultResponse(true, "抢购成功");

    }

    /**
     * 锁仓记录
     *
     * @param
     * @param request
     * @return
     */
    /**/
    @RequestMapping("/List")
    public ResultResponse getList(HttpServletRequest request) throws VoException {
        String lang = request.getHeader(Constant.api_header_lang);
        String userId = request.getHeader(Constant.api_header_userId);
        List<MovesayMoneyActiveList> movesayMoneyActiveLists = new ArrayList<MovesayMoneyActiveList>();
        try {
            movesayMoneyActiveLists = movesayMoneyActiveListService.selectRecordById(Integer.parseInt(userId));
            if (movesayMoneyActiveLists == null)
                return new ResultResponse(false, "该用户暂无锁仓记录");

        } catch (Exception e) {
            return new ResultResponse(false, I18nMessageService.getMsg(10101, lang));
        }
        return new ResultResponse(true, movesayMoneyActiveLists);
    }

    /**
     * 收益记录
     *
     * @param
     * @param request
     * @return
     */
    /**/
    @RequestMapping("/incomeList")
    public ResultResponse getIncomeList(HttpServletRequest request) throws VoException {
        String lang = request.getHeader(Constant.api_header_lang);
        String userId= request.getHeader(Constant.api_header_userId);
        List<MovesayOperateLog> moneyActiveLists = new ArrayList<MovesayOperateLog>();
        try {
            moneyActiveLists = moveOperateLogService.getIncomList(userId);
            if (moneyActiveLists == null)
                return new ResultResponse(false, "该用户暂无收益记录");

        } catch (Exception e) {
            return new ResultResponse(false, I18nMessageService.getMsg(10101, lang));
        }
        return new ResultResponse(true, moneyActiveLists);
    }

    /**
     * 推广收益
     *
     * @param
     * @param request
     * @return
     */
    /**/
    @RequestMapping("/promoteList")
    public ResultResponse promoteList(HttpServletRequest request) throws VoException {
        String lang = request.getHeader(Constant.api_header_lang);
        String userId = request.getHeader(Constant.api_header_userId);
        List<PromoteProfit> promoteProfits = new ArrayList< PromoteProfit>();
        try {
            promoteProfits =promoteFundService.getPromoteList(userId);
            if (promoteProfits == null)
                return new ResultResponse(false, "该用户暂无推广收益记录");

        } catch (Exception e) {
            return new ResultResponse(false, I18nMessageService.getMsg(10101, lang));
        }
        return new ResultResponse(true, promoteProfits);
    }


    /**
     * 动态余利宝立即抢购
     *
     * @param
     * @param request
     * @return
     */
    /**/
    @RequestMapping("/activeToBuy")
    public ResultResponse activeToBuy(@RequestBody @Valid RushToBuyVO vo, BindingResult results, HttpServletRequest request) throws VoException {
        String lang = request.getHeader(Constant.api_header_lang);
        BigDecimal userMoney = new BigDecimal("0");
        if (results.hasErrors())
            return new ResultResponse(false, results.getFieldError().getDefaultMessage());

        try {
            MovesayMoneyActive movesayMoneyActive = movesayMoneyActiveService.selectByPrimaryKey(vo.getActiveId());

            List<MovesayMoneyActiveList> movesayMoneyActiveLists = movesayMoneyActiveListService.selectRecordByUserId(vo.getActiveId(), vo.getUserId());

            /*个人抢购上限额度*/
            BigDecimal limit_max = movesayMoneyActive.getLimitMax();

            if (movesayMoneyActive == null) {
                return new ResultResponse(false, "该活动不存在");
            }

            if (vo.getJoinMoney().compareTo(movesayMoneyActive.getLimitMin()) == -1) {
                return new ResultResponse(false, "输入额度小于目前个人起购额度");
            }

            /*个人参与金额*/
            for (int i = 0; i < movesayMoneyActiveLists.size(); i++) {
                userMoney = userMoney.add(movesayMoneyActiveLists.get(i).getJoinMoney());
            }

            if (vo.getJoinMoney().add(userMoney).compareTo(limit_max) == 1) {
                return new ResultResponse(false, "个人抢购额度已达上限");
            }

            UserCoin userCoin = userCoinService.queryBalanceInfoByUserId(vo.getUserId(), movesayMoneyActive.getMarketId());

            if (userCoin == null) {
                return new ResultResponse(false, "用户资产账户不存在");
            }

            /*检验账户余额*/
            if (vo.getJoinMoney().compareTo(userCoin.getBanlance()) == 1) {
                return new ResultResponse(false, I18nMessageService.getMsg(10041, lang));
            }

            movesayMoneyActiveListService.insertActiveMovesayMoney(vo);

        } catch (Exception e) {
            return new ResultResponse(false, I18nMessageService.getMsg(10101, lang));
        }
        return new ResultResponse(true, "抢购成功");
    }


    /**
     * 复投
     *
     * @param
     * @param request
     * @return
     */
    /**/
    @RequestMapping("/repeatBuy")
    public ResultResponse activeToBuy(@RequestBody String vo, BindingResult results, HttpServletRequest request) throws VoException {
        String lang = request.getHeader(Constant.api_header_lang);
        if (results.hasErrors())
            return new ResultResponse(false, results.getFieldError().getDefaultMessage());

        if (StringUtils.isBlank(vo)) {
            return new ResultResponse(false, I18nMessageService.getMsg(10102, lang));
        }
        String num = new JsonParser().parse(vo).getAsJsonObject().get("num").getAsString();
        String id = new JsonParser().parse(vo).getAsJsonObject().get("id").getAsString();

        try {
            MovesayMoneyActiveList movesayMoneyActiveList = movesayMoneyActiveListService.selectByPrimaryKey(id);

            if(StringUtils.isEmpty(num)|| Double.parseDouble(num)==0||new BigDecimal(num).compareTo(movesayMoneyActiveList.getProfitsMoney())==1)
            {
                return new ResultResponse(false, "可复锁额度不足");
            }

            movesayMoneyActiveListService.repeatMovesayMoneyList(id,num);

        } catch (Exception e) {
            return new ResultResponse(false, I18nMessageService.getMsg(10101, lang));
        }
        return new ResultResponse(true, "复锁收益成功");
    }

    /**
     * 提现
     *
     * @param
     * @param request
     * @return
     */
    /**/
    @RequestMapping("/deposit")
    public ResultResponse deposit(@RequestBody String vo, BindingResult results, HttpServletRequest request) throws VoException {
        String lang = request.getHeader(Constant.api_header_lang);
        String userId = request.getHeader(Constant.api_header_userId);
        if (results.hasErrors())
            return new ResultResponse(false, results.getFieldError().getDefaultMessage());

        if (StringUtils.isBlank(vo)) {
            return new ResultResponse(false, I18nMessageService.getMsg(10102, lang));
        }

        String num = new JsonParser().parse(vo).getAsJsonObject().get("num").getAsString();
        String id = new JsonParser().parse(vo).getAsJsonObject().get("id").getAsString();


        try {
            MovesayMoneyActiveList movesayMoneyActiveList = movesayMoneyActiveListService.selectByPrimaryKey(id);
            MovesayMoneyActive     movesayMoneyActive =movesayMoneyActiveService.selectByPrimaryKey(movesayMoneyActiveList.getActiveId());

            if (new BigDecimal(num).compareTo(movesayMoneyActive.getDepositMoney())==-1)
            {
                return new ResultResponse(false, "可提现额度不足");
            }

            if (new BigDecimal(num).compareTo(movesayMoneyActiveList.getProfitsMoney())==1)
            {
                return new ResultResponse(true, "提现金额大于目前可提现额度");
            }

            movesayMoneyActiveListService.updateDepositRecord(id,num);

        } catch (Exception e) {
            return new ResultResponse(false, I18nMessageService.getMsg(10101, lang));
        }
        return new ResultResponse(true, "提现成功");
    }




}

