package com.chawuzhi.admin.conf;


import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import com.alibaba.druid.pool.DruidDataSource;

@Configuration
public class DruidDataSourceConfig implements EnvironmentAware{
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private RelaxedPropertyResolver propertyResolver;
    @Override
    public void setEnvironment(Environment environment) {
        propertyResolver = new RelaxedPropertyResolver(environment, "spring.datasource.");
    }

    private boolean getBoolean(String source) {
        if (source != null) {
            return source.equalsIgnoreCase("true");
        }
        return false;
    }



    @Bean
    @Primary
    public DataSource dataSource() {
        DruidDataSource datasource = new DruidDataSource();
    	datasource.setUrl(propertyResolver.getProperty("url"));
        datasource.setDriverClassName(propertyResolver.getProperty("driver-class-name"));
        datasource.setUsername(propertyResolver.getProperty("username"));
        datasource.setPassword(propertyResolver.getProperty("password"));
        datasource.setInitialSize(Integer.valueOf(propertyResolver.getProperty("initialSize")));
        datasource.setMinIdle(Integer.valueOf(propertyResolver.getProperty("minIdle")));
        datasource.setMaxWait(Long.valueOf(propertyResolver.getProperty("maxWait")));
        datasource.setMaxActive(Integer.valueOf(propertyResolver.getProperty("maxActive")));
        datasource.setTimeBetweenEvictionRunsMillis(Long.valueOf(propertyResolver.getProperty("timeBetweenEvictionRunsMillis")));
        datasource.setMinEvictableIdleTimeMillis(Long.valueOf(propertyResolver.getProperty("minEvictableIdleTimeMillis")));

        datasource.setValidationQuery(propertyResolver.getProperty("validationQuery"));
        datasource.setTestWhileIdle(getBoolean(propertyResolver.getProperty("testWhileIdle")));
        datasource.setTestOnBorrow(getBoolean(propertyResolver.getProperty("testOnBorrow")));
        datasource.setTestOnReturn(getBoolean(propertyResolver.getProperty("testOnReturn")));
        datasource.setPoolPreparedStatements(getBoolean(propertyResolver.getProperty("poolPreparedStatements")));
        datasource.setMaxPoolPreparedStatementPerConnectionSize(Integer.valueOf(propertyResolver.getProperty("maxPoolPreparedStatementPerConnectionSize")));

        try {
//            datasource.setFilters("stat,wall");
            datasource.setFilters(propertyResolver.getProperty("filters"));
        } catch (SQLException e) {
            logger.error("set druid filters error:", e);
        }
        return datasource;
    }


}

