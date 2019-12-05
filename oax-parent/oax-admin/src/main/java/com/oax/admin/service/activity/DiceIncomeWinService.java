package com.oax.admin.service.activity;

import com.github.pagehelper.PageInfo;
import com.oax.entity.activity.DiceIncomeWin;
import com.oax.entity.admin.param.PageParam;
import org.apache.ibatis.annotations.Param;

/**
 * @Auther: hyp
 * @Date: 2018/12/31 19:18
 * @Description: 后台根据用户的收入金额来进行调控概率
 */
public interface DiceIncomeWinService {

    void saveOne(DiceIncomeWin entity);

    void updateOne(DiceIncomeWin entity);

    PageInfo<DiceIncomeWin> list(Integer coinId, PageParam param);

    void deleteOne(Integer id);
}
