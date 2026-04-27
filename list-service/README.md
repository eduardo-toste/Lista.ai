# list-service

Microsserviço responsável pelo gerenciamento de listas de compras, itens e participantes dentro do ecossistema `Lista.ai`.

## Índice

- [1. Visão Geral](#1-visão-geral)
- [2. Responsabilidades do Serviço](#2-responsabilidades-do-serviço)
- [3. Stack Tecnológica](#3-stack-tecnológica)
- [4. Arquitetura](#4-arquitetura)
- [5. Estrutura de Pacotes](#5-estrutura-de-pacotes)
- [6. Endpoints Principais](#6-endpoints-principais)
- [7. Contratos da API](#7-contratos-da-api)
- [8. Documentação OpenAPI](#8-documentação-openapi)
- [9. Configuração](#9-configuração)
- [10. Execução Local](#10-execução-local)
- [11. Estratégia de Testes](#11-estratégia-de-testes)
- [12. Cobertura Atual de Integração](#12-cobertura-atual-de-integração)
- [13. Limitações e Evoluções Naturais](#13-limitações-e-evoluções-naturais)

## 1. Visão Geral

O `list-service` é o primeiro microsserviço implementado no repositório `Lista.ai`.

Seu papel é centralizar a lógica de gerenciamento de listas de compras, incluindo:

- criação e consulta de listas
- manutenção de itens
- manutenção de participantes
- validações de negócio relacionadas a duplicidade e consistência da lista

## 2. Responsabilidades do Serviço

O serviço atualmente suporta:

- criar listas de compras
- buscar uma lista por identificador
- listar listas com paginação
- renomear listas
- remover listas
- adicionar itens
- atualizar itens
- marcar itens como comprados
- remover itens
- adicionar participantes
- atualizar participantes
- remover participantes

## 3. Stack Tecnológica

- Java 21
- Spring Boot 3.5
- Spring Web
- Spring Data JPA
- Bean Validation
- PostgreSQL
- Springdoc OpenAPI
- JUnit 5
- H2 para execução dos testes automatizados

## 4. Arquitetura

O serviço está organizado em um estilo hexagonal pragmático, separando regra de negócio, casos de uso, entrada HTTP e persistência.

Essa estrutura foi adotada para:

- reduzir acoplamento entre domínio e framework
- facilitar testes em diferentes camadas
- manter responsabilidades mais explícitas
- preparar o serviço para evoluções futuras com menor impacto estrutural

## 5. Estrutura de Pacotes

```text
src/main/java/com/listaai/list
├── adapter
│   ├── inbound
│   │   └── web
│   └── outbound
│       └── persistence
├── application
│   ├── dto
│   ├── exception
│   ├── mapper
│   ├── port
│   ├── usecase
│   └── utils
├── configuration
└── domain
    ├── enums
    ├── exception
    └── model
```

### Papel dos principais pacotes

- `domain`: modelos e regras de negócio
- `application`: casos de uso, portas e mapeamentos internos
- `adapter/inbound/web`: controllers, requests, responses e tratamento web
- `adapter/outbound/persistence`: entidades JPA, repositórios e adaptadores de persistência
- `configuration`: configuração dos casos de uso e da documentação OpenAPI

## 6. Endpoints Principais

### 6.1. Listas

- `POST /lists`
- `GET /lists/{id}`
- `GET /lists`
- `PATCH /lists/{id}`
- `DELETE /lists/{id}`

### 6.2. Itens

- `POST /lists/{listId}/items`
- `PATCH /lists/{listId}/items/{itemId}`
- `PATCH /lists/{listId}/items/{itemId}/purchase`
- `DELETE /lists/{listId}/items/{itemId}`

### 6.3. Participantes

- `POST /lists/{listId}/participants`
- `PATCH /lists/{listId}/participants/{participantId}`
- `DELETE /lists/{listId}/participants/{participantId}`

## 7. Contratos da API

O serviço segue as seguintes convenções:

- operações de escrita retornam a representação atualizada da lista quando aplicável
- falhas de validação retornam `400 Bad Request`
- recursos inexistentes retornam `404 Not Found`
- conflitos de negócio, como item ou participante duplicado, retornam `409 Conflict`
- erros seguem a estrutura padronizada de `ErrorResponse`

## 8. Documentação OpenAPI

Com a aplicação em execução:

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

A documentação foi anotada com:

- schemas descritivos
- exemplos de request body
- documentação de parâmetros
- respostas de erro documentadas

## 9. Configuração

O datasource local padrão está em `src/main/resources/application.properties`.

Configuração atual:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/list_db
spring.datasource.username=list_user
spring.datasource.password=list_pass
```

Também existe um arquivo de referência:

- [src/main/resources/application.properties.example](/Users/eduardotoste/Documents/projects/Lista.ai/list-service/src/main/resources/application.properties.example:1)

## 10. Execução Local

### 10.1. Subir o banco

A partir da raiz do repositório:

```bash
export LIST_SERVICE_DB=list_db
export LIST_SERVICE_DB_USER=list_user
export LIST_SERVICE_DB_PASSWORD=list_pass
export LIST_SERVICE_DB_PORT=5432
docker compose up -d
```

### 10.2. Executar a aplicação

A partir da pasta `list-service`:

```bash
./mvnw spring-boot:run
```

## 11. Estratégia de Testes

O serviço já possui testes em diferentes níveis:

- testes unitários de domínio
- testes unitários de casos de uso
- testes de mapeadores
- testes de repositório com JPA
- testes de controller com `@WebMvcTest`
- testes de integração reais com `@SpringBootTest`

### Comandos úteis

Executar toda a suíte:

```bash
./mvnw test
```

Executar apenas os testes de integração:

```bash
./mvnw test -Dtest=ShoppingListIntegrationTest
```

Executar apenas os testes de controller:

```bash
./mvnw test -Dtest=ShoppingListControllerWebMvcTest,ShoppingListItemControllerWebMvcTest,ShoppingListParticipantControllerWebMvcTest
```

## 12. Cobertura Atual de Integração

A suíte de integração atual cobre:

- criação e busca de lista
- erros de validação
- retorno `404` para recursos inexistentes
- atualização e remoção de lista
- fluxo completo de itens
- fluxo completo de participantes
- cenários de conflito, como item duplicado e participante duplicado

## 13. Limitações e Evoluções Naturais

Os principais pontos que ainda podem evoluir neste serviço são:

- adoção de `Testcontainers` para testes com PostgreSQL real
- refinamento do contrato de paginação
- evolução de alguns contratos de update, se a API passar a suportar parcialidade mais explícita
- consolidação de documentação operacional adicional, caso o serviço cresça em infraestrutura e integrações

No estado atual, o `list-service` já é um serviço funcional, testado e documentado, com base suficiente para evoluções mais robustas.
