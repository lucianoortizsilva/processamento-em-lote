package com.lucianoortizsilva.lote.jobs.aviacao.chunks;

import java.util.Objects;
import java.util.Optional;

import com.lucianoortizsilva.lote.jobs.aviacao.model.Aeroporto;

public class AviacaoBusiness {

	static String getNomeAeroporto(final String codigo) {
		final Optional<Aeroporto> airport = Aeroporto.get(codigo);
		if (airport.isPresent()) {
			return airport.get().getDescription();
		}
		throw new RuntimeException("Aeroporto não encontrado: ".concat(codigo));
	}

	static String getUniqueCompanyName(final String fullCompanyName) {
		if (Objects.isNull(fullCompanyName)) {
			return null;
		}
		if (fullCompanyName.contains("||")) {
			return fullCompanyName.split("\\|\\|")[0];
		}
		return fullCompanyName;
	}
}