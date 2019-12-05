package com.oax.service.delay;

import com.oax.service.impl.activity.SnatchActivityServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * @Auther: hyp
 * @Date: 2019/1/24 17:25
 * @Description:
 */
@Slf4j
public class GetHashNumberListCallable implements Callable<List<Pair<Integer, String>>> {

    private Integer scope;
    public GetHashNumberListCallable(Integer scope){
        this.scope = scope;
    }

    @Override
    public List<Pair<Integer, String>> call() throws Exception {
        List<Pair<Integer, String>> list = new ArrayList<>();
        try {
            list = SnatchActivityServiceImpl.getHashNumberList(scope);
        } catch (Exception e) {
            log.warn("开奖获取hash接口失败.");
        }
        return list;
    }
}
