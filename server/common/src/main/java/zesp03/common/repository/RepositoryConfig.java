package zesp03.common.repository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import zesp03.common.core.App;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories
@EnableTransactionManagement
public class RepositoryConfig {
    @Bean(name = "entityManagerFactory")
    public EntityManagerFactory getEMF() {
        Map<String, String> map = new HashMap<>();
        map.put("javax.persistence.jdbc.password", App.getProperty("zesp03.mysql.password"));
        map.put("javax.persistence.jdbc.user", App.getProperty("zesp03.mysql.user"));
        map.put("javax.persistence.jdbc.url", App.getProperty("zesp03.mysql.url"));
        return Persistence.createEntityManagerFactory("CRM", map);
    }

    @Bean(name = "transactionManager")
    public JpaTransactionManager getTM(EntityManagerFactory emf) {
        JpaTransactionManager tm = new JpaTransactionManager();
        tm.setEntityManagerFactory(emf);
        return tm;
    }
}
