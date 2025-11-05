package com.lucianoortizsilva.lote.jobs.livraria.chunks.writers;

import javax.sql.DataSource;

import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lucianoortizsilva.lote.jobs.livraria.dto.LivroDTO;

@Configuration
public class LivroDataLakeWriterConfig {

	@Bean
	JdbcBatchItemWriter<LivroDTO> livroDataLakeWriter(@Qualifier("datalakeDataSource") final DataSource dataSource) {
		final var sql = """
					INSERT INTO livro(title, price, userId, profileName, reviewHelpfulness, reviewScore, reviewTime, reviewSummary, reviewText)
					          VALUES (:title, :price, :userId, :profileName, :reviewHelpfulness, :reviewScore, :reviewTime, :reviewSummary, :reviewText)
				""";
		return new JdbcBatchItemWriterBuilder<LivroDTO>()//
				.dataSource(dataSource)//
				.sql(sql)//
				.beanMapped()//
				.build();//
	}
}