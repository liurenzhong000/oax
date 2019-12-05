package com.oax.mapper.front;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oax.entity.admin.vo.BonusLogVo;
import com.oax.entity.front.BonusLog;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface BonusLogMapper extends BaseMapper<BonusLog> {

    List<BonusLogVo> selectVoForExcel(@Param("startTime")Date startTime, @Param("endTime") Date endTime);

    List<BonusLogVo> selectEmployeeForExcel(@Param("startTime")Date startTime, @Param("endTime") Date endTime);

    BigDecimal getBonusLogMapper(@Param("startTime")Date startTime, @Param("endTime") Date endTime);
}
