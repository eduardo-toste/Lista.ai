# recipe-service

`recipe-service` é o serviço responsável por transformar texto livre de receita em uma estrutura padronizada de itens de compra consumível por outros serviços, em especial o `list-service`.

## Responsabilidades

O serviço executa as seguintes funções:

- recebimento de texto de receita por endpoint REST
- construção de prompt restritivo para extração de ingredientes
- chamada ao provedor de IA por meio do Spring AI
- interpretação da resposta do modelo em JSON estruturado
- retorno de itens normalizados para consumo sistêmico

## Stack Tecnológica

- Java 21
- Spring Boot 3.5
- Spring Web
- Bean Validation
- Spring AI
- Google GenAI
- Jackson
- JUnit 5

## Arquitetura

O serviço segue uma estrutura enxuta, coerente com arquitetura hexagonal:

- `adapter/inbound/web`: endpoint, request e response HTTP
- `application`: casos de uso, portas e DTOs
- `adapter/outbound/ai`: construção de prompt, chamada ao modelo e mapeamento da resposta
- `configuration`: configuração dos casos de uso

O desenho preserva o provedor de IA atrás de uma porta outbound, facilitando evolução e substituição.

## API

### Endpoint

- `POST /recipe`

### Exemplo de Request

```json
{
  "recipeMessage": "strogonoff de frango"
}
```

### Exemplo de Response

```json
{
  "items": [
    {
      "name": "Frango",
      "quantity": 1,
      "unit": "KG",
      "purchased": false
    }
  ]
}
```

### Regras do Contrato

O contrato de resposta foi desenhado para consumo simples por outros serviços:

- o objeto raiz contém apenas `items`
- cada item contém `name`, `quantity`, `unit` e `purchased`
- `unit` deve respeitar os valores `UN`, `KG`, `G`, `L`, `ML`, `BOX`, `PACK`
- `purchased` deve ser `false` para itens extraídos

## Integração com o `list-service`

O `list-service` consome este serviço por OpenFeign com:

- método `POST`
- path `/recipe`
- corpo `{ "recipeMessage": "..." }`

O contrato é intencionalmente orientado a transporte. Cada serviço deve manter seus próprios DTOs locais, evitando compartilhamento direto de classes Java entre bounded contexts.

## Configuração

Configuração principal local em [application.properties](/Users/eduardotoste/Documents/projects/Lista.ai/recipe-service/src/main/resources/application.properties:1).

Arquivo de referência:

- [application.properties.example](/Users/eduardotoste/Documents/projects/Lista.ai/recipe-service/src/main/resources/application.properties.example:1)

Parâmetros relevantes:

```properties
server.port=8082
spring.ai.google.genai.api-key=your_key
spring.ai.google.genai.chat.options.model=gemini-2.0-flash
```

Para ambientes compartilhados ou produtivos, a recomendação é externalizar credenciais e não mantê-las em arquivos versionados.

## Execução Local

No diretório `recipe-service`:

```bash
./mvnw spring-boot:run
```

Endpoint local padrão:

- `http://localhost:8082`

## Fluxo de Processamento

1. o controller recebe `recipeMessage`
2. o caso de uso delega para `RecipeItemExtractorPort`
3. o adapter outbound constrói um prompt com regras restritivas
4. a resposta do modelo é interpretada como JSON
5. os itens normalizados são retornados pela camada web

## Considerações de Tratamento de Erro

Características relevantes da implementação atual:

- requests inválidos são barrados por Bean Validation
- respostas malformadas da IA geram falha no parsing
- erros do provedor são propagados a partir do adapter outbound

Evoluções naturais incluem timeouts, fallback controlado e contratos de erro mais expressivos.

## Testes

O serviço possui cobertura inicial de smoke test com Spring Boot e é um bom candidato para expansão com:

- testes de contrato do prompt
- testes do mapper de resposta da IA
- testes de controller para validação
- testes de integração com mock da porta de IA
