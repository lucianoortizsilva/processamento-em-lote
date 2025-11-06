package com.lucianoortizsilva.lote.jobs.aviacao.steps;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import com.lucianoortizsilva.lote.jobs.aviacao.dto.AviacaoDTO;
import com.lucianoortizsilva.lote.jobs.aviacao.dto.AviacaoEconomicaDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class Step02MigracaoCatalogoAviacaoConfig {

	@Autowired private JobRepository jobRepository;
	@Autowired private PlatformTransactionManager transactionManager;
	private final static int CINQUENTA_MIL = 50000;

	@Bean
	Step step02MigracaoCatalogoAviacaoManager() throws Exception {
		return new StepBuilder("step02MigracaoCatalogoAviacaoManager", jobRepository)//
				.partitioner("step02MigracaoCatalogoAviacaoSlave", partitioner())//
				.partitionHandler(partitionHandlerX(null))//
				.build();//
	}

	@Bean
	Step step02MigracaoCatalogoAviacaoSlave(final FlatFileItemReader<AviacaoDTO> aviacaoFileReader, final ItemProcessor<AviacaoDTO, AviacaoEconomicaDTO> aviacaoProcessor, final ItemWriter<AviacaoEconomicaDTO> aviacaoWriter) {
		return new StepBuilder("step02MigracaoCatalogoAviacaoSlave", jobRepository)//
				.<AviacaoDTO, AviacaoEconomicaDTO> chunk(12500, transactionManager)//
				.reader(aviacaoFileReader)//
				.processor(aviacaoProcessor)//
				.writer(aviacaoWriter)//
				.stream(aviacaoFileReader)//
				.build();//
	}

	@Bean
	Partitioner partitioner() {
		return gridSize -> {
			final Map<String, ExecutionContext> partitionMap = new HashMap<>();
			final int range = CINQUENTA_MIL / 4;
			final int remainder = CINQUENTA_MIL % 4;
			int start = 1;
			for (int i = 0; i < 4; i++) {
				int end = start + range - 1;
				if (i < remainder) {
					end += 1;
				}
				final ExecutionContext ctx = new ExecutionContext();
				ctx.putInt("startLine", start);
				ctx.putInt("endLine", end);
				partitionMap.put("partition" + i, ctx);
				start = end + 1;
			}
			return partitionMap;
		};
	}

	@Bean
	TaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setMaxPoolSize(4);
		executor.setCorePoolSize(4);
		executor.setQueueCapacity(1);
		executor.setThreadNamePrefix("thread_");
		return executor;
	}

	@Bean
	PartitionHandler partitionHandlerX(final Step step02MigracaoCatalogoAviacaoSlave) throws Exception {
		final TaskExecutorPartitionHandler taskExecutorPartitionHandler = new TaskExecutorPartitionHandler();
		taskExecutorPartitionHandler.setTaskExecutor(taskExecutor());
		taskExecutorPartitionHandler.setStep(step02MigracaoCatalogoAviacaoSlave);
		return taskExecutorPartitionHandler;
	}
}