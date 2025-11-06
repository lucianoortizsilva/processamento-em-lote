package com.lucianoortizsilva.lote.jobs.aviacao.steps;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.lucianoortizsilva.lote.jobs.aviacao.tasklets.DeleteAviacaoTasklet;

@Configuration
public class Step01DeleteAviacaoConfig {

	@Autowired private JobRepository jobRepository;
	@Autowired private PlatformTransactionManager transactionManager;
	@Autowired private DeleteAviacaoTasklet tasklet;

	@Bean
	Step step01DeleteAviacao() {
		return new StepBuilder("step01DeleteAviacao", jobRepository)//
				.tasklet(tasklet, transactionManager)//
				.build();//
	}
}