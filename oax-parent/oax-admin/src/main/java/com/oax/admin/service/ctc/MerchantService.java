package com.oax.admin.service.ctc;

import com.github.pagehelper.PageInfo;
import com.oax.entity.admin.param.MerchantParam;
import com.oax.entity.admin.vo.MerchantVo;
import com.oax.entity.ctc.Merchant;

/**
 * 商户相关
 */
public interface MerchantService {

    PageInfo<MerchantVo> pageForAdmin(MerchantParam param);

    void saveOne(Merchant merchant);

    void deleteOne(Integer id);
}
