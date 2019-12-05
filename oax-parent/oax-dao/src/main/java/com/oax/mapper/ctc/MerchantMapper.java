package com.oax.mapper.ctc;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oax.entity.admin.param.MerchantParam;
import com.oax.entity.admin.vo.MerchantVo;
import com.oax.entity.ctc.Merchant;

import java.util.List;


public interface MerchantMapper extends BaseMapper<Merchant> {

    List<MerchantVo> pageForAdmin(MerchantParam param);

    Merchant selectByUserId(Integer userId);

    List<MerchantVo> pageForAdminByCtcOrderId(MerchantParam param);
}
