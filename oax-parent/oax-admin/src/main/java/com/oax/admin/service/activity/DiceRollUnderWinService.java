package com.oax.admin.service.activity;

import com.github.pagehelper.PageInfo;
import com.oax.admin.form.ListDiceRollUnderWinForm;
import com.oax.entity.activity.DiceRollUnderWin;

/**
 * @Auther: hyp
 * @Date: 2019/1/14 15:00
 * @Description:
 */
public interface DiceRollUnderWinService {

    PageInfo list(ListDiceRollUnderWinForm form);

    void saveOne(DiceRollUnderWin entity);

    void updateOne(DiceRollUnderWin entity);

    void deleteOne(Integer id);

}
