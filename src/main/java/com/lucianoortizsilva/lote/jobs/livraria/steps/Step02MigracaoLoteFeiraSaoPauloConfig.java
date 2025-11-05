package com.lucianoortizsilva.lote.jobs.livraria.steps;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.lucianoortizsilva.lote.jobs.livraria.dto.LivroDTO;

@Configuration
public class Step02MigracaoLoteFeiraSaoPauloConfig {

	@Autowired private JobRepository jobRepository;
	@Autowired private PlatformTransactionManager transactionManager;

	@Bean
	Step step02MigracaoLoteFeiraSaoPaulo(final ItemReader<LivroDTO> livroLoteFeiraSaoPauloFileReader, final ItemWriter<LivroDTO> livroDataLakeWriter) {
		return new StepBuilder("step02MigracaoLoteFeiraSaoPaulo", jobRepository)//
				.<LivroDTO, LivroDTO> chunk(50, transactionManager)//
				.reader(livroLoteFeiraSaoPauloFileReader)//
				.writer(livroDataLakeWriter)//
				.build();//
	}
}