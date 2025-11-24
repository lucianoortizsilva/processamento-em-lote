package com.lucianoortizsilva.lote.jobs.aviacao.steps;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import com.lucianoortizsilva.lote.jobs.aviacao.dto.AviacaoDTO;
import com.lucianoortizsilva.lote.jobs.aviacao.dto.AviacaoEconomicaDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class Step02MigracaoCatalogoAviacaoConfig {

	@Autowired private JobRepository jobRepository;
	@Autowired private PlatformTransactionManager transactionManager;
	@Value("${jobaviacao.step02MigracaoCatalogoAviacao.totalRegistros}") private Integer totalRegistros;
	@Value("${jobaviacao.step02MigracaoCatalogoAviacao.gridSize}") private Integer gridSize;

	@Bean
	Step step02MigracaoCatalogoAviacaoManager(//
			final ItemReader<AviacaoDTO> aviacaoArquivoReader, //
			final ItemProcessor<AviacaoDTO, AviacaoEconomicaDTO> aviacaoProcessor, // 
			final ItemWriter<AviacaoEconomicaDTO> aviacaoWriter, // 
			final Partitioner partitioner, //
			final TaskExecutor taskExecutorAviacao) {
		return new StepBuilder("step02MigracaoCatalogoAviacao.Manager", jobRepository)//
				.partitioner("step02MigracaoCatalogoAviacao.Slave", partitioner)//
				.step(step02MigracaoCatalogoAviacaoSlave(aviacaoArquivoReader, aviacaoProcessor, aviacaoWriter))//)//
				.gridSize(gridSize)//Quantidade de partições/workers
				.taskExecutor(taskExecutorAviacao)//
				.build();//
	}

	Step step02MigracaoCatalogoAviacaoSlave(final ItemReader<AviacaoDTO> aviacaoArquivoReader, final ItemProcessor<AviacaoDTO, AviacaoEconomicaDTO> aviacaoProcessor, final ItemWriter<AviacaoEconomicaDTO> aviacaoWriter) {
		return new StepBuilder("step02MigracaoCatalogoAviacaoSlave", jobRepository)//
				.<AviacaoDTO, AviacaoEconomicaDTO> chunk(totalRegistros / gridSize, transactionManager)//
				.reader(aviacaoArquivoReader)//
				.processor(aviacaoProcessor)//
				.writer(aviacaoWriter)//
				.build();//
	}

}