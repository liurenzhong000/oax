package com.oax.mapper.ctc;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oax.entity.ctc.BankCard;
import com.oax.entity.front.vo.BankCardVo;

import java.util.List;
import java.util.Map;

/**
 * 用户银行卡
 */
public interface BankCardMapper extends BaseMapper<BankCard> {

    //用户是否已经拥有银行卡
    boolean hasBankCard(Integer userId);

    List<BankCardVo> listByUserId(Integer userId);

    void setDefault(Map<String, Object> params);

    void removeDefault(Integer userId);

    boolean uniqueCardNo(String cardNo);

    BankCard getDefaultCard(Integer userId);
}
