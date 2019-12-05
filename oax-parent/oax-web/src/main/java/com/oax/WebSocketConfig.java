package com.oax;

import com.oax.common.AccessTokenManager;
import com.oax.common.DeviceUtil;
import com.oax.context.HttpContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
@Slf4j
//通过EnableWebSocketMessageBroker 开启使用STOMP协议来传输基于代理(message broker)的消息,此时浏览器支持使用@MessageMapping 就像支持@RequestMapping一样。
//sendToUser 客户端订阅要多添加/user
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer{

    @Autowired
    private AccessTokenManager tokenManager;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) { //endPoint 注册协议节点,并映射指定的URl

        registry.addEndpoint("/endpointWisely").setHandshakeHandler(new DefaultHandshakeHandler() {
            @Override
            protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
                //将客户端标识封装为Principal对象，从而让服务端能通过getName()方法找到指定客户端
                Object o = attributes.get(Constant.api_header_userId);
                if (o == null) {
                    return null;
                }
                return new FastPrincipal(o.toString());
            }
        })
        //添加socket拦截器，用于从请求中获取客户端标识参数
        .addInterceptors(new HandleShakeInterceptors(tokenManager))
        .setAllowedOrigins("*").withSockJS();//注册一个Stomp 协议的endpoint,并指定 SockJS协议。
    }


    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {//配置消息代理(message broker)
        registry.enableSimpleBroker("/topic","/auth"); //广播式应配置一个/topic 消息代理 /auth表示需要鉴权

    }

    //定义一个自己的权限验证类
    class FastPrincipal implements Principal {
        private final String name;
        public FastPrincipal(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }
    }

    /**
     * 检查握手请求和响应, 对WebSocketHandler传递属性
     */
    public class HandleShakeInterceptors implements HandshakeInterceptor {

        AccessTokenManager tokenManager;

        public HandleShakeInterceptors(AccessTokenManager tokenManager){
            this.tokenManager = tokenManager;
        }
        /**
         * 在握手之前执行该方法, 继续握手返回true, 中断握手返回false.
         * 通过attributes参数设置WebSocketSession的属性
         * @param request
         * @param response
         * @param wsHandler
         * @param attributes
         * @return
         * @throws Exception
         */
        public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                       WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
            ServletServerHttpRequest req = (ServletServerHttpRequest) request;
            String userId = req.getServletRequest().getParameter(Constant.api_header_userId);
            String accessToken = req.getServletRequest().getParameter(Constant.api_header_accessToken);
            if(!StringUtils.isBlank(userId) && !StringUtils.isBlank(accessToken)) {
                boolean flag = tokenManager.check(Integer.parseInt(userId),accessToken,1);
                if (flag) {
                    attributes.put(Constant.api_header_userId, userId);
                }
            }
//            log.info("webSocket 连接，userId={}", userId);
            //保存客户端标识
            return true;
        }

        /**
         * 在握手之后执行该方法. 无论是否握手成功都指明了响应状态码和相应头.
         * @param request
         * @param response
         * @param wsHandler
         * @param exception
         */
        public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Exception exception) {

        }

    }







}