package com.oax.entity.admin.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ArticleListVo {
    //表的id
    private Integer id;
    //中文标题
    private String cnTitle;
    //浏览量
    private Integer readCount;
    //发布者 名称
    private String adminName;
    //1最新资讯 2常见问题 3官方公告 4法律支援
    private Integer type;
    //1 未发布 2已发布
    private Integer status;
    //发布时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date releaseTime;
    //更新时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCnTitle() {
        return cnTitle;
    }

    public void setCnTitle(String cnTitle) {
        this.cnTitle = cnTitle;
    }

    public Integer getReadCount() {
        return readCount;
    }

    public void setReadCount(Integer readCount) {
        this.readCount = readCount;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(Date releaseTime) {
        this.releaseTime = releaseTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
