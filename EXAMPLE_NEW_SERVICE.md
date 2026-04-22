# Exemplo: Criando um Novo Serviço Consumer

Este guia mostra como criar um novo microserviço que consome o evento `order.created`.

## Estrutura de Exemplo

```
services/notification-service/        # Exemplo de serviço que consome order.created
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/com/tradecards/notificationservice/
│   │   │   ├── NotificationServiceApplication.java
│   │   │   ├── consumers/
│   │   │   │   └── OrderCreatedConsumer.java
│   │   │   └── services/
│   │   │       └── NotificationService.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/tradecards/notificationservice/
└── README.md
```

## Exemplo de pom.xml

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
    
    <artifactId>notification-service</artifactId>
    <packaging>jar</packaging>
    
    <name>Notification Service</name>
    <description>Service that consumes order.created events and sends notifications</description>
    
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
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-webmvc</artifactId>
        </dependency>
        
        <!-- Lombok -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        
        <!-- Test Dependencies -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-kafka-test</artifactId>
            <scope>test</scope>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-webmvc-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
    
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

## Exemplo de Consumer

```java
package com.tradecards.notificationservice.consumers;

import com.tradecards.shared.events.OrderCreatedEvent;
import com.tradecards.notificationservice.services.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCreatedConsumer {
    
    private final NotificationService notificationService;
    
    @KafkaListener(
        topics = "order.created", 
        groupId = "notification-service-group",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(OrderCreatedEvent event) {
        log.info("Received OrderCreatedEvent: {}", event);
        
        try {
            notificationService.sendOrderNotification(event);
            log.info("Successfully processed order notification for order ID: {}", event.getId());
        } catch (Exception e) {
            log.error("Error processing order notification for order ID: {}", event.getId(), e);
            // Aqui você pode implementar lógica de retry ou dead letter queue
        }
    }
}
```

## Exemplo de Service

```java
package com.tradecards.notificationservice.services;

import com.tradecards.shared.events.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationService {
    
    public void sendOrderNotification(OrderCreatedEvent event) {
        // Implementar lógica de envio de notificação
        log.info("Sending notification for order {} to user {}", 
                 event.getId(), event.getUserId());
        
        // Exemplo: enviar email, SMS, push notification, etc.
        String message = String.format(
            "Your order #%d for amount %.2f has been created successfully!",
            event.getId(), 
            event.getAmount()
        );
        
        log.info("Notification message: {}", message);
        
        // Aqui você implementaria a integração real com serviços de notificação
        // Por exemplo: SendGrid, Twilio, Firebase Cloud Messaging, etc.
    }
}
```

## Exemplo de application.properties

```properties
# Application name
spring.application.name=notification-service

# Server Configuration
server.port=8082

# Kafka Consumer Configuration
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.consumer.group-id=notification-service-group
spring.kafka.consumer.auto-offset-reset=earliest
spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.consumer.value-deserializer=org.springframework.kafka.support.serializer.JsonDeserializer
spring.kafka.consumer.properties.spring.json.trusted.packages=*

# Kafka Consumer Error Handling
spring.kafka.consumer.enable-auto-commit=false
spring.kafka.listener.ack-mode=manual

# Logging
logging.level.com.tradecards.notificationservice=DEBUG
logging.level.org.apache.kafka=INFO
```

## Exemplo de Application Class

```java
package com.tradecards.notificationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
public class NotificationServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }
}
```

## Exemplo de Kafka Configuration

```java
package com.tradecards.notificationservice.config;

import com.tradecards.shared.events.OrderCreatedEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {
    
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;
    
    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;
    
    @Bean
    public ConsumerFactory<String, OrderCreatedEvent> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, OrderCreatedEvent.class.getName());
        
        return new DefaultKafkaConsumerFactory<>(
            props,
            new StringDeserializer(),
            new JsonDeserializer<>(OrderCreatedEvent.class)
        );
    }
    
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderCreatedEvent> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, OrderCreatedEvent> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
```

## Passos para Adicionar o Serviço

1. **Criar a estrutura de pastas:**
   ```bash
   mkdir -p services/notification-service/src/main/java/com/tradecards/notificationservice/{consumers,services,config}
   mkdir -p services/notification-service/src/main/resources
   mkdir -p services/notification-service/src/test/java/com/tradecards/notificationservice
   ```

2. **Criar os arquivos acima**

3. **Adicionar módulo ao pom.xml parent:**
   ```xml
   <modules>
       <module>shared/events</module>
       <module>services/order-service</module>
       <module>services/notification-service</module>
   </modules>
   ```

4. **Compilar:**
   ```bash
   mvn clean install
   ```

5. **Executar:**
   ```bash
   cd services/notification-service
   mvn spring-boot:run
   ```

## Testando a Integração

1. Inicie a infraestrutura:
   ```bash
   docker-compose up -d
   ```

2. Inicie o order-service (porta 8081)

3. Inicie o notification-service (porta 8082)

4. Crie um pedido:
   ```bash
   curl -X POST http://localhost:8081/api/orders \
     -H "Content-Type: application/json" \
     -d '{"userId": 1, "amount": 100.50}'
   ```

5. Verifique os logs do notification-service - você deve ver a mensagem de notificação sendo processada!

6. Verifique o Kafka UI em `http://localhost:8080` para ver o evento no tópico `order.created`
