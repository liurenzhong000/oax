package com.oax.entity.admin;

import java.io.Serializable;
import java.util.Date;

/**
 * operation_log
 * @author 
 */
public class OperationLog implements Serializable {
    private Integer id;

    /**
     * 管理员id
     */
    private Integer adminId;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 1查看用户详情中资产
2查看用户详情中转入记录 
3查询转入记录列表
     */
    private Integer type;

    private Date createTime;

    /**
     * 描述
     */
    private String content;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}