package com.oax.admin.service.impl.ctc;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oax.admin.service.ctc.MerchantService;
import com.oax.common.AssertHelper;
import com.oax.entity.admin.param.MerchantParam;
import com.oax.entity.admin.vo.MerchantVo;
import com.oax.entity.ctc.Merchant;
import com.oax.entity.enums.MerchantStatus;
import com.oax.entity.front.Member;
import com.oax.mapper.ctc.CtcAdvertMapper;
import com.oax.mapper.ctc.MerchantMapper;
import com.oax.mapper.front.MemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class MerchantServiceImpl implements MerchantService {

    @Autowired
    private MerchantMapper merchantMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private CtcAdvertMapper ctcAdvertMapper;

    @Override
    public PageInfo<MerchantVo> pageForAdmin(MerchantParam param) {
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        //按订单号查找的情况
        List<MerchantVo> merchantVos;
        if (param.getCtcOrderId() != null) {
            merchantVos = merchantMapper.pageForAdminByCtcOrderId(param);
        }
        merchantVos = merchantMapper.pageForAdmin(param);
        merchantVos.forEach( item ->{
            item.setPaymentWayDesc(item.getPaymentWay().desc);
            if (item.getBuyTotalCount() != 0) {
                item.setBuyRatio(BigDecimal.valueOf(item.getBuySuccCount()/item.getBuyTotalCount()));
            }else {
                item.setBuyRatio(BigDecimal.ZERO);
            }
            if (item.getBuyTotalCount() != 0) {
                item.setSaleRatio(BigDecimal.valueOf(item.getSaleSuccCount()/item.getSaleTotalCount()));
            }else {
                item.setSaleRatio(BigDecimal.ZERO);
            }
        });
        return new PageInfo<>(merchantVos);
    }

    @Override
    public void saveOne(Merchant merchant) {
        Member user = memberMapper.selectByPrimaryKey(merchant.getUserId());
        AssertHelper.notEmpty(user, "用户不存在");
        Merchant oldMerchant = merchantMapper.selectByUserId(user.getId());
        AssertHelper.isEmpty(oldMerchant, "商户信息已存在");
        merchantMapper.insert(merchant);
        //用户表添加商户标记字段
        user.setMerchant(true);
        memberMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public void deleteOne(Integer id) {
        Merchant merchant = merchantMapper.selectById(id);
        AssertHelper.notEmpty(merchant, "商户记录不存在");
        Member user = memberMapper.selectByPrimaryKey(merchant.getUserId());
        user.setMerchant(false);
        //更新商户状态为删除
        memberMapper.updateByPrimaryKeySelective(user);
        merchant.setStatus(MerchantStatus.DELETE);
        merchantMapper.updateById(merchant);
        //关闭该商家的广告
        ctcAdvertMapper.closeAllByUserId(merchant.getUserId());
    }

}
