package com.lucianoortizsilva.lote.jobs.aviacao.dto;

public record AviacaoDTO(String id, String flightDate, String startingAirport, String destinationAirport, String travelDuration, Boolean isBasicEconomy, String segmentsAirlineName, String segmentsEquipmentDescription) {}
