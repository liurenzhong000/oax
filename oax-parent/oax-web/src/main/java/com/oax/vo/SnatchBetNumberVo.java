package com.oax.vo;

import com.oax.entity.front.vo.SnatchDetailVo;
import lombok.Data;

import java.util.List;

/**
 * @Auther: hyp
 * @Date: 2019/1/18 15:35
 * @Description:用户投注编号页面实体
 */
@Data
public class SnatchBetNumberVo {

    /**未开奖个数*/
    private Integer unknownCount;

    /**未中奖个数*/
    private Integer notWinCount;

    /**中奖个数*/
    private Integer winCount;

    /**记录*/
    private List<SnatchDetailVo> detailVoList;
}
