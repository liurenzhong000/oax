package com.oax.service;

import com.oax.entity.front.PromoteFund;
import com.oax.entity.front.PromoteProfit;

import java.util.List;

public interface PromoteFundService {
    public List<PromoteProfit>  getPromoteList(String userId);
}
