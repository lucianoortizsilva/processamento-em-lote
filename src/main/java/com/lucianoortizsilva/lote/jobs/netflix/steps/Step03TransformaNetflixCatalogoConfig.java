package com.lucianoortizsilva.lote.jobs.netflix.steps;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.lucianoortizsilva.lote.jobs.netflix.vo.NetflixCatalogoVO;

@Configuration
public class Step03TransformaNetflixCatalogoConfig {

	@Autowired private JobRepository jobRepository;
	@Autowired private PlatformTransactionManager transactionManager;

	@Bean
	Step step03TransformaNetflixCatalogo(final ItemReader<NetflixCatalogoVO> netflixCatalogoJdbcReader, final ItemWriter<NetflixCatalogoVO> compositeNetflixCatalogoJdbcWriter) {
		return new StepBuilder("step03TransformaNetflixCatalogo", jobRepository)//
				.<NetflixCatalogoVO, NetflixCatalogoVO> chunk(20, transactionManager)//
				.reader(netflixCatalogoJdbcReader)//
				.writer(compositeNetflixCatalogoJdbcWriter)//
				.build();//
	}
}