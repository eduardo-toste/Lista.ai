# list-service

`list-service` é o serviço central do `Lista.ai`. Ele concentra o domínio de listas de compras e responde pelos fluxos de criação, manutenção, compartilhamento e geração inteligente de listas.

## Responsabilidades

O serviço contempla as seguintes capacidades:

- criação de listas manuais
- criação de listas inteligentes a partir de receita
- consulta individual e paginada de listas
- atualização de nome e exclusão de listas
- inclusão, edição, remoção e marcação de itens
- inclusão, edição e remoção de participantes
- publicação de eventos de compartilhamento em Kafka

## Stack Tecnológica

- Java 21
- Spring Boot 3.5
- Spring Web
- Spring Data JPA
- Bean Validation
- PostgreSQL
- Spring Cloud OpenFeign
- Spring Kafka
- Springdoc OpenAPI
- JUnit 5
- H2 para testes automatizados

## Arquitetura

O serviço segue uma organização baseada em arquitetura hexagonal pragmática:

- `domain`: entidades, enums e regras de negócio
- `application`: casos de uso, portas, DTOs e mapeadores de aplicação
- `adapter/inbound/web`: controllers REST, contratos HTTP e tratamento de erros
- `adapter/outbound/persistence`: integração com persistência JPA
- `adapter/outbound/api`: integração HTTP com o `recipe-service`
- `adapter/outbound/messaging`: publicação de eventos Kafka
- `configuration`: configuração de beans e integração com framework

Essa estrutura reduz acoplamento com detalhes de infraestrutura e preserva o domínio como núcleo da lógica de negócio.

## Fluxos Relevantes

### Criação de lista manual

- a requisição entra por `POST /lists`
- o contrato web é convertido em comando de aplicação
- o caso de uso monta o agregado e persiste a lista
- a resposta retorna a representação consolidada da lista

### Criação de lista inteligente

- a requisição entra por `POST /lists/smart`
- o caso de uso aciona o `recipe-service` por meio de uma porta outbound
- os itens retornados são convertidos para `ShoppingListItem`
- itens duplicados são normalizados por `nome + unidade`, com soma de quantidades
- a lista resultante é persistida e retornada

### Compartilhamento de lista

- a operação é acionada por `POST /lists/{id}/share`
- o caso de uso valida a consistência da lista
- um evento `shopping-list-shared` é publicado em Kafka
- o processamento de notificação ocorre de forma assíncrona

## Endpoints

### Listas

- `POST /lists`
- `POST /lists/smart`
- `GET /lists/{id}`
- `GET /lists`
- `PATCH /lists/{id}`
- `DELETE /lists/{id}`
- `POST /lists/{id}/share`

### Itens

- `POST /lists/{listId}/items`
- `PATCH /lists/{listId}/items/{itemId}`
- `PATCH /lists/{listId}/items/{itemId}/purchase`
- `DELETE /lists/{listId}/items/{itemId}`

### Participantes

- `POST /lists/{listId}/participants`
- `PATCH /lists/{listId}/participants/{participantId}`
- `DELETE /lists/{listId}/participants/{participantId}`

## Comportamento da API

Convenções adotadas pelo serviço:

- operações de escrita retornam a representação atualizada da lista quando aplicável
- falhas de validação retornam `400 Bad Request`
- recursos inexistentes retornam `404 Not Found`
- conflitos de negócio, como duplicidade de item ou participante, retornam `409 Conflict`
- respostas de erro são padronizadas pela camada web

## Integrações Externas

### PostgreSQL

Persistência principal do agregado de listas.

### `recipe-service`

Consumido no fluxo de lista inteligente via OpenFeign.

Propriedade relevante:

```properties
clients.recipe-service.url=http://localhost:8082
```

### Kafka

Utilizado para publicação de eventos de compartilhamento.

Propriedades relevantes:

```properties
spring.kafka.bootstrap-servers=localhost:19092
app.kafka.topic.shopping-list-shared=shopping-list-events
```

## Configuração

Configuração principal local em [application.properties](/Users/eduardotoste/Documents/projects/Lista.ai/list-service/src/main/resources/application.properties:1).

Arquivo de referência:

- [application.properties.example](/Users/eduardotoste/Documents/projects/Lista.ai/list-service/src/main/resources/application.properties.example:1)

Parâmetros locais relevantes:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/list_db
spring.datasource.username=list_user
spring.datasource.password=list_pass
spring.jpa.hibernate.ddl-auto=update
spring.kafka.bootstrap-servers=localhost:19092
clients.recipe-service.url=http://localhost:8082
```

## Execução Local

### 1. Subir a infraestrutura compartilhada

Na raiz do repositório:

```bash
docker compose up -d
```

### 2. Executar dependências necessárias

Para o fluxo de lista inteligente, o `recipe-service` deve estar em execução.

### 3. Executar o serviço

No diretório `list-service`:

```bash
./mvnw spring-boot:run
```

Endpoint local padrão:

- `http://localhost:8080`

## Documentação OpenAPI

Com a aplicação em execução:

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Testes

A cobertura automatizada inclui:

- testes de domínio
- testes de casos de uso
- testes de mapeadores web e de aplicação
- testes de persistência
- testes de repositório
- testes Web MVC
- testes de integração com Spring Boot

Comandos úteis:

```bash
./mvnw test
```

```bash
./mvnw -DskipTests compile
```

```bash
./mvnw test -Dtest=ShoppingListIntegrationTest
```

## Considerações de Projeto

Decisões relevantes na implementação:

- a integração HTTP com o `recipe-service` está isolada por porta e adapter outbound
- a deduplicação de itens extraídos da receita ocorre antes da entrada no agregado
- o compartilhamento é desacoplado da entrega de notificação por meio de evento assíncrono
