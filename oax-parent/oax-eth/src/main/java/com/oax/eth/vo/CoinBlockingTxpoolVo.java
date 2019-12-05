package com.oax.eth.vo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/26
 * Time: 9:59
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoinBlockingTxpoolVo {

    /**
     * 币种名称
     */
    private String coinName;

    /**
     * 币种id
     */
    private int coinId;

    private List<String> blockingTxHash;


}
