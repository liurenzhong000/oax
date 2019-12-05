package com.oax.admin.service.impl.bonus;

import com.oax.admin.service.UserCoinService;
import com.oax.admin.service.bonus.BCBBonusService;
import com.oax.entity.enums.UserCoinDetailType;
import com.oax.entity.front.vo.MemberCoinVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Auther: hyp
 * @Date: 2019/1/27 14:54
 * @Description: BCB 挖矿分红, usdt精度保留4位
 */
@Service
@Transactional
@Slf4j
public class BCBBonusServiceImpl implements BCBBonusService {

    @Autowired
    private UserCoinService userCoinService;

    @Override
    public void bonus(){
        BigDecimal bonusRatio = bonusRatio();
        //拥有bcb的用户
        List<MemberCoinVo> memberCoinVos = userCoinService.listBCBMemberCoinVos();
        BigDecimal allQty = BigDecimal.ZERO;
        for (MemberCoinVo item : memberCoinVos) {
            BigDecimal usdtQty = item.getBanlance().multiply(bonusRatio).setScale(4, BigDecimal.ROUND_HALF_UP);
            log.info("user_id = {}, bcb={}, usdtQty={}", item.getId(), item.getBanlance(), usdtQty);
//            userCoinService.addBalanceWithType(item.getId(), 10, usdtQty, "", UserCoinDetailType.BCB_BONUS);
            allQty = allQty.add(usdtQty);
        }
        log.info("usdt总分红量={}", allQty);
    }

    //1个BCB能分到多少usdt
    private BigDecimal bonusRatio(){
        BigDecimal bonusRatio = new BigDecimal("53466").multiply(new BigDecimal("10")).multiply(new BigDecimal("1E-10")).divide(new BigDecimal("6.7"));
        return bonusRatio;
    }

}
