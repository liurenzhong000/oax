package com.oax.common;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ：xiangwh
 * @ClassName:：RequestUtil
 * @Description： 处理请求信息请求工具类
 * @date ：2018年6月20日 上午10:18:43
 */
public class DeviceUtil {

    public static int getPlatform(HttpServletRequest request) {
        /**User Agent中文名为用户代理，简称 UA，它是一个特殊字符串头，使得服务器
         能够识别客户使用的操作系统及版本、CPU 类型、浏览器及版本、浏览器渲染引擎、浏览器语言、浏览器插件等*/
        String agent = request.getHeader("user-agent");
        //客户端类型常量 默认为pc端   1是pc   2是app
        int type = 1;
        if (agent.contains("iPhone") || agent.contains("iPod") || agent.contains("iPad")) {
            type = 2;
        } else if (agent.contains("Android") || agent.contains("Linux")) {
            type = 2;
        } else if (agent.indexOf("micromessenger") > 0) {
            type = 1;
        }
        return type;
    }

    public static int getPlatformType(HttpServletRequest request) {
        /**User Agent中文名为用户代理，简称 UA，它是一个特殊字符串头，使得服务器
         能够识别客户使用的操作系统及版本、CPU 类型、浏览器及版本、浏览器渲染引擎、浏览器语言、浏览器插件等*/
        String agent = request.getHeader("user-agent");
        //客户端类型常量 默认为pc端   1是pc   2是app
        int type = 1;
        if (agent.contains("iPhone") || agent.contains("iPod") || agent.contains("iPad")) {
            type = 2;
        } else if (agent.contains("Android") || agent.contains("Linux")) {
            type = 3;
        } else if (agent.indexOf("micromessenger") > 0) {
            type = 1;
        }
        return type;
    }
}
