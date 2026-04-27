# Lista.ai

Plataforma em evolução para gerenciamento de listas de compras, construída com foco em arquitetura orientada a microsserviços, modelagem de domínio clara e evolução incremental da qualidade técnica.

## Índice

- [1. Visão Geral](#1-visão-geral)
- [2. Proposta Técnica do Projeto](#2-proposta-técnica-do-projeto)
- [3. Objetivos do Projeto](#3-objetivos-do-projeto)
- [4. Estado Atual](#4-estado-atual)
- [5. O Que Ainda Não Foi Finalizado](#5-o-que-ainda-não-foi-finalizado)
- [6. Stack Tecnológica](#6-stack-tecnológica)
- [7. Arquitetura Hexagonal e Microsserviços](#7-arquitetura-hexagonal-e-microsserviços)
- [8. Valor Técnico Agregado Pelo Projeto](#8-valor-técnico-agregado-pelo-projeto)
- [9. Estrutura do Repositório](#9-estrutura-do-repositório)
- [10. Qualidade e Testes](#10-qualidade-e-testes)
- [11. Documentação da API](#11-documentação-da-api)
- [12. Execução Local](#12-execução-local)
- [13. Serviços do Repositório](#13-serviços-do-repositório)
- [14. Próximos Passos Naturais](#14-próximos-passos-naturais)

## 1. Visão Geral

O `Lista.ai` é um projeto voltado para o gerenciamento de listas de compras, com a proposta de evoluir para uma plataforma composta por múltiplos serviços com responsabilidades bem definidas.

O repositório já possui o primeiro microsserviço implementado, `list-service`, e serve como base prática para consolidar:

- arquitetura hexagonal
- DDD tático leve
- modelagem de domínio
- testes automatizados em múltiplas camadas
- documentação de API

Mais do que um CRUD simples, a proposta do projeto é demonstrar capacidade de estruturar software com preocupações reais de engenharia desde cedo: separação de responsabilidades, isolamento de regras de negócio, contratos HTTP bem definidos e evolução orientada a qualidade.

## 2. Proposta Técnica do Projeto

O `Lista.ai` foi concebido para chamar atenção não apenas pelo domínio funcional, mas pela forma como o problema é resolvido tecnicamente.

A proposta técnica combina:

- arquitetura hexagonal para desacoplar domínio e infraestrutura
- organização orientada a microsserviços para crescimento por responsabilidades
- testes automatizados em múltiplas camadas para reduzir regressão
- documentação OpenAPI para tornar o contrato explícito
- evolução incremental, permitindo amadurecer o projeto sem comprometer a base

Isso transforma o repositório em algo mais relevante do que um projeto de estudo isolado: ele passa a ser um exercício prático de construção de backend com critérios profissionais.

## 3. Objetivos do Projeto

Os objetivos de produto e arquitetura do projeto incluem:

- permitir criação e manutenção manual de listas de compras
- suportar participantes vinculados às listas
- evoluir para fluxos de compartilhamento e notificação
- incorporar funcionalidades assistidas por IA em serviços futuros
- estruturar o sistema em microsserviços com limites claros de responsabilidade

## 4. Estado Atual

Atualmente, o repositório contém um serviço implementado:

- `list-service`: API responsável pelo gerenciamento de listas, itens e participantes

O serviço já possui:

- endpoints REST funcionais
- persistência com PostgreSQL
- documentação OpenAPI/Swagger
- testes unitários
- testes de controller
- testes de repositório
- testes de integração reais com `@SpringBootTest`

## 5. O Que Ainda Não Foi Finalizado

O projeto ainda está em evolução. Neste momento, os seguintes pontos ainda não estão concluídos:

- `notification-service` ainda não foi implementado
- `recipe-service` ainda não foi implementado
- a integração com IA ainda não foi iniciada neste repositório
- a comunicação entre microsserviços ainda não existe
- a padronização de contrato paginado da API ainda pode ser refinada
- a suíte de integração ainda utiliza `H2`; uma camada adicional com `Testcontainers` pode ser adicionada no futuro
- a infraestrutura completa da plataforma ainda não está consolidada além do banco do `list-service`

## 6. Stack Tecnológica

- Java 21
- Spring Boot 3
- Spring Web
- Spring Data JPA
- Bean Validation
- PostgreSQL
- Springdoc OpenAPI / Swagger UI
- JUnit 5
- H2 para ambiente de testes automatizados
- Docker Compose para infraestrutura local

## 7. Arquitetura Hexagonal e Microsserviços

O projeto segue uma direção explícita de arquitetura orientada a microsserviços.

A ideia não é concentrar toda a lógica em uma aplicação monolítica única, mas dividir responsabilidades em serviços com objetivos claros, como:

- gerenciamento de listas
- notificações e compartilhamento
- geração de ingredientes com apoio de IA

Dentro de cada serviço, a base técnica adotada é a arquitetura hexagonal.

No `list-service`, isso já aparece de forma concreta:

- `domain`: regras centrais de negócio e modelos de domínio
- `application`: casos de uso, portas, DTOs e orquestração
- `adapter/inbound`: entrada HTTP e mapeamento web
- `adapter/outbound`: persistência e integração com infraestrutura
- `configuration`: configuração de beans e documentação OpenAPI

Essa abordagem agrega valor técnico porque:

- protege a regra de negócio do acoplamento direto com o framework
- facilita substituição de detalhes de infraestrutura
- melhora a testabilidade do sistema
- torna os limites de responsabilidade mais fáceis de enxergar
- prepara o código para crescer sem se degradar rapidamente

Em outras palavras: o projeto não usa arquitetura hexagonal apenas como discurso. Ela já influencia a forma como o código é organizado, testado e evoluído.

## 8. Valor Técnico Agregado Pelo Projeto

O que torna este projeto tecnicamente interessante é a combinação de escolhas que, juntas, elevam a qualidade da base:

- separação clara entre domínio, aplicação e infraestrutura
- uso de portas e adaptadores para reduzir dependência de implementação
- preocupação com testes unitários, web, persistência e integração real
- documentação da API tratada como parte do produto
- estrutura preparada para expansão em microsserviços
- foco em crescimento arquitetural progressivo, em vez de complexidade prematura

Isso faz com que o repositório tenha valor não só como demonstração funcional, mas como evidência de maturidade técnica em backend.

## 9. Estrutura do Repositório

```text
.
├── docker-compose.yml
├── README.md
└── list-service
    ├── pom.xml
    ├── mvnw
    ├── README.md
    └── src
```

## 10. Qualidade e Testes

O `list-service` já possui uma base de testes organizada em múltiplas camadas:

- testes de domínio
- testes de casos de uso
- testes de mapeadores
- testes de repositório com JPA
- testes de controller com `@WebMvcTest`
- testes de integração reais com `@SpringBootTest`

Esse ponto é importante porque o projeto já não está apenas “funcionando”; ele já possui mecanismos concretos de validação técnica.

## 11. Documentação da API

O serviço implementado expõe documentação OpenAPI.

Com o `list-service` em execução:

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

A documentação atual já inclui:

- schemas de request e response
- exemplos de payload
- documentação de respostas de erro
- explicitação de cenários `400`, `404`, `409` e `500`

## 12. Execução Local

O repositório raiz disponibiliza a infraestrutura de banco utilizada pelo `list-service`.

### 12.1. Subir o PostgreSQL

O `docker-compose.yml` utiliza as seguintes variáveis:

- `LIST_SERVICE_DB`
- `LIST_SERVICE_DB_USER`
- `LIST_SERVICE_DB_PASSWORD`
- `LIST_SERVICE_DB_PORT`

Exemplo:

```bash
export LIST_SERVICE_DB=list_db
export LIST_SERVICE_DB_USER=list_user
export LIST_SERVICE_DB_PASSWORD=list_pass
export LIST_SERVICE_DB_PORT=5432
docker compose up -d
```

### 12.2. Executar o serviço

As instruções detalhadas do serviço estão em:

- [list-service/README.md](/Users/eduardotoste/Documents/projects/Lista.ai/list-service/README.md:1)

## 13. Serviços do Repositório

### 13.1. Implementado

- [list-service/README.md](/Users/eduardotoste/Documents/projects/Lista.ai/list-service/README.md:1)

### 13.2. Planejados

- `notification-service`
- `recipe-service`

## 14. Próximos Passos Naturais

Os próximos passos mais coerentes para o repositório são:

- implementar os próximos microsserviços planejados
- evoluir a integração entre serviços
- adicionar testes de integração com PostgreSQL real usando `Testcontainers`
- estabilizar o contrato de paginação da API
- expandir a documentação operacional do ambiente

Hoje, o projeto já tem uma base sólida, mas ainda está em fase de consolidação como plataforma completa.
