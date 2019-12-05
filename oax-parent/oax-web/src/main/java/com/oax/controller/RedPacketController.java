package com.oax.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oax.common.DeviceUtil;
import com.oax.common.ResultResponse;
import com.oax.entity.front.Member;
import com.oax.entity.front.RedPacket;
import com.oax.entity.front.RedPacketLimit;
import com.oax.entity.front.RedPacketLimitInfo;
import com.oax.exception.VoException;
import com.oax.service.RedPacketService;
import com.oax.service.UserCoinService;
import com.oax.vo.RedPacketVO;
import com.oax.vo.UserRedPacketVO;

@RestController
@RequestMapping("/redPacket")
public class RedPacketController {
    @Autowired
    private RedPacketService redPacketService;
    @Value("${oax.redpacket.url}")
    private String url;
    @Autowired
    private UserCoinService userCoinService;
    public String filterEmoji(String source) {
        if(StringUtils.isNotBlank(source)){
            return source.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", "");
        }else{
            return source;
        }
    }
    @PostMapping("award")
    public ResultResponse awardRedPacket(@RequestBody RedPacket redPacket, HttpServletRequest request) throws VoException{
        redPacket.setWishWords(this.filterEmoji(redPacket.getWishWords()));
        redPacket.setUserId(Integer.parseInt(request.getHeader("userId")));
        String msg = null;
        //先校验数量  金额 是否在红包限制范围内
        RedPacketLimit redPacketLimit = redPacketService.getLimit(redPacket.getType(),redPacket.getCoinId());
        // type=1表示普通红包  type=2表示随机红包
        //判断金额和红包不能为空
        if (redPacket.getTotalCoinQty()==null) {
            msg = "红包金额必须大于0";
            throw new VoException(msg);
        }
        if (redPacket.getTotalPacketQty()==null) {
            msg = "红包数量必须大于0";
            throw new VoException(msg);
        }
        if(BigDecimal.ONE.divide(BigDecimal.TEN.pow(8)).multiply(
                new BigDecimal(redPacket.getTotalPacketQty())).compareTo(redPacket.getTotalCoinQty())>0){
            throw new VoException("红包金额过小");
        }
        if (redPacket.getType()==1){
            //红包个数超过普通红包限制
            if(redPacketLimit.getLimitPacketQty()<redPacket.getTotalPacketQty()){
                msg = "普通红包最多发送数量为";
                throw new VoException(msg+" "+redPacketLimit.getLimitPacketQty());
            }
            //红包金额超过普通红包金额限制
            if (redPacketLimit.getLimitCoinQty().
                    compareTo(redPacket.getTotalCoinQty().divide(new BigDecimal(redPacket.getTotalPacketQty())))<0){
                msg = "普通红包单个红包金额不能超过";
                throw new VoException(msg+" "+redPacketLimit.getLimitCoinQty());
            }
        }else{
            if(redPacketLimit.getLimitPacketQty()<redPacket.getTotalPacketQty()){
                msg = "随机红包最多发送数量为";
                throw new VoException(msg+" "+redPacketLimit.getLimitPacketQty());
            }
            if (redPacketLimit.getLimitCoinQty().compareTo(redPacket.getTotalCoinQty())<0){
                msg = "随机红包金额不能超过";
                throw new VoException(msg+" "+redPacketLimit.getLimitCoinQty());
            }
        }
        //校验金额是否充足
        BigDecimal banlance = redPacketService.getBanlance(redPacket.getCoinId(), redPacket.getUserId());
        if(banlance==null||banlance.compareTo(redPacket.getTotalCoinQty())<0){
            msg = "余额不足";
            throw new VoException(msg);
        }
        //查询人民币价值
        BigDecimal cnyPrice = userCoinService.selectCnyPriceByCoinId(redPacket.getCoinId());
        redPacket.setTotalCny(redPacket.getTotalCoinQty().multiply(cnyPrice));
        //所有校验完成之后开始发红包
        RedPacket rpk = redPacketService.awardRedPacket(redPacket);
        Map<String,Object> map = new HashMap<>();
        map.put("url",url+rpk.getId()+"/"+rpk.getUserId());
        if (rpk.getType()==1){
            map.put("title","【普通红包】不夸张地说，这是今年最该抢的币");
            map.put("desc","且领且珍惜 此处省去一万字......");
        }else{
            map.put("title","【拼手气红包】不夸张地说，这是今年最该抢的币");
            map.put("desc","且领且珍惜 此处省去一万字......");
        }
        map.put("redPacket",rpk);
        return new ResultResponse(true,map);
    }

    @PostMapping("grab")
    public  ResultResponse takeRedPacket(@RequestBody RedPacketVO vo, HttpServletRequest request)throws VoException{
        String regExp = null;
        if (vo.getRedPacketId()==null){
            throw new VoException("1","网络繁忙");
        }
        if (!vo.getAccountNumber().contains("@")) {
            //手机号校验
            regExp = "^(1)[3,4,5,7,8,9]\\d{9}$";
            if (!vo.getAccountNumber().matches(regExp)) {
                throw new VoException("1","手机号格式不正确");
            }
        }else{
            //邮箱校验
            regExp = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
            if (!vo.getAccountNumber().matches(regExp)) {
                throw new VoException("1","邮箱格式不正确");
            }
        }
        int sourceType = DeviceUtil.getPlatformType(request);
//        vo.setSource(request.getHeader("user-agent"));
        //查看账号是否存在
        if (!vo.getAccountNumber().contains("@")){
            vo.setAccountNumber("0086"+vo.getAccountNumber());
        }
        Member user = redPacketService.checkUser(vo.getAccountNumber());
        Integer coinId= redPacketService.getCoinByRedPacketId(vo.getRedPacketId());
        //判断该用户是否已领取过该红包
        if (user!=null){
            Map<String,Object> map = redPacketService.checkTakeRedPacket(user.getId(),vo.getRedPacketId());
            //加入人民币估值
            if(map!=null&&map.size()!=0){
                map.put("cnyPrice",userCoinService.selectCnyPriceByCoinId(coinId));
                map.put("takeFlag",1);
                map.put("registFlag", "0");
                return new ResultResponse(true,map);
            }
        }
        boolean flag = false;
        //如果不存在该用户就注册用户
        if(user==null){
            user = redPacketService.registByRedPacket(vo.getAccountNumber(),sourceType);
            flag = true;
        }
        //注册完用户后 查看用户是否有资产数据，然后判断是否加入该用户该币种的资产记录信息
        redPacketService.insertUserCoin(user.getId(),coinId);
        //领取红包
        Map<String,Object> mapInfo = redPacketService.takeRedPacket(user.getId(), vo, request.getHeader("lang"));
        if (flag){
            mapInfo.put("registFlag", "1");
        }else{
            mapInfo.put("registFlag", "0");
        }
        return new ResultResponse(true,mapInfo);
    }

    @PostMapping("awardRedPacketRecord")
    public  ResultResponse  redPacketPage(@RequestBody UserRedPacketVO vo,@RequestHeader int userId){
        vo.setUserId(userId);
        Map<String,Object> map = redPacketService.findRedPacketRecordByUserId(vo);
        return new ResultResponse(true,map);
    }

    @PostMapping("takeRedPacketDetails")
    public ResultResponse takeRedPacketDetails(@RequestBody UserRedPacketVO vo ){
        Map<String,Object> map = redPacketService.takeRedPacketDetails(vo);
        return new ResultResponse(true,map);
    }

    @PostMapping("grabRedPacketRecord")
    public  ResultResponse grabRedPacketRecord(@RequestBody UserRedPacketVO vo,@RequestHeader int userId){
        vo.setUserId(userId);
        Map<String,Object> map = redPacketService.grabRedPacketRecord(vo);
        return new ResultResponse(true,map);
    }

    @GetMapping("/init/{id}")
    public ResultResponse init(@PathVariable(name = "id") int id){
        Map<String, Object> map = redPacketService.initRedPacket(id);
        return new ResultResponse(true,map);
    }

    @GetMapping("/index/{type}")
    public  ResultResponse index(@PathVariable(name = "type") int type,@RequestHeader int userId){
        List<RedPacketLimitInfo> list = redPacketService.index(type);
        //查询用户所有币的资产
        List<Map<String,Object>> userCoinList = redPacketService.selectRedPacketUserCoin(list,userId);
        Map<String,Object> map = new HashMap<>();
        map.put("coinList",list);
        map.put("userCoinList",userCoinList);
        return new ResultResponse(true,map);
    }
}
