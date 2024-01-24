package com.my.comparustesttask;

import org.testcontainers.containers.PostgreSQLContainer;

public class PostgreDB1ContainerBase extends PostgreSQLContainer<PostgreDB1ContainerBase> {
    private static final String IMAGE_VERSION = "postgres:11.1";
    private static PostgreDB1ContainerBase container;

    private PostgreDB1ContainerBase() {
        super(IMAGE_VERSION);
    }

    public static PostgreDB1ContainerBase getInstance() {
        if (container == null) {
            container = new PostgreDB1ContainerBase()
                    .withDatabaseName("data-base-1")
                    .withUsername("sa")
                    .withPassword("sa")
                    .withInitScript("dbscripts/create_and_populate_users_table.sql");
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("DB_URL", container.getJdbcUrl());
    }

    @Override
    public void stop() {
        super.stop();
    }
}
