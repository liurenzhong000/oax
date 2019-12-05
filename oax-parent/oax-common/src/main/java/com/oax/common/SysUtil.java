package com.oax.common;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SysUtil{
    public static String getRemoteIp(HttpServletRequest request) {
    	// 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址
    	 
		String ip = request.getHeader("X-Forwarded-For");   
        if (ip == null || ip.length() == 0||"unKnown".equalsIgnoreCase(ip)) {
        	ip = request.getHeader("X-Real-IP");
//        	log.info("getIpAddress(HttpServletRequest) - X-Real-IP - String ip=" + ip);
        }
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	        ip = request.getHeader("Proxy-Client-IP");
//	        log.info("getIpAddress(HttpServletRequest) - Proxy-Client-IP - String ip=" + ip);
	    }
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	        ip = request.getHeader("WL-Proxy-Client-IP");
//	        log.info("getIpAddress(HttpServletRequest) - WL-Proxy-Client-IP - String ip=" + ip);
	    }
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	        ip = request.getRemoteAddr();
//	        log.info("getIpAddress(HttpServletRequest) - getRemoteAddr - String ip=" + ip);
	    }
//		log.info("getIpAddress(HttpServletRequest) - String ip=" + ip);
	    return ip;
    }
}
