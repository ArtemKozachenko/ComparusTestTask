package com.my.comparustesttask.repository;

import com.my.comparustesttask.config.ApplicationContextProvider;
import com.my.comparustesttask.entity.User;
import com.my.comparustesttask.postprocessor.DataSourceProperties;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class UserRepositoryImpl implements UserRepository {
    private EntityManager entityManager;
    private final DataSourceProperties.ColumnsMapping mapping;
    private final String tableName;
    private final String entityManagerFactoryBeanName;

    private static final String SELECT_ALL_FROM_QUERY = "SELECT %s as id, %s as username, %s as name, %s as surname FROM %s";

    public UserRepositoryImpl(DataSourceProperties.ColumnsMapping mapping, String tableName, String entityManagerFactoryBeanName) {
        this.mapping = mapping;
        this.tableName = tableName;
        this.entityManagerFactoryBeanName = entityManagerFactoryBeanName;
    }

    @PostConstruct
    private void postConstruct() {
        EntityManagerFactory managerFactory = ApplicationContextProvider.getApplicationContext().getBean(entityManagerFactoryBeanName, EntityManagerFactory.class);
        entityManager = managerFactory.createEntityManager();
        log.info("postConstruct method executed, managerFactory name: {}", entityManagerFactoryBeanName);
    }

    @Override
    public List<User> findAll() {
        return entityManager.createNativeQuery(getMappedSelectAllQuery(), User.class).getResultList();
    }

    @Override
    public List<User> findByName(String name) {
        return entityManager.createNativeQuery(getMappedWhereColumnIsQuery(mapping.getName()), User.class)
                .setParameter(1, name).getResultList();
    }

    private String getMappedWhereColumnIsQuery(String columnName) {
        StringBuilder builder = new StringBuilder(getMappedSelectAllQuery());
        builder.append(" ").append("WHERE").append(" ").append(columnName).append("=?");
        return builder.toString();
    }

    private String getMappedSelectAllQuery() {
        return String.format(SELECT_ALL_FROM_QUERY, mapping.getId(), mapping.getUsername(), mapping.getName(), mapping.getSurname(), tableName);
    }

}
