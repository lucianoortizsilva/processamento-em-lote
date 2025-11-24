package com.lucianoortizsilva.lote.jobs.aviacao.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ArquivoPartitioner implements Partitioner {

	@Value("${jobaviacao.step02MigracaoCatalogoAviacao.totalRegistros}") private Integer totalRegistros;
	@Value("${jobaviacao.step02MigracaoCatalogoAviacao.gridSize}") private Integer gridSize;

	@Override
	public Map<String, ExecutionContext> partition(int gridSize) {
		final Map<String, ExecutionContext> partitionMap = new HashMap<>();
		for (int i = 0; i < gridSize; i++) {
			final ExecutionContext ctx = new ExecutionContext();
			ctx.putInt("particao", i);
			partitionMap.put("partition" + i, ctx);
		}
		return partitionMap;

	}

	public int calcularPrimeiroItemLeitura(int particao) {
		int indexPrimeiroItem = (particao * (totalRegistros / gridSize));
		return indexPrimeiroItem;
	}

	public int getItensLimit() {
		return totalRegistros / gridSize;
	}

}