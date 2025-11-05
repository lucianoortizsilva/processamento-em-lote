## O que é o projeto?

O processamento-em-lote é um sistema de processamento batch implementado com Spring Batch, voltado para ingestão e manipulação de grandes volumes de dados. 
Usa diferentes fontes de dados para um Data Lake, utilizando orquestração por eventos via RabbitMQ. O objetivo principal é demonstrar, de forma didática, como construir pipelines de ETL com paralelismo, integração assíncrona entre sistemas e uso de diferentes bancos de dados.

Além da parte batch, o projeto conta com uma interface gráfica simples desenvolvida com Java Swing, permitindo ao usuário disparar jobs diretamente pela UI, que publicará mensagens no RabbitMQ e iniciará o fluxo de processamento.

## Tecnologias envolvidas 

- Java21
- Spring Boot 3.4.x
- Spring Batch
- Spring AMQP
- RabbitMQ
- PostgreSQL (3 bancos: Data Lake, Data Warehouse, e Spring Batch Metadata)
- Swing (UI em Java Desktop)
- Docker Compose para orquestração dos containers (bancos e mensageria)

### Como funciona o(s) jobs desenvolvidos?

O projeto contém o job principal denominado livraria, cuja execução é disparada por uma mensagem na fila do RabbitMQ (queue job_queue). O payload dessa mensagem indica o nome do job a ser executado.
 
### Pipeline do Job livraria: 
```java 
com.lucianoortizsilva.lote.jobs.livraria.LivrariaJobConfig
```
O job é composto por **4 steps**\
**Step 1** - Remoção de Dados Existentes (Step sincronizado):\
**Step 2** - Remove todos os registros da tabela livro do Data Lake para garantir a reprocessabilidade.\
**Step 3** - Implementado como um Tasklet que executa um comando SQL de DELETE.\
**Step 4** - Migração dos Lotes das Feiras (Steps paralelos):

Após a limpeza (**Step 1**) , os outros 3 steps executam em paralelo, cada um responsável por processar um lote de dados de uma feira diferente: Feira de São Paulo, Feira de Porto Alegre, e Feira de Curitiba.
Cada step de migração realiza:
*Leitura:* Um FlatFileItemReader lê os dados de um CSV específico da feira.
*Escrita:* Os dados lidos (LivroDTO) são inseridos na tabela livro do Data Lake através de um JdbcBatchItemWriter.
Toda a orquestração do job é feita usando Flow e SimpleAsyncTaskExecutor para garantir o paralelismo dos passos de migração.

### Detalhes do fluxo assíncrono 
A interface Swing envia para a fila RabbitMQ uma mensagem contendo o nome do job desejado.
Um consumidor (RabbitMQConsumer) consome essa mensagem e aciona o job correspondente via Spring Batch, utilizando a fábrica de jobs (JobFactory).
O status e logs de execução podem ser monitorados no console ou por logs configurados via Logback.
Estrutura dos dados - A tabela alvo no Data Lake é recriada automaticamente na inicialização do container PostgreSQL, baseada no script init_database_datalake.sql.

### Como executar

**1º** - Suba os serviços via Docker Compose. Na raíz do projeto execute:
```sh
sudo docker-compose down -v && sudo docker-compose build && sudo docker-compose up
```
**2º** - Use a interface Swing para selecionar e disparar o job. Na raíz do projeto execute:
```sh
mvn -Dspring-boot.run.profiles=local spring-boot:run
```

### Observações
O projeto foi configurado para aprendizado/demonstração, e pode ser facilmente expandido para incluir validações, novos passos batch, integração com Data Warehouse e transformações nos dados.

A única entidade atualmente processada é LivroDTO, mas outros fluxos podem ser facilmente adicionados seguindo o padrão do projeto.
