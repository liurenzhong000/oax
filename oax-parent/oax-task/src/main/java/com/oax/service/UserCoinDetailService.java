package com.oax.service;

import com.oax.entity.enums.UserCoinDetailType;
import com.oax.entity.front.UserCoin;
import lombok.Setter;
import org.springframework.stereotype.Service;

public interface UserCoinDetailService {

    void addUserCoinDetail(UserCoin oldUserCoin, String targetId, UserCoinDetailType type);
}
