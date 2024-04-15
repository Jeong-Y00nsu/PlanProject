package com.jRyun.demo.planProject.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
@MapperScan(value = "com.jRyun.demo.planProject.user.mapper", sqlSessionFactoryRef = "userSqlSessionFactory")
public class UserMapperConfig {

    @Bean(name="userDatasource")
    @ConfigurationProperties(prefix = "spring.datasource.user")
    public DataSource dataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean(name="userSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("userDatasource")DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);
        sqlSessionFactory.setTypeAliasesPackage("com.jRyun.demo.planProject.user");
        sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*.xml"));
        return sqlSessionFactory.getObject();
    }

    @Bean(name="userSqlSession")
    public SqlSessionTemplate sqlSession(SqlSessionFactory sqlSessionFactory){
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
