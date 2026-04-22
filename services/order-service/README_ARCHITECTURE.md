# Order Service - Microsserviço de Ordens

## Arquitetura Limpa (Clean Architecture)

Este microsserviço foi estruturado seguindo os princípios de Clean Architecture, separando as responsabilidades em camadas bem definidas.

### Estrutura de Pastas

```
src/main/java/com/tradecards/orderservice/
│
├── domain/                          # Camada de Domínio (Core)
│   ├── entities/
│   │   └── Order.java              # Entidade de domínio
│   ├── repositories/
│   │   └── OrderRepository.java    # Interface do repositório (porta)
│   └── usecases/
│       ├── CreateOrderUseCase.java
│       ├── GetOrderUseCase.java
│       ├── ListOrdersUseCase.java
│       └── DeleteOrderUseCase.java
│
├── application/                     # Camada de Aplicação
│   ├── dtos/
│   │   ├── OrderRequest.java       # DTO de entrada
│   │   └── OrderResponse.java      # DTO de saída
│   └── services/
│       └── OrderService.java       # Orquestração dos use cases
│
├── infrastructure/                  # Camada de Infraestrutura
│   └── persistence/
│       ├── entities/
│       │   └── OrderEntity.java    # Entidade JPA
│       └── repositories/
│           ├── OrderJpaRepository.java      # Interface Spring Data JPA
│           └── OrderRepositoryImpl.java     # Implementação do repositório de domínio
│
└── presentation/                    # Camada de Apresentação
    ├── controllers/
    │   └── OrderController.java    # REST Controller
    └── exceptions/
        └── GlobalExceptionHandler.java  # Tratamento global de exceções
```

### Camadas

#### 1. **Domain Layer** (Domínio)
- Contém as regras de negócio puras
- **Entities**: Modelos de domínio (Order)
- **Repositories**: Interfaces (portas) sem implementação
- **Use Cases**: Casos de uso específicos do negócio
- Não depende de nenhuma outra camada

#### 2. **Application Layer** (Aplicação)
- Orquestra os casos de uso
- **DTOs**: Objetos de transferência de dados
- **Services**: Coordena múltiplos use cases

#### 3. **Infrastructure Layer** (Infraestrutura)
- Implementações técnicas
- **Persistence**: Acesso ao banco de dados
- **OrderEntity**: Entidade JPA com anotações do framework
- **OrderRepositoryImpl**: Implementação concreta do repositório de domínio
- Realiza a conversão entre entidades de domínio e entidades JPA

#### 4. **Presentation Layer** (Apresentação)
- Interface com o mundo externo
- **Controllers**: Endpoints REST
- **Exception Handlers**: Tratamento de erros

### Endpoints REST

- `POST /api/orders` - Criar uma ordem
- `GET /api/orders/{id}` - Buscar ordem por ID
- `GET /api/orders` - Listar todas as ordens
- `DELETE /api/orders/{id}` - Deletar uma ordem

### Banco de Dados

A tabela `orders` foi criada com apenas o campo `id` (auto-incremento).

**Migração Flyway**: `V1__create_orders_table.sql`

### Configuração

O arquivo `application.properties` contém:
- Configuração do PostgreSQL (porta 5432)
- Configuração do JPA/Hibernate
- Configuração do Flyway
- Servidor rodando na porta 8081

### Como executar

1. Iniciar os containers Docker:
```bash
docker-compose up -d
```

2. Executar a aplicação:
```bash
./mvnw spring-boot:run
```

### Próximos Passos

- Adicionar campos adicionais na entidade Order conforme necessário
- Implementar validações
- Adicionar integração com Kafka para eventos
- Implementar testes unitários e de integração
