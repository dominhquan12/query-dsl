package com.example.demo.aop;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;
@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
public class AppConfig implements TransactionManagementConfigurer {

    private final EntityManagerFactory emf;

    public AppConfig(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new JpaTransactionManager(emf);
    }

    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return transactionManager(); // trả về bean đúng với EMF
    }

}
