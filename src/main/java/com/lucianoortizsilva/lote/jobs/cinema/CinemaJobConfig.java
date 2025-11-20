package com.lucianoortizsilva.lote.jobs.cinema;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CinemaJobConfig {

	@Autowired private JobRepository jobRepository;
	@Autowired private Step step01EntradaFilme;

	@Bean
	Job cinemaJob() {
		return new JobBuilder("cinemaJob", jobRepository)//
				.start(step01EntradaFilme)//
				.build();//
	}
}
