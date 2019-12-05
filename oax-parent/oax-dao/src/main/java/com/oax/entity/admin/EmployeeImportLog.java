package com.oax.entity.admin;

import java.io.Serializable;
import java.util.Date;

/**
 * employee_import_log
 * @author 
 */
public class EmployeeImportLog implements Serializable {
    private Integer id;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 执行结果
     */
    private String importLog;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getImportLog() {
        return importLog;
    }

    public void setImportLog(String importLog) {
        this.importLog = importLog;
    }
}