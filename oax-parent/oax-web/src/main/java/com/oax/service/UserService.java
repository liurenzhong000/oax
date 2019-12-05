package com.oax.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.github.pagehelper.PageInfo;
import com.oax.entity.admin.param.PageParam;
import com.oax.entity.admin.param.UserInviteParam;
import com.oax.entity.admin.vo.UserInviteVo;
import com.oax.entity.front.Member;
import com.oax.entity.front.vo.InviteUserVo;
import com.oax.exception.VoException;

public interface UserService {

	Member login(HttpServletRequest request,String userName, String password, String lang) throws VoException;

	void logout(Integer userId);

	boolean checkExist(String account);

	int register(Member user, String parentCode);

	Integer selectIdByCode(String code);

	int forgetPassword(Integer userId, String password);

	int setTransactionPassword(Member entity) throws VoException;

	int updateTransactionPassword(Member entity, String oldPassword, String lang) throws VoException;

	int updateLoginPassword(Member entity, String oldPassword, String lang) throws VoException;

	boolean bindEmail(Member user, String code, String lang) throws VoException;

	boolean bindPhone(HttpServletRequest request,Member user, String code, String lang) throws VoException;

	Member selectById(String userId);

	boolean bindGoogleCode(Member member) throws VoException;

	int identityAuthen(Member user);

	int updateByPrimaryKeySelective(Member user);

	Map<String, Object> userCenter(String userId);

	void addLoginLog(HttpServletRequest request, Member user, Integer source, String lang);

	Member selectByEmail(String email);

	Member selectByPhone(String phone);

	int forgetTransactionPassword(Member user);

	PageInfo<UserInviteVo> getByUserIdQueryUserInvite(UserInviteParam userInviteParam);

	String selectFailReasonByUserId(Integer userId);

	Map<String, Object> myInvate(String userId);

	Member selectByApiKey(String apiKey);

    Map<String, Object> queryCheckType(String username, String lang) throws VoException;

	boolean updatePhone(Member user, String lang)throws VoException;

	PageInfo<InviteUserVo> inviteList(Integer userId, PageParam pageParam);

	boolean existByIdNo(String idNo);

	String getPhoneOrEmailById(Integer id);


}