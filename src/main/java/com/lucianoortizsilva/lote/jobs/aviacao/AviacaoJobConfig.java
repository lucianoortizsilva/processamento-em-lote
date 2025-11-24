package com.lucianoortizsilva.lote.jobs.aviacao;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * 
 * <strong> Esse JOB possui particionamento local com 1 arquivo de entrada</strong>
 * <br><br>
 * Resumo do processamento:
 * <ul>
 * 	<li> Step 01 - Deleta os dados da tabela de aviação </li><br>	  
 * 	<li> Step 02 - Possui 1 MANAGER e 'N' SLAVES </li>
 * 	<p>Defino a quantidade de partições/workers/grid em <b>10</b> @see ArquivoPartitioner</p> 
 *  <p>Defino a quantidade de threads em <b>4</b> @see TaskExecutorConfig</p> 
 * 	<strong>Chunk:</strong> totalRegistros / gridSize <br><br>
 *  <strong>No Reader:</strong> Leio apenas o arquivo arquivos/itinerarios.csv<br>
 *  <strong>No Processor:</strong> Faço apenas uma validação simples dos dados<br>
 *  <strong>No Writer:</strong> Gravo os dados na tabela de aviação<br>
 * </p>
 * </li>
 * </ul>
 * 
 * */
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