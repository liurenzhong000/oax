package com.oax.service.eos;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.oax.common.EmptyHelper;
import com.oax.common.HttpRequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: hyp
 * @Date: 2019/1/10 11:31
 * @Description: eospack的接口，免费key有2/s 不可用
 */
@SpringBootTest
@Slf4j
public class EosPackTest {

    @Test
    public void test(){
        for (int i = 0; i < 100; i++) {
            getHashNumberList();
        }

    }

    public static List<Pair<String, Integer>> getHashNumberList(){
        List<Pair<String, Integer>> numList = new ArrayList<>();
        //获取EOS最顶部交易块
        String responseStr = HttpRequestUtil.sendGet("https://api.eospark.com/api", "module=block&action=get_latest_block&apikey=0316ba5bc0c60010ce30187c7f7ec4df");
        log.info(responseStr);
        JSONObject jsonObject = JSONObject.parseObject(responseStr);
        if (jsonObject.getInteger("errno") != 0){
            throw new RuntimeException("11");
        }
        JSONObject dataJsonObject = jsonObject.getJSONObject("data");
        JSONArray transactionsArray = dataJsonObject.getJSONArray("transactions");
        log.info("EOS区块高度：{}", dataJsonObject.getString("block_num"));
        for (Object transactions : transactionsArray) {
            JSONObject transactionsObj = (JSONObject) transactions;
            String hash;
            try {
                JSONObject trxObj = transactionsObj.getJSONObject("trx");
                hash = trxObj.getString("id");
                log.info("hash={}", hash);
            } catch (Exception e) {
                continue;
            }
            Integer number = getNumber(hash);
            if (number >= 0) {
                //TODO 布隆过滤器，去除重复的hash
                numList.add(new ImmutablePair<>(hash, number));
            }
        }


        if (EmptyHelper.isEmpty(numList)) {
            log.info("没有获取到数值，重新获取一次");
            numList = getHashNumberList();
        }
        return numList;
    }
    //从hash的尾数中获取数值
    public static Integer getNumber(String hash) {
        boolean flag = true;
        if (NumberUtils.isCreatable(StringUtils.substring(hash, hash.length() - 3, hash.length()))){
            Integer number = NumberUtils.toInt(StringUtils.substring(hash, hash.length() - 3, hash.length()));
            if (number.equals(100)) {
                return number;
            }
        }

        if (NumberUtils.isCreatable(StringUtils.substring(hash, hash.length() - 2, hash.length()))){
            Integer number = NumberUtils.toInt(StringUtils.substring(hash, hash.length() - 2, hash.length()));
            return number;
        }

        if (NumberUtils.isCreatable(StringUtils.substring(hash, hash.length() - 1, hash.length())) && flag){
            Integer number = NumberUtils.toInt(StringUtils.substring(hash, hash.length() - 1, hash.length()));
            return number;
        }
        return 0;
    }
}
