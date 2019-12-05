package com.oax.entity.ctc;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户银行卡管理
 */
@Setter
@Getter
@Entity
public class BankCard implements Serializable {

    /**
     * id
     */
    @Id
    @GeneratedValue
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**用户id*/
    private Integer userId;

    /**开户用户名称*/
    private String realName;

    /**开户银行名称*/
    private String bankName;

    /**开户省市*/
    private String city;

    /**开户支行*/
    private String bankBranch;

    /**银行卡号*/
    private String cardNo;

    /**添加日期*/
    private Date createDate;

    /**
     * 是否默认收付银行卡
     */
    private boolean defaultCard;

    /**银行卡代号，用来获取logo*/
    private String cardCode;
}
