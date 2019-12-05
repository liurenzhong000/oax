package com.oax.service;

import java.util.List;
import java.util.Map;

public interface RedPacketService {

    public List<Map<String,Object>> findOverdueRedPacket();

    boolean upUserCoinAndInsertRecharge(Map<String,Object> params) throws Exception;
}
