package com.oax.api;

import com.oax.domain.TradeData;
import lombok.Data;


@Data
public class TradeResponse<T> {
    /*{
        "status": "ok",
            "ch": "market.btcusdt.trade.detail",
            "ts": 1489473346905,
          "tick": {
                "id": 600848670,
                "ts": 1489464451000,
                "data": [{
                   "id": 600848670,
                "price": 7962.62,
                "amount": 0.0122,
                "direction": "buy",
                "ts": 1489464451000}]
                 }
    }*/
    private String status;
    private String ch;
    private String ts;
    public String errCode;
    public String errMsg;
    private TradeData tick;
}
