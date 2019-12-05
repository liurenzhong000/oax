package com.oax.entity.front;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;

public class InrestRatio implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer day;
    private BigDecimal ratio;

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public BigDecimal getRatio() {
        return ratio;
    }

    public void setRatio(BigDecimal ratio) {
        this.ratio = ratio;
    }

    public InrestRatio(Integer day, BigDecimal ratio) {
        this.day = day;
        this.ratio = ratio;
    }

    public InrestRatio() {
    }

    @Override
    public String toString() {
        return "InrestRatio{" +
                "day=" + day +
                ", ratio=" + ratio +
                '}';
    }

    public static void main(String[] args) {
        List<InrestRatio> list = new ArrayList<>();
        InrestRatio model1 = new InrestRatio(1, new BigDecimal(1));
        InrestRatio model2 = new InrestRatio(2, new BigDecimal(2));
        InrestRatio model3 = new InrestRatio(3, new BigDecimal(3));
        InrestRatio model4 = new InrestRatio(4, new BigDecimal(4));
        InrestRatio model5 = new InrestRatio(5, new BigDecimal(5));
        InrestRatio model6 = new InrestRatio(6, new BigDecimal(6));
        InrestRatio model7 = new InrestRatio(7, new BigDecimal(7));
        InrestRatio model8 = new InrestRatio(8, new BigDecimal(8));
        InrestRatio model9 = new InrestRatio(9, new BigDecimal(9));
        InrestRatio model10 = new InrestRatio(10, new BigDecimal(10));
        InrestRatio model11 = new InrestRatio(11, new BigDecimal(11));
        InrestRatio model12 = new InrestRatio(12, new BigDecimal(12));
        list.add(model1);
        list.add(model2);
        list.add(model3);
        list.add(model4);
        list.add(model5);
        list.add(model6);
        list.add(model7);
        list.add(model8);
        list.add(model9);
        list.add(model10);
        list.add(model11);
        list.add(model12);
        System.out.println(JSON.toJSONString(list));

    }
}
