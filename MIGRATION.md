# Migração para Monorepo - Resumo

## 📋 O Que Foi Feito

Este repositório foi convertido de um projeto único (order-service) para uma arquitetura de **monorepo Maven** que suporta múltiplos microserviços.

## 🎯 Motivação

O projeto agora suporta múltiplos serviços que se comunicam via eventos Kafka. Outros serviços precisam consumir o evento `order.created` publicado pelo order-service.

## 🏗️ Estrutura Anterior vs Nova

### Antes
```
orders-spring-boot/
├── src/
├── pom.xml
├── docker-compose.yml
└── README.md
```

### Depois
```
orders-spring-boot/
├── pom.xml                          # POM parent/agregador
├── docker-compose.yml               # Infraestrutura compartilhada
├── README.md                        # Documentação do monorepo
├── EXAMPLE_NEW_SERVICE.md           # Guia para adicionar novos serviços
│
├── shared/                          # Módulos compartilhados
│   └── events/                      # Eventos Kafka
│       ├── pom.xml
│       └── src/
│           └── main/java/com/tradecards/shared/events/
│               └── OrderCreatedEvent.java
│
└── services/                        # Microserviços
    └── order-service/               # Serviço de pedidos (migrado)
        ├── pom.xml
        ├── src/
        ├── HELP.md
        └── README_ARCHITECTURE.md
```

## 🔄 Mudanças Realizadas

### 1. Criação da Estrutura de Monorepo
- ✅ Criado POM parent na raiz (`tradecards-parent`)
- ✅ Movido order-service para `services/order-service/`
- ✅ Criado módulo `shared/events` para eventos compartilhados

### 2. Módulo Shared/Events
- ✅ Criado `OrderCreatedEvent` compartilhado
- ✅ Configurado com Jackson para serialização JSON
- ✅ Usa Lombok para reduzir boilerplate
- ✅ Inclui campos adicionais: `timestamp` e `eventId`

### 3. Atualização do Order-Service
- ✅ POM atualizado para usar parent `tradecards-parent`
- ✅ Dependência adicionada ao módulo `shared/events`
- ✅ Imports atualizados para usar `com.tradecards.shared.events.OrderCreatedEvent`
- ✅ Removido `OrderCreatedEvent` duplicado local

### 4. Docker Compose
- ✅ Adicionada rede compartilhada (`tradecards-network`)
- ✅ Comentários explicativos para adicionar novos serviços
- ✅ Organização melhorada dos serviços de infraestrutura

### 5. Documentação
- ✅ README.md completo com instruções de uso do monorepo
- ✅ EXAMPLE_NEW_SERVICE.md com exemplo de consumer service
- ✅ Guia passo a passo para adicionar novos serviços
- ✅ README_ARCHITECTURE.md movido para services/order-service/

## 📦 Módulos do Maven

```xml
<modules>
    <module>shared/events</module>      <!-- Build primeiro -->
    <module>services/order-service</module>
</modules>
```

### Ordem de Build
1. **shared/events** - Compilado primeiro (dependência dos serviços)
2. **services/order-service** - Depende de shared/events

## 🚀 Como Usar

### Compilar Tudo
```bash
mvn clean install
```

### Compilar Apenas um Módulo
```bash
mvn clean install -pl shared/events
mvn clean install -pl services/order-service -am  # -am compila dependências
```

### Executar Order-Service
```bash
cd services/order-service
mvn spring-boot:run
```

## ➕ Adicionar Novo Serviço

1. Criar estrutura em `services/novo-servico/`
2. Criar `pom.xml` com parent `tradecards-parent`
3. Adicionar dependência ao módulo `shared/events`
4. Adicionar módulo ao POM parent
5. Implementar consumer Kafka para `order.created`

Consulte [EXAMPLE_NEW_SERVICE.md](EXAMPLE_NEW_SERVICE.md) para exemplo completo.

## 🎨 Padrões e Convenções

### Nomenclatura de Módulos
- **shared/[nome]**: Módulos compartilhados (events, utils, etc.)
- **services/[nome]-service**: Microserviços

### Portas
- 8081: order-service
- 8082+: Próximos serviços
- 8080: Kafka UI
- 5432: PostgreSQL
- 9092: Kafka

### Eventos Kafka
- Formato: `{Entity}{Action}Event` (ex: OrderCreatedEvent)
- Tópicos: `{entity}.{action}` (ex: order.created)
- Todos os eventos em `shared/events`

## 🧪 Testes

```bash
# Testar tudo
mvn test

# Testar módulo específico
mvn test -pl services/order-service
```

## 🔍 Verificação da Migração

### Build Bem-Sucedido ✅
```
[INFO] Reactor Summary for TradeCArds Microservices 0.0.1-SNAPSHOT:
[INFO] 
[INFO] TradeCArds Microservices ........................... SUCCESS
[INFO] Shared Events ...................................... SUCCESS
[INFO] Order Service ...................................... SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
```

### Funcionalidades Mantidas ✅
- ✅ Order-service continua funcionando normalmente
- ✅ Endpoints REST mantidos
- ✅ Publicação de eventos Kafka funcional
- ✅ Conexão com PostgreSQL mantida
- ✅ Flyway migrations funcionando

## 📝 Próximos Passos Sugeridos

1. **Adicionar Notification Service**
   - Consumir eventos `order.created`
   - Enviar notificações por email/SMS
   - Porta: 8082

2. **Adicionar Inventory Service**
   - Consumir eventos `order.created`
   - Atualizar estoque de produtos
   - Porta: 8083

3. **Adicionar Payment Service**
   - Consumir eventos `order.created`
   - Processar pagamentos
   - Publicar eventos `payment.processed`
   - Porta: 8084

4. **Melhorias na Infraestrutura**
   - Adicionar Eureka (Service Discovery)
   - Adicionar API Gateway
   - Adicionar Config Server
   - Adicionar Distributed Tracing (Zipkin/Jaeger)

## 📚 Recursos

- [README.md](README.md) - Documentação principal
- [EXAMPLE_NEW_SERVICE.md](EXAMPLE_NEW_SERVICE.md) - Exemplo de novo serviço
- [services/order-service/README_ARCHITECTURE.md](services/order-service/README_ARCHITECTURE.md) - Arquitetura do Order Service

## 🤝 Contribuindo

Ao adicionar novos serviços:
1. Siga a estrutura de pastas estabelecida
2. Use o módulo `shared/events` para eventos Kafka
3. Configure porta única no application.properties
4. Adicione documentação no README do serviço
5. Atualize o POM parent

## ✅ Checklist de Migração

- [x] Estrutura de monorepo criada
- [x] POM parent configurado
- [x] Módulo shared/events criado
- [x] Order-service migrado e funcionando
- [x] Docker Compose atualizado
- [x] Documentação completa
- [x] Build bem-sucedido
- [x] Exemplo de novo serviço documentado

---

**Migração concluída com sucesso!** 🎉

O repositório está pronto para receber novos microserviços que consumirão o evento `order.created`.
