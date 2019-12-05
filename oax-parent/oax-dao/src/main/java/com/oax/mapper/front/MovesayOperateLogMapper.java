package com.oax.mapper.front;

import com.oax.entity.front.MoveActiveNo;
import com.oax.entity.front.MovesayOperateLog;
import com.oax.entity.front.MovesayOperateLogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MovesayOperateLogMapper {
    long countByExample(MovesayOperateLogExample example);

    int deleteByExample(MovesayOperateLogExample example);

    int deleteByPrimaryKey(String id);

    int insert(MovesayOperateLog record);

    int insertSelective(MovesayOperateLog record);

    List<MovesayOperateLog> selectByExample(MovesayOperateLogExample example);

    MovesayOperateLog selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") MovesayOperateLog record, @Param("example") MovesayOperateLogExample example);

    int updateByExample(@Param("record") MovesayOperateLog record, @Param("example") MovesayOperateLogExample example);

    int updateByPrimaryKeySelective(MovesayOperateLog record);

    int updateByPrimaryKey(MovesayOperateLog record);

    String selectMaxOrderNo( MoveActiveNo param);

    List<MovesayOperateLog> getIncomList(String userId);

}