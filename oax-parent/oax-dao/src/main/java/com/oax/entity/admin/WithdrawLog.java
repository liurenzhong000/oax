package com.oax.entity.admin;

import java.io.Serializable;
import java.util.Date;

/**
 * withdraw_log
 *
 * @author
 */
public class WithdrawLog implements Serializable {
    /**
     * 转出记录主键
     */
    private Integer id;

    /**
     * 转出记录id
     */
    private Integer withdrawId;

    /**
     * 操作员id
     */
    private Integer adminId;

    /**
     * 操作员名称
     */
    private String adminName;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 操作日志
     */
    private String remark;

    /**
     * 描述
     */
    private String description;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getWithdrawId() {
        return withdrawId;
    }

    public void setWithdrawId(Integer withdrawId) {
        this.withdrawId = withdrawId;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}