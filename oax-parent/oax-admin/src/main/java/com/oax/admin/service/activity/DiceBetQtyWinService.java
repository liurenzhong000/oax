package com.oax.admin.service.activity;

import com.github.pagehelper.PageInfo;
import com.oax.admin.form.ListDiceBetQtyWinForm;
import com.oax.entity.activity.DiceBetQtyWin;

/**
 * @Auther: hyp
 * @Date: 2019/1/5 14:51
 * @Description:
 */
public interface DiceBetQtyWinService {
    PageInfo list(ListDiceBetQtyWinForm form);

    void saveOne(DiceBetQtyWin entity);

    void updateOne(DiceBetQtyWin entity);

    void deleteOne(Integer id);
}
