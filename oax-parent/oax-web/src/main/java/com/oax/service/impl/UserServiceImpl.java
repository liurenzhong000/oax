package com.oax.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.oax.Constant;
import com.oax.common.*;
import com.oax.common.enums.RedisKeyEnum;
import com.oax.common.enums.SysConfigEnum;
import com.oax.druid.DataSource;
import com.oax.druid.DataSourceType;
import com.oax.entity.admin.Lv2CheckLog;
import com.oax.entity.admin.param.PageParam;
import com.oax.entity.admin.param.UserInviteParam;
import com.oax.entity.admin.vo.UserInviteVo;
import com.oax.entity.front.ErrorPasswordLog;
import com.oax.entity.front.Member;
import com.oax.entity.front.UserCenter;
import com.oax.entity.front.UserLoginLog;
import com.oax.entity.front.vo.InviteUserVo;
import com.oax.exception.VoException;
import com.oax.mapper.admin.Lv2CheckLogMapper;
import com.oax.mapper.admin.UserInfoMapper;
import com.oax.mapper.front.ErrorPasswordLogMapper;
import com.oax.mapper.front.MemberMapper;
import com.oax.mapper.front.UserLoginLogMapper;
import com.oax.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

	@Value("${host.invateUrl}")
	private String invateUrl;

	@Autowired
	private MemberMapper memberMapper;
	@Autowired
	private AccessTokenManager tokenManager;
	@Autowired
	private UserLoginLogMapper userLoginDao;
	@Autowired
	private Lv2CheckLogMapper lv2CheckLogMapper;
	@Autowired
	private ErrorPasswordLogMapper errorPasswordLogMapper;
	@Autowired
	private I18nMessageService I18nMessageService;
	@Autowired
	private  EmailCaptchaService emailService;
	@Autowired
	private  SmsCaptchaService smsService;
	@Autowired
	private SysConfigService sysConfigService;
	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private UserInfoMapper userInfoMapper;

	@Override
	@Transactional
	@DataSource(DataSourceType.MASTER)
	public Member login(HttpServletRequest request,String userName,String password,String lang) throws VoException {
		String ip= SysUtil.getRemoteIp(request);
		if (StringUtils.isNotBlank(ip) && StringUtils.contains(ip, ",")) {//加了防护好像ip会变成115.199.127.106, 185.254.242.41
			ip = StringUtils.substringBefore(ip, ",");
		}
		Member user=memberMapper.selectByPhoneOrEmail(userName);
		if(user==null) {
			throw new VoException(I18nMessageService.getMsg(10062, lang));
		}else {
			//查询当天错误次数
			Integer errorCounts=errorPasswordLogMapper.selectByIpInDay(ip,1);
			if(errorCounts>=Constant.LONGIN_COUNT_LIMIT) {
				throw new VoException(I18nMessageService.getMsg(10054, lang));
			}

			if(!MD5.encrypt(password).equals(user.getPassword())) {
				ErrorPasswordLog record=new ErrorPasswordLog();
				record.setUserId(user.getId());
				record.setType(1);
				record.setIp(ip);
				record.setCreateTime(new Date());
				record.setUpdateTime(new Date());
				errorPasswordLogMapper.insertSelective(record);

				Integer counts=Constant.LONGIN_COUNT_LIMIT-errorCounts-1;
				throw new VoException(I18nMessageService.getMsg(10055, lang)+" "+counts+" "+I18nMessageService.getMsg(10056, lang));
			}
			if(user.getLevel()==0) {
				throw new VoException("10020",I18nMessageService.getMsg(10020, lang));
			}
			if(user.getLockStatus()==1) {
				throw new VoException(I18nMessageService.getMsg(10021, lang));
			}
		}

		return user;
	}


	@Override
	public void logout(Integer userId) {
		tokenManager.delete(userId);
	}


	@Override
	@DataSource(DataSourceType.SLAVE)
	public boolean checkExist(String account) {
		boolean flag=false;
		Member user=memberMapper.selectByPhoneOrEmail(account);
		if(user!=null) {
			flag=true;
		}
		return flag;
	}


	@Override
	@DataSource(DataSourceType.MASTER)
	public int register(Member user,String parentCode) {
		if(StringUtils.isNotBlank(parentCode)) {
			Integer parentId=selectIdByCode(parentCode);
			if(parentId!=null) {
				user.setFromUserId(parentId);
			}
		}
		int count=memberMapper.insertSelective(user);
		return count;

	}


	@Override
	@DataSource(DataSourceType.SLAVE)
	public Integer selectIdByCode(String code) {
		Integer id=memberMapper.selectIdByCode(code);
		return id;
	}


	@Override
	@DataSource(DataSourceType.MASTER)
	public int forgetPassword(Integer userId, String password) {
		Member user=new Member();
		user.setId(userId);
		user.setPassword(MD5.encrypt(password));
		int b=memberMapper.updateByPrimaryKeySelective(user);
		return b;
	}


	@Override
	@DataSource(DataSourceType.MASTER)
	public int setTransactionPassword(Member entity) throws VoException {
		int b=memberMapper.updateByPrimaryKeySelective(entity);
		return b;
	}


	@Override
	@DataSource(DataSourceType.MASTER)
	public int updateTransactionPassword(Member entity,String oldPassword,String lang) throws VoException {
		int b=0;

		Member user=memberMapper.selectByPrimaryKey(entity.getId());
		if(MD5.encrypt(oldPassword).equals(user.getTransactionPassword())) {
			b=memberMapper.updateByPrimaryKeySelective(entity);
		}else {
			throw new VoException("10058",I18nMessageService.getMsg(10058, lang));
		}

		return b;
	}


	@Override
	@DataSource(DataSourceType.MASTER)
	public int updateLoginPassword(Member entity,String oldPassword,String lang) throws VoException {
		int b=0;
		Member user=memberMapper.selectByPrimaryKey(entity.getId());

		if(MD5.encrypt(oldPassword).equals(user.getPassword())) {
			b=memberMapper.updateByPrimaryKeySelective(entity);

		}else {
			throw new VoException("10058",I18nMessageService.getMsg(10058, lang));
		}
		return b;
	}


	@Override
	@DataSource(DataSourceType.MASTER)
	public boolean bindEmail(Member user,String code,String lang) throws VoException {
		boolean b=true;
		try {
			boolean flag=emailService.checkEmailCode(code, user.getEmail());
			if(flag) {
				int a=memberMapper.updateByPrimaryKeySelective(user);
				if(a<=0){
					b=false;
				}
			}else {
				throw new VoException(I18nMessageService.getMsg(10019, lang));
			}
		} catch (Exception e) {
			throw new VoException(I18nMessageService.getMsg(10019, lang));
		}

		return b;
	}

	@Override
	@DataSource(DataSourceType.MASTER)
	public boolean bindPhone(HttpServletRequest request,Member user, String code,String lang) throws VoException {
		boolean b=true;
		try {
			boolean flag=smsService.checkSms(request,user.getPhone(),code,lang);
			if(flag) {
				int a=memberMapper.updateByPrimaryKeySelective(user);
				if(a<=0){
					b=false;
				}
			}else {
				throw new VoException(I18nMessageService.getMsg(10007, lang));
			}
		} catch (Exception e) {
			throw new VoException(I18nMessageService.getMsg(10007, lang));
		}

		return b;
	}

	@Override
	@DataSource(DataSourceType.MASTER)
	public boolean updatePhone(Member user,String lang) throws VoException {
		boolean b=true;
		try {
			int a=memberMapper.updateByPrimaryKeySelective(user);
			if(a<=0){
				b=false;
			}
		} catch (Exception e) {
			throw new VoException(I18nMessageService.getMsg(10101, lang));
		}
		return b;
	}


	@Override
	@DataSource(DataSourceType.SLAVE)
	public Member selectById(String userId) {
		Member user=memberMapper.selectByPrimaryKey(Integer.parseInt(userId));
		return user;
	}


	@Override
	@DataSource(DataSourceType.MASTER)
	public boolean bindGoogleCode(Member member) throws VoException {
		int count=memberMapper.updateByPrimaryKeySelective(member);
		if(count>0) {
			return true;
		}else {
			return false;
		}
	}


	@Override
	@DataSource(DataSourceType.MASTER)
	public int identityAuthen(Member user) {

		return memberMapper.updateByPrimaryKeySelective(user);
	}

	@Override
	@DataSource(DataSourceType.MASTER)
	public PageInfo<UserInviteVo> getByUserIdQueryUserInvite(UserInviteParam userInviteParam)
	{
		PageHelper.startPage(1,5);
		List<UserInviteVo> inviteUserList = memberMapper.getByUserIdQueryUserInvite(userInviteParam);
		return new PageInfo<>(inviteUserList);
	}


	@Override
	@DataSource(DataSourceType.MASTER)
	public int updateByPrimaryKeySelective(Member user) {
		return memberMapper.updateByPrimaryKeySelective(user);
	}


	@Override
	@DataSource(DataSourceType.SLAVE)
	public Map<String, Object> userCenter(String userId) {
		Map<String,Object> resultMap=new HashMap<>();

		Member member=memberMapper.selectByPrimaryKey(Integer.parseInt(userId));
		List<UserLoginLog> userLoginLogList=userLoginDao.selectByUserId(Integer.parseInt(userId));
//		Integer registercounts=memberMapper.selectInvateCounts(Integer.parseInt(userId),0);
//		Integer vertycounts=memberMapper.selectInvateCounts(Integer.parseInt(userId),1);
		Integer registercounts = getRegistercounts(Integer.parseInt(userId));
		Integer vertycounts = getVertycounts(Integer.parseInt(userId));

		UserCenter usercenter=new UserCenter();
		usercenter.setId(member.getId());
		usercenter.setCheckStatus(member.getCheckStatus());
		usercenter.setCode(member.getCode());
		usercenter.setEmail(member.getEmail());
		usercenter.setEmailStatus(member.getEmailStatus());
		usercenter.setGoogleStatus(member.getGoogleStatus());

		usercenter.setIdNo(member.getIdNo());
		usercenter.setLevel(member.getLevel());
		usercenter.setNeedTransactionPassword(member.getNeedTransactionPassword());
		usercenter.setPhone(member.getPhone());
		usercenter.setPhoneStatus(member.getPhoneStatus());
		usercenter.setRegisterCounts(registercounts);
		usercenter.setVertyCounts(vertycounts);
		usercenter.setGoogleKey(member.getGoogleKey());
		usercenter.setInvateAddress(invateUrl+"?code="+member.getCode());
		if(member.getCheckStatus()==2) {
			usercenter.setIdName(member.getIdName());
		}else {
			if(member.getRegisterType()==1) {
				usercenter.setIdName(member.getPhone());
			}else {
				usercenter.setIdName(member.getEmail());
			}

		}

		if(StringUtils.isNotBlank(member.getTransactionPassword())) {
			usercenter.setTransactionPasswordIsNull(0);
		}else {
			usercenter.setTransactionPasswordIsNull(1);
		}

		// 查询用户不同等级总额度
		BigDecimal LEVEL1_BTC = new BigDecimal(sysConfigService.getConfig(SysConfigEnum.LEVEL1_BTC.getName()));
		BigDecimal LEVEL2_BTC = new BigDecimal(sysConfigService.getConfig(SysConfigEnum.LEVEL2_BTC.getName()));
		BigDecimal LEVEL3_BTC = new BigDecimal(sysConfigService.getConfig(SysConfigEnum.LEVEL3_BTC.getName()));

		resultMap.put("usercenter", usercenter);
		resultMap.put("LEVEL1_BTC", LEVEL1_BTC);
		resultMap.put("LEVEL2_BTC", LEVEL2_BTC);
		resultMap.put("LEVEL3_BTC", LEVEL3_BTC);
		resultMap.put("userLoginLogList", userLoginLogList);
		resultMap.put("showLeader", member.getType() == 3); //是否显示团队业绩
		return resultMap;
	}

	private Integer getRegistercounts(Integer userId){
		List<Integer> oneLevelUserIds = new ArrayList<>();
		List<Integer> twoLevelUserIds = new ArrayList<>();
		List<Integer> threeLevelUserIds = new ArrayList<>();
		oneLevelUserIds = memberMapper.getIdsByFromUserId(userId);
		if (!EmptyHelper.isEmpty(oneLevelUserIds)) {
			twoLevelUserIds = memberMapper.getIdsByFromUserIds(oneLevelUserIds);
		}
		if (!EmptyHelper.isEmpty(twoLevelUserIds)) {
			//错误统计代码
			threeLevelUserIds = memberMapper.getIdsByFromUserIds(oneLevelUserIds);

			//TODO 正确统计代码
//			threeLevelUserIds = memberMapper.getIdsByFromUserIds(twoLevelUserIds);
		}
		Integer registercounts = oneLevelUserIds.size() + twoLevelUserIds.size() + threeLevelUserIds.size();
		return registercounts;
	}

	private Integer getVertycounts(Integer userId){
		//错误统计代码
		List<Integer> oneLevelUserIds = new ArrayList<>();
		List<Integer> twoLevelUserIds = new ArrayList<>();
		List<Integer> threeLevelUserIds = new ArrayList<>();
		oneLevelUserIds = memberMapper.getCheckIdsByFromUserId(userId);
		if (!EmptyHelper.isEmpty(oneLevelUserIds)) {
			twoLevelUserIds = memberMapper.getCheckIdsByFromUserIds(oneLevelUserIds);
		}
		if (!EmptyHelper.isEmpty(twoLevelUserIds)) {
			threeLevelUserIds = memberMapper.getCheckIdsByFromUserIds(oneLevelUserIds);
		}
		Integer vertycounts = oneLevelUserIds.size() + twoLevelUserIds.size() + threeLevelUserIds.size();



		//TODO 正确统计代码
//		List<Integer> oneLevelUserIds = new ArrayList<>();
//		List<Integer> twoLevelUserIds = new ArrayList<>();
//		oneLevelUserIds = memberMapper.getIdsByFromUserId(userId);
//		if (!EmptyHelper.isEmpty(oneLevelUserIds)) {
//			twoLevelUserIds = memberMapper.getIdsByFromUserIds(oneLevelUserIds);
//		}
//		//未认证的下级的下级认证了要统计上
//		List<Integer> oneLevelCheckedIds = new ArrayList<>();
//		List<Integer> twoLevelCheckedIds = new ArrayList<>();
//		List<Integer> threeLevelCheckedIds = new ArrayList<>();
//		oneLevelCheckedIds = memberMapper.getCheckIdsByFromUserId(userId);
//		if (!EmptyHelper.isEmpty(oneLevelUserIds)) {
//			twoLevelCheckedIds = memberMapper.getCheckIdsByFromUserIds(oneLevelUserIds);
//		}
//		if (!EmptyHelper.isEmpty(twoLevelUserIds)) {
//			threeLevelCheckedIds = memberMapper.getCheckIdsByFromUserIds(twoLevelUserIds);
//		}
//		Integer vertycounts = oneLevelCheckedIds.size() + twoLevelCheckedIds.size() + threeLevelCheckedIds.size();
		return vertycounts;
	}

	@Override
	@DataSource(DataSourceType.SLAVE)
	public PageInfo<InviteUserVo> inviteList(Integer userId, PageParam pageParam) {
		//查询出3级的用户id
		Map<Integer, Object> inviteIdMap = inviteIdMap(userId);
		if (EmptyHelper.isEmpty(inviteIdMap)) {
			return new PageInfo<>(new ArrayList<>());
		}
		PageHelper.startPage(pageParam.getPageNum(), pageParam.getPageSize());
		List<InviteUserVo> inviteUserVos = memberMapper.getInviteUserVoByIds(Lists.newArrayList(inviteIdMap.keySet()));
		inviteUserVos.forEach(item -> {
			item.setTier(inviteIdMap.get(item.getId()).toString());
			item.setAuthentication(item.getCheckStatus()>1);
		});
//		inviteUserVos.sort(Comparator.comparing(InviteUserVo::getId).thenComparing(InviteUserVo::getLevel));
		return new PageInfo<>(inviteUserVos);
	}

	private Map<Integer, Object> inviteIdMap(Integer userId) {
		Map<Integer, Object> idMaps = new LinkedHashMap<>();
		List<Integer> oneLevelUserIds = new ArrayList<>();
		List<Integer> twoLevelUserIds = new ArrayList<>();
		List<Integer> threeLevelUserIds = new ArrayList<>();
		oneLevelUserIds = memberMapper.getIdsByFromUserId(userId);
		oneLevelUserIds.forEach(item -> idMaps.put(item, "一"));
		if (!EmptyHelper.isEmpty(oneLevelUserIds)) {
			twoLevelUserIds = memberMapper.getIdsByFromUserIds(oneLevelUserIds);
			twoLevelUserIds.forEach(item -> idMaps.put(item, "二"));
		}
		if (!EmptyHelper.isEmpty(twoLevelUserIds)) {
			threeLevelUserIds = memberMapper.getIdsByFromUserIds(twoLevelUserIds);
			threeLevelUserIds.forEach(item -> idMaps.put(item, "三"));
		}
		return idMaps;
	}

    @Override
    public boolean existByIdNo(String idNo) {
        return memberMapper.existByIdNo(idNo);
    }

    @Override
	@DataSource(DataSourceType.MASTER)
	@Async
	public void addLoginLog(HttpServletRequest request,Member user,Integer source,String lang) {
		//添加纪录
		String ip=SysUtil.getRemoteIp(request);

		String ipAddress="";

		try {
//			String jsonstr=HttpRequestUtil.sendPost("http://ip.taobao.com/service/getIpInfo.php", "ip="+ip);
//			if(StringUtils.isNotBlank(jsonstr)&&"0".equals(JSONObject.parseObject(jsonstr).getString("code"))) {
//				JSONObject json=JSONObject.parseObject(jsonstr);
//
//				String country=json.getJSONObject("data").getString("country");
//				String province=json.getJSONObject("data").getString("region");
//				String city=json.getJSONObject("data").getString("city");
//				ipAddress=country+" "+province+" "+city;
//			}
			if (StringUtils.isNotBlank(ip) && StringUtils.contains(ip, ",")) {//加了防护好像ip会变成115.199.127.106, 185.254.242.41
				ip = StringUtils.substringBefore(ip, ",");
			}
			String jsonStr = HttpRequestUtil.sendGet("http://ip.360.cn/IPQuery/ipquery", "ip=" + ip);
			if(StringUtils.isNotBlank(jsonStr)&&"0".equals(JSONObject.parseObject(jsonStr).getString("errno"))) {
				JSONObject json=JSONObject.parseObject(jsonStr);
				ipAddress=json.getString("data");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	/*	if(StringUtils.isNotBlank(user.getEmail())) {
			boolean b=ipIsChange(user.getId(),ip);
			if(b) {
				emailService.sendIpChangeEmail(user.getEmail(), lang);
			}
		}*/
		UserLoginLog record=new UserLoginLog();
		record.setAddress(ipAddress);
		record.setIp(ip);
		record.setLoginTime(new Date());
		record.setSource(source);
		record.setUserId(user.getId());

		userLoginDao.insertSelective(record);
	}


	@Override
	@DataSource(DataSourceType.SLAVE)
	public Member selectByEmail(String email) {
		Member user=memberMapper.selectByPhoneOrEmail(email);
		return user;
	}


	@Override
	@DataSource(DataSourceType.SLAVE)
	public Member selectByPhone(String phone) {
		Member user=memberMapper.selectByPhoneOrEmail(phone);
		return user;
	}


	@Override
	@DataSource(DataSourceType.MASTER)
	public int forgetTransactionPassword(Member user) {
		int b=memberMapper.updateByPrimaryKeySelective(user);
		return b;
	}


	@Override
	@DataSource(DataSourceType.SLAVE)
	public String selectFailReasonByUserId(Integer userId) {
		String reason="";
		Lv2CheckLog log=lv2CheckLogMapper.selectOneByUserId(userId);
		if(log!=null) {
			reason=log.getRemark();
		}
		return reason;
	}


	@Override
	@DataSource(DataSourceType.SLAVE)
	public Map<String, Object> myInvate(String userId) {
		Map<String, Object> map=new HashMap<>();

		Member member=memberMapper.selectByPrimaryKey(Integer.parseInt(userId));
		Integer registercounts=memberMapper.selectInvateCounts(Integer.parseInt(userId),0);
		Integer vertycounts=memberMapper.selectInvateCounts(Integer.parseInt(userId),1);
		String invateAddress=invateUrl+"?code="+member.getCode();

		map.put("registercounts", registercounts);
		map.put("vertycounts", vertycounts);
		map.put("invateAddress", invateAddress);
		return map;
	}


	@Override
	@DataSource(DataSourceType.SLAVE)
	public Member selectByApiKey(String apiKey) {
		String key = RedisKeyEnum.API_LIST.getKey()+apiKey;
		Member member = redisUtil.getObject(key, Member.class);
		if(member==null){
			member=memberMapper.selectByApiKey(apiKey);
			redisUtil.setObject(key,member);
		}
		return member;
	}


	@Override
	@DataSource(DataSourceType.SLAVE)
	public Map<String, Object> queryCheckType(String username,String lang) throws VoException {
		Map<String, Object> map=new HashMap<>();
		Member member=memberMapper.selectByPhoneOrEmail(username);
		if(member!=null) {
			if(member.getGoogleStatus()==1) {
				map.put("googleStatus", 1);
			}else {
				map.put("googleStatus", 0);
			}

			if(member.getPhoneStatus()==1) {
				Date now=new Date();
				if(now.getTime()-member.getRegisterTime().getTime()>180000) {
					map.put("phoneStatus", 1);
				}else {
					map.put("phoneStatus", 0);
				}

			}else {
				map.put("phoneStatus", 0);
			}

			if(member.getEmailStatus()==1) {
				map.put("emailStatus", 1);
			}else {
				map.put("emailStatus", 0);
			}
		}else {
			throw new VoException(I18nMessageService.getMsg(10013, lang));
		}
		return map;
	}

	@Override
	@DataSource(DataSourceType.SLAVE)
	public String getPhoneOrEmailById(Integer id) {
		return memberMapper.getPhoneOrEmailById(id);
	}


}