package com.oax.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oax.entity.front.vo.SnatchCoinVo;
import com.oax.entity.front.vo.SnatchConfigTypeVo;
import com.oax.entity.front.vo.SnatchDetailAggregateVo;
import lombok.Data;

import java.util.List;

/**
 * @Auther: hyp
 * @Date: 2019/1/20 17:24
 * @Description: 一元夺宝投注记录页默认数据一次性返回
 */
@Data
public class SnatchDetailPageVo {

    /**所有币种*/
    private List<SnatchCoinVo> allCoins;

    /**币种奖池类型*/
    private List<SnatchConfigTypeVo> configTypeVos;

    /**开奖统计数据*/
    private Page<SnatchDetailAggregateVo> pageDetails;
}
