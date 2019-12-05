package com.oax.mapper.front;

import com.oax.OaxApiApplicationTest;
import com.oax.admin.service.count.CountDataService;
import com.oax.common.ResultResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;

//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@Slf4j
public class CountDataTest extends OaxApiApplicationTest
{

    @Autowired
    private CountDataService countDataService;

    @Test
    public void Test(){
//        @PathVariable(name = "coin_id") int coin_id
        BigDecimal count =  countDataService.CountBorC(54);
        System.out.println(count);
//        return new ResultResponse(true, count);
    }

}
