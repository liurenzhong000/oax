package com.oax.service.impl;

import com.oax.common.HttpRequestUtil;
import org.junit.Test;

/**
 * mytoken.io 搜索BHB
 */
public class MytokenTest {

    //http://mytoken.io/api/ticker/search?category=currency&keyword=BHB&timestamp=1543989015032&code=b78d1daaace953177c719067cae1e7de&platform=web_pc&v=1.0.0&language=zh_CN&legal_currency=CNY
    @Test
    public void searchBHB(){
        while (true) {
            String str = HttpRequestUtil.sendGet("http://mytoken.io/api/ticker/search", "?category=currency&keyword=BHB&timestamp=1543989015032&code=b78d1daaace953177c719067cae1e7de&platform=web_pc&v=1.0.0&language=zh_CN&legal_currency=CNY");
            System.out.println(str);
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis());
    }
}
