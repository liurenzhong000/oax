package com.oax.admin.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.oax.admin.service.OperationLogService;
import com.oax.entity.admin.OperationLog;
import com.oax.mapper.admin.OperationLogMapper;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/7/25
 * Time: 21:41
 */
@Service
public class OperationLogServiceImpl implements OperationLogService {

    @Autowired
    private OperationLogMapper operationLogMapper;


    @Override
    @Async
    public void operationLog(OperationLog operationLog) {

         operationLogMapper.insertSelective(operationLog);
    }
}
