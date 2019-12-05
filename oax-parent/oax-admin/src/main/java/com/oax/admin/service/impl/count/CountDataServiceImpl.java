package com.oax.admin.service.impl.count;

import com.oax.admin.service.count.CountDataService;
import com.oax.mapper.front.UserCoinMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
public class CountDataServiceImpl implements CountDataService {

    @Autowired
    private UserCoinMapper userCoinMapper;

    /**
     * 获取总量
     * */
    public BigDecimal CountBorC(Integer coinId){
        BigDecimal count =  userCoinMapper.getCountBHBORBCB(coinId);
        return count;
    }

    /**
     * 获取大于700的BHB的电话号码
     * */
//    public List<CountDataVo> phoneList(){
//        return userCoinMapper.getCountSelectPhone();
//    }
}
