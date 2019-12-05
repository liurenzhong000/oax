package com.oax.entity.front;

import java.io.Serializable;
import java.util.Date;

/**
 * i18n_msg_category
 *
 * @author
 */
public class I18nMsgCategory implements Serializable {
    private Integer id;

    /**
     * 分类名
     */
    private String name;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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