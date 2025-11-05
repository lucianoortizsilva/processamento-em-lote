package com.lucianoortizsilva.lote.jobs.livraria.steps;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.lucianoortizsilva.lote.jobs.livraria.tasklets.DeleteLivroTasklet;

@Configuration
public class Step01RemocaoLivroConfig {

	@Autowired private JobRepository jobRepository;
	@Autowired private PlatformTransactionManager transactionManager;
	@Autowired private DeleteLivroTasklet tasklet;

	@Bean
	Step step01RemocaoLivro() {
		return new StepBuilder("step01RemocaoLivro", jobRepository)//
				.tasklet(tasklet, transactionManager)//
				.build();//
	}
}