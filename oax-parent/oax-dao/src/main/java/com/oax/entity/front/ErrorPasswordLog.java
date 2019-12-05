package com.oax.entity.front;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * error_password_log
 *
 * @author
 */
@Data
public class ErrorPasswordLog implements Serializable {
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * IP
     */
    private String ip;

    /**
     * 类型：1登录密码 2交易密码3验证码
     */
    private Integer type;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}