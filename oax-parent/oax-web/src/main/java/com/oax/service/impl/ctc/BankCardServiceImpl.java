package com.oax.service.impl.ctc;

import com.oax.common.AssertHelper;
import com.oax.common.BankCardUtil;
import com.oax.common.MapHelper;
import com.oax.druid.DataSource;
import com.oax.druid.DataSourceType;
import com.oax.entity.ctc.BankCard;
import com.oax.entity.front.vo.BankCardVo;
import com.oax.mapper.ctc.BankCardMapper;
import com.oax.mapper.ctc.CtcOrderMapper;
import com.oax.service.ctc.BankCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BankCardServiceImpl implements BankCardService {

    @Autowired
    private BankCardMapper bankCardMapper;

    @Autowired
    private CtcOrderMapper ctcOrderMapper;

    @Override
    @DataSource(DataSourceType.MASTER)
    public void saveOne(BankCard bankCard) {
        //验证卡号真实性
        BankCardUtil.validateBankCard(bankCard.getCardNo(), bankCard.getBankName());
        AssertHelper.isTrue(!bankCardMapper.uniqueCardNo(bankCard.getCardNo()), "该银行卡已绑定");
        //判断是否为用户的第一个银行卡
        boolean hasBankCare = bankCardMapper.hasBankCard(bankCard.getUserId());
        if (!hasBankCare) {
            bankCard.setDefaultCard(true);
        }
        bankCard.setCardCode(BankCardUtil.getCodeByCardNo(bankCard.getCardNo()));
        bankCardMapper.insert(bankCard);
    }

    @Override
    @DataSource(DataSourceType.SLAVE)
    public List<BankCardVo> listByUserId(Integer userId) {
        List<BankCardVo> bankCardVos = bankCardMapper.listByUserId(userId);
        //填写icon图片地址
        bankCardVos.forEach(item->item.setIconUrl(BankCardUtil.getLogoByCode(item.getCardNo())));
        return bankCardVos;
    }

    @Override
    @DataSource(DataSourceType.MASTER)
    public void setDefault(Integer id, Integer userId) {
        boolean hasNoFinishOrder = ctcOrderMapper.hasNoFinishOrder(userId);
        AssertHelper.isTrue(!hasNoFinishOrder, "拥有未完成订单无法修改默认银行卡");
        BankCard bankCard = bankCardMapper.selectById(id);
        AssertHelper.notEmpty(bankCard, "银行卡不存在");
        AssertHelper.isTrue(bankCard.getUserId().equals(userId), "银行卡不存在");
        bankCardMapper.removeDefault(userId);
        bankCardMapper.setDefault(MapHelper.buildParams("id", id, "userId", userId));
    }

    @Override
    @DataSource(DataSourceType.MASTER)
    public void deleteOne(Integer id, Integer userId) {
        BankCard bankCard = bankCardMapper.selectById(id);
        AssertHelper.notEmpty(bankCard, "银行卡不存在");
        AssertHelper.isTrue(bankCard.getUserId().equals(userId), "银行卡不存在");
        bankCardMapper.deleteById(bankCard);
    }

}
