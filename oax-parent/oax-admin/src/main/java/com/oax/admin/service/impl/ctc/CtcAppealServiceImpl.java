package com.oax.admin.service.impl.ctc;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oax.admin.service.ctc.CtcAppealService;
import com.oax.entity.admin.param.CtcAppealParam;
import com.oax.entity.admin.vo.CtcAppealVo;
import com.oax.entity.enums.CtcOrderType;
import com.oax.mapper.ctc.CtcAppealMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CtcAppealServiceImpl implements CtcAppealService {

    @Autowired
    private CtcAppealMapper ctcAppealMapper;

    @Override
    public PageInfo<CtcAppealVo> page(CtcAppealParam param) {
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        List<CtcAppealVo> ctcAppealVos = ctcAppealMapper.pageForAdmin(param);
        ctcAppealVos.forEach( item ->{
            item.setStatusDesc(item.getStatus().desc);
            item.setTypeDesc(item.getType().descForUser);
            item.setOrderStatusDesc(item.getOrderStatus().desc);
            item.setTradeTypeDesc("网银支付");
//            if (item.getType() == CtcOrderType.USER_SALE) {//用户卖出类型的，sql查询内容用户数据和商家数据相反，代码转换
//                Integer userId = item.getMerchantUserId();
//                String userIdName = item.getMerchantIdName();
//                String userName = item.getMerchantUserName();
//                item.setMerchantUserId(item.getUserId());
//                item.setMerchantIdName(item.getIdName());
//                item.setMerchantUserName(item.getUserName());
//                item.setUserId(userId);
//                item.setIdName(userIdName);
//                item.setUserName(userName);
//            }
        });
        return new PageInfo<>(ctcAppealVos);
    }
}
