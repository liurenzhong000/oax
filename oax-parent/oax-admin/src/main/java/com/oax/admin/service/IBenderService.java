package com.oax.admin.service;

import com.github.pagehelper.PageInfo;
import com.oax.entity.admin.param.BannerParam;
import com.oax.entity.admin.param.BannerUpdateParam;
import com.oax.entity.admin.vo.BannerVo;
import com.oax.entity.front.Banner;

public interface IBenderService {
    /*
     * bender管理
     */
    PageInfo<BannerVo> bannerManageList(BannerParam banner);

    /*
     * banner更新
     */
    Integer update(BannerUpdateParam banner);

    /*
     * banner删除
     */
    Integer deleteBanner(Integer id);

    /*
     * banner保存
     */
    Integer saveBanner(Banner banner);
}
