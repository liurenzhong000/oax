package com.oax.entity.front;

import java.io.Serializable;

/**
 * @author ：xiangwh
 * @ClassName:：BannerInfo
 * @Description： banner出参model
 * @date ：2018年6月22日 下午6:59:32
 */
public class BannerInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String title;
    private String url;
    private String image;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "BannerInfo [id=" + id + ", title=" + title + ", url=" + url + ", image=" + image + "]";
    }

}
