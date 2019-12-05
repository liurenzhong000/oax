package com.oax.mapper.front;

import com.oax.entity.admin.param.UserInviteParam;
import com.oax.entity.admin.vo.UserInviteVo;
import com.oax.entity.front.vo.InviteUserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.oax.entity.front.Member;

import java.util.Date;
import java.util.List;

@Mapper
public interface MemberMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Member record);

    int insertSelective(Member record);

    Member selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Member record);

    int updateByPrimaryKey(Member record);


    Member selectByPhoneOrEmail(String username);

    Integer selectIdByCode(String code);

    Integer selectInvateCounts(@Param("id") Integer id, @Param("flag") Integer flag);

    Member selectByApiKey(String apiKey);

    List<UserInviteVo> getByUserIdQueryUserInvite(UserInviteParam userInviteParam);

    /**
     * 根据userId查询下级user
     * @param userId
     * @return
     */
    List<Member> getChildrenUserByUserId(Integer userId);

    List<Integer> getIdsByFromUserId(Integer userId);

    List<Integer> getIdsByFromUserIds(@Param("userIds") List<Integer> userIds);

    List<Integer> getCheckIdsByFromUserId(Integer userId);

    List<Integer> getCheckIdsByFromUserIds(@Param("userIds") List<Integer> userIds);

    List<InviteUserVo> getInviteUserVoByIds(@Param("userIds") List<Integer> userIds);

    boolean existByIdNo(String idNo);

    List<Integer> getIdsByType(int i);

    String getPhoneOrEmailById(Integer id);
}
