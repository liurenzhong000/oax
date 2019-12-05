package com.oax.webSocket;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URI;

/**
 * @Auther: hyp
 * @Date: 2019/1/20 16:01
 * @Description:
 */
@SpringBootTest
public class webSocketTest {

    @Test
    public void test(){
        try {
            // 这里用的binance的socket接口，国内调用需要VPN，使用换成你的就行
//            String url = "wss://stream.binance.com:9443/ws/ethbtc@ticker";
//            String url = "wss://stream.binance.com:9443/ws/ethbtc@depth20";
            //http://47.106.117.226:8080/endpointWisely/topic/marketCategory/all
            String url = "ws://localhost:8080/topic/marketList/54";
            URI uri = new URI(url);
            WebSocketClient mWs = new WebSocketClient(uri){
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    System.out.println("==========");
                }

                @Override
                public void onMessage(String s) {
                    System.out.println(s);
                }

                @Override
                public void onClose(int i, String s, boolean b) {
                    System.out.println("==========onClose"+s);
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                }
            };
            mWs.connect();
            System.out.println("haha");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(10000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
