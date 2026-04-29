# Lista.ai

`Lista.ai` é uma plataforma orientada a microsserviços para gestão de listas de compras, com suporte a fluxos inteligentes apoiados por IA e integrações assíncronas entre serviços.

O repositório está estruturado em torno de três serviços principais:

- `list-service`: responsável pelo gerenciamento de listas, itens, participantes e compartilhamento
- `recipe-service`: responsável pela conversão de receitas em itens estruturados de compra
- `notification-service`: responsável pelo consumo de eventos de compartilhamento e envio de notificações

## Visão Geral

O projeto foi concebido para materializar, de forma aplicada, os seguintes princípios:

- arquitetura hexagonal pragmática
- separação clara entre domínio, aplicação e infraestrutura
- comunicação síncrona entre serviços via HTTP
- integração assíncrona orientada a eventos com Kafka
- incorporação progressiva de capacidades de IA em fluxos de negócio bem delimitados

Dentro desse desenho, a plataforma já contempla um cenário funcional completo:

1. criação e manutenção de listas no `list-service`
2. geração automática de itens a partir de receitas via `recipe-service`
3. compartilhamento de listas com publicação de eventos
4. processamento assíncrono desses eventos pelo `notification-service`

## Estrutura do Repositório

```text
.
├── .env
├── docker-compose.yml
├── README.md
├── list-service
├── notification-service
└── recipe-service
```

## Serviços

| Serviço | Responsabilidade | Integração Principal | Porta Padrão |
| --- | --- | --- | --- |
| `list-service` | Gestão de listas, criação inteligente e compartilhamento | REST, Kafka Producer, OpenFeign | `8080` |
| `recipe-service` | Extração de itens a partir de receita | REST | `8082` |
| `notification-service` | Processamento de eventos e envio de notificações | Kafka Consumer | n/a |

## Arquitetura

### `list-service`

- concentra o domínio principal da plataforma
- persiste dados em PostgreSQL
- publica eventos de compartilhamento em Kafka
- consome o `recipe-service` via OpenFeign para criação de listas inteligentes

### `recipe-service`

- expõe um endpoint REST para interpretação de receitas
- constrói prompts restritivos para extração de ingredientes
- utiliza Google GenAI por meio do Spring AI
- retorna itens normalizados compatíveis com o contrato consumido pelo `list-service`

### `notification-service`

- consome eventos do tópico de compartilhamento
- converte a mensagem recebida em comando de aplicação
- envia notificações WhatsApp por meio de templates Twilio

## Infraestrutura Local

A infraestrutura compartilhada para desenvolvimento está definida em [docker-compose.yml](/Users/eduardotoste/Documents/projects/Lista.ai/docker-compose.yml:1) e contempla:

- PostgreSQL para o `list-service`
- broker Kafka
- Kafka UI para inspeção local

Endpoints locais padrão:

- PostgreSQL: `localhost:5432`
- Kafka: `localhost:19092`
- Kafka UI: `http://localhost:8081`

## Execução Local

### 1. Subir a infraestrutura

O arquivo `.env` na raiz do projeto é utilizado pelo Docker Compose para configuração do banco local.

```bash
docker compose up -d
```

### 2. Configurar os serviços

Cada serviço possui um `application.properties.example` com os parâmetros esperados para execução local.

Recomendação:

- manter credenciais reais e chaves de API fora de arquivos versionados
- preferir variáveis de ambiente ou mecanismos externos de gestão de segredos

### 3. Executar os serviços

Em terminais separados:

```bash
cd list-service && ./mvnw spring-boot:run
```

```bash
cd recipe-service && ./mvnw spring-boot:run
```

```bash
cd notification-service && ./mvnw spring-boot:run
```

## Fluxos Principais

### Lista manual

- criação de lista diretamente no `list-service`
- manutenção de itens e participantes
- compartilhamento sob demanda

### Lista inteligente

- envio de receita para o fluxo `POST /lists/smart`
- chamada interna do `list-service` para o `recipe-service`
- extração e normalização de itens antes da persistência

### Compartilhamento e notificação

- acionamento do endpoint de compartilhamento
- publicação de evento em Kafka
- consumo do evento pelo `notification-service`
- envio de notificação aos participantes

## Qualidade e Testes

A cobertura automatizada atualmente é mais ampla no `list-service`, incluindo:

- testes de domínio
- testes de casos de uso
- testes de mapeadores
- testes de repositório
- testes Web MVC
- testes de integração

Os demais serviços possuem cobertura inicial e estão preparados para expansão conforme a evolução das responsabilidades e integrações.

## Documentação dos Serviços

A documentação específica de cada serviço está disponível em:

- [README do list-service](./list-service/README.md)
- [README do recipe-service](./recipe-service/README.md)
- [README do notification-service](./notification-service/README.md)

## Evoluções Naturais

Os próximos aprimoramentos mais naturais para a plataforma incluem:

- fortalecimento da gestão de segredos e credenciais
- ampliação da cobertura automatizada nos serviços de integração
- políticas de resiliência para chamadas externas e processamento assíncrono
- aprofundamento de observabilidade, rastreabilidade e estratégias de deploy
