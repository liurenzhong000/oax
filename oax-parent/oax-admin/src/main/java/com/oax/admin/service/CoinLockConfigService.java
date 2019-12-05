package com.oax.admin.service;

import com.github.pagehelper.PageInfo;
import com.oax.entity.admin.vo.CoinLockConfigVo;
import com.oax.entity.front.CoinLockConfig;

public interface CoinLockConfigService {
    boolean saveOrUpdate(CoinLockConfig coinLockConfig);

    CoinLockConfig selectById(Integer id);

    PageInfo<?> getPage(CoinLockConfigVo vo);

    boolean isShow(Integer id, Integer isShow);
}
