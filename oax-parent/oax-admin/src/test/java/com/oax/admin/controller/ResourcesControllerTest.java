package com.oax.admin.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.oax.admin.service.count.CountDataService;
import com.oax.common.ResultResponse;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.oax.OaxApiApplicationTest;
import com.oax.admin.service.ResourcesService;
import com.oax.entity.admin.Resources;
import com.oax.entity.admin.vo.MenuVo;

import lombok.extern.slf4j.Slf4j;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/4
 * Time: 16:21
 */
@Component
@Slf4j
public class ResourcesControllerTest extends OaxApiApplicationTest {

    @Autowired
    private ResourcesService resourcesService;


    @Test
    public void getResourcesList() {

        PageInfo<Resources> resourcesPageInfo = resourcesService.selectByPage(1, 10);

        assert resourcesPageInfo != null;
    }


    @Test
    public void loadMenu() {

        List<MenuVo> resourcesList = resourcesService.selectUserMenu(1);

        Map<Integer, List<MenuVo>> collect = resourcesList.stream().collect(Collectors.groupingBy(MenuVo::getParentid));

        List<MenuVo> listByParentId = getListByParentId(collect, 1);

        String s = JSON.toJSONString(listByParentId);


        System.out.println(s);
        assert listByParentId.size() > 0;
    }

    private List<MenuVo> getListByParentId(Map<Integer, List<MenuVo>> collect, Integer id) {

        List<MenuVo> menuVos = collect.get(id);

        if (CollectionUtils.isNotEmpty(menuVos)) {
            for (MenuVo menuVo : menuVos) {

                int pId = menuVo.getId();
                List<MenuVo> listByParentId = getListByParentId(collect, pId);
                menuVo.setChildrens(listByParentId);

            }
        }
        return menuVos;
    }

    @Test
    public void name() throws InterruptedException {

        ArrayList<Integer> integers = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);


        ArrayList<Integer> badInter = new ArrayList<>();
        integers.parallelStream()
                .forEach(
                       integer -> {
                           try {
                               add(integer);
                           } catch (Exception e) {
                               badInter.add(integer);
                           }
                       }
                );

        log.info("badInter:{}",badInter.toString());
    }

    private void add(Integer integer) {

//        try {
////            Thread.sleep(5);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        int i = 10 / integer;
        log.info("{}",integer);


    }

    @Test
    public void getCount() {
        CountDataController controller = new CountDataController();
//        BigDecimal count =  controller.getCountDataBHBorBCB(54);
//        System.out.println(count);

//        return new ResultResponse(true, count);
    }
}