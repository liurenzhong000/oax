package com.oax.admin.service;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oax.OaxApiApplicationTest;
import com.oax.entity.admin.Resources;
import com.oax.entity.front.Market;
import com.oax.mapper.admin.ResourcesMapper;
import com.oax.mapper.front.MarketMapper;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/4
 * Time: 14:01
 */
@Component
public class MarketServiceTest extends OaxApiApplicationTest {

    @Autowired
    private MarketMapper marketMapper;

    @Autowired
    private ResourcesMapper resourcesMapper;

    @Test
    public void test() {
        Market market = marketMapper.selectByPrimaryKey(1);

        assert market == null;
    }

    @Test
    public void test2() {
        List<Resources> resources = resourcesMapper.selectAll();

        assert resources.size() > 0;


    }
}