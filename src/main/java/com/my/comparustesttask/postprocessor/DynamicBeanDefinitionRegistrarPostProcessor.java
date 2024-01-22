package com.my.comparustesttask.postprocessor;

import com.my.comparustesttask.factory.DataSourceFactory;
import com.my.comparustesttask.factory.EntityManagerFactory;
import com.my.comparustesttask.repository.UserRepository;
import com.my.comparustesttask.repository.UserRepositoryImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * BeanDefinitionRegistryPostProcessor register dedicated amount of EntityManagerFactory beans based on yaml config file
 * and custom @{@link com.my.comparustesttask.repository.UserRepositoryImpl UserRepository} beans accordingly which
 * can use related EntityManager for obtaining data from DB.
 */
public class DynamicBeanDefinitionRegistrarPostProcessor implements BeanDefinitionRegistryPostProcessor {

    public static final String DATA_SOURCE_PROPERTIES_PREFIX = "datasources";
    public static final String HIBERNATE_PROPERTIES_PREFIX = "spring.jpa";
    private final List<DataSourceProperties> dataSourcePropertiesList;
    private final Map<String, String> hibernateProperties;

    public DynamicBeanDefinitionRegistrarPostProcessor(Environment environment) {
        dataSourcePropertiesList = Binder.get(environment)
                .bind(DATA_SOURCE_PROPERTIES_PREFIX, Bindable.listOf(DataSourceProperties.class))
                .orElseThrow(IllegalStateException::new);

        hibernateProperties = Binder.get(environment)
                .bind(HIBERNATE_PROPERTIES_PREFIX, Bindable.mapOf(String.class, String.class))
                .orElseThrow(IllegalStateException::new);
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        dataSourcePropertiesList.forEach(dsProperty -> {
            //building EntityManagerFactory bean
            DataSource dataSource = DataSourceFactory.createDataSource(dsProperty);
            LocalContainerEntityManagerFactoryBean entityManager = EntityManagerFactory.createEntityManager(dsProperty, dataSource, hibernateProperties);
            final String entityManagerFactoryBeanName = "entityManagerFactory-" + dsProperty.getName();
            createGenericBeanDefinition(entityManager, entityManager.getClass(), entityManagerFactoryBeanName, beanDefinitionRegistry);

            //building custom UserRepository bean
            UserRepository userRepository = new UserRepositoryImpl(dsProperty.getMapping(), dsProperty.getTable(), entityManagerFactoryBeanName);
            final String userRepositoryBeanName = "userRepository-" + dsProperty.getName();
            createGenericBeanDefinition(userRepository, userRepository.getClass(), userRepositoryBeanName, beanDefinitionRegistry);
        });
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        // no op
    }

    private <T> void createGenericBeanDefinition(T beanInstance, Class<? extends T> beanClass, String beanName, BeanDefinitionRegistry beanDefinitionRegistry) {
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(beanClass);
        beanDefinition.setInstanceSupplier(() -> beanInstance);
        beanDefinitionRegistry.registerBeanDefinition(beanName, beanDefinition);
    }
}
