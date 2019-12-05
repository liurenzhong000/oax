package com.oax.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oax.entity.admin.param.PageParam;
import com.oax.entity.front.UserCoinLeader;

public interface UserCoinLeaderService {
    Page<UserCoinLeader> pageForWeb(PageParam pageParam, Integer userId);
}
