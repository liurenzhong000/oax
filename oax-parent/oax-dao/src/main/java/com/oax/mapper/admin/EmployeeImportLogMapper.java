package com.oax.mapper.admin;

import com.oax.entity.admin.EmployeeImportLog;

public interface EmployeeImportLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(EmployeeImportLog record);

    int insertSelective(EmployeeImportLog record);

    EmployeeImportLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EmployeeImportLog record);

    int updateByPrimaryKeyWithBLOBs(EmployeeImportLog record);

    int updateByPrimaryKey(EmployeeImportLog record);
}