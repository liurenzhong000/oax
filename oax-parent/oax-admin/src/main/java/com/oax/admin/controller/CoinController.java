package com.oax.admin.controller;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.oax.admin.service.CoinService;
import com.oax.admin.walletclient.BtcApiClient;
import com.oax.common.Base64Utils;
import com.oax.common.PageResultResponse;
import com.oax.common.RedisUtil;
import com.oax.common.ResultResponse;
import com.oax.common.enums.CoinTypeEnum;
import com.oax.common.enums.RedisKeyEnum;
import com.oax.entity.admin.param.BtcCoin;
import com.oax.entity.admin.param.EthCoin;
import com.oax.entity.admin.param.EthTokenCoin;
import com.oax.entity.admin.param.OmniCoin;
import com.oax.entity.admin.param.SimpleCoinParam;
import com.oax.entity.admin.vo.CoinOutQtyCountVo;
import com.oax.entity.admin.vo.MarketCoinVo;
import com.oax.entity.admin.vo.SimpleCoin;
import com.oax.entity.front.Coin;
import com.oax.entity.front.CoinWithBLOBs;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/12
 * Time: 11:18
 * 币种 控制层
 */
@RestController
@RequestMapping("/coins")
public class CoinController {
    @Autowired
    private CoinService coinService;

    @Autowired
    private RedisUtil redisUtil;

    @PostMapping("/page")
    public ResultResponse getSimpleCoinsByPage(@RequestBody @Valid SimpleCoinParam simpleCoinParam, BindingResult result) {
        if (result.hasErrors()) {
            return new ResultResponse(false, result.getFieldError().getDefaultMessage());
        }
        PageInfo<SimpleCoin> coinPageInfo = coinService.selectSimpleCoinByParam(simpleCoinParam);

        PageResultResponse<SimpleCoin> simpleCoinPageResultResponse = new PageResultResponse<>();

        BeanUtils.copyProperties(coinPageInfo, simpleCoinPageResultResponse);

        simpleCoinPageResultResponse.setParam(simpleCoinParam);
        return new ResultResponse(true, simpleCoinPageResultResponse);
    }

    @GetMapping("/{coinId}")
    public ResultResponse getCoin(@PathVariable(name = "coinId") int coinId) {

        CoinWithBLOBs coinWithBLOBs = coinService.selectById(coinId);

        return new ResultResponse(true, coinWithBLOBs);
    }


    private final String regETHAddress = "^0x([a-z]|[A-Z]|[0-9]){40}";

    /**
     * 修改比特币
     *
     * @param btcCoin
     * @return
     */
    @PutMapping("/btc")
    public ResultResponse updateBTCcoin(@RequestBody @Valid BtcCoin btcCoin, BindingResult result) {

        if (result.hasErrors()) {
            return new ResultResponse(false, result.getFieldError().getDefaultMessage());
        }


        String coldAddress = btcCoin.getColdAddress();

        if (coldAddress.length()<26||coldAddress.length()>35){
            return new ResultResponse(false,"冷钱包地址有误");
        }

        if (btcCoin.getId() == null || btcCoin.getId() <= 0) {
            return new ResultResponse(false, "更新id不能为空");
        }
        try {
            final Integer coinId = btcCoin.getId();
            CoinWithBLOBs btcCoinWithBLOBs = coinService.selectById(coinId);


            if (btcCoinWithBLOBs.getType() != CoinTypeEnum.BTC.getType()) {
                return new ResultResponse(false, "更新币种类型有误");
            }

            BeanUtils.copyProperties(btcCoin, btcCoinWithBLOBs);
            //检查 是否存在相同名称
            ResultResponse checkResult = checkCoinSameName(coinId, btcCoinWithBLOBs);
            if (checkResult != null) return checkResult;

            btcCoinWithBLOBs.setMainAddressPassword(null);
            coinService.updateCionBySelect(btcCoinWithBLOBs);


            redisUtil.delete(RedisKeyEnum.COIN_LIST.getKey());

            return new ResultResponse(true, "更新BTC虚拟币信息成功");

        } catch (Exception e) {
            e.printStackTrace();

            return new ResultResponse(false, "更新BTC虚拟币信息失败");
        }

    }


    /**
     * 修改omni-token
     *
     * @param omniCoin
     * @return
     */
    @PutMapping("/omni")
    public ResultResponse updateOmniTokencoin(@RequestBody @Valid OmniCoin omniCoin, BindingResult result) {

        if (result.hasErrors()) {
            return new ResultResponse(false, result.getFieldError().getDefaultMessage());
        }


        String coldAddress = omniCoin.getColdAddress();

        if (coldAddress.length()<26||coldAddress.length()>35){
            return new ResultResponse(false,"冷钱包地址有误");
        }

        if (omniCoin.getId() == null || omniCoin.getId() <= 0) {
            return new ResultResponse(false, "更新id不能为空");
        }
        try {
            final Integer coinId = omniCoin.getId();
            CoinWithBLOBs btcCoinWithBLOBs = coinService.selectById(coinId);


            if (btcCoinWithBLOBs.getType() != CoinTypeEnum.USDT.getType()) {
                return new ResultResponse(false, "更新币种类型有误");
            }

            BeanUtils.copyProperties(omniCoin, btcCoinWithBLOBs);
            //检查 是否存在相同名称
            ResultResponse checkResult = checkCoinSameName(coinId, btcCoinWithBLOBs);
            if (checkResult != null) return checkResult;

            btcCoinWithBLOBs.setMainAddressPassword(null);
            coinService.updateCionBySelect(btcCoinWithBLOBs);


            redisUtil.delete(RedisKeyEnum.COIN_LIST.getKey());

            return new ResultResponse(true, "更新omni虚拟币信息成功");

        } catch (Exception e) {
            e.printStackTrace();
            return new ResultResponse(false, "更新omni虚拟币信息失败");
        }

    }


    /**
     * 检查对应传入的name,数据库是否存在
     *
     * @param coinId           排除检查id(可谓null,全查询)
     * @param btcCoinWithBLOBs 被检查对象
     * @return
     */
    private ResultResponse checkCoinSameName(Integer coinId, CoinWithBLOBs btcCoinWithBLOBs) {
        //TODO 测试关闭校验
        String shortName = btcCoinWithBLOBs.getShortName();

//            coinService.count

        String fullName = btcCoinWithBLOBs.getFullName();

        String cnName = btcCoinWithBLOBs.getCnName();

        List<Coin> coinList = coinService.selectByNameAndWithOutId(shortName, fullName, cnName, coinId);

        final List<Coin> shortNameEqualList = coinList.stream()
                .filter(coin -> StringUtils.equals(shortName, coin.getShortName()))
                .collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(shortNameEqualList)) {
            return new ResultResponse(false, "简称:" + shortName + ",已存在");
        }

        final List<Coin> fullNameEqualList = coinList.stream()
                .filter(coin -> StringUtils.equals(fullName, coin.getFullName()))
                .collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(fullNameEqualList)) {
            return new ResultResponse(false, "全称:" + fullName + ",已存在");
        }

        final List<Coin> cnNameEqualList = coinList.stream()
                .filter(coin -> StringUtils.equals(cnName, coin.getCnName()))
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(cnNameEqualList)) {
            return new ResultResponse(false, "全称:" + cnName + ",已存在");
        }

        Integer propertyid = btcCoinWithBLOBs.getPropertyid();

        if (propertyid!=null){
            List<Coin> propertyidCoinList = coinService.selectByPropertyidWithOutCheckCoinId(propertyid,coinId);
            if (CollectionUtils.isNotEmpty(propertyidCoinList)){
                return new ResultResponse(false,"OMNI类型:"+propertyid+",已存在");
            }
        }


        return null;
    }

    /**
     * 修改以太坊
     *
     * @param ethCoin
     * @return
     */
    @PutMapping("/eth")
    public ResultResponse updateETHcoin(@RequestBody @Valid EthCoin ethCoin, BindingResult result) {

        if (result.hasErrors()) {
            return new ResultResponse(false, result.getFieldError().getDefaultMessage());
        }

        String coldAddress = ethCoin.getColdAddress();

        boolean matches = Pattern.matches(regETHAddress, coldAddress);
        if (!matches) {
            return new ResultResponse(false,"冷钱包地址有误");
        }


        if (ethCoin.getId() == null || ethCoin.getId() <= 0) {
            return new ResultResponse(false, "更新id不能为空");
        }
        try {
            final Integer coinId = ethCoin.getId();
            CoinWithBLOBs ethCoinWithBLOBs = coinService.selectById(coinId);

            if (ethCoinWithBLOBs.getType() != CoinTypeEnum.ETH.getType()) {
                return new ResultResponse(false, "更新币种类型有误");
            }

            BeanUtils.copyProperties(ethCoin, ethCoinWithBLOBs);
            //检查 是否存在相同名称
            ResultResponse checkResult = checkCoinSameName(coinId, ethCoinWithBLOBs);
            if (checkResult != null) return checkResult;



            ethCoinWithBLOBs.setMainAddressPassword(null);
            coinService.updateCionBySelect(ethCoinWithBLOBs);


            redisUtil.delete(RedisKeyEnum.COIN_LIST.getKey());
            return new ResultResponse(true, "更新ETH虚拟币信息成功");
        } catch (Exception e) {
            e.printStackTrace();

            return new ResultResponse(false, "更新ETH虚拟币信息失败");
        }
    }



    /**
     * 修改以太坊代币
     *
     * @param ethTokenCoin
     * @return
     */
    @PutMapping("/ethToken")
    public ResultResponse updateETHTokenCoin(@RequestBody @Valid EthTokenCoin ethTokenCoin, BindingResult result) {

        if (result.hasErrors()) {
            return new ResultResponse(false, result.getFieldError().getDefaultMessage());
        }



        if (ethTokenCoin.getId() == null || ethTokenCoin.getId() <= 0) {
            return new ResultResponse(false, "更新id不能为空");
        }
        try {
            final Integer coinId = ethTokenCoin.getId();
            CoinWithBLOBs ethCoinWithBLOBs = coinService.selectById(coinId);

            if (ethCoinWithBLOBs.getType() != CoinTypeEnum.ETH_TOKEN.getType()) {
                return new ResultResponse(false, "更新币种类型有误");
            }



            BeanUtils.copyProperties(ethTokenCoin, ethCoinWithBLOBs);

            //检查 是否存在相同名称
            ResultResponse checkResult = checkCoinSameName(coinId, ethCoinWithBLOBs);
            if (checkResult != null) return checkResult;

            ethCoinWithBLOBs.setMainAddressPassword(null);
            coinService.updateCionBySelect(ethCoinWithBLOBs);

            redisUtil.delete(RedisKeyEnum.COIN_LIST.getKey());
            return new ResultResponse(true, "更新ETH虚拟币信息成功");
        } catch (Exception e) {
            e.printStackTrace();

            return new ResultResponse(false, "更新ETH虚拟币信息失败");
        }
    }

    /**
     * 新增比特币(只有一个)
     *
     * @param btcCoin
     * @param result
     * @return
     */
    @PostMapping("/btc")
    public ResultResponse addBTCcoin(@RequestBody @Valid BtcCoin btcCoin, BindingResult result) {
        if (result.hasErrors()) {
            return new ResultResponse(false, result.getFieldError().getDefaultMessage());
        }
        try {
            List<Coin> btcCoinList = coinService.selectByType(CoinTypeEnum.BTC.getType());
            if (CollectionUtils.isNotEmpty(btcCoinList)) {
                return new ResultResponse(false, "btc只允许添加一个");
            }
            CoinWithBLOBs coinWithBLOBs = new CoinWithBLOBs();
            BeanUtils.copyProperties(btcCoin, coinWithBLOBs);
            coinWithBLOBs.setType(CoinTypeEnum.BTC.getType());

            //检查 是否存在相同名称
            ResultResponse checkResult = checkCoinSameName(null, coinWithBLOBs);
            if (checkResult != null) return checkResult;

            coinService.addCionBySelect(coinWithBLOBs);

            redisUtil.delete(RedisKeyEnum.COIN_LIST.getKey());
            return new ResultResponse(true, "添加BTC虚拟币信息成功");

        } catch (Exception e) {
            e.printStackTrace();
            return new ResultResponse(false, "添加BTC虚拟币信息失败");
        }

    }

    /**
     * omni-token(多个父币 BTC)
     *
     * @param omniCoin
     * @param result
     * @return
     */
    @PostMapping("/omni")
    public ResultResponse addOmniCoin(@RequestBody @Valid OmniCoin omniCoin, BindingResult result) {
        if (result.hasErrors()) {
            return new ResultResponse(false, result.getFieldError().getDefaultMessage());
        }
        try {
            List<Coin> btcCoinList = coinService.selectByType(CoinTypeEnum.BTC.getType());
            if (CollectionUtils.isEmpty(btcCoinList)) {
                return new ResultResponse(false, "请先添加BTC");
            }
            CoinWithBLOBs coinWithBLOBs = new CoinWithBLOBs();
            BeanUtils.copyProperties(omniCoin, coinWithBLOBs);
            coinWithBLOBs.setType(CoinTypeEnum.USDT.getType());



            //检查 是否存在相同名称
            ResultResponse checkResult = checkCoinSameName(null, coinWithBLOBs);
            if (checkResult != null) return checkResult;
            coinWithBLOBs.setParentId(btcCoinList.get(0).getId());
            coinService.addCionBySelect(coinWithBLOBs);

            redisUtil.delete(RedisKeyEnum.COIN_LIST.getKey());
            return new ResultResponse(true, "添加omni虚拟币信息成功");

        } catch (Exception e) {
            e.printStackTrace();
            return new ResultResponse(false, "添加omni虚拟币信息失败");
        }

    }

    /**
     * 新增以太坊(只有一个)
     *
     * @param ethCoin
     * @param result
     * @return
     */
    @PostMapping("/eth")
    public ResultResponse addETHcoin(@RequestBody @Valid EthCoin ethCoin, BindingResult result) {

        if (result.hasErrors()) {
            return new ResultResponse(false, result.getFieldError().getDefaultMessage());
        }
        try {

            List<Coin> etcCoinList = coinService.selectByType(CoinTypeEnum.ETH.getType());
            if (CollectionUtils.isNotEmpty(etcCoinList)) {
                return new ResultResponse(false, "eth只允许添加一个");
            }


            CoinWithBLOBs coinWithBLOBs = new CoinWithBLOBs();
            BeanUtils.copyProperties(ethCoin, coinWithBLOBs);
            coinWithBLOBs.setType(CoinTypeEnum.ETH.getType());

            coinWithBLOBs.setMainAddressPassword(Base64Utils.getBase64(Base64Utils.getBase64(coinWithBLOBs.getMainAddressPassword())));

            //检查 是否存在相同名称
            ResultResponse checkResult = checkCoinSameName(null, coinWithBLOBs);
            if (checkResult != null) return checkResult;

            coinService.addCionBySelect(coinWithBLOBs);

            redisUtil.delete(RedisKeyEnum.COIN_LIST.getKey());
            return new ResultResponse(true, "添加ETH虚拟币信息成功");

        } catch (Exception e) {
            e.printStackTrace();

            return new ResultResponse(false, "添加ETH虚拟币信息失败");
        }
    }

    /**
     * 新增以太坊代币(多个)
     *
     * @param ethTokenCoin
     * @param result
     * @return
     */
    @PostMapping("/ethToken")
    public ResultResponse addETHTokenCoin(@RequestBody @Valid EthTokenCoin ethTokenCoin, BindingResult result) {

        if (result.hasErrors()) {
            return new ResultResponse(false, result.getFieldError().getDefaultMessage());
        }

        try {

            CoinWithBLOBs coinWithBLOBs = new CoinWithBLOBs();
            BeanUtils.copyProperties(ethTokenCoin, coinWithBLOBs);
            coinWithBLOBs.setType(CoinTypeEnum.ETH_TOKEN.getType());


            //检查 是否存在相同名称
            ResultResponse checkResult = checkCoinSameName(null, coinWithBLOBs);
            if (checkResult != null) return checkResult;


            List<Coin> coinList = coinService.selectByType(CoinTypeEnum.ETH.getType());

            if (CollectionUtils.isEmpty(coinList)){
                return new ResultResponse(false,"请先添加ETH");
            }
            Coin coin = coinList.get(0);

            coinWithBLOBs.setParentId(coin.getId());
            coinService.addCionBySelect(coinWithBLOBs);

            redisUtil.delete(RedisKeyEnum.COIN_LIST.getKey());
            return new ResultResponse(true, "添加ethToken虚拟币信息成功");

        } catch (Exception e) {
            e.printStackTrace();

            return new ResultResponse(false, "添加ethToken虚拟币信息失败");
        }
    }

    @Autowired
    private BtcApiClient btcApiClient;

    @GetMapping("mainRecharge")
    public ResultResponse getMainRecharge(){
        return btcApiClient.newAccount();
    }

    @GetMapping("/marketCoins")
    public ResultResponse getAllMarketCoinVo() {

        List<MarketCoinVo> marketCoinVoList = coinService.selectNameAll();
        return new ResultResponse(true, marketCoinVoList);
    }


    @GetMapping("/outQty/count")
    public ResultResponse getAllWalletCount(@RequestParam(required = false, defaultValue = "1") int pageNum,
                                            @RequestParam(required = false, defaultValue = "10") int pageSize,
                                            @RequestParam(required = false) Integer coinId) {

        PageInfo<CoinOutQtyCountVo> pageInfo = coinService.selectCoinOutQtyCountVoByPage(coinId, pageNum, pageSize);

        PageResultResponse<CoinOutQtyCountVo> pageResultResponse = new PageResultResponse<>();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("coinId", coinId);
        BeanUtils.copyProperties(pageInfo, pageResultResponse);
        pageResultResponse.setParam(jsonObject);

        return new ResultResponse(true, pageResultResponse);
    }

    /**
     * 获取币种的充值提现统计
     */
//    @GetMapping("/RW/page")
//    public ResultResponse getRWPage(){
//        select
//        c.short_name,
//                IFNULL(SUM(r.qty),0) AS rechargeQty,
//        ifnull(sum(w.qty),0) as withdrawQty,
//        IFNULL(sum(w.fee),0
//                coin c
//                left join withdraw w on w.coin_id = c.id and w.status >= 2 and w.create_time >='2018-12-08 00:00:00' and w.create_time <'2018-12-09 00:00:00'
//                left join recharge r on r.coin_id = c.id and r.type = 0 and r.create_time >='2018-12-08 00:00:00' and r.create_time <'2018-12-09 00:00:00'
//                where 1=) as withdrawFee
//        from1
//        and c.id = 54
//        group by c.id
//    }
}
