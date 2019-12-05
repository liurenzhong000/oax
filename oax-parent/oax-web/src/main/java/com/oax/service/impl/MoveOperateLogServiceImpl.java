package com.oax.service.impl;

import com.oax.druid.DataSource;
import com.oax.druid.DataSourceType;
import com.oax.entity.front.MovesayOperateLog;
import com.oax.mapper.front.MovesayOperateLogMapper;
import com.oax.service.MoveOperateLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MoveOperateLogServiceImpl implements MoveOperateLogService {

    @Autowired
    private MovesayOperateLogMapper movesayOperateLogMapper;

    @DataSource(DataSourceType.MASTER)
    public List<MovesayOperateLog> getIncomList(String userId)
    {
       return movesayOperateLogMapper.getIncomList(userId);
    }
}
