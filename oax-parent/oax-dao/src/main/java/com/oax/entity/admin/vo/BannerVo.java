package com.oax.entity.admin.vo;

public class BannerVo {
    //表的id
    private Integer id;
    //1 pc 2app端
    private Integer type;
    //banner标题
    private String title;
    //中文图片
    private String cnImage;
    //英文图片
    private String enImage;
    //广告链接
    private String url;
    //1未发布 2已发布
    private Integer status;
    //发布者
    private String adminName;
    //排序号
    private Integer sortNo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCnImage() {
        return cnImage;
    }

    public void setCnImage(String cnImage) {
        this.cnImage = cnImage;
    }

    public String getEnImage() {
        return enImage;
    }

    public void setEnImage(String enImage) {
        this.enImage = enImage;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public Integer getSortNo() {
        return sortNo;
    }

    public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
    }
}
