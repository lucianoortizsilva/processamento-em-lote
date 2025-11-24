package com.lucianoortizsilva.lote.jobs.aviacao.chunks;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import com.lucianoortizsilva.lote.jobs.aviacao.config.ArquivoPartitioner;
import com.lucianoortizsilva.lote.jobs.aviacao.dto.AviacaoDTO;

@Configuration
public class AviacaoArquivoReaderConfig {

	@Autowired private ArquivoPartitioner arquivoPartitioner;

	@Bean
	@StepScope
	CustomArquivoReader<AviacaoDTO> aviacaoArquivoReader(@Value("#{stepExecutionContext['particao']}") final Integer particao) {
		final int linhaPrimeiroItem = arquivoPartitioner.calcularPrimeiroItemLeitura(particao);
		return new CustomArquivoReader<>(aviacaoArquivoReader(linhaPrimeiroItem), arquivoPartitioner.getItensLimit());
	}

	FlatFileItemReader<AviacaoDTO> aviacaoArquivoReader(final int linhaPrimeiroItem) {
		return new FlatFileItemReaderBuilder<AviacaoDTO>()//
				.name("aviacaoFileReader")//
				.resource(new FileSystemResource("arquivos/itinerarios.csv"))//
				.delimited()//
				.names("id", "flightDate", "startingAirport", "destinationAirport", "travelDuration", "isBasicEconomy", "segmentsAirlineName", "segmentsEquipmentDescription")//
				.addComment("--")//
				.currentItemCount(linhaPrimeiroItem)//
				.targetType(AviacaoDTO.class)//
				.build();//
	}
}
