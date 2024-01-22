package com.my.comparustesttask.factory;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

import javax.sql.DataSource;

public class DataSourceFactory {
    public static DataSource createDataSource(com.my.comparustesttask.postprocessor.DataSourceProperties properties) {
        DataSourceProperties dataSourceProperties = new DataSourceProperties();
        dataSourceProperties.setUrl(properties.getUrl());
        dataSourceProperties.setUsername(properties.getUser());
        dataSourceProperties.setPassword(properties.getPassword());
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }
}
