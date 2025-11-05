package com.lucianoortizsilva.lote.configs;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class JdbcTemplateConfig {
	
	@Bean(name = "jdbcTemplateSpringDataSource")
	JdbcTemplate primaryJdbcTemplate(@Qualifier("springDataSource") final DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}
	
	@Bean(name = "jdbcTemplateDatalakeSource")
	JdbcTemplate datalakeDataSource(@Qualifier("datalakeDataSource") final DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}
	
	@Bean(name = "jdbcTemplateDatawarehouseSource")
	JdbcTemplate secondaryJdbcTemplate(@Qualifier("datawarehouseDataSource") final DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}
}