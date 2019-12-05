package com.oax.admin.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oax.admin.service.EmployeeImportLogService;
import com.oax.entity.admin.EmployeeImportLog;
import com.oax.mapper.admin.EmployeeImportLogMapper;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/9/1
 * Time: 17:09
 */
@Service
public class EmployeeImportLogServiceImpl implements EmployeeImportLogService {
    @Autowired
    private EmployeeImportLogMapper employeeImportLogMapper;

    @Override
    public int insert(EmployeeImportLog employeeImportLog) {
        return employeeImportLogMapper.insertSelective(employeeImportLog);
    }
}
