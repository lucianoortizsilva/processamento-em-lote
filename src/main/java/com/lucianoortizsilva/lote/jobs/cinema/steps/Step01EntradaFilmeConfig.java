package com.lucianoortizsilva.lote.jobs.cinema.steps;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.lucianoortizsilva.lote.jobs.cinema.vo.FilmeVO;

@Configuration
public class Step01EntradaFilmeConfig {

	@Autowired private JobRepository jobRepository;
	@Autowired private PlatformTransactionManager transactionManager;

	@Bean
	Step step01EntradaFilme(final ItemReader<FilmeVO> peopleFileReader, final ItemProcessor<FilmeVO, FilmeVO> filmeProcessorConfig, final ItemWriter<FilmeVO> peopleWriter) {
		return new StepBuilder("step01EntradaFilme", jobRepository)//
				.<FilmeVO, FilmeVO> chunk(1, transactionManager)//
				.reader(peopleFileReader)//
				.processor(filmeProcessorConfig)//
				.writer(peopleWriter)//
				.build();//
	}
}