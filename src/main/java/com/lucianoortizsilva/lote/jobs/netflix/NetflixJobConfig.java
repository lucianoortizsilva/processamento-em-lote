package com.lucianoortizsilva.lote.jobs.netflix;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * * <strong>Esse JOB executa um fluxo completo ETL no catálogo da Netflix</strong>
 * <br><br>
 * Resumo do processamento:
 * <ul>
 * <li> Step01 - Deleta os dados das tabelas de catálogo (staging/lake/warehouse) </li><br>
 * <li> Step02 - Carrega os registros do arquivo <b>arquivos/netflix.csv</b> para a tabela staging <b>netflix_catalogo</b> (data lake)</li><br>
 * <li> Step03 - Lê os registros da tabela staging, classifica por categoria e grava nas tabelas finais <b>netflix_catalogo_documentario</b> e <b>netflix_catalogo_comedia</b> (data warehouse) </li>
 * <ul>
 * <li> Regras de classificação: * <ul>
 * <li>Registros contendo "Documentaries" vão para <b>netflix_catalogo_documentario</b></li>
 * <li>Registros contendo "Comedies" vão para <b>netflix_catalogo_comedia</b></li>
 * <li>Registros que não atendem as regras ou possuem informações incompletas são descartados</li>
 * </ul>
 * </li>
 * </ul>
 * <strong>Chunk (step02):</strong>2 registros <br>
 * <strong>Chunk (step03):</strong>20 registros <br>
 * <strong>Processamento paralelizado no step02 (ThreadPoolTaskExecutor com4 threads)</strong><br>
 * </ul>
 *
 */
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