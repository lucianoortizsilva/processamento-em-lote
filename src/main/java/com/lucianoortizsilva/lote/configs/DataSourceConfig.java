package com.lucianoortizsilva.lote.configs;

import javax.sql.DataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DataSourceConfig {
	
	@Primary
	@Bean
	@ConfigurationProperties(prefix = "spring.datasource")
	DataSource springDataSource() {
		return DataSourceBuilder.create().build();
	}
	
	@Bean
	@ConfigurationProperties(prefix = "datalake.datasource")
	DataSource datalakeDataSource() {
		return DataSourceBuilder.create().build();
	}
	
	@Bean
	@ConfigurationProperties(prefix = "datawarehouse.datasource")
	DataSource datawarehouseDataSource() {
		return DataSourceBuilder.create().build();
	}
}