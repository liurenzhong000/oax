package com.oax.entity.activity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * BHB抢购活动
 */
@Setter
@Getter
@Entity
public class PanicBuyActivity implements Serializable {

    /**
     * id
     */
    @Id
    private Integer id;

    /**达标人数*/
    private Integer reach;

    /**达标基数*/
    private Integer reachBase;

    /**需要多少人达标*/
    private Integer reachNeed;

    /**参与人数*/
    private Integer participate;

    /**参与人数基数*/
    private Integer participateBase;

    /**购买人数*/
    private Integer buyCount;

    /**完成时间*/
    private Date finishTime;

    /**创建时间*/
    private Date createTime;

    /**
     * 状态
     * @see Status
     * */
    private Integer status;

    /**活动开始时间*/
    private Date startTime;

    /**活动结束时间*/
    private Date endTime;

    public enum Status{
        CLOSE("未开始"), OPEN("开启"), FINISH("结束"), ORDER_CLOSE("用户订单关闭");
        String desc;

        Status(String desc) {
            this.desc = desc;
        }
    }
}
