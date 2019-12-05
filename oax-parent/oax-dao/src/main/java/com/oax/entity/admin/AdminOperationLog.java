package com.oax.entity.admin;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * 运营后台操作日志
 */
@Entity
@Data
public class AdminOperationLog {

    @Id
    @GeneratedValue
    private Long id;

    /**操作用户*/
    private String sessionUser;

    /**创建时间*/
    private Date createDate;

    /**提交的数据*/
    @Column(columnDefinition = "TEXT")
    private String params;

    /**请求链接*/
    private String requestUrl;

    /**请求方法名*/
    private String methodName;

    /**操作内容*/
    private String apiOperation;

    /**操作ip*/
    private String ip;

}
