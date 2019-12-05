package com.oax.admin.service;

import com.github.pagehelper.PageInfo;
import com.oax.entity.admin.param.profitParam;
import com.oax.entity.front.PromoteProfit;

import java.util.List;

public interface PromoteFundService {
    PageInfo<PromoteProfit> getPromoteListAll(profitParam profitParam);
}
