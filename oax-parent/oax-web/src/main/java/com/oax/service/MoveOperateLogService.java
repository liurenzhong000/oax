package com.oax.service;

import com.oax.entity.front.MovesayOperateLog;

import java.util.List;

public interface MoveOperateLogService {
    List<MovesayOperateLog> getIncomList(String userId);
}
