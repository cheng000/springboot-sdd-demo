package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * MyBatis / 事务相关配置
 * <p>
 * 实际数据源、SqlSessionFactory 由 {@code mybatis-spring-boot-starter} 自动装配，
 * 此处仅显式开启注解事务（{@code @Transactional}）便于在 Service 实现中使用。
 */
@Configuration
@EnableTransactionManagement
public class MybatisConfig {
}
