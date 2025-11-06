package com.lucianoortizsilva.lote.jobs.aviacao.chunks;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lucianoortizsilva.lote.jobs.aviacao.dto.AviacaoDTO;
import com.lucianoortizsilva.lote.jobs.aviacao.dto.AviacaoEconomicaDTO;

@Configuration
public class AviacaoProcessorConfig {
	
	@Bean
	ItemProcessor<AviacaoDTO, AviacaoEconomicaDTO> aviacaoProcessor() {
		final ItemProcessor<AviacaoDTO, AviacaoEconomicaDTO> process = new ItemProcessor<>() {
			
			@Override
			public AviacaoEconomicaDTO process(final AviacaoDTO flight) throws Exception {
				if (flight.isBasicEconomy()) {
					final var id = flight.id();
					final var flightDate = flight.flightDate();
					final var startingAirport = AviacaoBusiness.getNomeAeroporto(flight.startingAirport());
					final var destinationAirport = AviacaoBusiness.getNomeAeroporto(flight.destinationAirport());
					final var segmentsAirlineName = AviacaoBusiness.getUniqueCompanyName(flight.segmentsAirlineName());
					return new AviacaoEconomicaDTO(id, flightDate, startingAirport, destinationAirport, segmentsAirlineName);
				}
				return null;
			}
		};
		return process;
	}
}