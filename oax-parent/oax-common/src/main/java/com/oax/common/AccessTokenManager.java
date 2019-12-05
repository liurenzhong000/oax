package com.oax.common;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccessTokenManager {

    @Autowired
    private RedisUtil redisUtil;

    public String create(Integer userId,Integer source) {

        String accessToken = null;
        try {

            InetAddress addr = InetAddress.getLocalHost();
            accessToken = addr.getHostAddress().toString().replaceAll("\\.", "") + UUID.randomUUID().toString().replaceAll("\\-", "");
            if(source.intValue()==1){
                redisUtil.setString("accessToken." + userId, accessToken);
            }else{
                redisUtil.setString("accessToken." + userId, accessToken,7 * 24 * 60 * 60);
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return accessToken;
    }
    
    public boolean check(Integer userId, String accessToken,Integer source) {
        boolean flag = false;
        if (accessToken != null && !"".equals(accessToken)) {

            String key = "accessToken." + userId;
            String rightAccessToken = redisUtil.getString(key);

            flag = accessToken.equals(rightAccessToken);
            if (flag) {
               Long timeout=source.intValue()==1?3600l:604800l;
                flag = redisUtil.expire(key,timeout);
            }

        }
        return flag;
    }

    public void delete(Integer userId) {

        String key = "accessToken." + userId;
        redisUtil.delete(key);

    }

    public String createTest(Integer userId) {
        String accessToken = null;

		accessToken = "oaxtoken" + userId;
		redisUtil.setString("accessTokenTest." + userId, accessToken);
        return accessToken;
    }
    
    public boolean checkTest(Integer userId, String accessToken) {
        boolean flag = false;
        if (accessToken != null && !"".equals(accessToken)) {

            String key = "accessTokenTest." + userId;
            String rightAccessToken = redisUtil.getString(key);

            flag = accessToken.equals(rightAccessToken);
            if (flag) {
                flag = redisUtil.expire(key);
            }

        }
        return flag;
    }

}
