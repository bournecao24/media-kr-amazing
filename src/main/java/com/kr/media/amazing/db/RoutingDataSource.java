package com.kr.media.amazing.db;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;


/**
 * 如何存储动态选择的 key 以及在哪设置 key ？
 */
public class RoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return RoutingDataSourceContext.getDataSourceRoutingKey();
    }
}
