package com.oax.utils;

import java.math.BigDecimal;
import java.util.regex.Pattern;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.oax.OaxApiApplicationTest;

import lombok.extern.slf4j.Slf4j;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/28
 * Time: 14:26
 */
@Slf4j
@Component
public class BtcRPCApiUtilsTest extends OaxApiApplicationTest {


    @Autowired
    private BtcRPCApiUtils btcRPCApiUtils;
    @Test
    public void newAccount() {

        String s = null;
        try {
            s = btcRPCApiUtils.newAccount();
        } catch (Exception e) {
            e.printStackTrace();
        }
        boolean matches = Pattern.matches("^[13][a-km-zA-HJ-NP-Z1-9]{25,35}$", s);

        log.info("newAccount::{},长度:{},匹配结果:{}",s,s.length(),matches);

    }

    @Test
    public void getblockcount() {


    }

    @Test
    public void getbalance() {

        String getbalance = null;
        String userBalance = null;

        try {
            getbalance = btcRPCApiUtils.getbalance(null);
            userBalance = btcRPCApiUtils.getbalance("2NBjtY4DjUvAv67MimaviF5dEdfG15Cfg6L");
        } catch (Exception e) {
            e.printStackTrace();
        }



        log.info("serverBalance::{}",getbalance);
        log.info("userBalance::{}",userBalance);

    }

    @Test
    public void sendtoaddress() {


        String txId = null;
        try {
            txId = btcRPCApiUtils.sendtoaddressByRaw("2NBjtY4DjUvAv67MimaviF5dEdfG15Cfg6L", new BigDecimal("0.2"),"moUPk9iZMjZ5dXxFhjRdZXGfURKmgqpBEq",new BigDecimal("0.01"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.info("sendtoaddress::{}",txId);

    }

    @Test
    public void listtransactions() {

        JSONArray listtransactions = null;

        try {
            listtransactions = btcRPCApiUtils.listtransactions();
        } catch (Exception e) {
            e.printStackTrace();
        }


        JSONObject o = (JSONObject)listtransactions.get(1);
        for (Object listtransaction : listtransactions) {

            JSONObject jsonObject = (JSONObject) listtransaction;


            log.info(jsonObject.toJSONString());
        }

//        log.info("listtransactions::{}",listtransactions);
    }

    @Test
    public void getTransaction() {

        JSONObject transaction = null;
        try {
            transaction = btcRPCApiUtils.getTransaction("1d1a63ba53780cf9e97347f308753d4717be8b3299c1c18e448101751cf02f2");
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.info("transaction::{}",transaction.toJSONString());
    }


    @Test
    public void sendtoaddressByRaw() throws Exception {


        String s = btcRPCApiUtils.sendtoaddressByRaw("mfrowNgczrif4imCFtwEQoa2sYtjbjSqeo", BigDecimal.ONE, "moUPk9iZMjZ5dXxFhjRdZXGfURKmgqpBEq",new BigDecimal("2"));

        log.info(s);
    }
}