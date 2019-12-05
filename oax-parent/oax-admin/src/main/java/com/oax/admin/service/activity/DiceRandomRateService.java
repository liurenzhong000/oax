package com.oax.admin.service.activity;

import com.github.pagehelper.PageInfo;
import com.oax.admin.form.ListDiceRandomRateForm;
import com.oax.entity.activity.DiceRandomRate;

/**
 * @Auther: hyp
 * @Date: 2019/1/5 18:23
 * @Description:
 */
public interface DiceRandomRateService {
    PageInfo list(ListDiceRandomRateForm form);

    void saveOne(DiceRandomRate entity);

    void updateOne(DiceRandomRate entity);

    void deleteOne(Integer id);
}
