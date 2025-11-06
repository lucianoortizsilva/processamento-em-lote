package com.lucianoortizsilva.lote.jobs.aviacao.chunks;

import javax.sql.DataSource;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lucianoortizsilva.lote.jobs.aviacao.dto.AviacaoEconomicaDTO;

@Configuration
public class AviacaoWriterConfig {
	
	@Bean
	JdbcBatchItemWriter<AviacaoEconomicaDTO> aviacaoWriter(@Qualifier("datalakeDataSource") final DataSource dataSource) {
		final var sql = """
				INSERT INTO aviacao(id, flightDate, startingAirport, destinationAirport, segmentsAirlineName)
				     VALUES (:id, :flightDate, :startingAirport, :destinationAirport, :segmentsAirlineName)
					""";
		return new JdbcBatchItemWriterBuilder<AviacaoEconomicaDTO>()//
				.dataSource(dataSource)//
				.sql(sql)//
				.beanMapped()//
				.build();//
	}
}