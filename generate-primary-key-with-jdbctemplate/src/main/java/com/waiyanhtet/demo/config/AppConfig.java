package com.waiyanhtet.demo.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

@Configuration
@ComponentScan(basePackages = "com.waiyanhtet.demo.dao")
@PropertySource(value = "sql.properties")
public class AppConfig {

	@Bean
	public DataSource dataSource() {
		EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
		builder.setType(EmbeddedDatabaseType.HSQL);
		builder.addScript("classpath:/database.sql");
		return builder.build();
	}

	@Bean
	public JdbcOperations jdbcTemplate(DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

	@Bean
	public SimpleJdbcInsert simpleJdbcInsert(DataSource dataSource) {
		var simpleJdbcInsert = new SimpleJdbcInsert(dataSource);
		simpleJdbcInsert.setTableName("category");
		simpleJdbcInsert.setGeneratedKeyName("id");
		return simpleJdbcInsert;
	}

}
