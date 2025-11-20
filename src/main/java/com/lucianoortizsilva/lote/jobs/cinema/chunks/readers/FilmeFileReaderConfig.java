package com.lucianoortizsilva.lote.jobs.cinema.chunks.readers;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import com.lucianoortizsilva.lote.jobs.cinema.vo.FilmeVO;

@Configuration
public class FilmeFileReaderConfig {
	
	@Bean
	FlatFileItemReader<FilmeVO> peopleFileReader() {
		return new FlatFileItemReaderBuilder<FilmeVO>()//
				.name("peopleFileReader")//
				.resource(new FileSystemResource("files/people.csv"))//
				.delimited()//
				.names("id")//
				.addComment("--")//
				.targetType(FilmeVO.class)//
				.build();//
	}
}