package com.oax.entity.ctc;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.oax.entity.enums.CtcOrderLogType;
import com.oax.entity.enums.CtcOrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * ctc订单状态变更记录
 * 用来后续处理订单状态的多情况变更
 */
@Setter
@Getter
@Entity
public class CtcOrderLog implements Serializable {

    @Id
    @GeneratedValue
    @TableId(type = IdType.AUTO)
    private Long id;

    /**CTC订单id*/
    private Long ctcOrderId;

    /**CTC订单状态变更前*/
    private CtcOrderStatus beforeStatus;

    /**CTC订单状态变更前*/
    private CtcOrderStatus afterStatus;

    /**变更操作类型*/
    private CtcOrderLogType type;

    /**操作用户id*/
    private Integer userId;

    private Date createDate;

    private CtcOrderLog(){}

    public static CtcOrderLog newInstance(Long ctcOrderId, CtcOrderStatus beforeStatus, CtcOrderStatus afterStatus, CtcOrderLogType type, Integer userId){
        CtcOrderLog ctcOrderLog = new CtcOrderLog();
        ctcOrderLog.setCtcOrderId(ctcOrderId);
        ctcOrderLog.setBeforeStatus(beforeStatus);
        ctcOrderLog.setAfterStatus(afterStatus);
        ctcOrderLog.setType(type);
        ctcOrderLog.setUserId(userId);
        ctcOrderLog.setCreateDate(new Date());
        return ctcOrderLog;
    }
}
