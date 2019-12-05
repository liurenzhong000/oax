package com.oax.mapper.front;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oax.entity.front.Robot;

import java.util.List;

public interface RobotMapper extends BaseMapper<Robot> {

    List<Robot> selectOpen();

}