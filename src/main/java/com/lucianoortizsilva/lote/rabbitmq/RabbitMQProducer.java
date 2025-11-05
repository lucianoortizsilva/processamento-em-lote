package com.lucianoortizsilva.lote.rabbitmq;

import java.time.Duration;
import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RabbitMQProducer {
	
	public void send(final Payload payload) throws Exception {
		awaiting();
		final ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		factory.setUsername("guest");
		factory.setPassword("guest");
		factory.setPort(5672);
		try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
			final String message = new Gson().toJson(payload);
			channel.basicPublish(RabbitMQConfig.EXCHANGE_TOPIC_NAME, RabbitMQConfig.EXCHANGE_TOPIC_ROUNTING_KEY, null, message.getBytes("UTF-8"));
		} catch (final Exception e) {
			throw e;
		}
	}
	
	private static void awaiting() {
		try {
			Thread.sleep(Duration.ofSeconds(3));
		} catch (final InterruptedException e) {
			log.error(e.getMessage());
		}
	}
}