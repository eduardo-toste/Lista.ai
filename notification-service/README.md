# notification-service

`notification-service` é o serviço responsável por consumir eventos de compartilhamento de listas e enviar notificações WhatsApp aos participantes.

## Responsabilidades

O serviço contempla:

- consumo de eventos `shopping-list-shared` via Kafka
- desserialização da mensagem recebida
- conversão do evento em comando de aplicação
- geração de requests de notificação por participante
- envio de mensagens WhatsApp por meio de templates Twilio

## Stack Tecnológica

- Java 21
- Spring Boot 3.5
- Spring Kafka
- Jackson
- Twilio Java SDK
- JUnit 5

## Arquitetura

O serviço é orientado a mensagens e segue uma estrutura simples baseada em portas e adapters:

- `adapter/inbound/messaging`: consumer Kafka, payload de entrada e mapper do evento
- `application`: comando, caso de uso, mapper e porta outbound
- `adapter/outbound`: implementação de envio via Twilio
- `configuration`: configuração dos casos de uso

Na versão atual, o serviço não expõe API HTTP pública.

## Contrato de Evento

O consumo é realizado a partir do tópico configurado por:

```properties
app.kafka.topic.shopping-list-shared=shopping-list-events
```

O evento consumido contém:

- `eventType`
- `occurredAt`
- `shoppingListId`
- `shoppingListName`
- `items`
- `participants`

Cada participante é processado individualmente para envio da notificação correspondente.

## Fluxo de Entrega via Twilio

Para cada participante presente no evento:

1. o caso de uso monta um `NotificationRequest`
2. o adapter outbound resolve o template apropriado
3. as variáveis são serializadas em JSON
4. a mensagem WhatsApp é enviada via Twilio

## Configuração

Configuração principal local em [application.properties](/Users/eduardotoste/Documents/projects/Lista.ai/notification-service/src/main/resources/application.properties:1).

Arquivo de referência:

- [application.properties.example](/Users/eduardotoste/Documents/projects/Lista.ai/notification-service/src/main/resources/application.properties.example:1)

Propriedades relevantes:

```properties
spring.kafka.bootstrap-servers=localhost:19092
spring.kafka.consumer.group-id=notification-service
app.kafka.topic.shopping-list-shared=shopping-list-events
twilio.account-sid=your_sid
twilio.auth-token=your_token
twilio.whatsapp.from=whatsapp:+14155238886
app.twilio.templates.shopping-list-shared=your_template_sid
```

Para ambientes compartilhados, recomenda-se externalizar credenciais Twilio e identificadores de template.

## Execução Local

### 1. Subir a infraestrutura compartilhada

Na raiz do repositório:

```bash
docker compose up -d
```

### 2. Executar o serviço

No diretório `notification-service`:

```bash
./mvnw spring-boot:run
```

O serviço não possui porta HTTP pública como parte do fluxo principal.

## Requisitos para Integração Local

Para validar o fluxo ponta a ponta localmente:

- executar o `list-service`
- executar o `notification-service`
- manter Kafka disponível em `localhost:19092`
- configurar credenciais Twilio válidas e template WhatsApp correspondente

Em seguida, acionar:

- `POST /lists/{id}/share` no `list-service`

## Considerações de Projeto

Características relevantes da implementação:

- o consumo ocorre de forma síncrona por mensagem recebida
- o envio é realizado individualmente por participante
- templates desconhecidos resultam em erro de aplicação
- mensagens inválidas geram falha de desserialização

## Testes

O serviço possui cobertura inicial de smoke test com Spring Boot e comporta expansão com:

- testes do consumer Kafka
- testes de mapper de evento
- testes do caso de uso de fan-out por participante
- testes do adapter Twilio com isolamento do boundary externo
