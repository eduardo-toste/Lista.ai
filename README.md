# Lista.ai

Projeto em evolução para gerenciamento de listas de compras, com foco em arquitetura orientada a microsserviços, separação de responsabilidades e evolução futura com apoio de IA.

## Índice

- [1. Visão Geral](#1-visão-geral)
- [2. Proposta Técnica](#2-proposta-técnica)
- [3. Estado Atual](#3-estado-atual)
- [4. O Que Ainda Está em Evolução](#4-o-que-ainda-está-em-evolução)
- [5. Stack Principal](#5-stack-principal)
- [6. Estrutura do Repositório](#6-estrutura-do-repositório)
- [7. Qualidade do Projeto](#7-qualidade-do-projeto)
- [8. READMEs dos Serviços](#8-readmes-dos-serviços)

## 1. Visão Geral

O `Lista.ai` utiliza o domínio de listas de compras como base para construir uma plataforma com crescimento progressivo, tanto em funcionalidades quanto em arquitetura.

A proposta é começar com o gerenciamento de listas, itens e participantes, e evoluir para fluxos mais inteligentes, incluindo recursos apoiados por IA e comunicação entre serviços.

Do ponto de vista técnico, o projeto prioriza uma base organizada, com responsabilidades bem definidas, contratos claros e estrutura preparada para expansão.

## 2. Proposta Técnica

O projeto foi estruturado para demonstrar, na prática:

- arquitetura hexagonal aplicada a serviços reais
- evolução orientada a microsserviços
- comunicação assíncrona entre serviços com Kafka como direção arquitetural
- separação entre domínio, aplicação e infraestrutura
- documentação de API como parte do produto
- testes automatizados em múltiplas camadas

Isso dá ao repositório um valor técnico que vai além de um projeto de estudo isolado ou de um CRUD convencional.

## 3. Estado Atual

Atualmente, o repositório já possui um microsserviço implementado:

- `list-service`: responsável pelo gerenciamento de listas, itens e participantes

Esse serviço já conta com:

- API REST funcional
- persistência com PostgreSQL
- documentação OpenAPI/Swagger
- testes unitários, web, persistência e integração

## 4. O Que Ainda Está em Evolução

O projeto ainda não está completo como plataforma. Os principais pontos em aberto hoje são:

- implementação dos próximos microsserviços planejados
- integração entre serviços
- funcionalidades relacionadas a IA
- evolução adicional da infraestrutura e do ambiente de execução

Esse ponto é intencional: o repositório já tem uma base sólida, mas ainda preserva espaço claro para expansão arquitetural.

## 5. Stack Principal

- Java 21
- Spring Boot 3
- Spring Web
- Spring Data JPA
- Bean Validation
- PostgreSQL
- Apache Kafka
- Springdoc OpenAPI / Swagger UI
- JUnit 5
- Docker Compose

## 6. Estrutura do Repositório

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

## 7. Qualidade do Projeto

O serviço já implementado possui uma base concreta de qualidade, incluindo:

- testes de domínio
- testes de casos de uso
- testes de controllers
- testes de persistência
- testes de integração reais
- documentação OpenAPI anotada

Os detalhes de arquitetura, endpoints, configuração e estratégia de testes ficam concentrados nos READMEs específicos de cada serviço.

## 8. READMEs dos Serviços

Cada microsserviço deve possuir sua própria documentação operacional e técnica.

Para uma visão mais detalhada de arquitetura interna, endpoints, configuração, execução e estratégia de testes, a leitura recomendada é seguir para o README específico de cada serviço.

Serviços documentados atualmente:

- [list-service](./list-service/README.md)

Serviços planejados para documentação futura:

- `notification-service`
- `recipe-service`
