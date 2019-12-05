package com.oax.druid;

public enum  DataSourceType {
    MASTER("master"),
    SLAVE("slave");

    private String type;

    DataSourceType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
