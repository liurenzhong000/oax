package com.oax.entity.ctc;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.oax.entity.enums.CtcAppealStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * CTC 订单申述
 */
@Setter
@Getter
@Entity
public class CtcAppeal  implements Serializable {

    @Id
    @GeneratedValue
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * ctc订单号
     */
    private Long ctcOrderId;

    private Integer userId;

    /**
     * 申述详情
     */
    private String appealDesc;

    /**
     * 申述状态（申诉中，处理中，处理完成，已撤销）
     */
    private CtcAppealStatus status;

    private Date createDate;

    public CtcAppeal() {

    }

    public static CtcAppeal newInstance(Integer userId, Long ctcOrderId, String appealDesc) {
        CtcAppeal ctcAppeal = new CtcAppeal();
        ctcAppeal.setUserId(userId);
        ctcAppeal.setCtcOrderId(ctcOrderId);
        ctcAppeal.setAppealDesc(appealDesc);
        ctcAppeal.setStatus(CtcAppealStatus.APPEAL);
        ctcAppeal.setCreateDate(new Date());
        return ctcAppeal;
    }

}

