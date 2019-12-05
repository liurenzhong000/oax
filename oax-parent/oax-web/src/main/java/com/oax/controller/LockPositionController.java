package com.oax.controller;


import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.oax.service.CommonCheckService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.oax.common.ResultResponse;
import com.oax.entity.front.CoinLockConfig;
import com.oax.service.CoinLockConfigService;
import com.oax.service.I18nMessageService;
import com.oax.service.LockPositionService;
import com.oax.vo.CoinLockConfigVO;
import com.oax.vo.LockPositionRecordVO;
import com.oax.vo.LockPositionVO;

@RestController
@RequestMapping("lockPosition")
public class LockPositionController {
    @Autowired
    private CoinLockConfigService coinLockConfigService;
    @Autowired
    private LockPositionService lockPositionService;
    @Autowired
    private I18nMessageService msgService;
    @Autowired
    private CommonCheckService commonCheckService;

    @GetMapping("index")
    public ResultResponse index(){
        Map<String, Object> coinLockConfig = coinLockConfigService.getCoinLockConfig();
        return new ResultResponse(true, coinLockConfig);
    }

    @PostMapping("initLockPositionInfo")
    public ResultResponse getLockPositionInfo(@RequestBody CoinLockConfigVO vo , HttpServletRequest request){
        Integer userId = null;
        if (StringUtils.isNotEmpty(request.getHeader("userId"))){
            userId = Integer.parseInt(request.getHeader("userId"));
        };
        Map<String,Object> map = new HashMap<String, Object>();
        CoinLockConfig coinLockConfig = lockPositionService.selectCoinLockConfigById(vo.getId());
        map.put("coinLockConfig", coinLockConfig);
        if (userId!=null){
            BigDecimal banlance = lockPositionService.getUserCoinByCoinId(userId,vo.getCoinId());
            map.put("banlance", banlance==null?0:banlance);
        }else{
            map.put("banlance", 0);
        }
        //计息时间
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        String date = new SimpleDateFormat("yyyy-MM-dd 00:00:00").format(calendar.getTime());
        map.put("interestTime", date);
        List<Map<String,Object>> marketList = lockPositionService.getMarketInfoList(vo.getCoinId());
        map.put("marketList", marketList);
        return new ResultResponse(true, map);
    }

    //用户锁仓
    @PostMapping("lock")
    public ResultResponse save(@RequestBody LockPositionVO vo,@RequestHeader Integer userId,@RequestHeader String lang){
        vo.setUserId(userId);
        commonCheckService.checkFreezing(userId);
        String msg = null;
        //校验锁仓数量必须大于0
        if (vo.getLockQty()==null||vo.getLockQty().compareTo(BigDecimal.ZERO)<=0){
            msg = msgService.getMsg(10052, lang);
            return new ResultResponse(false, msg);
        }
        //锁仓周期必须大于0
        if (vo.getLockDays()==null||vo.getLockDays()<1){
            msg = msgService.getMsg(10080, lang);
            return new ResultResponse(false, msg);
        }
        //校验是否不小于基准锁仓数量
        CoinLockConfig coinLockConfig = lockPositionService.selectCoinLockConfigByCoinId(vo.getCoinId());
        if (coinLockConfig.getStandardLockQty()==null||vo.getLockQty().compareTo(coinLockConfig.getStandardLockQty())<0){
            msg = msgService.getMsg(10079, lang);
            return new ResultResponse(false, msg);
        }
        //校验锁仓数量小数位是否超过8位
        String lockedQty = vo.getLockQty().toString();
        if (lockedQty.indexOf(".")>-1){
            int index = lockedQty.indexOf(".");
            lockedQty = lockedQty.substring(index+1);
            if (lockedQty.length()>8){
                msg = msgService.getMsg(10083, lang);
                return new ResultResponse(false, msg);
            }
        }
        //校验资产是否余额不足
        BigDecimal banlance = lockPositionService.getUserCoinByCoinId(userId,vo.getCoinId());
        if (banlance==null||banlance.compareTo(vo.getLockQty())<0){
            msg = msgService.getMsg(10044, lang);
            return new ResultResponse(false, msg);
        }
        boolean flag = lockPositionService.lock(vo);
        if (flag){
            msg = msgService.getMsg(10078, lang);
        }else{
            msg = msgService.getMsg(10100, lang);
        }
        return new ResultResponse(flag, msg);
    }

    @GetMapping("/declock/{id}")
    public ResultResponse declock(@PathVariable(name = "id") Integer id,@RequestHeader Integer userId,@RequestHeader String lang){
        boolean flag = lockPositionService.declock(id,userId);
        String msg = null;
        if (flag){
            msg = msgService.getMsg(10081, lang);
        }else{
            msg = msgService.getMsg(10082, lang);
        }
        return new ResultResponse(flag, msg);
    }

    @PostMapping("page")
    public ResultResponse getPage(@RequestBody LockPositionRecordVO vo,@RequestHeader Integer userId){
        vo.setUserId(userId);
        PageInfo<?> pageInfo = lockPositionService.getPage(vo);
        return new ResultResponse(true, pageInfo);
    }

    @GetMapping("/lockCoin/init")
    public ResultResponse init(){
        List<CoinLockConfig> list = coinLockConfigService.selectAll();
        return new ResultResponse(true, list);
    }

    @GetMapping("/interest/{lockPositionId}")
    public ResultResponse getInterestSharebonusList(@PathVariable(name = "lockPositionId") Integer lockPositionId){
        List<Map<String,Object>> list = lockPositionService.getInterestSharebonusList(lockPositionId);
        return new ResultResponse(true, list);
    }

}
