package com.lucianoortizsilva.lote.jobs.netflix.steps;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.lucianoortizsilva.lote.jobs.netflix.tasklets.DeleteNetflixCatalogoTasklet;

@Configuration
public class Step01DeleteNetflixCatalogoConfig {

	@Autowired private JobRepository jobRepository;
	@Autowired private PlatformTransactionManager transactionManager;
	@Autowired private DeleteNetflixCatalogoTasklet tasklet;

	@Bean
	Step step01DeleteNetflixCatalogo() {
		return new StepBuilder("step01DeleteNetflixCatalogo", jobRepository)//
				.tasklet(tasklet, transactionManager)//
				.build();//
	}
}