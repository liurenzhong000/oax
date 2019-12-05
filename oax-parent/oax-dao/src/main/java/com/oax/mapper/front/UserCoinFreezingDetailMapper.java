package com.oax.mapper.front;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oax.entity.front.UserCoinFreezingDetail;

import java.util.List;

public interface UserCoinFreezingDetailMapper extends BaseMapper<UserCoinFreezingDetail> {

    List<UserCoinFreezingDetail> selectYesterdayList();
}
