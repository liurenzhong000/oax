

package com.oax.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oax.common.BaseController;
import com.oax.common.ResultResponse;
import com.oax.entity.front.ArticleTypeDetail;
import com.oax.entity.front.Kline;
import com.oax.entity.front.MarketInfo;
import com.oax.entity.front.SysConfig;
import com.oax.entity.front.Trade;
import com.oax.entity.front.TradeInfo;
import com.oax.outparam.CategoryMarket;
import com.oax.service.ArticleService;
import com.oax.service.KlineService;
import com.oax.service.OrdersService;
import com.oax.service.SysConfigService;
import com.oax.service.TradeCoinService;
import com.oax.service.TradeService;
import com.oax.service.UserCoinService;
import com.oax.service.UserMaketService;
import com.oax.vo.KlineVO;
import com.oax.vo.TransactionPageVO;

/** 
* @ClassName:：TransactionPageController 
* @Description： 交易页面
* @author ：xiangwh  
* @date ：2018年6月23日 上午11:19:02 
*  
*/
@RestController
@RequestMapping("transactionPage")
public class TransactionPageController extends BaseController{
	//交易页面 公告接口
	@Autowired 
	private  ArticleService articleService;
	//交易页面  所以市场交易对接口
	@Autowired
	private TradeCoinService tradeCoinService;
	//交易页面  用户搜藏交易对
	@Autowired
	private  UserMaketService userMaketService;
	//交易页面 k线图数据
	@Autowired
	private KlineService klineService;
	//交易页面 实时委托页面
	@Autowired
	private OrdersService ordersService;
	//交易页面 实时交易
	@Autowired
	private TradeService tradeService;
	//用户资产  
	@Autowired
	private UserCoinService userCoinService;
	@Autowired
	private SysConfigService sysConfigService;
	
	@SuppressWarnings("unchecked")
	@RequestMapping("index")
	public  ResultResponse market(@RequestHeader String lang,HttpServletRequest request,@RequestBody TransactionPageVO vo) {
		Integer userId = null;
		if (StringUtils.isNotBlank(request.getHeader("userId"))) {
			userId = Integer.valueOf(request.getHeader("userId"));
		}
		//交易页面公告
		List<ArticleTypeDetail> articleList=(List<ArticleTypeDetail>)articleService.noticeList(lang, 2).get("list");
		//交易页面 市场交易对
		List<CategoryMarket> allMaketList = tradeCoinService.findList(userId);
		//交易页面 用户搜藏交易对
		List<MarketInfo> userMaketList = null;
		if(userId!=null) {
			userMaketList= userMaketService.findListByUser(userId);
		}
		
		//交易页面  交易对k线图
		KlineVO klineVO = new KlineVO();
		klineVO.setMarketId(vo.getMarketId());
		klineVO.setMinType(vo.getMinType());
		List<Kline> klineList = klineService.findListByMarketId(klineVO);
		
		//交易页面  实时委托
		Map<String, Object> marketOrdersMap = ordersService.getMarketOrdersList(vo.getMarketId());
		
		//交易页面  实时成交记录
		Trade trade = new Trade();
		trade.setUserId(userId);
		trade.setMarketId(vo.getMarketId());
		List<TradeInfo> marketTradeList = tradeService.getTradeList(vo.getMarketId(),null);
		SysConfig sysConfig = sysConfigService.marketFeeRate();
		
		Map<String, Object> result = new HashMap<>();
		result.put("articleList", articleList);
		result.put("allMaketList", allMaketList);
		result.put("userMaketList", userMaketList);
		result.put("klineList", klineList);
		result.put("marketOrdersMap", marketOrdersMap);
		result.put("marketTradeList", marketTradeList);
		result.put("feeRate", sysConfig.getValue());
		
		return new ResultResponse(true, result);
	}


    /**
     * 改版首页数据获取
     */
    @RequestMapping("indexes")
    public  ResultResponse marketNew(@RequestHeader String lang,HttpServletRequest request,@RequestBody TransactionPageVO vo) {
        Integer userId = null;
        if (StringUtils.isNotBlank(request.getHeader("userId"))) {
            userId = Integer.valueOf(request.getHeader("userId"));
        }
        //交易页面 市场交易对
        List<CategoryMarket> allMaketList = tradeCoinService.findList(userId);
        //交易页面 用户搜藏交易对
        List<MarketInfo> userMaketList = null;
        if(userId!=null) {
            userMaketList= userMaketService.findListByUser(userId);
        }

        //交易页面  交易对k线图
        KlineVO klineVO = new KlineVO();
        klineVO.setMarketId(vo.getMarketId());
        klineVO.setMinType(vo.getMinType());

        //交易页面  实时委托
        Map<String, Object> marketOrdersMap = ordersService.getMarketOrdersList(vo.getMarketId());

        //交易页面  实时成交记录
        Trade trade = new Trade();
        trade.setUserId(userId);
        trade.setMarketId(vo.getMarketId());
        List<TradeInfo> marketTradeList = tradeService.getTradeList(vo.getMarketId(),null);
        SysConfig sysConfig = sysConfigService.marketFeeRate();

        Map<String, Object> result = new HashMap<>();
        result.put("allMaketList", allMaketList);
        result.put("userMaketList", userMaketList);
        result.put("marketOrdersMap", marketOrdersMap);
        result.put("marketTradeList", marketTradeList);
        result.put("feeRate", sysConfig.getValue());

        return new ResultResponse(true, result);
    }
	
	//交易页面   与用户有关的数据接口  
	@RequestMapping("{marketId}")	
	public ResultResponse userMarket(@RequestHeader String lang,HttpServletRequest request,@PathVariable(name="marketId") Integer marketId) {
		Integer userId = null;
		if (StringUtils.isNotBlank(request.getHeader("userId"))) {
			userId = Integer.valueOf(request.getHeader("userId"));
		}
		List<TradeInfo> marketTradeList = tradeService.getTradeList(marketId,userId);	
		Map<String, Object> userData = userCoinService.getUserCoinMap(marketId, userId);	
		String needTransactionPassword = ordersService.isNeedTransactionPassword(userId);
		userData.put("needTransactionPassword", needTransactionPassword);
		userData.put("marketTradeList", marketTradeList);
		return new ResultResponse(true, userData);
	}
	
	
}
