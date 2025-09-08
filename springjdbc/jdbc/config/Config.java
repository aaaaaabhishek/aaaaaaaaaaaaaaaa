package com.springjdbc.jdbc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;

@Configuration
public class Config {

    @Bean
    public JdbcTemplate getTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource); // ✅ inject DataSource
    }
    @Bean
    public SimpleJdbcInsert getTemplate2(DataSource dataSource) {
        return new SimpleJdbcInsert(dataSource)
                .withTableName("employees")          // table
                .usingGeneratedKeyColumns("id");
    }
}
