package com.oax.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.UnixCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oax.common.RedisUtil;
import com.oax.common.ShareCodeUtil;
import com.oax.common.enums.RedisKeyEnum;
import com.oax.druid.DataSource;
import com.oax.druid.DataSourceType;
import com.oax.entity.admin.vo.RedPacketVo;
import com.oax.entity.front.GrabRedPacketLog;
import com.oax.entity.front.Member;
import com.oax.entity.front.RedPacket;
import com.oax.entity.front.RedPacketLimit;
import com.oax.entity.front.RedPacketLimitInfo;
import com.oax.entity.front.RedPacketRecord;
import com.oax.entity.front.UserCoin;
import com.oax.exception.VoException;
import com.oax.mapper.front.MemberMapper;
import com.oax.mapper.front.RedPacketMapper;
import com.oax.mapper.front.UserCoinMapper;
import com.oax.service.EmailCaptchaService;
import com.oax.service.RedPacketService;
import com.oax.service.SmsCaptchaService;
import com.oax.service.UserCoinService;
import com.oax.vo.RedPacketVO;
import com.oax.vo.UserRedPacketVO;

@Service
public class RedPacketServiceImpl implements RedPacketService {
    @Autowired
    private RedPacketMapper mapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private UserCoinMapper userCoinMapper;
    @Autowired
    private SmsCaptchaService smsService;
    @Autowired
    private EmailCaptchaService emailService;
    @Autowired
    private UserCoinService userCoinService;
    @Value("${oax.redpacket.url}")
    private String url;

    @Transactional
    @DataSource(DataSourceType.MASTER)
    @Override
    public RedPacket awardRedPacket(RedPacket redPacket) throws VoException {
        Calendar current = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        end.add(Calendar.DATE, 1);
        redPacket.setCreateTime(current.getTime());
        redPacket.setUpdateTime(current.getTime());
        redPacket.setExpireTime(end.getTime());
        redPacket.setGrabPacketQty(0);
        redPacket.setGrabCoinQty(new BigDecimal(0));
        String coinName = mapper.getCoinName(redPacket.getCoinId());
        redPacket.setCoinName(coinName);
        redPacket.setIsRefund(0);
        Integer insertRows = mapper.insert(redPacket);
        if (insertRows!=null&&insertRows>0){
            Integer updateRows = mapper.updateUserCoin(redPacket);
            if (updateRows==null||updateRows==0){
                throw new VoException("派发红包后修改资产异常");
            }
        }else{
            throw new VoException("派发红包异常");
        }
        return redPacket;
    }

    @DataSource(DataSourceType.SLAVE)
    @Override
    public BigDecimal getBanlance(Integer coinId, Integer userId) {
        BigDecimal banlance = mapper.getBanlance(coinId,userId);
        return banlance;
    }

    @DataSource(DataSourceType.SLAVE)
    @Override
    public RedPacketLimit getLimit(Integer type, Integer coinId) {
        List<RedPacketLimit> list = redisUtil.getList(RedisKeyEnum.REDPACKET_LIMIT.getKey(), RedPacketLimit.class);
        if (list!=null&&list.size()!=0){
            return getRedPacketLimit(list,type,coinId);
        }else{
            list = mapper.getLimit();
            redisUtil.setList(RedisKeyEnum.REDPACKET_LIMIT.getKey(),list,-1);
        }
        return getRedPacketLimit(list,type,coinId);
    }

    @DataSource(DataSourceType.SLAVE)
    @Override
    public BigDecimal getCNY(Integer coinId) {
        return mapper.getCnyPrice(coinId);
    }

    @Transactional
    @DataSource(DataSourceType.MASTER)
    @Override
    public Member registByRedPacket(String accountNumber,int sourceType) {
        String code=ShareCodeUtil.generateShortUuid();
        String pwd = this.initPassword();
        Member user = new Member();
        if(accountNumber.contains("@")){
            user.setEmail(accountNumber);
            //注册类型  2是邮箱
            user.setRegisterType(2);
            user.setEmailStatus(1);
            user.setPhoneStatus(0);
            user.setGoogleStatus(0);
        }else{
            user.setPhone(accountNumber);
            //注册类型  1是手机
            user.setRegisterType(1);
            user.setEmailStatus(0);
            user.setPhoneStatus(1);
            user.setGoogleStatus(0);
        }
        user.setPassword(UnixCrypt.crypt(pwd, DigestUtils.sha256Hex(pwd)));
        user.setLockStatus(0);
        user.setCheckStatus(0);
        user.setNeedTransactionPassword(0);
        user.setCode(code);
        user.setLevel(1);
        user.setSource(sourceType);
        user.setRegisterTime(new Date());
        user.setUpdateTime(new Date());
        user.setType(1);
        mapper.insertUserSelective(user);
        //推送密码到邮箱或者手机号
        if (accountNumber.contains("@")){
            emailService.sendEmailPwd(accountNumber,pwd);
        }else{
            smsService.sendPwd(user.getPhone(),pwd);
        }
        return user;
    }

    @DataSource(DataSourceType.SLAVE)
    @Override
    public Member checkUser(String accountNumber) {
        Member member = memberMapper.selectByPhoneOrEmail(accountNumber);
        return member;
    }

    @Transactional
    @DataSource(DataSourceType.MASTER)
    @Override
    public void insertUserCoin(Integer userId, Integer coinId) {
        Integer row = mapper.getUserCoinRecord(userId,coinId);
        //如果没有记录
        if (row==0) {
            //查询用户资产表中是否有对应的同类型的币的地址或者密码
            UserCoin uc=new UserCoin();
            uc.setUserId(userId);
            uc.setCoinId(coinId);
            uc.setBanlance(new BigDecimal("0"));
            uc.setFreezingBanlance(new BigDecimal("0"));
            uc.setCreateTime(new Date());
            uc.setUpdateTime(new Date());
            userCoinMapper.insertSelective(uc);
        }
    }

    @Transactional
    @DataSource(DataSourceType.MASTER)
    @Override
    public Map<String, Object> takeRedPacket(Integer userId, RedPacketVO vo,String lang) throws VoException {
        GrabRedPacketLog log = new GrabRedPacketLog();
        //根据红包id查询红包信息
        RedPacket redPacket = mapper.getRedPacketById(vo.getRedPacketId());
        String msg = null;
        //如果红包已过期,提示
        if (new Date().after(redPacket.getExpireTime())){
            msg = "红包已过期";
            throw new VoException("2",msg);
        }
        //如果红包已被领取完
        if (redPacket.getTotalPacketQty()-redPacket.getGrabPacketQty()<=0){
            msg = "手慢了，红包派完了";
            throw new VoException("3",msg);
        }
        //抢到的红包金额
        BigDecimal amount = null;
        //判断是普通红白还是随机红包  1普通 2随机
        if (redPacket.getType().equals(1)){
            BigDecimal indexAmout = redPacket.getTotalCoinQty().divide(new BigDecimal(redPacket.getTotalPacketQty()));
            log.setCoinQty(indexAmout);
            log.setUserId(userId);
            log.setRedPacketId(vo.getRedPacketId());
            log.setCreateTime(new Date());
            log.setSources(vo.getSource());
            log.setCny(redPacket.getTotalCny().divide(new BigDecimal(redPacket.getTotalPacketQty()),2,BigDecimal.ROUND_HALF_UP));
            log.setCoinName(redPacket.getCoinName());
            log.setCoinId(redPacket.getCoinId());
            //拆红包
            Integer count = mapper.updateRedPacket(vo.getRedPacketId(),log.getCoinQty());
            //如果拆红包成功
            if(count!=null && count>0){
                //修改资产数据数据，
                mapper.addUserCoin(redPacket.getCoinId(),userId,log.getCoinQty());
                //插入抢红包数据
                mapper.insertLogs(log);
                amount = indexAmout;
            }
        }else{
            //随机红包
            //如果不是最后一人领取


            Integer remainingPacketQty = redPacket.getTotalPacketQty()-redPacket.getGrabPacketQty();
            BigDecimal remainingCoinQty = redPacket.getTotalCoinQty().subtract(redPacket.getGrabCoinQty());
            //获取领取红包的金额
            BigDecimal indexAmout = getRandomMoney(remainingPacketQty,remainingCoinQty);
            log.setCoinQty(indexAmout);
            log.setUserId(userId);
            log.setRedPacketId(vo.getRedPacketId());
            log.setCreateTime(new Date());
            log.setSources(vo.getSource());
            log.setCny(redPacket.getTotalCny().multiply(indexAmout).divide(redPacket.getTotalCoinQty(),2,BigDecimal.ROUND_HALF_UP));
            log.setCoinName(redPacket.getCoinName());
            log.setCoinId(redPacket.getCoinId());
            //拆红包
            Integer count = mapper.updateRedPacket(vo.getRedPacketId(),log.getCoinQty());
            //如果拆红包成功
            if(count!=null && count>0){
                //修改资产数据数据，
                mapper.addUserCoin(redPacket.getCoinId(),userId,log.getCoinQty());
                //添加抢红包数据
                mapper.insertLogs(log);

                amount=indexAmout;
            }
        }
        if (amount==null){
            msg = "手慢了，红包派完了";
            throw new VoException("3",msg);
        }
        BigDecimal cnyPrice = userCoinService.selectCnyPriceByCoinId(redPacket.getCoinId());
        Map<String,Object> map = new HashMap<>();
        map.put("coinName",redPacket.getCoinName());
        map.put("coinQty",amount);
        map.put("wishWords",redPacket.getWishWords());
        map.put("accountNumber",redPacket.getAccountNumber());
        map.put("cnyPrice",cnyPrice);
        return map;

    }

    @DataSource(DataSourceType.SLAVE)
    @Override
    public Map<String,Object> checkTakeRedPacket(Integer userId, Integer redPacketId) {
        Map<String,Object> map = mapper.checkTakeRedPacket(userId,redPacketId);
        return map;
    }

    @DataSource(DataSourceType.SLAVE)
    @Override
    public Map<String, Object> findRedPacketRecordByUserId(UserRedPacketVO vo) {
        PageHelper.startPage(vo.getPageNo(),vo.getPageSize());
        List<RedPacketRecord> redPacketVos = mapper.findRedPacketPageByUserId(vo.getUserId());
        PageInfo<RedPacketRecord> pageInfo = new PageInfo<>(redPacketVos);
        BigDecimal totalCNY = mapper.getSumCnyByUserId(vo.getUserId());
        Map<String,Object> map = new HashMap<>();
        map.put("totalCNY",totalCNY);
        map.put("pageInfo",pageInfo);
        return map;
    }

    @DataSource(DataSourceType.SLAVE)
    @Override
    public Map<String, Object> takeRedPacketDetails(UserRedPacketVO vo) {
        PageHelper.startPage(vo.getPageNo(),vo.getPageSize());
        Map<String,Object> redPacketInfo = mapper.getRedPacketInfo(vo.getId());
        redPacketInfo.put("id",vo.getId());
        if ((Integer)(redPacketInfo.get("type"))==1){
            redPacketInfo.put("title","【普通红包】不夸张地说，这是今年最该抢的币");
            redPacketInfo.put("desc","且领且珍惜 此处省去一万字......");
        }else{
            redPacketInfo.put("title","【拼手气红包】不夸张地说，这是今年最该抢的币");
            redPacketInfo.put("desc","且领且珍惜 此处省去一万字......");
        }
        redPacketInfo.put("url",url+vo.getId()+"/"+redPacketInfo.get("userId"));
        List<Map<String,Object>> redPacketLogDetails = mapper.getRedPacketDetails(vo.getId());
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(redPacketLogDetails);
        Map<String,Object> map = new HashMap<>();
        map.put("redPacketInfo",redPacketInfo);
        map.put("redPacketLogDetailsPageInfo",pageInfo);
        return map;
    }

    @DataSource(DataSourceType.SLAVE)
    @Override
    public Map<String, Object> grabRedPacketRecord(UserRedPacketVO vo) {
        BigDecimal totalCNY = mapper.grabRedPacketTotalCny(vo.getUserId());
        List<Map<String,Object>> grabRedPacketInfo = mapper.grabRedPacketRecord(vo.getUserId());
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(grabRedPacketInfo);
        Map<String,Object> map = new HashMap<>();
        map.put("totalCNY",totalCNY);
        map.put("grabRedPacketPageInfo",pageInfo);
        return map;
    }

    @DataSource(DataSourceType.SLAVE)
    @Override
    public Integer getCoinByRedPacketId(Integer redPacketId) {
        return mapper.getCoinByRedPacketId(redPacketId);
    }

    @DataSource(DataSourceType.SLAVE)
    @Override
    public Map<String,Object> initRedPacket(int id) {
        return mapper.initRedPacket(id);
    }

    @DataSource(DataSourceType.SLAVE)
    @Override
    public List<RedPacketLimitInfo> index(int type) {
        List<RedPacketLimit> list = redisUtil.getList("redPacketLimit", RedPacketLimit.class);
        if (list!=null&&list.size()!=0){
            list = getRedPacketLimitByType(list,type);
        }else{
            list = mapper.getLimit();
            redisUtil.setList("redPacketLimit",list,-1);
            list = getRedPacketLimitByType(list,type);
        }
        List<RedPacketLimitInfo> resultList = new ArrayList<>();
        for (RedPacketLimit redPacketLimit : list) {
            RedPacketLimitInfo info = new RedPacketLimitInfo();
            info.setId(redPacketLimit.getId());
            info.setCoinId(redPacketLimit.getCoinId());
            info.setLimitCoinQty(redPacketLimit.getLimitCoinQty());
            info.setLimitPacketQty(redPacketLimit.getLimitPacketQty());
            info.setCoinName(redPacketLimit.getCoinName());
            info.setCnyPrice(userCoinService.selectCnyPriceByCoinId(redPacketLimit.getCoinId()));
            info.setType(redPacketLimit.getType());
            resultList.add(info);
        }
        return resultList;
    }

    @DataSource(DataSourceType.SLAVE)
    @Override
    public List<Map<String, Object>> selectRedPacketUserCoin(List<RedPacketLimitInfo> list,int userId) {
        List<Map<String,Object>> userCoinList = mapper.selectRedPacketUserCoin(list,userId);
        for (Map<String, Object> map : userCoinList) {
            BigDecimal cnyPrice = userCoinService.selectCnyPriceByCoinId((Integer) map.get("coinId"));
            map.put("cnyPrice",cnyPrice);
        }
        return userCoinList;
    }

    private List<RedPacketLimit> getRedPacketLimitByType(List<RedPacketLimit> list, int type) {
        List<RedPacketLimit> newList = list.stream()
                .filter(e ->
                        e.getType().equals(type)
                ).collect(Collectors.toList());
        return newList;
    }

    public RedPacketLimit getRedPacketLimit(List<RedPacketLimit> list,Integer type,Integer coinId){
        Optional<RedPacketLimit> optionalRedPacketLimit = list.stream()
                .filter(redPacketLimit ->
                        redPacketLimit.getCoinId().equals(coinId)
                                && redPacketLimit.getType().equals(type)
                )
                .findFirst();

        if (optionalRedPacketLimit.isPresent()) {
            RedPacketLimit redPacketLimit = optionalRedPacketLimit.get();
            return redPacketLimit;
        }
        return null;
    }

    public static void main(String[] args) {
//        long a = 1000000000000L;
//        long rad = (long)Math.random()*a;
//        System.out.println(rad);
        int count =6;
        BigDecimal a = new BigDecimal(6);
        RedPacketServiceImpl r = new RedPacketServiceImpl();
        for (int i = 0; i < 6; i++) {
            System.out.println("amount"+(i+1)+"="+r.getRandomMoney(count,a));
        }

    }


    @DataSource(DataSourceType.SLAVE)
    @Override
    public RedPacketVo selectById(Integer redPacketId) {
        return mapper.selectRedPacketVoById(redPacketId);
    }
    @DataSource(DataSourceType.SLAVE)
    @Override
    public PageInfo<RedPacketVo> selectByUserId(Integer userId, Integer pageNum, Integer pageSize) {

        PageHelper.startPage(pageNum,pageSize);
        List<RedPacketVo> redPacketVos = mapper.selectRedPacketVoByUserIdId(userId);

        return new PageInfo<>(redPacketVos);
    }


    public String  initPassword(){
        String a = "";
        for (int i=0;i<6;i++){
            a+= (int)(1+Math.random()*10);
        }
        return a;
    }


    public BigDecimal getRandomMoney(int remainSize,BigDecimal remainMoney) {
        // remainSize 剩余的红包数量
        // remainMoney 剩余的钱
        if (remainSize == 1) {
            return remainMoney;
        }
        Random r = new Random();
        BigDecimal min   = BigDecimal.ONE.divide(BigDecimal.TEN.pow(8));
        BigDecimal max   = remainMoney.multiply(new BigDecimal(2)).divide(new BigDecimal(remainSize),8,BigDecimal.ROUND_FLOOR);
        BigDecimal money = max.multiply(new BigDecimal(r.nextDouble())).setScale(8,BigDecimal.ROUND_FLOOR);
        money = money.compareTo(min)<=0?min:money;
        return money;
    }


}
