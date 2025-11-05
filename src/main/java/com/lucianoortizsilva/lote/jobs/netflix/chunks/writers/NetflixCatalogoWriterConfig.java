package com.lucianoortizsilva.lote.jobs.netflix.chunks.writers;

import javax.sql.DataSource;

import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lucianoortizsilva.lote.jobs.netflix.vo.NetflixCatalogoVO;

@Configuration
public class NetflixCatalogoWriterConfig {

	@Bean
	JdbcBatchItemWriter<NetflixCatalogoVO> netflixCatalogWriter(@Qualifier("datalakeDataSource") final DataSource dataSource) {
		final var sql = """
					INSERT INTO netflix_catalogo (id, title, "cast", country, releaseYear, duration, listedIn)
					                     VALUES (:id, :title, :cast, :country, :releaseYear, :duration, :listedIn)
				""";
		return new JdbcBatchItemWriterBuilder<NetflixCatalogoVO>()//
				.dataSource(dataSource)//
				.sql(sql)//
				.beanMapped()//
				.build();//
	}
}