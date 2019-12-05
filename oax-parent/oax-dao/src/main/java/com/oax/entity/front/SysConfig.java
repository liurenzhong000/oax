package com.oax.entity.front;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotBlank;

/**
 * sys_config
 *
 * @author
 */
public class SysConfig implements Serializable {
    private Integer id;

    @NotBlank(message = "名称不能为空")
    private String name;

    @NotBlank(message = "值不能为空")
    private String value;

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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}