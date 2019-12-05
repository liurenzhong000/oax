package com.oax.entity.front;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * @Auther: hyp
 * @Date: 2019/2/26 16:42
 * @Description: 后台给用户添加相关标注信息
 */
@Data
@Entity
public class UserRemark {

    @Id
    @GeneratedValue
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Integer userId;

    /**标注内容*/
    private String remark;

    /**管理员名称*/
    private String adminName;

    /**添加日期*/
    private Date createTime;

}
