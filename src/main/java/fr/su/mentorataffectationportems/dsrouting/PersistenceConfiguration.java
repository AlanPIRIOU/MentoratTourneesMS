package fr.su.mentorataffectationportems.dsrouting;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "fr.su.mentorataffectationportems.model",
        entityManagerFactoryRef = "multiEntityManager",
        transactionManagerRef = "multiTransactionManager"
)
public class PersistenceConfiguration {


    private static final Logger LOGGER = LogManager.getLogger(PersistenceConfiguration.class);

    private static final String PACKAGE_SCAN = "fr.su.mentorataffectationportems.model";

    @Autowired
    private Environment env;

    @Value("${DATASOURCE_LIST}")
    private String[] listDatasource;

    @Bean(name = "multiEntityManager")
    public LocalContainerEntityManagerFactoryBean multiEntityManager(DataSource dataSourceRouting) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSourceRouting);
        em.setPackagesToScan(PACKAGE_SCAN);
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(hibernateProperties());
        return em;
    }

    @Bean(name = "dataSourceRouting")
    public DataSource getDatasource() {
        Map<Object, Object> map = new HashMap<>();
        EntrepotRoutingDataSource entrepotRoutingDataSource = new EntrepotRoutingDataSource();
        for (String dsName : listDatasource) {
            String url = env.getProperty("DATABASE_URL_" + dsName);
            String userName = env.getProperty("DATABASE_USERNAME_" + dsName);
            String password = env.getProperty("DATABASE_PASSWORD_" + dsName);
            if (checkDatasourceParams(dsName, url, userName, password)) {
                HikariConfig config = new HikariConfig();
                config.setDriverClassName("com.ibm.as400.access.AS400JDBCDriver");
                config.setJdbcUrl(url);
                config.setUsername(userName);
                config.setPassword(password);
                config.setConnectionTestQuery("values 1");
                config.setMaximumPoolSize(5);
                config.setConnectionTimeout(3000);
                // Like this you can configure multiple properties here
                HikariDataSource d = new HikariDataSource(config);
                map.put(dsName, d);
            }
        }
        entrepotRoutingDataSource.setTargetDataSources(map);

        return entrepotRoutingDataSource;
    }

    private boolean checkDatasourceParams(String dsName, String url, String userName, String password) {
        if (url == null || url.isEmpty()) {
            LOGGER.error("No URL for DS {}", dsName);
            return false;
        }
        if (userName == null || userName.isEmpty()) {
            LOGGER.error("No user name for DS {}", dsName);
            return false;
        }
        if (password == null || password.isEmpty()) {
            LOGGER.error("No password for DS {}", dsName);
            return false;
        }
        return true;
    }

    @Bean(name = "multiTransactionManager")
    public PlatformTransactionManager multiTransactionManager(DataSource dataSourceRouting) {
        JpaTransactionManager transactionManager
                = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(
                multiEntityManager(dataSourceRouting).getObject());
        return transactionManager;
    }

    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", env.getProperty("spring.jpa.properties.hibernate.dialect"));
        properties.put("hibernate.show_sql", env.getProperty("spring.jpa.show-sql"));
        return properties;
    }
}