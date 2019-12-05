package com.oax.admin.service;

import com.github.pagehelper.PageInfo;
import com.oax.entity.admin.param.rushPageParam;
import com.oax.entity.admin.vo.MovesayMoneyActiveListVO;
import com.oax.entity.front.MovesayMoneyActiveList;


public interface MoneyActiveUserListService {

    PageInfo<MovesayMoneyActiveListVO> selectByPageParam(rushPageParam rushPageParam);

}

