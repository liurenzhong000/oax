package com.oax.entity.admin.param;

import javax.validation.constraints.NotNull;

public class OperationParam {

    @NotNull(message = "活动id不能为空")
    private int id;

    @NotNull(message = "活动开启状态不能为空")
    private Byte display_open;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Byte getDisplay_open() {
        return display_open;
    }

    public void setDisplay_open(Byte display_open) {
        this.display_open = display_open;
    }

    @Override
    public String toString() {
        return "OperationParam{" +
                "id=" + id +
                ", display_open=" + display_open +
                '}';
    }
}
