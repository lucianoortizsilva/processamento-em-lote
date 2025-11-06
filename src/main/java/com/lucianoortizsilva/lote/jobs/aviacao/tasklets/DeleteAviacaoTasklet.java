package com.lucianoortizsilva.lote.jobs.aviacao.tasklets;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class DeleteAviacaoTasklet implements Tasklet {

	@Autowired private JdbcTemplate jdbcTemplateDatalakeSource;

	@Override
	public RepeatStatus execute(final StepContribution contribution, final ChunkContext chunkContext) throws Exception {
		jdbcTemplateDatalakeSource.execute("DELETE FROM aviacao");
		return RepeatStatus.FINISHED;
	}
}