package com.lucianoortizsilva.lote.jobs.livraria.chunks.readers;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import com.lucianoortizsilva.lote.jobs.livraria.dto.LivroDTO;

@Configuration
public class LivroLoteFeiraCuritibaReaderConfig {

	@Bean
	FlatFileItemReader<LivroDTO> livroLoteFeiraCuritibaReader() {
		return new FlatFileItemReaderBuilder<LivroDTO>()//
				.name("livroLoteFeiraCuritibaReader")//
				.resource(new FileSystemResource("arquivos/livros_lote_feira_curitiba.csv"))//
				.delimited().delimiter(",")//
				.names(getNames())//
				.fieldSetMapper(fieldSet -> create(fieldSet))//
				.build();//
	}

	private static String[] getNames() {
		return new String[] { //
				"id", //
				"title", //
				"price", //
				"userId", //
				"profileName", //
				"reviewHelpfulness", //
				"reviewScore", //
				"reviewTime", //
				"reviewSummary", //
				"reviewText" };
	}

	private static LivroDTO create(final FieldSet fieldSet) {
		final String title = fieldSet.readString("title");
		final String price = fieldSet.readString("price");
		final String userId = fieldSet.readString("userId");
		final String profileName = fieldSet.readString("profileName");
		final String reviewHelpfulness = fieldSet.readString("reviewHelpfulness");
		final String reviewScore = fieldSet.readString("reviewScore");
		final String reviewTime = fieldSet.readString("reviewTime");
		final String reviewSummary = fieldSet.readString("reviewSummary");
		final String reviewText = fieldSet.readString("reviewText");
		return new LivroDTO(title, price, userId, profileName, reviewHelpfulness, reviewScore, reviewTime, reviewSummary, reviewText);
	}
}