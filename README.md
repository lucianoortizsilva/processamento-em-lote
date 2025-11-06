## O que é o projeto?

<p>O processamento-em-lote é um sistema de processamento batch implementado com Spring Batch, voltado para ingestão e manipulação de grandes volumes de dados.</p> 
<p>Usa diferentes fontes de dados para um Data Lake, utilizando orquestração por eventos via RabbitMQ. O objetivo principal é demonstrar, de forma didática, como construir pipelines de ETL com paralelismo, integração assíncrona entre sistemas e uso de diferentes bancos de dados.</p>
<p>Além da parte batch, o projeto conta com uma interface gráfica simples desenvolvida com Java Swing, permitindo ao usuário disparar jobs diretamente pela UI, que publicará mensagens no RabbitMQ e iniciará o fluxo de processamento.</p>

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

<hr>

### Pipeline do Job netflix: 
```java 
com.lucianoortizsilva.lote.jobs.netflix.NetflixJobConfig
```
O job netflixJob consiste em três etapas principais (steps), executadas sequencialmente, que realizam a carga, transformação e classificação dos dados do catálogo da Netflix.\ 
Abaixo está o fluxo detalhado desse pipeline:

**Step 1** - Exclusão dos dados atuais (step01DeleteNetflixCatalogo)
Responsabilidade: Remove todos os registros das tabelas de catálogo do Data Lake e Data Warehouse relacionados à Netflix, garantindo que não haja dados residuais de execuções anteriores.
Como: 
- Executa comandos SQL DELETE nas tabelas:
- netflix_catalogo (Data Lake)
- netflix_catalogo_documentario (Data Warehouse)
- netflix_catalogo_comedia (Data Warehouse)

**Step 2** - Carga dos dados a partir do CSV (step02LoadNetflixCatalogo)
Responsabilidade: Lê os dados do arquivo CSV (arquivos/netflix.csv) e insere todos os registros na tabela netflix_catalogo do Data Lake.
Como:
- Reader: Utiliza FlatFileItemReader para ler o CSV, considerando campos como id, title, cast, country, releaseYear, duration, listedIn etc.
- Writer: Usa JdbcBatchItemWriter para inserir os dados na tabela netflix_catalogo, mapeando diretamente os campos do objeto para as colunas da tabela.
- Chunk Size: Processa em chunks de2 registros.
- Execução paralela: Utiliza um pool de threads para acelerar a operação (ThreadPoolTaskExecutor).

**Step 3** - Transformação e classificação dos dados (step03TransformaNetflixCatalogo)
Responsabilidade: Lê os registros da tabela netflix_catalogo do Data Lake, identifica o gênero do conteúdo e distribui os registros nas tabelas correspondentes do Data Warehouse:
Tabela netflix_catalogo_documentario para "Documentaries"
Tabela netflix_catalogo_comedia para "Comedies"
Como:
- Reader: Utiliza JdbcCursorItemReader para ler da tabela do Data Lake.
- Writer: Implementa ClassifierCompositeItemWriter, encaminhando cada item ao writer adequado conforme o gênero identificado no campo listedIn:
- Se pertencer a "Documentaries", escreve na tabela de documentários.
- Se pertencer a "Comedies", escreve na tabela de comédias.
- Se não pertencer a nenhum desses, descarta.

<hr>

### Pipeline do Job aviação:
```java 
com.lucianoortizsilva.lote.jobs.aviacao.AviacaoJobConfig
```

O job aviacaoJob é responsável por processar grandes volumes de dados de itinerários aéreos, aplicando filtros e transformações, e armazenando apenas informações de voos em classe econômica (Basic Economy) no Data Lake. O pipeline utiliza particionamento e processamento paralelo para alta performance.

O pipeline é composto por duas etapas principais:

**Step1** – Exclusão dos Dados Atuais (step01DeleteAviacao)
- Responsabilidade: Remove todos os registros existentes da tabela aviacao do Data Lake para garantir reprocessamento idempotente.
- Como: Implementado por um Tasklet que executa um comando SQL DELETE FROM aviacao.

**Step2** – Migração/Processamento com Partitionamento (step02MigracaoCatalogoAviacaoManager)
- Responsabilidade: Lê o arquivo CSV de itinerários (arquivos/itinerarios.csv), processa os registros e insere no Data Lake:
O processamento ocorre em4 partições, cada uma processando aproximadamente12.500 linhas (totalizando50.000 linhas).
Cada partição executa o step02MigracaoCatalogoAviacaoSlave de forma paralela (ThreadPool de4 threads).
- Reader: FlatFileItemReader lê uma faixa específica de linhas do CSV conforme a partição.
- Processor: Filtra apenas voos de classe econômica (Basic Economy) e faz transformações (ex: traduz campos de aeroportos para nomes descritivos, remove duplicidade em nomes de companhias aéreas).
- Writer: Usa JdbcBatchItemWriter para inserir apenas os registros processados da classe econômica na tabela aviacao do Data Lake.
Todos os campos esperados: id, flightDate, startingAirport, destinationAirport, segmentsAirlineName.

<hr>

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


