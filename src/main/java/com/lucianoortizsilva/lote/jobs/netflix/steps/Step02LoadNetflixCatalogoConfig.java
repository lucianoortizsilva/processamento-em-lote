package com.lucianoortizsilva.lote.jobs.netflix.steps;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import com.lucianoortizsilva.lote.jobs.netflix.vo.NetflixCatalogoVO;

@Configuration
public class Step02LoadNetflixCatalogoConfig {

	@Autowired private JobRepository jobRepository;
	@Autowired private PlatformTransactionManager transactionManager;

	@Bean
	Step step02LoadNetflixCatalogo(final ItemReader<NetflixCatalogoVO> netflixCatalogoFileReader, final ItemWriter<NetflixCatalogoVO> netflixCatalogWriter) {
		return new StepBuilder("step02LoadNetflixCatalogo", jobRepository)//
				.<NetflixCatalogoVO, NetflixCatalogoVO> chunk(2, transactionManager)//
				.reader(netflixCatalogoFileReader)//
				.writer(netflixCatalogWriter)//
				.taskExecutor(poolTaskExecutor())//
				.build();//
	}

	@Bean
	TaskExecutor poolTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setMaxPoolSize(4);
		executor.setCorePoolSize(4);
		executor.setQueueCapacity(4);
		executor.setThreadNamePrefix("thread_");
		return executor;
	}
}