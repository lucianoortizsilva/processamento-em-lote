package com.lucianoortizsilva.lote.jobs.cinema.chunks.processors;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.lucianoortizsilva.lote.jobs.cinema.vo.FilmeVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class FilmeProcessorConfig implements ItemProcessor<FilmeVO, FilmeVO> {

	private static final RestTemplate restTemplate = new RestTemplate();

	@Override
	public FilmeVO process(final FilmeVO filme) throws Exception {
		try {
			final String uri = String.format("https://my-json-server.typicode.com/lucianoortizsilva/processamento-em-lote/filmes/%d", filme.getId());
			restTemplate.getForEntity(uri, String.class);
		} catch (final RestClientResponseException e) {
			log.error(e.getMessage());
		}
		return filme;
	}
}