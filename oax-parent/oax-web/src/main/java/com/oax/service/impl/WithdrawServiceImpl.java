package com.oax.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.oax.common.enums.WithdrawStatusEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oax.druid.DataSource;
import com.oax.druid.DataSourceType;
import com.oax.entity.front.WithdrawResult;
import com.oax.mapper.front.WithdrawMapper;
import com.oax.service.IWithdrawService;
import com.oax.vo.WithdrawListVO;

@Service
public class WithdrawServiceImpl implements IWithdrawService {

    @Autowired
    private WithdrawMapper withdrawMapper;

    @DataSource(DataSourceType.SLAVE)
    public PageInfo<WithdrawResult> getByUserIdWithdraw(WithdrawListVO vo) {
    	Map<String, Object> map=new HashMap<>();
    	map.put("userId", vo.getUserId());
    	map.put("coinName", vo.getCoinName());
    	if(StringUtils.isNoneBlank(vo.getStatus())) {
    		String[] array=vo.getStatus().split(",");
    		map.put("status", array);
    	}
    	
    	PageHelper.startPage(vo.getPageIndex(), vo.getPageSize());
    	List<WithdrawResult> list=withdrawMapper.selectByUserId(map);
    	//提现拉黑的显示待审核
    	list.forEach(item -> {
    		if (item.getStatus() == WithdrawStatusEnum.BLOCK.getStatus()){
    			item.setStatus(((Byte)WithdrawStatusEnum.WAIT_FIRST_CHECK.getStatus()).intValue());
			}
		});
        return new PageInfo<>(list) ;
    }
}