package com.oax.service.activity;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.oax.entity.activity.DiceActivity;
import com.oax.entity.admin.param.PageParam;
import com.oax.entity.front.vo.DiceActivityVo;
import com.oax.entity.front.vo.DiceConfigVo;
import com.oax.vo.DiceIndexVo;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Auther: hyp
 * @Date: 2018/12/26 09:33
 * @Description:
 */
public interface DiceActivityService extends IService<DiceActivity> {

    DiceActivityVo bet(Integer userId, Integer coinId, Integer rollUnder, BigDecimal betQty);

    Integer signIn(Integer userId) throws IllegalAccessException;

    Page<DiceActivityVo> diceList(PageParam pageParam, Integer userId, Integer coinId);

    DiceIndexVo index(Integer userId, Integer coinId);

}
