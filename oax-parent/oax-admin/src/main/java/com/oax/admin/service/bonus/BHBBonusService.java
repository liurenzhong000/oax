package com.oax.admin.service.bonus;

import com.github.pagehelper.PageInfo;
import com.oax.entity.admin.param.BonusParam;
import com.oax.entity.admin.param.PageParam;
import com.oax.entity.front.Bonus;

public interface BHBBonusService {

    void BHBBonus(String password, Integer day,Double bhbUSDTratio,Double oneLevelBonus,
                  Double twoLevelBonus,Double threeLevelBonus,Double selfLevelBonus);

    PageInfo<Bonus> pageForAdmin(BonusParam bonusParam);
}
