package com.oax.admin.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;


@Configuration
@EnableTransactionManagement
public class DruidConfig {

    @Value("${datasource.type}")
    private Class<? extends DataSource> dataSourceType;


    @Bean
    @ConfigurationProperties(prefix = "master.datasource")
    public DataSource getDataSource() {
        return DataSourceBuilder.create().type(dataSourceType).build();
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }
    @Bean
    public ServletRegistrationBean druidServlet() {

        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        //登录查看信息的账号密码.

        servletRegistrationBean.addInitParameter("loginUsername", "oax");

        servletRegistrationBean.addInitParameter("loginPassword", "oax");

        servletRegistrationBean.addInitParameter("resetEnable","false");
        return servletRegistrationBean;
    }
}
