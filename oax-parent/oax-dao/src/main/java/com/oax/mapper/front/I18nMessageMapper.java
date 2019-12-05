package com.oax.mapper.front;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.oax.entity.front.I18nMessage;

@Mapper
public interface I18nMessageMapper {

    public List<I18nMessage> findList();

    public int add(I18nMessage i18nMessage);

}
