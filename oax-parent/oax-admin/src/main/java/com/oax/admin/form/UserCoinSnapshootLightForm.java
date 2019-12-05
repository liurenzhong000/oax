package com.oax.admin.form;

import com.oax.entity.admin.param.PageParam;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Auther: hyp
 * @Date: 2019/1/14 18:26
 * @Description:
 */
@Data
public class UserCoinSnapshootLightForm extends PageParam {

    private Integer userId;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

}
