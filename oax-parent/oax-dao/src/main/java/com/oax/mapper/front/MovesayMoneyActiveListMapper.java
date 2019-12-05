package com.oax.mapper.front;

import com.oax.entity.admin.param.rushPageParam;
import com.oax.entity.admin.vo.MovesayMoneyActiveListVO;
import com.oax.entity.front.MovesayMoneyActiveList;
import com.oax.entity.front.MovesayMoneyActiveListExample;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MovesayMoneyActiveListMapper {
    long countByExample(MovesayMoneyActiveListExample example);

    int deleteByExample(MovesayMoneyActiveListExample example);

    int deleteByPrimaryKey(String id);

    int insert(MovesayMoneyActiveList record);

    int insertSelective(MovesayMoneyActiveList record);

    List<MovesayMoneyActiveList> selectByExample(MovesayMoneyActiveListExample example);

    MovesayMoneyActiveList selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") MovesayMoneyActiveList record, @Param("example") MovesayMoneyActiveListExample example);

    int updateByExample(@Param("record") MovesayMoneyActiveList record, @Param("example") MovesayMoneyActiveListExample example);

    int updateByPrimaryKeySelective(MovesayMoneyActiveList record);

    int updateByPrimaryKey(MovesayMoneyActiveList record);

    int updateStatus(@Param("id") String id,
                     @Param("status") Integer status);

    List<MovesayMoneyActiveListVO> selectRushPage(rushPageParam rushPageParam);

    String  selectMaxOrderNo(Integer active_id);
}