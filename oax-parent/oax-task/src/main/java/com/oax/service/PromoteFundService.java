package com.oax.service;

import com.oax.entity.front.PromoteFund;

import java.util.List;

public interface PromoteFundService {

    List<PromoteFund> getPromoteByUser(Integer user_id);


}
