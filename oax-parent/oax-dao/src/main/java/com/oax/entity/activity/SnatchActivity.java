package com.oax.entity.activity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Auther: hyp
 * @Date: 2019/1/16 10:14
 * @Description: 夺宝奖池记录
 */
@Entity
@Data
public class SnatchActivity {

    @Id
    @GeneratedValue
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**配置id*/
    private Integer configId;

    /**奖池名称*/
    private String configName;

    /**期数*/
    private Integer ordinal;

    /**币种id*/
    private Integer coinId;

    /**当前完成进度*/
    private Integer finishQuantity;

    /**最大数量*/
    private Integer quantity;

    /**最大投注数量*/
    private Integer maxQuantity;

    /**中奖人数*/
    private Integer winNumber;

    /**最小投注单位*/
    private BigDecimal unit;

    /**开奖时间*/
    private Date lotteryTime;

//    /**开奖hash，用','隔开*/
//    private String hashes;

//    /**中奖号，用','隔开*/
//    private String winDetailIds;

    /**后台调控机器人投注概率*/
    private Integer robotBackWin;

    /**计划添加机器人投注次数*/
    private Integer robotWinCount;

    /**当前已添加机器次数*/
    private Integer currRobotWinCount;

    private Date createTime;

    /**是否已经结束0进行中，1已开奖
     * @see Status
     * */
    private Integer status;

    @Version
    private Integer version;

    public enum Status{
        RUNNING("进行中"), FINISH("已开奖");
        String desc;

        Status(String desc) {
            this.desc = desc;
        }
    }
}
