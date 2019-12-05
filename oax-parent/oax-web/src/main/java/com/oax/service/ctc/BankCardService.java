package com.oax.service.ctc;

import com.oax.entity.ctc.BankCard;
import com.oax.entity.front.vo.BankCardVo;

import java.util.List;

public interface BankCardService {

    void saveOne(BankCard bankCard);

    List<BankCardVo> listByUserId(Integer userId);

    void setDefault(Integer id, Integer userId);

    void deleteOne(Integer id, Integer userId);
}
