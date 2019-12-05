package com.oax.druid;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 *  动态数据源
 */
public class DynamicDataSource extends AbstractRoutingDataSource{
    @Override
    protected Object determineCurrentLookupKey() {
        if (DataSourceHolder.getDataSource() != null) {
            return DataSourceHolder.getDataSource();
        }
        return DataSourceType.MASTER.getType();
    }
}
