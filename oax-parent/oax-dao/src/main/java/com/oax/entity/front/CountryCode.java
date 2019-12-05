package com.oax.entity.front;

import org.hibernate.validator.constraints.NotBlank;

public class CountryCode {
    private Integer id;

    @NotBlank(message = "中文名不能为空")
    private String cnName;
    @NotBlank(message = "英文名不能为空")
    private String enName;

    @NotBlank(message = "简称不能为空")
    private String shortName;
    @NotBlank(message = "code码不能为空")
    private String code;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
}