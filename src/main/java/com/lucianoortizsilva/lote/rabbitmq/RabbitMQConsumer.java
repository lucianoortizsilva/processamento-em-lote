package com.lucianoortizsilva.lote.rabbitmq;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.amqp.AmqpIOException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.lucianoortizsilva.lote.jobs.JobFactory;
import com.rabbitmq.client.Channel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RabbitMQConsumer {

	@Autowired private ObjectMapper objectMapper;
	@Autowired private JobFactory jobFactory;
	@Autowired private JobLauncher jobLauncher;

	@RabbitListener(queues = RabbitMQConfig.QUEUE_NAME, ackMode = "MANUAL")
	public void receiveMessage(final Message message, final Channel channel) {
		Payload payload = new Payload();
		try {
			payload = getPayload(message);
			final Job algoritmo = jobFactory.getJob(payload);
			log.info(">>>>>> START - processing job <<<<<<");
			confirmarRecebimentoMensagem(channel, message, payload);
			jobLauncher.run(algoritmo, createInitialJobParameterMap(payload));
		} catch (final Exception e) {
			imprimirErrosAoConsumirMensagem(e);
		} finally {
			log.info(">>>>>> END - job processed with success <<<<<<");
		}
	}

	private static JobParameters createInitialJobParameterMap(final Payload payload) {
		final JobParameters jobParameters = new JobParametersBuilder() //
				.addString("name", payload.getName()) //
				.addString("dhActual", LocalDateTime.now().toString()) //
				.toJobParameters();
		return jobParameters;
	}

	@Retryable(retryFor = { AmqpIOException.class }, maxAttempts = 1000, backoff = @Backoff(delay = 5000))
	public static void confirmarRecebimentoMensagem(final Channel channel, final Message message, final Payload payload) {
		final boolean deletarTodasMensagens = false;
		final long identificador = message.getMessageProperties().getDeliveryTag();
		try {
			channel.basicAck(identificador, deletarTodasMensagens);
			log.info("---------------------------------------------------");
			log.info("MESSAGE RECEIVED: {}", new Gson().toJson(payload));
			log.info("---------------------------------------------------");
		} catch (final IOException e) {
			log.error("Error on confirm received of the message from queue", e);
		}
	}

	private static void imprimirErrosAoConsumirMensagem(final Exception e) {
		log.error("Occurred error on consumer message from queue RabbitMQ.", e);
		for (final String erro : getErros(e)) {
			log.error(erro);
		}
	}

	private Payload getPayload(final Message message) throws StreamReadException, DatabindException, IOException {
		return objectMapper.readValue(message.getBody(), Payload.class);
	}

	private static Set<String> getErros(final Exception e) {
		final Set<String> mensagens = new HashSet<>();
		Throwable currentException = e;
		int count = 0;
		final int maxCausas = 5;
		while (currentException != null && count < maxCausas) {
			final String errorMessage = currentException.getMessage();
			if (isNotEmpty(errorMessage) && mensagens.add(errorMessage)) {
				count++;
			}
			currentException = currentException.getCause();
		}
		return mensagens;
	}
}