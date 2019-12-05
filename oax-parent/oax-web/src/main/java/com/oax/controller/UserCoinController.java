package com.oax.controller;


import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.xml.ws.Binding;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oax.common.AssertHelper;
import com.oax.common.EmptyHelper;
import com.oax.common.RedisUtil;
import com.oax.common.constant.RedisKeyConstant;
import com.oax.common.json.JsonHelper;
import com.oax.common.json.TypeReferences;
import com.oax.common.AccessLimit;
import com.oax.context.HttpContext;
import com.oax.entity.admin.param.PageParam;
import com.oax.entity.front.*;
import com.oax.service.IBonusService;
import com.oax.service.UserCoinLeaderService;
import com.oax.vo.BonusVO;
import com.sun.org.apache.xerces.internal.util.EntityResolverWrapper;
import jdk.nashorn.internal.ir.RuntimeNode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.oax.Constant;
import com.oax.common.ResultResponse;
import com.oax.entity.admin.vo.MarketCoinVo;
import com.oax.exception.VoException;
import com.oax.exception.VoVailder;
import com.oax.service.I18nMessageService;
import com.oax.service.UserCoinService;
import com.oax.vo.CoinListVO;
import com.oax.vo.WithdrawalVO;

@RestController
@RequestMapping("/userCoin")
public class UserCoinController {
	@Autowired
	private UserCoinService userCoinService;
	
	@Autowired
	private I18nMessageService I18nMessageService;

	@Autowired
	private IBonusService bonusService;

	@Autowired
	private UserCoinLeaderService userCoinLeaderService;

	@Autowired
	private RedisUtil redisUtil;

	private final String BAD_USER_KEY = "bad_user_list";

	private final String USER_COIN_KEY = "user_coin:";

	private String getUserCoinKey(String userId) {
		return USER_COIN_KEY + userId;
	}
	
	/**
	 * 用户资产列表
	 * @return
	 * @throws VoException
	 */
	@RequestMapping("/propertyList")
//	@AccessLimit(limit = 4,sec = 15)
	public ResultResponse propertyList(@RequestBody @Valid CoinListVO vo, BindingResult result,HttpServletRequest request) throws VoException {
		String lang = request.getHeader(Constant.api_header_lang);
		String userId = request.getHeader(Constant.api_header_userId);
		VoVailder.valid(result);
		boolean success = true;

		List<Integer> badUserList = redisUtil.getList(BAD_USER_KEY, Integer.class);
		if (badUserList != null && badUserList.contains(Integer.valueOf(userId))){
			AssertHelper.isTrue(false, "系统异常，请稍后重试");
		}
		Object data = null;
//		try {
			vo.setUserId(Integer.parseInt(userId));
//			String jsonStr = redisUtil.getString(getUserCoinKey(userId));
////			Map<String, Object> coinMap;
////			if (!EmptyHelper.isEmpty(jsonStr)) {
////				coinMap = JsonHelper.readValue(jsonStr, TypeReferences.REF_MAP_OBJECT);
////			} else {
////				coinMap = userCoinService.coinList(vo);
////				//缓存1分钟
////				redisUtil.setString(getUserCoinKey(userId), JsonHelper.writeValueAsString(coinMap), 60);
////			}
////			data=coinMap;
			data = userCoinService.coinListNew(vo);
//		} catch (Exception e) {
//			return new ResultResponse(false, I18nMessageService.getMsg(10101, lang));
//		}
		return new ResultResponse(success, data);
	}

	/**
	 * 充值
	 * @return
	 * @throws VoException
	 */
	@RequestMapping("/recharge/{coinId}")
	public ResultResponse recharge(@PathVariable("coinId") Integer coinId,HttpServletRequest request) throws VoException {
		String userId = request.getHeader(Constant.api_header_userId);
		String lang = request.getHeader(Constant.api_header_lang);
	
		Map<String, Object> map=new HashMap<>();
		String address = userCoinService.getAddressQRBarcode(userId,coinId,lang);
		map.put("address", address);
		
		if(StringUtils.isNotBlank(address)) {
			return new ResultResponse(true, map);
		}else {
			return new ResultResponse(false,I18nMessageService.getMsg(10101, lang));
		}
	}

	/**
	 * 提现之前调用，判断是否绑定身份证
	 */
	@GetMapping("/beforeWithdrawal")
	public ResultResponse beforeWithdrawal(HttpServletRequest request){
		Integer userId = Integer.valueOf(request.getHeader(Constant.api_header_userId));
		Map<String, Object> data = userCoinService.beforeWithdrawal(userId);
		return new ResultResponse(true, data);
	}

	@RequestMapping("/withdrawal")
	public ResultResponse withdrawal(@RequestBody @Valid WithdrawalVO vo, BindingResult result,HttpServletRequest request) throws VoException {
		Integer userId = HttpContext.getUserId();
		boolean lock = redisUtil.tryLock(RedisKeyConstant.LOCK_WITHDRAW + userId);
		if (lock) {
			try {
				return withdrawalWithLock(vo, result, request);
			} finally {
				redisUtil.unLock(RedisKeyConstant.LOCK_WITHDRAW + userId);
			}
		} else {
			throw new IllegalArgumentException("重复提交！");
		}
	}
	/**
	 * 提现
	 * @return
	 * @throws VoException
	 */
	private ResultResponse withdrawalWithLock(@RequestBody @Valid WithdrawalVO vo, BindingResult result,HttpServletRequest request) throws VoException {
		String lang = request.getHeader(Constant.api_header_lang);
		String userId = request.getHeader(Constant.api_header_userId);
		
		VoVailder.valid(result);
		String reg="^0x([a-z]|[A-Z]|[0-9]){40}";       
        CoinWithBLOBs coin =userCoinService.selectCoinById(vo.getCoinId());
        
        if(coin.getType()==2||coin.getType()==4) {
        	if(vo.getAddress().length()<26&&vo.getAddress().length()>35) {
        		return new ResultResponse(false, I18nMessageService.getMsg(10023, lang));
        	}
        }else if(coin.getType()==1||coin.getType()==3){
        	if(!vo.getAddress().matches(reg)) {
        		return new ResultResponse(false, I18nMessageService.getMsg(10023, lang));
        	}
        }
        
		Withdraw withdraw=new Withdraw();
		withdraw.setUserId(Integer.parseInt(userId));
		withdraw.setCoinId(vo.getCoinId());
		withdraw.setToAddress(vo.getAddress());
		withdraw.setQty(vo.getQty().setScale(8, BigDecimal.ROUND_DOWN));
		withdraw.setStatus((byte)0);
		withdraw.setCreateTime(new Date());
		withdraw.setUpdateTime(new Date());
		
		int counts=userCoinService.withdrawal(withdraw,lang);			
		if(counts>0) {
			return new ResultResponse(true, I18nMessageService.getMsg(10053, lang));
		}else {
			return new ResultResponse(false, I18nMessageService.getMsg(10101, lang));
		}
		
	}
	
	/**
	 * 交易
	 * @return
	 * @throws VoException
	 */
	@RequestMapping("/toTrade/{coinId}")
	public ResultResponse toTrade(@PathVariable("coinId") Integer coinId,HttpServletRequest request) throws VoException {
		String lang = request.getHeader(Constant.api_header_lang);
		
		boolean success = true;
		Object data = null;
		try {
			List<Map<String, Object>> list=userCoinService.toTrade(coinId);		
			data=list;
		} catch (Exception e) {
			return new ResultResponse(false, I18nMessageService.getMsg(10101, lang));
		}
		return new ResultResponse(success, data);
	}
	
	/**
	 * 币种列表
	 * @return
	 * @throws VoException
	 */
	@RequestMapping("/list")
	public ResultResponse list(HttpServletRequest request) throws VoException {
		String lang = request.getHeader(Constant.api_header_lang);
		
		boolean success = true;
		Object data = null;
		try {
			List<MarketCoinVo> list = userCoinService.list();
			data=list;
		} catch (Exception e) {
			return new ResultResponse(false, I18nMessageService.getMsg(10101, lang));
		}
		return new ResultResponse(success, data);
	}
	
	/**
	 * 充值显示
	 * @return
	 * @throws VoException
	 */
	@RequestMapping("/rechargeShow/{coinId}")
	public ResultResponse rechargeShow(@PathVariable("coinId") Integer coinId,HttpServletRequest request) throws VoException {
		String lang = request.getHeader(Constant.api_header_lang);
		String userId = request.getHeader(Constant.api_header_userId);
		
		boolean success = true;
		Object data = null;
		try {
			data = userCoinService.rechargeShow(Integer.parseInt(userId),coinId);
		} catch (Exception e) {
			return new ResultResponse(false, I18nMessageService.getMsg(10101, lang));
		}
		return new ResultResponse(success, data);
	}
	
	/**
	 * 查询用户单个币种资产
	 * @return
	 * @throws VoException
	 */
	@RequestMapping("/queryCoinInfo/{coinId}")
	public ResultResponse queryCoinInfo(@PathVariable("coinId") Integer coinId,HttpServletRequest request) throws VoException {
		String lang = request.getHeader(Constant.api_header_lang);
		String userId = request.getHeader(Constant.api_header_userId);
		
		if(coinId==null) {
			return new ResultResponse(false, "coinId不能为空!");
		}
		boolean success = true;
		Object data = userCoinService.queryCoinInfoByUserId(Integer.parseInt(userId),coinId,lang);
		
		return new ResultResponse(success, data);
	}

	/**
	 * 分红详情
	 * @return
	 */
	@RequestMapping("/bonus/{leftCoinId}")
	public ResultResponse bonus(@PathVariable("leftCoinId") Integer leftCoinId, PageParam pageParam, HttpServletRequest request) {
		String lang = request.getHeader(Constant.api_header_lang);
		String userId = request.getHeader(Constant.api_header_userId);
		Wrapper<Bonus> wrapper = new QueryWrapper<Bonus>().lambda().eq(Bonus::getToUserId,Long.valueOf(userId))
					.eq(Bonus::getLeftCoinId,Long.valueOf(leftCoinId)).orderByDesc(Bonus::getId);

		Page<BonusVO> bonusPage = bonusService.pageBonus(pageParam,wrapper);
		return new ResultResponse(true, bonusPage);
	}

	/**
	 * 领导人下级持仓统计
	 * @return
	 */
	@GetMapping("/leader")
	public ResultResponse leader(PageParam pageParam) {
		Integer userId = HttpContext.getUserId();
		Page<UserCoinLeader> page = userCoinLeaderService.pageForWeb(pageParam, userId);
		return new ResultResponse(true, page);
	}
}
