package com.oax.mapper.front;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.oax.entity.front.SysConfig;

@Mapper
public interface SysConfigMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysConfig record);

    int insertSelective(SysConfig record);

    SysConfig selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysConfig record);

    int updateByPrimaryKey(SysConfig record);

    /**
     * 根据 name获取 SysConfig
     *
     * @param name
     * @return
     */
    SysConfig selectByName(String name);
    List<SysConfig> selectAll();
}