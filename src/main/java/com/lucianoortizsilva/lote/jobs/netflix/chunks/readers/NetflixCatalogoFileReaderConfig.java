package com.lucianoortizsilva.lote.jobs.netflix.chunks.readers;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import com.lucianoortizsilva.lote.jobs.netflix.vo.NetflixCatalogoVO;

@Configuration
public class NetflixCatalogoFileReaderConfig {

	@Bean
	FlatFileItemReader<NetflixCatalogoVO> netflixCatalogoFileReader() {
		return new FlatFileItemReaderBuilder<NetflixCatalogoVO>()//
				.name("netflixCatalogoFileReader")//
				.resource(new FileSystemResource("arquivos/netflix.csv"))//
				.delimited().delimiter(";")//
				.names(getNames())//
				.saveState(false)//
				.fieldSetMapper(fieldSet -> create(fieldSet))//
				.build();//
	}

	private static String[] getNames() {
		return new String[] { //
				"id", //
				"title", //
				"cast", //
				"country", //
				"releaseYear", //
				"duration", //
				"listedIn"//
		};
	}

	private static NetflixCatalogoVO create(final FieldSet fieldSet) {
		final String id = fieldSet.readString("id");
		final String title = fieldSet.readString("title");
		final String cast = fieldSet.readString("cast");
		final String country = fieldSet.readString("country");
		final String releaseYear = fieldSet.readString("releaseYear");
		final String duration = fieldSet.readString("duration");
		final String listedIn = fieldSet.readString("listedIn");
		return new NetflixCatalogoVO(id, title, cast, country, releaseYear, duration, listedIn);
	}
}