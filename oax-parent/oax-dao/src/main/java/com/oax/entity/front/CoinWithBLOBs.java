package com.oax.entity.front;

import java.io.Serializable;

/**
 * coin
 *
 * @author
 */
public class CoinWithBLOBs extends Coin implements Serializable {
    /**
     * 中文描述
     */
    private String cnDescription;

    /**
     * 英文描述
     */
    private String enDescription;

    private static final long serialVersionUID = 1L;

    public String getCnDescription() {
        return cnDescription;
    }

    public void setCnDescription(String cnDescription) {
        this.cnDescription = cnDescription;
    }

    public String getEnDescription() {
        return enDescription;
    }

    public void setEnDescription(String enDescription) {
        this.enDescription = enDescription;
    }
}