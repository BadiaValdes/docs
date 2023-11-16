# Resumen comunicación entre MS

Para enviar mensajes de un microservicio a otro a través de Kafka, se necesitan dos componentes principales: un productor y un consumidor. Aquí hay un ejemplo de cómo enviar un mensaje de un microservicio a otro utilizando Kafka:

En el microservicio del productor:

```java
@Autowired
private KafkaTemplate<String, String> kafkaTemplate;

public void sendMessage(String message) {
    kafkaTemplate.send("topic-name", message);
}
```

En el microservicio del consumidor:

```java
@KafkaListener(topics = "topic-name", groupId = "group-id")
public void receiveMessage(String message) {
    // Procesar el mensaje recibido
}
```