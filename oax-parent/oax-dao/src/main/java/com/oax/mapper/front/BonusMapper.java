package com.oax.mapper.front;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oax.entity.front.Bonus;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zl
 * @since 2018-11-26
 */
public interface BonusMapper extends BaseMapper<Bonus> {

    void batchInsert(@Param("list") List<Bonus> oneLevelBonuses);

}
