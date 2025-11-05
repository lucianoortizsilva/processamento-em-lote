package com.lucianoortizsilva.lote.jobs.netflix.chunks.writers;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.batch.item.support.builder.ClassifierCompositeItemWriterBuilder;
import org.springframework.classify.Classifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import com.lucianoortizsilva.lote.jobs.netflix.vo.NetflixCatalogoVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class CompositeNetflixCatalogoJdbcWriterConfig {

	@Bean
	ClassifierCompositeItemWriter<NetflixCatalogoVO> compositeNetflixCatalogoJdbcWriter(final JdbcBatchItemWriter<NetflixCatalogoVO> netflixCatalogDocumentaryJdbcWriter, final JdbcBatchItemWriter<NetflixCatalogoVO> netflixCatalogComedieJdbcWriter) {
		return new ClassifierCompositeItemWriterBuilder<NetflixCatalogoVO>()//
				.classifier(writer(netflixCatalogDocumentaryJdbcWriter, netflixCatalogComedieJdbcWriter))//
				.build();//
	}

	private static Classifier<NetflixCatalogoVO, ItemWriter<? super NetflixCatalogoVO>> writer(final JdbcBatchItemWriter<NetflixCatalogoVO> netflixCatalogDocumentaryJdbcWriter, final JdbcBatchItemWriter<NetflixCatalogoVO> netflixCatalogComedieJdbcWriter) {
		return new Classifier<>() {

			private static final long serialVersionUID = 1L;

			@Override
			public ItemWriter<? super NetflixCatalogoVO> classify(final NetflixCatalogoVO vo) {
				final String[] listedIn = vo.listedIn().split(",");
				if (existsAllData(vo)) {
					if (isDocumentary(listedIn)) {
						return netflixCatalogDocumentaryJdbcWriter;
					} else if (isComedie(listedIn)) {
						return netflixCatalogComedieJdbcWriter;
					}
				}
				return dummyWriter;
			}

			private boolean existsAllData(final NetflixCatalogoVO vo) {
				return isNotEmpty(vo.id())//
						&& isNotEmpty(vo.cast())//
						&& isNotEmpty(vo.country())//
						&& isNotEmpty(vo.duration())//
						&& isNotEmpty(vo.listedIn())//
						&& isNotEmpty(vo.title())//
						&& isNotEmpty(vo.releaseYear());//
			}

			@SuppressWarnings("unchecked")
			private boolean isDocumentary(final String[] listedIn) {
				if (nonNull(listedIn)) {
					final List<String> categories = (List<String>) CollectionUtils.arrayToList(listedIn);
					return categories.stream().anyMatch(category -> category.equalsIgnoreCase("Documentaries"));
				}
				return false;
			}

			@SuppressWarnings("unchecked")
			private boolean isComedie(final String[] listedIn) {
				if (nonNull(listedIn)) {
					final List<String> categories = (List<String>) CollectionUtils.arrayToList(listedIn);
					return categories.stream().anyMatch(category -> category.equalsIgnoreCase("Comedies"));
				}
				return false;
			}

			ItemWriter<NetflixCatalogoVO> dummyWriter = items -> {
				//log.info(items.toString());
			};
		};
	}
}