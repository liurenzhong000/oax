package com.oax.druid;

import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 *  动态获取DataSource 实现读写分离
 */
@Configuration
//@PropertySource(value = "classpath:/db.properties")
@MapperScan(value = "com.oax.mapper")
@EnableTransactionManagement
public class DataSourceConfig {
	
	@Value("${datasource.type}")
	private Class<? extends DataSource> dataSourceType;

    @Autowired
    WallFilter wallFilter;

    @Bean(name = "masterDataSource")
    @ConfigurationProperties(prefix = "master.datasource")
    public DataSource masterDataSource() {
        return DataSourceBuilder.create().type(dataSourceType).build();
    }

    @Bean(name = "slaveDataSource")
    @ConfigurationProperties(prefix = "slave.datasource")
    public DataSource slaveDataSource() {
    	return DataSourceBuilder.create().type(dataSourceType).build();
    }

    //动态获取数据源
    @Bean(name = "dynamicDataSource")
    @Primary
    public DataSource getDataSource() {
        DynamicDataSource dataSource = new DynamicDataSource();
        dataSource.setTargetDataSources(targetDataSources());
        return dataSource;
    }

    private Map<Object, Object> targetDataSources() {
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DataSourceType.MASTER.getType(), masterDataSource());
        targetDataSources.put(DataSourceType.SLAVE.getType(), slaveDataSource());
        return targetDataSources;
    }

    @Bean(name = "wallFilter")
    @DependsOn("wallConfig")
    public WallFilter wallFilter(WallConfig wallConfig){
        WallFilter wallFilter = new WallFilter();
        wallFilter.setConfig(wallConfig);
        return wallFilter;
    }

    @Bean(name = "wallConfig")
    public WallConfig wallConfig(){
        WallConfig wallConfig = new WallConfig();
        wallConfig.setMultiStatementAllow(true);//允许一次执行多条语句
        wallConfig.setNoneBaseStatementAllow(true);//允许一次执行多条语句
        return wallConfig;
    }

}
