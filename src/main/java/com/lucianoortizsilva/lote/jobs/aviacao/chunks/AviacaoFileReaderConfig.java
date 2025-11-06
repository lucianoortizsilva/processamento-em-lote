package com.lucianoortizsilva.lote.jobs.aviacao.chunks;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import com.lucianoortizsilva.lote.jobs.aviacao.dto.AviacaoDTO;

@Configuration
public class AviacaoFileReaderConfig {

	@Bean
	@StepScope
	FlatFileItemReader<AviacaoDTO> aviacaoFileReader(@Value("#{stepExecutionContext['startLine']}") final Integer startLine, @Value("#{stepExecutionContext['endLine']}") final Integer endLine) {
		final int linesToRead = endLine - startLine + 1;
		final FlatFileItemReader<AviacaoDTO> reader = new FlatFileItemReader<>() {

			private int linesRead = 0;

			@Override
			protected AviacaoDTO doRead() throws Exception {
				if (linesRead >= linesToRead) {
					return null;
				}
				final AviacaoDTO pessoa = super.doRead();
				if (pessoa != null) {
					linesRead++;
				}
				return pessoa;
			}
		};
		reader.setResource(new FileSystemResource("arquivos/itinerarios.csv"));
		final DefaultLineMapper<AviacaoDTO> mapper = new DefaultLineMapper<>();
		final DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setNames("id", "flightDate", "startingAirport", "destinationAirport", "travelDuration", "isBasicEconomy", "segmentsAirlineName", "segmentsEquipmentDescription");
		mapper.setLineTokenizer(tokenizer);
		mapper.setFieldSetMapper(fieldSet -> new AviacaoDTO(//
				fieldSet.readString("id"), //
				fieldSet.readString("flightDate"), //
				fieldSet.readString("startingAirport"), //
				fieldSet.readString("destinationAirport"), //
				fieldSet.readString("travelDuration"), //
				Boolean.parseBoolean(fieldSet.readString("isBasicEconomy")), //
				fieldSet.readString("segmentsAirlineName"), //
				fieldSet.readString("segmentsEquipmentDescription")));//
		reader.setLineMapper(mapper);
		reader.setLinesToSkip(startLine - 1);
		reader.setSaveState(false);//para caso ocorrer erro no meio do processamento, nao reinicie com dados inconsistentes. (Multi Thread).
		return reader;
	}
}