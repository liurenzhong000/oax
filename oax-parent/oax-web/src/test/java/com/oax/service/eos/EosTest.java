package com.oax.service.eos;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.oax.common.EmptyHelper;
import com.oax.common.HttpRequestUtil;
import com.oax.common.json.JsonHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Auther: hyp
 * @Date: 2019/1/8 10:58
 * @Description: 从eso浏览器中获取区块信息，对hash进行hashcode并取模 eostracker.io
 */
@SpringBootTest
@Slf4j
public class EosTest {


    @Test
    public void test() throws InterruptedException {
        List<Pair<String, Integer>> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Thread.sleep(500);
            list.addAll(getHashNumberList(100));
        }

        List<Integer> modelList = list.stream().map(pair-> pair.getRight()).collect(Collectors.toList());
        Map<Integer, Integer> map = modelList.parallelStream().collect(Collectors.toConcurrentMap(w -> w, w -> 1, Integer::sum));
        log.info(JsonHelper.writeValueAsString(map));
    }

    public static List<Pair<String, Integer>> getHashNumberList(Integer scope){
        List<Pair<String, Integer>> numList = new ArrayList<>();
        //获取EOS最顶部交易块
        String responseStr = HttpRequestUtil.sendGet("https://proxy.eosnode.tools/v1/chain/get_info", "");
        JSONObject jsonObject = JSONObject.parseObject(responseStr);
        Integer headBlockNum = jsonObject.getInteger("head_block_num");
        log.info("获取当前EOS区块高度={}", headBlockNum);
        //获取最顶部交易块的交易数据
        String hashStr = HttpRequestUtil.sendPost("https://proxy.eosnode.tools/v1/chain/get_block", "{\"block_num_or_id\": $headBlockNum}".replace("$headBlockNum", headBlockNum+""));
        JSONObject hashStrObject = JSONObject.parseObject(hashStr);
        JSONArray transactionsArray = hashStrObject.getJSONArray("transactions");
        for (Object transactions : transactionsArray) {
            JSONObject transactionsObj = (JSONObject) transactions;
            String hash;
            try {
                JSONObject trxObj = transactionsObj.getJSONObject("trx");
                hash = trxObj.getString("id");
            } catch (Exception e) {
                continue;
            }
            Integer number = getModel(hash, scope);
            if (number >= 0) {
                //TODO 布隆过滤器，去除重复的hash
                numList.add(new ImmutablePair<>(hash, number));
            }
        }

        if (EmptyHelper.isEmpty(numList)) {
            log.info("没有获取到数值，重新获取一次");
            numList = getHashNumberList(scope);
        }
        return numList;
    }

    public static Integer getModel(String hash, Integer scope){
        Integer code = new String(hash).hashCode();
        Integer model = Math.abs(code % scope);
        log.info("hash={} - code={} - model={}", hash, code, model);
        return model;
    }


}
