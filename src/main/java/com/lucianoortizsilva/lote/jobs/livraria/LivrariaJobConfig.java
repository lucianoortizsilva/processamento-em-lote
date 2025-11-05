package com.lucianoortizsilva.lote.jobs.livraria;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@Configuration
public class LivrariaJobConfig {

	@Autowired private JobRepository jobRepository;
	@Autowired private Step step01RemocaoLivro;
	@Autowired private Step step02MigracaoLoteFeiraSaoPaulo;
	@Autowired private Step step03MigracaoLoteFeiraPortoAlegre;
	@Autowired private Step step04MigracaoLoteFeiraCuritiba;

	@Bean
	Job livrariaJob() {
		return new JobBuilder("livrariaJob", jobRepository)//
				.start(flows(step01RemocaoLivro, step02MigracaoLoteFeiraSaoPaulo, step03MigracaoLoteFeiraPortoAlegre, step04MigracaoLoteFeiraCuritiba))//
				.end().build();////
	}

	@SuppressWarnings("resource")
	private static Flow flows(Step step01RemocaoLivro1, Step step02MigracaoLoteFeiraSaoPaulo2, Step step03MigracaoLoteFeiraPortoAlegre3, Step step04MigracaoLoteFeiraCuritiba4) {
		return new FlowBuilder<Flow>("stepsParalelos")//
				.start(step01RemocaoLivro1)//STEP: SINCRONO
				.split(new SimpleAsyncTaskExecutor("THREAD_"))//
				.add(flowStep02MigracaoLoteFeiraSaoPaulo2(step02MigracaoLoteFeiraSaoPaulo2), // 
						flowStep03MigracaoLoteFeiraPortoAlegre3(step03MigracaoLoteFeiraPortoAlegre3), // 
						flowStep04MigracaoLoteFeiraCuritiba4(step04MigracaoLoteFeiraCuritiba4))//STEPS ASSINCRONOS
				.build();//
	}

	private static Flow flowStep02MigracaoLoteFeiraSaoPaulo2(Step step02MigracaoLoteFeiraSaoPaulo2) {
		return new FlowBuilder<Flow>("flowStep02MigracaoLoteFeiraSaoPaulo2")//
				.start(step02MigracaoLoteFeiraSaoPaulo2)//
				.build();//
	}

	private static Flow flowStep03MigracaoLoteFeiraPortoAlegre3(Step step03MigracaoLoteFeiraPortoAlegre3) {
		return new FlowBuilder<Flow>("flowStep03MigracaoLoteFeiraPortoAlegre3")//
				.start(step03MigracaoLoteFeiraPortoAlegre3)//
				.build();//
	}

	private static Flow flowStep04MigracaoLoteFeiraCuritiba4(Step step04MigracaoLoteFeiraCuritiba4) {
		return new FlowBuilder<Flow>("flowStep04MigracaoLoteFeiraCuritiba4")//
				.start(step04MigracaoLoteFeiraCuritiba4)//
				.build();//
	}
}