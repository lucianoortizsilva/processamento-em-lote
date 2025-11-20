package com.lucianoortizsilva.lote.jobs.cinema.chunks.processors;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
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
			final ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
			final String body = response.getBody();
			log.info("Filme encontrado: {}", body);
			return new Gson().fromJson(body, FilmeVO.class);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}
}