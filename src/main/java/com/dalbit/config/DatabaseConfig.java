package com.dalbit.config;

import com.dalbit.mybatis.RefreshableSqlSessionFactoryBean;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.sf.log4jdbc.Log4jdbcProxyDataSource;
import net.sf.log4jdbc.tools.Log4JdbcCustomFormatter;
import net.sf.log4jdbc.tools.LoggingType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * DATABASE 환경설정
 */
@Configuration
@MapperScan(basePackages= "com.dalbit")
public class DatabaseConfig {

    @Value("${spring.datasource.driverClassName}")
    private String JDBC_DRIVER_CLASS_NAME;

    @Value("${spring.datasource.url}")
    private String JDBC_URL;

    @Value("${spring.datasource.username}")
    private String JDBC_USERNAME;

    @Value("${spring.datasource.password}")
    private String JDBC_PASSWORD;

    @Value("${spring.datasource.connection.timeout}")
    private String CONNECTION_TIMEOUT;

    @Value("${spring.datasource.idle.timeout}")
    private String IDLE_TIMEOUT;

    @Bean
    public HikariConfig hikariConfig() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(JDBC_DRIVER_CLASS_NAME);
        hikariConfig.setJdbcUrl(JDBC_URL);
        hikariConfig.setUsername(JDBC_USERNAME);
        hikariConfig.setPassword(JDBC_PASSWORD);
        hikariConfig.setConnectionTimeout(Long.valueOf(CONNECTION_TIMEOUT));
        hikariConfig.setIdleTimeout(Long.valueOf(IDLE_TIMEOUT));
        hikariConfig.setMaxLifetime(Long.valueOf(IDLE_TIMEOUT));
        return hikariConfig;
    }

    @Bean
    public DataSource dataSource() {
        DataSource dataSource = new HikariDataSource(hikariConfig());
        Log4JdbcCustomFormatter formatter = new Log4JdbcCustomFormatter();
        formatter.setLoggingType(LoggingType.SINGLE_LINE);
        formatter.setSqlPrefix("[RUNNING SQL] => ");

        Log4jdbcProxyDataSource log4jdbcDs = new Log4jdbcProxyDataSource(dataSource);
        log4jdbcDs.setLogFormatter(formatter);
        return log4jdbcDs;

    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {

        RefreshableSqlSessionFactoryBean refreshableSqlSessionFactoryBean = new RefreshableSqlSessionFactoryBean();
        refreshableSqlSessionFactoryBean.setDataSource(dataSource);
        refreshableSqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:com/dalbit/**/mapper/**/*.xml"));
        refreshableSqlSessionFactoryBean.setConfigLocation(new DefaultResourceLoader().getResource("classpath:mybatis/mybatis-config.xml"));
        refreshableSqlSessionFactoryBean.afterPropertiesSet();

        return refreshableSqlSessionFactoryBean.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) throws Exception {
        final SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory);
        return sqlSessionTemplate;
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }
}