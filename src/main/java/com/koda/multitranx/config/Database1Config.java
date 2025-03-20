package com.koda.multitranx.config;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Lazy
@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = {"com.koda.multitranx"}, sqlSessionTemplateRef = "defSqlSessionTemplate")
public class Database1Config {
    @Value("${spring.datasource.hikari.database1.mapper-locations}")
    private String mapperLocations;

    @Bean(name = "defDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.hikari.database1")
    @Primary
    public DataSource defDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean(name = "defSqlSessionFactory")
    @Primary
    public SqlSessionFactory defSqlSessionFactory(@Qualifier("defDataSource") DataSource dataSource) throws Exception{

        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setTypeAliasesPackage("com.koda.multitranx");
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperLocations));
        sqlSessionFactoryBean.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);  // 문자열 camelCase 변경
        sqlSessionFactoryBean.getObject().getConfiguration().setCallSettersOnNulls(true);        // null 일경우 key 생성
        sqlSessionFactoryBean.getObject().getConfiguration().setReturnInstanceForEmptyRow(true);
        return sqlSessionFactoryBean.getObject();
    }

    @Bean(name = "defSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate defSqlSessionTemplate(@Qualifier("defSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean(name = "defTxManager")
    @Primary
    public PlatformTransactionManager defTxManager(@Qualifier("defDataSource") DataSource dataSource) {
        DataSourceTransactionManager txManager = new DataSourceTransactionManager();
        txManager.setDataSource(dataSource);

        return txManager;
    }
}
