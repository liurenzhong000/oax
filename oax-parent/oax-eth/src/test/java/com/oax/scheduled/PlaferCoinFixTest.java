package com.oax.scheduled;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oax.OaxApiApplicationTest;
import com.oax.common.enums.PlatformTransferTypeEnum;
import com.oax.entity.front.PlatformTransfer;
import com.oax.eth.utils.EthRPCApiUtils;
import com.oax.mapper.front.PlatformTransferMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/7/27
 * Time: 15:53
 */
@Component
@Slf4j
public class PlaferCoinFixTest extends OaxApiApplicationTest {

    @Autowired
    private PlatformTransferMapper platformTransferMapper;

    @Autowired
    private EthRPCApiUtils ethRPCApiUtils;

    @Test
    public void test() throws Exception {

        List<PlatformTransfer> platformTransferList = platformTransferMapper.selectByType(PlatformTransferTypeEnum.TO_MAIN_ADDRESS_TYPE.getType());


        for (PlatformTransfer platformTransfer : platformTransferList) {


            String txId = platformTransfer.getTxId();

            Map<String, Object> transactionByHash = ethRPCApiUtils.getTransactionByHash(txId);

            String input = (String) transactionByHash.get("input");


            log.info(input);

        }

    }
}
