# TradeCArds Microservices - Monorepo

Este é um monorepo Maven que contém todos os microsserviços da plataforma TradeCArds. A arquitetura é baseada em microsserviços que se comunicam via eventos Kafka.

## 📁 Estrutura do Projeto

```
tradecards-microservices/
├── pom.xml                          # Parent POM (agregador)
├── docker-compose.yml               # Infraestrutura compartilhada
├── README.md                        # Este arquivo
├── README_ARCHITECTURE.md           # Documentação da arquitetura
│
├── shared/                          # Módulos compartilhados
│   └── events/                      # Eventos Kafka compartilhados
│       ├── pom.xml
│       └── src/main/java/com/tradecards/shared/events/
│           └── OrderCreatedEvent.java
│
└── services/                        # Microsserviços
    ├── order-service/               # Serviço de pedidos
    │   ├── pom.xml
    │   ├── src/
    │   ├── HELP.md
    │   └── README.md
    │
    └── [novo-servico]/              # Adicione novos serviços aqui
```

## 🏗️ Arquitetura

### Módulos

1. **shared/events**: Contém os eventos Kafka compartilhados entre microsserviços
2. **services/order-service**: Gerencia pedidos e publica o evento `order.created`
3. **services/[novo-servico]**: Você pode adicionar novos serviços seguindo o padrão

### Comunicação entre Serviços

Os microsserviços se comunicam de forma assíncrona via Kafka:

- **order-service** → publica `order.created` no tópico `order.created`
- **[novo-servico]** → consome `order.created` e processa os pedidos

## 🚀 Como Começar

### Pré-requisitos

- Java 17+
- Maven 3.8+
- Docker & Docker Compose

### 1. Iniciar a Infraestrutura

```bash
# Inicia PostgreSQL, Kafka, Zookeeper e Kafka UI
docker-compose up -d

# Verificar os logs
docker-compose logs -f
```

**Serviços disponíveis:**
- PostgreSQL: `localhost:5432`
- Kafka: `localhost:9092`
- Kafka UI: `http://localhost:8080`
- Zookeeper: `localhost:2181`

### 2. Compilar Todos os Módulos

```bash
# Na raiz do projeto
mvn clean install
```

Isso compila:
1. `shared/events` (primeiro)
2. `services/order-service`

### 3. Executar o Order Service

```bash
# Opção 1: Via Maven
cd services/order-service
mvn spring-boot:run

# Opção 2: Via JAR
cd services/order-service
mvn clean package
java -jar target/order-service-0.0.1-SNAPSHOT.jar
```

O serviço estará disponível em: `http://localhost:8081`

### 4. Testar o Order Service

```bash
# Criar um pedido (publica evento order.created)
curl -X POST http://localhost:8081/api/orders \
  -H "Content-Type: application/json" \
  -d '{"userId": 1, "amount": 100.50}'

# Listar pedidos
curl http://localhost:8081/api/orders

# Buscar pedido por ID
curl http://localhost:8081/api/orders/1
```

### 5. Verificar Eventos no Kafka

Acesse o Kafka UI em `http://localhost:8080` e verifique o tópico `order.created`.

## 📦 Adicionando um Novo Serviço

### Passo 1: Criar a estrutura do serviço

```bash
mkdir -p services/novo-servico/src/main/java/com/tradecards/novoservico
mkdir -p services/novo-servico/src/main/resources
```

### Passo 2: Criar o pom.xml do serviço

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <parent>
        <groupId>com.tradecards</groupId>
        <artifactId>tradecards-parent</artifactId>
        <version>0.0.1-SNAPSHOT</version>
        <relativePath>../../pom.xml</relativePath>
    </parent>
    
    <artifactId>novo-servico</artifactId>
    <packaging>jar</packaging>
    
    <name>Novo Serviço</name>
    <description>Descrição do novo serviço</description>
    
    <dependencies>
        <!-- Shared Events Module -->
        <dependency>
            <groupId>com.tradecards</groupId>
            <artifactId>events</artifactId>
        </dependency>
        
        <!-- Spring Boot Starters -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-kafka</artifactId>
        </dependency>
        
        <!-- Adicione outras dependências necessárias -->
    </dependencies>
    
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

### Passo 3: Adicionar ao POM parent

Edite `pom.xml` na raiz e adicione o módulo:

```xml
<modules>
    <module>shared/events</module>
    <module>services/order-service</module>
    <module>services/novo-servico</module> <!-- NOVO -->
</modules>
```

### Passo 4: Criar um Consumer Kafka

```java
package com.tradecards.novoservico.consumers;

import com.tradecards.shared.events.OrderCreatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedConsumer {
    
    @KafkaListener(topics = "order.created", groupId = "novo-servico-group")
    public void consume(OrderCreatedEvent event) {
        System.out.println("Pedido recebido: " + event);
        // Processar o evento aqui
    }
}
```

### Passo 5: Configurar application.properties

```properties
# Server Configuration
server.port=8082  # Use uma porta diferente

# Kafka Configuration
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.consumer.group-id=novo-servico-group
spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.consumer.value-deserializer=org.springframework.kafka.support.serializer.JsonDeserializer
spring.kafka.consumer.properties.spring.json.trusted.packages=*
```

### Passo 6: Compilar e executar

```bash
# Na raiz do projeto
mvn clean install

# Executar o novo serviço
cd services/novo-servico
mvn spring-boot:run
```

## 🔧 Comandos Úteis

```bash
# Compilar apenas um módulo específico
mvn clean install -pl shared/events

# Compilar um serviço e suas dependências
mvn clean install -pl services/order-service -am

# Executar testes
mvn test

# Pular testes na compilação
mvn clean install -DskipTests

# Ver árvore de dependências
mvn dependency:tree

# Limpar tudo
mvn clean

# Executar um serviço específico
cd services/order-service
mvn spring-boot:run
```

## 📋 Módulo Shared/Events

O módulo `shared/events` contém os eventos Kafka compartilhados:

### OrderCreatedEvent

```java
public class OrderCreatedEvent {
    private Long id;           // ID do pedido
    private Long userId;       // ID do usuário
    private BigDecimal amount; // Valor do pedido
    private Instant timestamp; // Timestamp do evento
    private String eventId;    // ID único do evento
}
```

**Tópico Kafka**: `order.created`

### Adicionando Novos Eventos

1. Crie a classe do evento em `shared/events/src/main/java/com/tradecards/shared/events/`
2. Use `@Data`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor` do Lombok
3. Compile: `mvn clean install -pl shared/events`
4. Use o evento nos serviços

## 🐳 Docker Compose

### Serviços de Infraestrutura

- **Zookeeper**: Coordenação do Kafka
- **Kafka**: Message broker
- **PostgreSQL**: Banco de dados
- **Kafka UI**: Interface web para gerenciar Kafka

### Comandos Docker

```bash
# Iniciar todos os serviços
docker-compose up -d

# Parar todos os serviços
docker-compose down

# Ver logs
docker-compose logs -f [service-name]

# Reiniciar um serviço
docker-compose restart [service-name]

# Remover volumes (CUIDADO: apaga dados)
docker-compose down -v
```

## 🧪 Testes

```bash
# Executar todos os testes
mvn test

# Testar um serviço específico
cd services/order-service
mvn test

# Gerar relatório de cobertura
mvn clean test jacoco:report
```

## 📚 Documentação Adicional

- [Arquitetura do Order Service](services/order-service/README_ARCHITECTURE.md)
- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Apache Kafka Documentation](https://kafka.apache.org/documentation/)

## 🤝 Convenções

### Estrutura de Pacotes

```
com.tradecards.<servico>/
├── application/        # Camada de aplicação
│   ├── services/
│   ├── dtos/
│   └── events/        # Producers/Consumers
├── domain/            # Camada de domínio
│   ├── entities/
│   ├── repositories/
│   └── usecases/
├── infrastructure/    # Camada de infraestrutura
│   └── persistence/
└── presentation/      # Camada de apresentação
    └── controllers/
```

### Nomenclatura

- **Eventos**: `{Entity}{Action}Event` (ex: `OrderCreatedEvent`)
- **Serviços**: `{Entity}Service` (ex: `OrderService`)
- **Repositórios**: `{Entity}Repository` (ex: `OrderRepository`)
- **Controllers**: `{Entity}Controller` (ex: `OrderController`)

### Portas

- 8081: order-service
- 8082: próximo serviço
- 8080: Kafka UI
- 5432: PostgreSQL
- 9092: Kafka

## 🐛 Troubleshooting

### Erro de compilação no módulo shared

```bash
cd shared/events
mvn clean install
```

### Kafka não conecta

Verifique se o Kafka está rodando:
```bash
docker-compose ps
docker-compose logs kafka
```

### Porta já em uso

Mude a porta no `application.properties`:
```properties
server.port=8082
```

## 📝 License

[Adicione sua licença aqui]
