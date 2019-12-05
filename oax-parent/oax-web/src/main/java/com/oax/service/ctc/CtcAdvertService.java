package com.oax.service.ctc;

import com.github.pagehelper.PageInfo;
import com.oax.druid.DataSource;
import com.oax.druid.DataSourceType;
import com.oax.entity.enums.CtcAdvertStatus;
import com.oax.entity.front.param.CtcAdvertParam;
import com.oax.entity.front.vo.CtcAdvertBalanceVo;
import com.oax.entity.front.vo.ListCtcAdvertVo;
import com.oax.form.CtcAdvertForm;
import com.oax.form.UpdateCtcAdvertForm;
import com.oax.vo.CtcAdvertInfoVO;

public interface CtcAdvertService {

    void saveOne(CtcAdvertForm form) throws Exception;

    CtcAdvertBalanceVo coinInfo(Integer userId, Integer coinId);

    void updateStatus(Integer id, CtcAdvertStatus status);

    CtcAdvertInfoVO info();

    PageInfo<ListCtcAdvertVo> pageForWeb(CtcAdvertParam param, Integer userId);

    void updateOne(Integer userId, Integer id, UpdateCtcAdvertForm form);

    PageInfo<ListCtcAdvertVo> pageForAdvert(CtcAdvertParam param)throws Exception;
}
