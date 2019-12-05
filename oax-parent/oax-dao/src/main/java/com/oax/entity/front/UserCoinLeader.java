package com.oax.entity.front;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 领导人下级持仓统计记录
 */
@Data
@Entity
public class UserCoinLeader {

    @Id
    @GeneratedValue
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**领导人用户id*/
    private Integer userId;

    /**创建时间id*/
    private Date createTime;

    /**一级所有用户平均持仓量和  可用+冻结*/
    private BigDecimal oneLevelSum;

    /**二级所有用户平均持仓量和  可用+冻结*/
    private BigDecimal twoLevelSum;

    /**三级所有用户平均持仓量和  可用+冻结*/
    private BigDecimal threeLevelSum;

    /**3级持仓和*/
    private BigDecimal allLevelSum;

    /**bnb的usdt单价*/
    private BigDecimal price;

    /**数据统计的是哪天的*/
    private Date dataTime;

}
