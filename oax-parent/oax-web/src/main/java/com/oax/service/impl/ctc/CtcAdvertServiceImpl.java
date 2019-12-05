package com.oax.service.impl.ctc;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oax.common.AssertHelper;
import com.oax.common.BeanHepler;
import com.oax.common.EmptyHelper;
import com.oax.common.RedisUtil;
import com.oax.context.HttpContext;
import com.oax.druid.DataSource;
import com.oax.druid.DataSourceType;
import com.oax.entity.ctc.CtcAdvert;
import com.oax.entity.ctc.Merchant;
import com.oax.entity.enums.CtcAdvertStatus;
import com.oax.entity.enums.CtcAdvertType;
import com.oax.entity.front.Coin;
import com.oax.entity.front.Member;
import com.oax.entity.front.UserCoin;
import com.oax.entity.front.param.CtcAdvertParam;
import com.oax.entity.front.vo.CtcAdvertBalanceVo;
import com.oax.entity.front.vo.CtcAdvertVo;
import com.oax.entity.front.vo.ListCtcAdvertVo;
import com.oax.form.CtcAdvertForm;
import com.oax.form.UpdateCtcAdvertForm;
import com.oax.mapper.ctc.CtcAdvertMapper;
import com.oax.mapper.ctc.CtcOrderMapper;
import com.oax.mapper.ctc.MerchantMapper;
import com.oax.mapper.front.CoinMapper;
import com.oax.mapper.front.MemberMapper;
import com.oax.mapper.front.UserCoinMapper;
import com.oax.service.CommonCheckService;
import com.oax.service.ctc.CtcAdvertService;
import com.oax.vo.CtcAdvertInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@Transactional
public class CtcAdvertServiceImpl implements CtcAdvertService {

    @Autowired
    private UserCoinMapper userCoinMapper;

    @Autowired
    private CoinMapper coinMapper;

    @Autowired
    private MerchantMapper merchantMapper;

    @Autowired
    private CtcAdvertMapper ctcAdvertMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private CtcOrderMapper ctcOrderMapper;

    @Autowired
    private CommonCheckService commonCheckService;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    @DataSource(DataSourceType.SLAVE)
    public CtcAdvertBalanceVo coinInfo(Integer userId, Integer coinId) {
        coinId = coinId == null ? 10 : coinId;//默认usdt，usdt的id
        //判断币是否存在
        Coin coin = coinMapper.selectById(coinId);
        AssertHelper.notEmpty(coin, "币种不存在");
        //用户拥有的币的个数
        BigDecimal hasQty = userCoinMapper.getBanlanceByCoinIdAndUserId(userId, coin.getId());
        CtcAdvertBalanceVo ctcAdvertBalanceVO = new CtcAdvertBalanceVo();
        ctcAdvertBalanceVO.setBanlance(hasQty);
        ctcAdvertBalanceVO.setCoinName(coin.getShortName());
        return ctcAdvertBalanceVO;
    }

    @Override
    @DataSource(DataSourceType.MASTER)
    public void updateStatus(Integer id, CtcAdvertStatus status) {
        Integer userId = HttpContext.getUserId();
        CtcAdvert ctcAdvert = ctcAdvertMapper.selectById(id);
        AssertHelper.notEmpty(ctcAdvert, "广告不存在");
        AssertHelper.isTrue(userId.equals(ctcAdvert.getUserId()), "广告不存在");
        //是否存在相同类型的广告状态为发布中
        if (status == CtcAdvertStatus.PUTAWAY) {
            boolean hasPutawayByType = ctcAdvertMapper.hasPutawayByType(userId, ctcAdvert.getType().ordinal());
            AssertHelper.isTrue(!hasPutawayByType, "同一类型只能发布一个广告");
        }

        ctcAdvert.setStatus(status);
        ctcAdvertMapper.updateById(ctcAdvert);
        log.info("商户更新广告状态成功 id:{} - userId:{} - status:{}", ctcAdvert.getId(),userId, status);
    }

    @Override
    @DataSource(DataSourceType.MASTER)
    public void saveOne(CtcAdvertForm form)throws Exception {
        Integer userId = HttpContext.getUserId();
        Integer coinId = form.getCoinId() == null ? 10 : form.getCoinId();//默认usdt，usdt的id
        //判断币是否存在
        Coin coin = coinMapper.selectById(coinId);
        //获取商户
        Merchant merchant = merchantMapper.selectByUserId(userId);
        checkCtcAdvertForm(form, userId, coin, merchant);
        //保存
        CtcAdvert ctcAdvert = BeanHepler.copy(form, CtcAdvert.class);
        ctcAdvert.setUserId(userId);
        ctcAdvert.setCoinId(coinId);
        ctcAdvert.setStatus(CtcAdvertStatus.SOLDOUT);//广告默认是下架的
        ctcAdvert.setCoinName(coin.getShortName());
        ctcAdvert.setRemainQty(form.getTotalQty());//剩余可交易余额=总的可交易额
        ctcAdvert.setMerchantId(merchant.getId());
        ctcAdvert.setCreateDate(new Date());
        ctcAdvertMapper.insert(ctcAdvert);
        log.info("商户新增广告成功 id:{} - userId:{}", ctcAdvert.getId(),userId);
    }

    //数据判空验证
    private void checkCtcAdvertForm(CtcAdvertForm form, Integer userId, Coin coin, Merchant merchant)throws Exception{
        AssertHelper.notEmpty(coin, "币种不存在");
        AssertHelper.notEmpty(merchant, "当前用户商户信息不存在");
        //用户拥有的币的个数
        BigDecimal hasQty = userCoinMapper.getBanlanceByCoinIdAndUserId(userId, coin.getId());
        BigDecimal minQty = form.getMinQty();
        BigDecimal maxQty = form.getMaxQty();
        BigDecimal totalQty =  form.getTotalQty();
        AssertHelper.isTrue(minQty.compareTo(maxQty) <= 0, "最小个数要小于等于最大个数");
        AssertHelper.isTrue(maxQty.compareTo(totalQty) <= 0 , "最大个数要小于等于要交易的个数");
        if (form.getType() == CtcAdvertType.SALE) {
            AssertHelper.isTrue(totalQty.compareTo(hasQty) <= 0 , "总数要小于等于拥有个数");
        }
//        AssertHelper.isTrue(totalQty.compareTo(new BigDecimal(1000)) > 0, "卖出数量要大于1000");
        //商户是否绑定银行卡
        commonCheckService.checkHasBankCard(userId);
    }

    @Override
    @DataSource(DataSourceType.SLAVE)
    public CtcAdvertInfoVO info() {
        Integer userId = HttpContext.getUserId();
        CtcAdvertInfoVO infoVO = new CtcAdvertInfoVO();
        CtcAdvertVo saleAdvert = getSaleAdvert();
        CtcAdvertVo buyAdvert = getBuyAdvert();
        infoVO.setSaleAdvert(saleAdvert);
        infoVO.setBuyAdvert(buyAdvert);
        if (userId != null) {
            Member user = memberMapper.selectByPrimaryKey(HttpContext.getUserId());
            infoVO.setMerchant(user.getMerchant());
            int newOrderSize = ctcOrderMapper.countNewOrder(userId, getLastRefreshDate(userId));
            infoVO.setNewOrderSize(newOrderSize);
        } else {
            infoVO.setMerchant(false);
        }

        if (saleAdvert != null) {
            UserCoin merchantUserCoin = userCoinMapper.selectByUserIdAndCoinId(saleAdvert.getUserId(), saleAdvert.getCoinId());
            if (merchantUserCoin != null && merchantUserCoin.getBanlance().compareTo(saleAdvert.getMaxQty()) < 0
                    && merchantUserCoin.getBanlance().compareTo(saleAdvert.getMinQty()) > 0) {//商户余额小于上限时,修改上限为余额
                saleAdvert.setMaxQty(merchantUserCoin.getBanlance());
            }
        }
        return infoVO;
    }

    private Date getLastRefreshDate(Integer userId) {
        String dateStr = redisUtil.getString(CtcOrderServiceImpl.lastRefreshTimeKey + userId);
        if (dateStr == null) {
            return new Date(0);
        }
        return new Date(Long.parseLong(dateStr));
    }

    //获取用户买币广告
    private CtcAdvertVo getSaleAdvert(){
        //获取两个商家的卖出广告
        List<CtcAdvertVo> twoCtcAdvertVos = ctcAdvertMapper.getTopSaleTwoAdvertVo();
        if (EmptyHelper.isEmpty(twoCtcAdvertVos)) {
            return null;
        }
        if (twoCtcAdvertVos.size() == 1) {
            return twoCtcAdvertVos.get(0);
        }
        //返回价格最低的那个，如果价格相同，返回不是上一次交易的那个
        if (twoCtcAdvertVos.size() == 2) {
            if (twoCtcAdvertVos.get(0).getCnyPrice().equals(twoCtcAdvertVos.get(1).getCnyPrice())) {
                //获取上一个订单是谁的
                Integer ctcAdvertId = ctcOrderMapper.getLastUserBuyOrderAdvertId();
                if (ctcAdvertId == null) {
                    return twoCtcAdvertVos.get(0);
                } else {
                    return ctcAdvertId == twoCtcAdvertVos.get(0).getId()?twoCtcAdvertVos.get(1):twoCtcAdvertVos.get(0);
                }
            } else {
                return twoCtcAdvertVos.get(0);
            }
        }
        return null;
    }

    //获取用户卖币的广告
    private CtcAdvertVo getBuyAdvert(){
        //获取两个商家的买入广告
        List<CtcAdvertVo> twoCtcAdvertVos = ctcAdvertMapper.getTopBuyTwoAdvertVo();
        if (EmptyHelper.isEmpty(twoCtcAdvertVos)) {
            return null;
        }
        if (twoCtcAdvertVos.size() == 1) {
            return twoCtcAdvertVos.get(0);
        }
        //返回价格最低的那个，如果价格相同，返回不是上一次交易的那个
        if (twoCtcAdvertVos.size() == 2) {
            if (twoCtcAdvertVos.get(0).getCnyPrice() == twoCtcAdvertVos.get(1).getCnyPrice()) {
                //获取上一个订单是谁的
                Integer ctcAdvertId = ctcOrderMapper.getLastUserSaleOrderAdvertId();
                if (ctcAdvertId == null) {
                    return twoCtcAdvertVos.get(0);
                } else {
                    return ctcAdvertId == twoCtcAdvertVos.get(0).getId()?twoCtcAdvertVos.get(1):twoCtcAdvertVos.get(0);
                }
            } else {
                return twoCtcAdvertVos.get(0);
            }
        }
        return null;
    }

    @Override
    @DataSource(DataSourceType.SLAVE)
    public PageInfo<ListCtcAdvertVo> pageForWeb(CtcAdvertParam param, Integer userId) {
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        Map<String, Object> params = BeanHepler.copyToMap(param);
        params.put("userId", userId);
        List<ListCtcAdvertVo> listCtcAdvertVos = ctcAdvertMapper.pageForWeb(params);
        listCtcAdvertVos.forEach(item ->{
            item.setStatusDesc(item.getStatus().desc);
            item.setTypeDesc(item.getType().desc);
        });
        return new PageInfo<>(listCtcAdvertVos);
    }




    @Override
    @DataSource(DataSourceType.MASTER)
    public void updateOne(Integer userId, Integer id, UpdateCtcAdvertForm form) {
        CtcAdvert ctcAdvert = ctcAdvertMapper.selectById(id);
        AssertHelper.notEmpty(ctcAdvert, "广告不存在");
        AssertHelper.isTrue(userId.equals(ctcAdvert.getUserId()), "广告不存在");
        BigDecimal oldRemainQty = ctcAdvert.getRemainQty();
        //用户拥有的币的个数
        BigDecimal minQty = form.getMinQty();
        BigDecimal maxQty = form.getMaxQty();
        AssertHelper.isTrue(minQty.compareTo(maxQty) <= 0, "最小个数要小于等于最大个数");
        AssertHelper.isTrue(maxQty.compareTo(form.getRemainQty()) <= 0 , "最大个数要小于等于可交易的个数");
        UserCoin userCoin = userCoinMapper.selectByUserIdAndCoinId(ctcAdvert.getUserId(), ctcAdvert.getCoinId());
        if (ctcAdvert.getType() == CtcAdvertType.SALE) {
            AssertHelper.isTrue(form.getRemainQty().compareTo(userCoin.getBanlance()) <= 0, "可交易量要小于余额");
        }


        ctcAdvert.setCnyPrice(form.getCnyPrice());
        ctcAdvert.setMinQty(form.getMinQty());
        ctcAdvert.setMaxQty(form.getMaxQty());
        ctcAdvert.setRemainQty(form.getRemainQty());
        ctcAdvert.setTotalQty(ctcAdvert.getTotalQty().add(ctcAdvert.getRemainQty().subtract(oldRemainQty)));
        ctcAdvertMapper.updateById(ctcAdvert);
    }
    @Override
    public PageInfo<ListCtcAdvertVo> pageForAdvert(CtcAdvertParam param) {
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        Map<String, Object> params = BeanHepler.copyToMap(param);
        params.put("status",0);
        params.put("type",0);
        List<ListCtcAdvertVo> listCtcAdvertVos = ctcAdvertMapper.pageForAdvert(params);
        listCtcAdvertVos.forEach(item -> {
            item.setStatusDesc(item.getStatus().desc);
            item.setTypeDesc(item.getType().desc);
        });
        return new PageInfo<>(listCtcAdvertVos);
    }


}