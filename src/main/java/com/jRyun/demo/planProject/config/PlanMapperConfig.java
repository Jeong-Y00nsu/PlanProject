package com.jRyun.demo.planProject.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.jRyun.demo.planProject.plan.mapper", sqlSessionFactoryRef = "planSqlSessionFactory")
public class PlanMapperConfig {

    @Primary
    @Bean(name = "planDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.plan")
    public DataSource planDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "planSqlSessionFactory")
    public SqlSessionFactory planSqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(planDataSource());
        sessionFactory.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources("classpath:/mapper/plan/*.xml"));
        return sessionFactory.getObject();
    }

    @Primary
    @Bean(name = "planSqlSessionTemplate")
    public SqlSessionTemplate planSqlSessionTemplate() throws Exception {
        return new SqlSessionTemplate(planSqlSessionFactory());
    }
}
