package com.lucianoortizsilva.lote.jobs.netflix.tasklets;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DeleteNetflixCatalogoTasklet implements Tasklet {

	@Autowired private JdbcTemplate jdbcTemplateDatalakeSource;
	@Autowired private JdbcTemplate jdbcTemplateDatawarehouseSource;

	@Override
	public RepeatStatus execute(final StepContribution contribution, final ChunkContext chunkContext) throws Exception {
		jdbcTemplateDatalakeSource.execute("DELETE FROM netflix_catalogo");
		jdbcTemplateDatawarehouseSource.execute("DELETE FROM netflix_catalogo_documentario");
		jdbcTemplateDatawarehouseSource.execute("DELETE FROM netflix_catalogo_comedia");
		return RepeatStatus.FINISHED;
	}
}
