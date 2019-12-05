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
 * 用户资金冻结记录
 */
@Data
@Entity
public class UserCoinFreezingDetail {

    @Id
    @GeneratedValue
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**领导人用户id*/
    private Integer userId;

    /**币种id*/
    private Integer coinId;

    /**创建时间*/
    private Date createTime;

    /**冻结时间*/
    private Date freezingTime;

    /**解冻时间*/
    private Date unfreezeTime;

    /**冻结的个数*/
    private BigDecimal qty;

    /**更新时间*/
    private Date updateTime;

    /**状态：0-冻结，1-已解冻
     * @see Status
     * */
    private Integer status;
    /**
     * 操作用户名称
     */
    private String userName;


    public enum Status{
        FREEZING("冻结"), UNFREEZE("解冻");
        String desc;
        Status(String desc) {
            this.desc = desc;
        }
    }
}
