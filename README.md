# BTG Pactual - Order Processing Microservice

<p align="center" width="100%">
    <img width="40%" src="./images/btg-logo.jpg"> 
</p>

<h3 align="center">Microserviço de Processamento de Pedidos</h3>

<p align="center">
  <img alt="Language: Java" src="https://img.shields.io/badge/language-java-blue">  
  <img alt="Framework: SpringBoot" src="https://img.shields.io/badge/framework-springboot-green">
  <img alt="API: Rest" src="https://img.shields.io/badge/api-rest-lightblue">
  <img alt="Container: Docker" src="https://img.shields.io/badge/container-docker-2496ED">
  <img alt="Data Base: MongoDB" src="https://img.shields.io/badge/database-mongoDB-13AA52">
  <img alt="Mensageria: RabbitMQ" src="https://img.shields.io/badge/mensageria-rabbitMQ-FF6600">

</p>

## 📋 Descrição

Microserviço que consome eventos de pedidos via **RabbitMQ**, persiste em **MongoDB** e expõe uma **API REST** para consultar pedidos de clientes com cálculo automático de totais.

## 🏗️ Arquitetura

```
┌─────────────────────────────────────────────────────────┐
│                    RabbitMQ (Fila)                      │
│            (btg-pactual-order-queue)                    │
└──────────────────────┬──────────────────────────────────┘
                       │ Consome Eventos
                       ▼
┌──────────────────────────────────────────────────────────┐
│         Spring Boot Microservice                         │
│  ┌────────────────────────────────────────────────────┐  │
│  │         OrderCreatedListener                       │  │
│  │  (Consome mensagens da fila RabbitMQ)              │  │
│  └────────────────────────────────────────────────────┘  │
│                       │                                  │
│                       ▼                                  │
│  ┌────────────────────────────────────────────────────┐  │
│  │         OrderService                               │  │
│  │  (Processa e salva pedidos)                        │  │
│  └────────────────────────────────────────────────────┘  │
│                       │                                  │
│                       ▼                                  │
│  ┌────────────────────────────────────────────────────┐  │
│  │         OrderRepository (MongoDB)                  │  │
│  │  (Persistência de dados)                           │  │
│  └────────────────────────────────────────────────────┘  │
│                                                          │
└──────────────────────────────────────────────────────────┘
                       │
                       │ Consulta
                       ▼
┌──────────────────────────────────────────────────────────┐
│         API REST - OrderController                       │
│  GET /customers/{customerId}/orders                      │
│  (Consulta pedidos com paginação)                        │
└──────────────────────────────────────────────────────────┘
```

## 🚀 Tecnologias

| Tecnologia | Versão |
|-----------|--------|
| Java | 17 |
| Spring Boot | 3.4.4 |
| Spring Data MongoDB | - |
| Spring AMQP (RabbitMQ) | - |
| Lombok | - |
| JUnit 5 | - |
| Docker | - |

## ✨ Funcionalidades

- ✅ Consome eventos JSON da fila RabbitMQ
- ✅ Calcula total do pedido automaticamente (`quantidade × preço`)
- ✅ Persiste em MongoDB com índices para performance
- ✅ API REST com paginação para listar pedidos por cliente
- ✅ Calcula total consolidado de todos os pedidos do cliente
- ✅ Logging automático e tratamento de erros

## 📊 Modelo de Dados

### Mensagem RabbitMQ Consumida

```json
{
  "codigoPedido": 1001,
  "codigoCliente": 1,
  "itens": [
    {"produto": "lápis", "quantidade": 100, "preco": 1.10},
    {"produto": "caderno", "quantidade": 10, "preco": 1.00}
  ]
}
```

### MongoDB - Coleção `tb_order`

```javascript
{
  "_id": Long,                    // ID do Pedido
  "customerId": Long,             // ID do Cliente (indexado)
  "total": Decimal128,            // Valor total
  "items": [
    {
      "product": String,
      "quantity": Integer,
      "price": Decimal128
    }
  ]
}
```

## 🔌 API REST

### GET /customers/{customerId}/orders

Retorna pedidos paginados e total consolidado do cliente.

**Parâmetros:**
```
customerId: Long (path) - ID do cliente
page: Integer (query) - Página [padrão: 0]
pageSize: Integer (query) - Itens por página [padrão: 10]
```

**Resposta:**
```json
{
  "sumary": {
    "totalOnOrders": 1150.00
  },
  "data": [
    {
      "orderId": 1001,
      "custumerId": 1,
      "total": 150.00
    }
  ],
  "pagination": {
    "page": 0,
    "pageSize": 10,
    "totalElements": 2,
    "totalPages": 1
  }
}
```

## 🚀 Como Usar

### Pré-requisitos
- Java 17+
- Maven 3.6+
- Docker & Docker Compose

### Iniciar com Docker Compose

```bash
cd springboot_desafio_btg-pactual
docker-compose -f local/docker-compose.yaml up -d
```

Serviços iniciados:
- MongoDB: `localhost:27017`
- RabbitMQ Management: `localhost:15672` (guest/guest)
- Aplicação: `localhost:8080`

### Build Local

```bash
mvn clean install
mvn clean package -DskipTests
```

### Executar

```bash
# Opção 1: Maven
mvn spring-boot:run

# Opção 2: JAR
java -jar target/btg-pactual-0.0.1-SNAPSHOT.jar
```

### Testes

```bash
mvn test                                # Testes unitários
mvn test jacoco:report                  # Com cobertura
mvn pitest:mutationCoverage             # Mutation testing
```

## 📌 Exemplos

### Enviar Pedido para RabbitMQ

Acesse `http://localhost:15672` (guest/guest) e envie para fila `btg-pactual-order-queue`:

```json
{
  "codigoPedido": 1001,
  "codigoCliente": 1,
  "itens": [
    {"produto": "Mouse", "quantidade": 2, "preco": 50.00},
    {"produto": "Teclado", "quantidade": 1, "preco": 150.00}
  ]
}
```

### Consultar Pedidos

```bash
curl -X GET "http://localhost:8080/customers/1/orders?page=0&pageSize=10"
```

**Postman/Insomnia:**
- Método: GET
- URL: `http://localhost:8080/customers/1/orders`
- Query params: `page=0&pageSize=10`

## 📁 Estrutura do Projeto

```
src/main/java/com_springboot_/btg_pactual/
├── BtgPactualApplication.java
├── config/
│   └── RabbitMqConfig.java
├── controller/
│   ├── OrderController.java
│   └── dto/
│       ├── ApiResponse.java
│       ├── OrderResponse.java
│       └── PaginationResponse.java
├── entity/
│   ├── OrderEntity.java
│   └── OrderItem.java
├── listener/
│   ├── OrderCreatedListener.java
│   └── dto/
│       ├── OrderCreatedEvent.java
│       └── OrderItemEvent.java
├── repository/
│   └── OrderRepository.java
└── service/
    └── OrderService.java
```

## 🔍 Componentes Principais

### OrderController
- Expõe endpoint GET `/customers/{id}/orders`
- Retorna pedidos paginados e total consolidado

### OrderCreatedListener
- Escuta fila RabbitMQ `btg-pactual-order-queue`
- Consome eventos JSON e delega ao Service

### OrderService
- `save()` - Processa evento e persiste no MongoDB
- `findAllByCustumerId()` - Busca paginada de pedidos
- `findTotalOnOrdersByCustomerId()` - Total consolidado com agregação

### OrderEntity
- Mapeamento da coleção `tb_order`
- Índice em `customerId` para performance
- BigDecimal para valores monetários

### RabbitMqConfig
- Declara fila `btg-pactual-order-queue`
- Configura serialização JSON (Jackson)

## 📡 Configuração

**application.properties:**
```properties
spring.application.name=btg-pactual
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.database=btg-pactual-database
spring.data.mongodb.auto-index-creation=true
```

**Variáveis de ambiente:**
```bash
export MONGO_HOST=seu-host
export MONGO_PORT=27017
export MONGO_DATABASE=seu-banco
```

### MongoDB 
```bash
docker ps | grep mongo
docker-compose -f local/docker-compose.yaml restart mongodb
```

### RabbitMQ 
```bash
# Acessar: http://localhost:15672 (guest/guest)
docker ps | grep rabbitmq
```

### Aplicação não inicia
```bash
mvn spring-boot:run --debug
netstat -ano | findstr :8080  # Windows - porta 8080 em uso?
```
