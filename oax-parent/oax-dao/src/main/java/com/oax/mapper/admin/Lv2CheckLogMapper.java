package com.oax.mapper.admin;


import com.oax.entity.admin.Lv2CheckLog;

public interface Lv2CheckLogMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(Lv2CheckLog record);

    Lv2CheckLog selectByPrimaryKey(Integer id);

    Lv2CheckLog selectOneByUserId(Integer userId);

}