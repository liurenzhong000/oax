package com.oax.admin.service.impl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oax.entity.admin.param.rushPageParam;
import com.oax.entity.admin.vo.MovesayMoneyActiveListVO;
import com.oax.entity.front.MovesayMoneyActiveList;
import com.oax.entity.front.MovesayMoneyActiveListExample;
import com.oax.mapper.front.MovesayMoneyActiveListMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.oax.admin.service.MoneyActiveUserListService;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: kevin
 * Date: 2018/10/17
 * Time: 23:07
 * app 业务扩展接口
 */
@Service
public class MoneyActiveUserListServiceImpl implements MoneyActiveUserListService {



    @Autowired
    private MovesayMoneyActiveListMapper movesayMoneyActiveListMapper;


    @Override
    public PageInfo<MovesayMoneyActiveListVO> selectByPageParam(rushPageParam rushPageParam) {
        PageHelper.startPage(rushPageParam.getPageNum(), rushPageParam.getPageSize());

        List<MovesayMoneyActiveListVO> list = movesayMoneyActiveListMapper.selectRushPage(rushPageParam);
        return new PageInfo<MovesayMoneyActiveListVO>(list);
    }

}
