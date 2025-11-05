package com.lucianoortizsilva.lote.jobs.netflix;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NetflixJobConfig {

	@Autowired private JobRepository jobRepository;
	@Autowired private Step step01DeleteNetflixCatalogo;
	@Autowired private Step step02LoadNetflixCatalogo;
	@Autowired private Step step03TransformaNetflixCatalogo;

	@Bean
	Job netflixJob() {
		return new JobBuilder("netflixJob", jobRepository)//
				.start(step01DeleteNetflixCatalogo)//
				.next(step02LoadNetflixCatalogo)//
				.next(step03TransformaNetflixCatalogo)//
				.build();//
	}
}