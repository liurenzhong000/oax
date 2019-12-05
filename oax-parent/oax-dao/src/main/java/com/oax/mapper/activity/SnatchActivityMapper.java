package com.oax.mapper.activity;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oax.entity.activity.SnatchActivity;
import com.oax.entity.admin.vo.SnatchActivityAdminVo;
import com.oax.entity.front.vo.SnatchActivityVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Auther: hyp
 * @Date: 2019/1/16 18:53
 * @Description:
 */
public interface SnatchActivityMapper  extends BaseMapper<SnatchActivity> {

    List<SnatchActivityVo> listActivityByCoinId(Integer coinId);

    List<SnatchActivityVo> listNewlyLottery();

    List<SnatchActivityAdminVo> pageForAdmin(@Param("id") Integer id, @Param("configId") Integer configId, @Param("coinId") Integer coinId);
}
