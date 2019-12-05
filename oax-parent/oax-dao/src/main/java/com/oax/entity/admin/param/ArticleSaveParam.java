package com.oax.entity.admin.param;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ArticleSaveParam {
    //1最新资讯 2常见问题 3官方公告 4法律支援
    @NotNull
    private Integer type;
    //中文标题
    @NotBlank
    private String cnTitle;
    //英文标题
    @NotBlank
    private String enTitle;
    //中文内容
    @NotBlank
    private String cnContent;
    //英文内容
    @NotBlank
    private String enContent;
    //1 未发布 2已发布
    @NotNull
    private Integer status;
    //发布者 id
    private Integer adminId;
    //浏览量 默认为0
    private int readCount;
    //发布时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date releaseTime;
    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    //更新时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCnTitle() {
        return cnTitle;
    }

    public void setCnTitle(String cnTitle) {
        this.cnTitle = cnTitle;
    }

    public String getEnTitle() {
        return enTitle;
    }

    public void setEnTitle(String enTitle) {
        this.enTitle = enTitle;
    }

    public String getCnContent() {
        return cnContent;
    }

    public void setCnContent(String cnContent) {
        this.cnContent = cnContent;
    }

    public String getEnContent() {
        return enContent;
    }

    public void setEnContent(String enContent) {
        this.enContent = enContent;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }

    public Date getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(Date releaseTime) {
        this.releaseTime = releaseTime;
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
