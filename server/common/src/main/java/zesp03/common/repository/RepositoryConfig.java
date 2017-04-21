package zesp03.common.repository;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import zesp03.common.core.App;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableJpaRepositories
@EnableTransactionManagement
public class RepositoryConfig {
    @Bean(destroyMethod = "close")
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setIdleTimeout(30_000L);
        config.setMaximumPoolSize(10);
        config.setDriverClassName("com.mysql.jdbc.Driver");
        config.setConnectionTestQuery("SELECT 1");
        config.setPoolName("springHikariCP");
        config.setAutoCommit(false);
        config.setUsername(App.getMysqlUser());
        config.setJdbcUrl(App.getMysqlUrl());
        config.setPassword(App.getMysqlPassword());
        return new HikariDataSource(config);
    }

    @Bean
    public EntityManagerFactory entityManagerFactory() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(false);

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setPersistenceUnitName("myPersistenceUnit");
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setDataSource(dataSource());
        factory.setPackagesToScan("zesp03.common.entity");

        HashMap<String, Object> map = new HashMap<>();
        map.put("hibernate.jdbc.batch_size", 100);
        map.put("hibernate.jdbc.batch_versioned_data", true);
        map.put("hibernate.order_inserts", true);
        map.put("hibernate.order_updates", true);
        map.put("hibernate.hbm2ddl.auto", "validate");
        map.put("hibernate.dialect", "org.hibernate.dialect.MySQL57InnoDBDialect");
        factory.setJpaPropertyMap(map);

        factory.afterPropertiesSet();
        return factory.getObject();
    }

    @Bean(name = "transactionManager")
    public JpaTransactionManager getTM(EntityManagerFactory emf) {
        JpaTransactionManager tm = new JpaTransactionManager();
        tm.setEntityManagerFactory(emf);
        return tm;
    }
}
