package com.oax.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oax.druid.DataSource;
import com.oax.druid.DataSourceType;
import com.oax.entity.front.WithdrawAddress;
import com.oax.mapper.front.WithdrawAddressMapper;
import com.oax.service.IWithdrawAddressService;
import com.oax.vo.WithdrawAddressListVO;

@Service
public class WithdrawAddressServiceImpl implements IWithdrawAddressService {

    @Autowired
    private WithdrawAddressMapper withdrawAddressMapper;

    @Transactional
    @DataSource(DataSourceType.MASTER)
    public int save(WithdrawAddress vo) {
        return withdrawAddressMapper.insertSelective(vo);
    }

	@Override
	@DataSource(DataSourceType.SLAVE)
	public PageInfo<WithdrawAddress> list(WithdrawAddressListVO vo) {
		PageHelper.startPage(vo.getPageIndex(), vo.getPageSize());
		List<WithdrawAddress> list=withdrawAddressMapper.getByUserIdQueryurlList(vo.getUserId(),vo.getCoinId());
	 return new PageInfo<>(list);
	}

	@Override
	@DataSource(DataSourceType.MASTER)
	public int deleteById(Integer id) {
		return withdrawAddressMapper.deleteByPrimaryKey(id);
	}

	@Override
	@DataSource(DataSourceType.MASTER)
	public int updateById(WithdrawAddress vo) {
		return withdrawAddressMapper.updateByPrimaryKeySelective(vo);		
	}
}
