package com.my.comparustesttask;

import org.testcontainers.containers.PostgreSQLContainer;

public class PostgreDB2ContainerBase extends PostgreSQLContainer<PostgreDB2ContainerBase> {
    private static final String IMAGE_VERSION = "postgres:11.1";
    private static PostgreDB2ContainerBase container;

    private PostgreDB2ContainerBase() {
        super(IMAGE_VERSION);
    }

    public static PostgreDB2ContainerBase getInstance() {
        if (container == null) {
            container = new PostgreDB2ContainerBase()
                    .withDatabaseName("data-base-2")
                    .withUsername("sa")
                    .withPassword("sa")
                    .withInitScript("dbscripts/create_and_populate_user_data_table.sql");
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("DB2_URL", container.getJdbcUrl());
    }

    @Override
    public void stop() {
        super.stop();
    }
}
