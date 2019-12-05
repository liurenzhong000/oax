package com.oax.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.oax.entity.admin.param.PageParam;
import com.oax.entity.front.Bonus;
import com.oax.vo.BonusVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zl
 * @since 2018-11-26
 */
public interface IBonusService extends IService<Bonus> {

    Page<BonusVO> pageBonus(PageParam pageParam, Wrapper<Bonus> wrapper);
}
