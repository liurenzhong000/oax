package com.oax.admin.form;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class PanicBuyActivityForm {

    @NotNull(message = "In不能为空")
    private Integer id;

    /**达标基数*/
    @NotNull(message = "达标基数")
    private Integer reachBase;

    /**需要多少人达标*/
    @NotNull(message = "活动达标人数不能为空")
    private Integer reachNeed;

    /**参与人数基数*/
    @NotNull(message = "参与人数基数")
    private Integer participateBase;

    /**活动开始时间*/
    @NotNull(message = "活动开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /**活动结束时间*/
    @NotNull(message = "活动结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
}
