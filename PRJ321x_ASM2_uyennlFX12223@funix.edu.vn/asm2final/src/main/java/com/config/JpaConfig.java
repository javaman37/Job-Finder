package com.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "com")
@EnableTransactionManagement
@ComponentScan(basePackages = {"com"})
public class JpaConfig {

    // Cấu hình nguồn dữ liệu (DataSource) cho kết nối MySQL
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/spring_workcv?allowPublicKeyRetrieval=true&useSSL=false&useUnicode=true&characterEncoding=UTF-8");
        dataSource.setUsername("springstudent");
        dataSource.setPassword("springstudent");
        return dataSource;
    }

    // Cấu hình EntityManagerFactory để quản lý các entity và tích hợp với Hibernate
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan(new String[]{"com.entity"}); // Quét các entity trong gói "com.entity"
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setJpaProperties(additionalProperties()); // Thêm thuộc tính Hibernate
        return em;
    }

    // Cấu hình TransactionManager để quản lý giao dịch trong JPA
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }

    // Xử lý dịch các ngoại lệ JPA thành ngoại lệ Spring
    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    // Cấu hình các thuộc tính bổ sung cho Hibernate
    Properties additionalProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "validate"); // Kiểm tra cấu trúc bảng
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect"); // Sử dụng MySQL dialect
        properties.setProperty("hibernate.show_sql", "true"); // Hiển thị câu lệnh SQL
        properties.setProperty("hibernate.format_sql", "true"); // Định dạng câu lệnh SQL
        return properties;
    }
    
}
