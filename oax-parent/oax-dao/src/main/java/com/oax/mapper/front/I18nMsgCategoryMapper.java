package com.oax.mapper.front;

import com.oax.entity.front.I18nMsgCategory;

public interface I18nMsgCategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(I18nMsgCategory record);

    int insertSelective(I18nMsgCategory record);

    I18nMsgCategory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(I18nMsgCategory record);

    int updateByPrimaryKey(I18nMsgCategory record);
}