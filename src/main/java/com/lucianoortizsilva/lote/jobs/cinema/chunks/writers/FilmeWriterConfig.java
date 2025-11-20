package com.lucianoortizsilva.lote.jobs.cinema.chunks.writers;

import javax.sql.DataSource;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lucianoortizsilva.lote.jobs.cinema.vo.FilmeVO;

@Configuration
public class FilmeWriterConfig {

	@Bean
	JdbcBatchItemWriter<FilmeVO> peopleWriter(@Qualifier("datalakeDataSource") final DataSource dataSource) {
		final var sql = "INSERT INTO input_filme (id, nome, ano, genero) VALUES (:id, :nome, :ano, :genero)";
		return new JdbcBatchItemWriterBuilder<FilmeVO>()//
				.dataSource(dataSource)//
				.sql(sql)//
				.beanMapped()//
				.build();//
	}
}