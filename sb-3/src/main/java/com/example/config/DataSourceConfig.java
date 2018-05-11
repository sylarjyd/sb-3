package com.example.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
 
import javax.sql.DataSource;
 
 
@Configuration
@PropertySource(value="classpath:application.properties")
public class DataSourceConfig {
	
	@Bean(name = "dataSource")
    @Qualifier("dataSource")
    @ConfigurationProperties(prefix="spring.datasource")
    public DataSource dataSource() {
         return DataSourceBuilder.create().build();  
    }
	
	@Bean(name = "jdbcTemplate")
    @Qualifier("jdbcTemplate")
    public JdbcTemplate jdbcTemplate(@Qualifier("dataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
 
}