package com.oax.common;

import java.util.HashMap;
import java.util.Map;

public class MapHelper {
    public static Map<String, Object> buildParams(Object... paramPair) {
        Map<String, Object> params = new HashMap<>();
        if (paramPair != null) {
            if (paramPair.length % 2 != 0){
                throw new RuntimeException("参数个数不对，参数名和值必须成对出现");
            }
            for (int i = 0; i < paramPair.length; i++) {
                String key = (String) paramPair[i++];
                Object paramVal = paramPair[i];
                params.put(key, paramVal);
            }
        }
        return params;
    }
}
