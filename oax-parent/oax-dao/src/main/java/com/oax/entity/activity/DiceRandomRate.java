package com.oax.entity.activity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @Auther: hyp
 * @Date: 2019/1/5 18:19
 * @Description:
 */
@Data
@Entity
public class DiceRandomRate implements Serializable {

    @Id
    @GeneratedValue
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer coinId;

    private Integer randomRate;
}
