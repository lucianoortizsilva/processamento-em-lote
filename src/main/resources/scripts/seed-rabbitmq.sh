#!/bin/sh

# Aguarda o RabbitMQ ficar pronto
until curl -u guest:guest -s http://svc-rabbitmq:15672/api/healthchecks/node | grep -q '"status":"ok"'; do
  echo "Aguardando RabbitMQ...";
  sleep 1;
done

# Cria exchange
curl -u guest:guest -H "content-type:application/json" \
  -XPUT -d '{"type":"topic", "durable":true}' \
  http://svc-rabbitmq:15672/api/exchanges/%2F/job.exchange

# Cria fila
curl -u guest:guest -H "content-type:application/json" \
  -XPUT -d '{"durable":true}' \
  http://svc-rabbitmq:15672/api/queues/%2F/job_queue

# Cria binding
curl -u guest:guest -H "content-type:application/json" \
  -XPOST -d '{"routing_key":"job.execute","arguments":{}}' \
  http://svc-rabbitmq:15672/api/bindings/%2F/e/job.exchange/q/job_queue