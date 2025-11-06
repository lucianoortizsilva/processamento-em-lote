package com.lucianoortizsilva.lote.jobs.aviacao;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AviacaoJobConfig {

	@Autowired private JobRepository jobRepository;
	@Autowired private Step step01DeleteAviacao;
	@Autowired private Step step02MigracaoCatalogoAviacaoManager;

	@Bean
	Job aviacaoJob() {
		return new JobBuilder("aviacaoJob", jobRepository)//
				.start(step01DeleteAviacao)//
				.next(step02MigracaoCatalogoAviacaoManager)//
				.build();//
	}
}