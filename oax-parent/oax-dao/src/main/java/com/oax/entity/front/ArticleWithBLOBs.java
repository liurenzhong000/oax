package com.oax.entity.front;

import java.io.Serializable;

/**
 * article
 *
 * @author
 */
public class ArticleWithBLOBs extends Article implements Serializable {
    /**
     * 中文内容
     */
    private String cnContent;

    /**
     * 英文内容
     */
    private String enContent;

    private static final long serialVersionUID = 1L;

    public String getCnContent() {
        return cnContent;
    }

    public void setCnContent(String cnContent) {
        this.cnContent = cnContent;
    }

    public String getEnContent() {
        return enContent;
    }

    public void setEnContent(String enContent) {
        this.enContent = enContent;
    }
}