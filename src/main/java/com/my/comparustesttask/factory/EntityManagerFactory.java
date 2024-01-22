package com.my.comparustesttask.factory;

import com.my.comparustesttask.postprocessor.DataSourceProperties;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.Map;

public class EntityManagerFactory {
    public static LocalContainerEntityManagerFactoryBean createEntityManager(DataSourceProperties properties, DataSource dataSource, Map<String, String> hibernateProperties) {
        LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
        entityManager.setPersistenceUnitName("unit-" + properties.getName());
        entityManager.setDataSource(dataSource);
        entityManager.setPackagesToScan("com.my.comparustesttask.entity");
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        entityManager.setJpaVendorAdapter(vendorAdapter);
        entityManager.setJpaPropertyMap(hibernateProperties);
        return entityManager;
    }
}
