package com.oax.admin.shiro;


import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oax.admin.enums.ResourcesTypeEnum;
import com.oax.admin.service.ResourcesService;
import com.oax.admin.shiro.config.RestPathMatchingFilterChainResolver;
import com.oax.admin.shiro.filter.MyAuthcFilter;
import com.oax.admin.shiro.filter.MyPermissionsAuthorizationFilter;
import com.oax.common.SpringContextHolder;
import com.oax.entity.admin.Resources;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/5/29
 * Time: 18:08
 * <p>
 * shiro Filter 管理器
 */
@Component
public class ShiroFilterChainManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShiroFilterChainManager.class);
    @Autowired
    private ResourcesService resourcesService;


    // 初始化获取过滤链
    public Map<String, Filter> initGetFilters() {
        Map<String, Filter> filters = new LinkedHashMap<>();
        MyAuthcFilter myAuthcFilter = new MyAuthcFilter();
        myAuthcFilter.setLoginUrl("/login");
        filters.put("myAuthc", myAuthcFilter);

        MyPermissionsAuthorizationFilter myPermissionsAuthorizationFilter = new MyPermissionsAuthorizationFilter();
        myPermissionsAuthorizationFilter.setUnauthorizedUrl("/403");

        filters.put("myPerms", myPermissionsAuthorizationFilter);
        return filters;
    }

    // 初始化获取过滤链规则
    public Map<String, String> initGetFilterChain() {
        Map<String, String> filterChain = new LinkedHashMap<>();
        // -------------anon 默认过滤器忽略的URL
        List<String> defalutAnon = Arrays.asList("/", "/test/**","/static/**","/home","/wetest","/druid/**");
        defalutAnon.forEach(ignored -> filterChain.put(ignored,"anon"));
        if (resourcesService != null) {
            List<Resources> rolePermRules = this.resourcesService.selectAllByType(ResourcesTypeEnum.BUTTON.getResourcesType());
            if (null != rolePermRules) {
                rolePermRules.forEach(rule -> {
                    String Chain = rule.toFilterChain();
                    if (null != Chain) {
                        filterChain.putIfAbsent(rule.getUrl(), Chain);
                    }
                });
            }
        }

        filterChain.put("/**", "myAuthc");
        return filterChain;
    }

    // 动态重新加载过滤链规则
    public void reloadFilterChain() {
        ShiroFilterFactoryBean shiroFilterFactoryBean = SpringContextHolder.getBean(ShiroFilterFactoryBean.class);
        AbstractShiroFilter abstractShiroFilter = null;
        try {
            abstractShiroFilter = (AbstractShiroFilter) shiroFilterFactoryBean.getObject();
            RestPathMatchingFilterChainResolver filterChainResolver = (RestPathMatchingFilterChainResolver) abstractShiroFilter.getFilterChainResolver();
            DefaultFilterChainManager filterChainManager = (DefaultFilterChainManager) filterChainResolver.getFilterChainManager();
            filterChainManager.getFilterChains().clear();
            shiroFilterFactoryBean.getFilterChainDefinitionMap().clear();
            shiroFilterFactoryBean.setFilterChainDefinitionMap(this.initGetFilterChain());
            shiroFilterFactoryBean.getFilterChainDefinitionMap().forEach((k, v) -> filterChainManager.createChain(k, v));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
