package com.oax.admin.service.activity;


import com.github.pagehelper.PageInfo;
import com.oax.entity.activity.PanicBuyActivity;
import com.oax.entity.activity.PanicBuyDetail;
import com.oax.entity.activity.PanicBuyHelp;
import com.oax.entity.admin.param.PanicBuyActivityParam;
import com.oax.entity.admin.param.PanicBuyDetailParam;
import com.oax.entity.admin.param.PanicBuyHelpParam;

public interface PanicBuyActivityService {

    void saveOne(PanicBuyActivity entity);

    void updateOne(Integer id, Integer participateBase, Integer reachBase);

    PageInfo<PanicBuyActivity> pageForAdmin(PanicBuyActivityParam param);

    PageInfo<PanicBuyDetail> pageDetailForAdmin(PanicBuyDetailParam param);

    PageInfo<PanicBuyHelp> pageHelpForAdmin(PanicBuyHelpParam param);

    void open(Integer id);

    void finish(Integer id);

    void orderClose(Integer id);
}
