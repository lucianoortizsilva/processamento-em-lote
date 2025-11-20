package com.lucianoortizsilva.lote.jobs;

import static java.util.Objects.isNull;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.MDC;
import org.springframework.batch.core.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.lucianoortizsilva.lote.rabbitmq.Payload;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JobFactory {

	@Autowired private ApplicationContext applicationContext;
	private final Map<String, Job> strategies = new HashMap<>();

	@PostConstruct
	public void init() {
		strategies.put("livraria", applicationContext.getBean("livrariaJob", Job.class));
		strategies.put("netflix", applicationContext.getBean("netflixJob", Job.class));
		strategies.put("aviacao", applicationContext.getBean("aviacaoJob", Job.class));
		strategies.put("cinema", applicationContext.getBean("cinemaJob", Job.class));
	}

	public Job getJob(final Payload payload) {
		final Job bo = strategies.get(payload.getName());
		if (isNull(bo)) {
			final StringBuilder message = new StringBuilder();
			message.append("\n#########################################");
			message.append("\nJOB NÃO ENCONTRADO!.");
			message.append("\n#########################################");
			throw new RuntimeException("JOB NÃO ENCONTRADO - ".concat(message.toString()));
		}
		MDC.put("name", payload.getName());
		return bo;
	}
}