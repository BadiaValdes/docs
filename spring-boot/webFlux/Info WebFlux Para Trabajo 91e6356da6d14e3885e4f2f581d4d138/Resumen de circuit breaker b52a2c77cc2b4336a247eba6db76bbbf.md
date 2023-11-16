# Resumen de circuit breaker

El esquema circuit breaker es un patrón de diseño que se utiliza para prevenir fallos en sistemas distribuidos. Consiste en interrumpir temporalmente el flujo de datos entre un servicio cliente y un servicio servidor cuando se detecta una sobrecarga o un fallo en el servidor. Esto permite evitar que se propaguen los fallos a otros componentes del sistema, y permite recuperarse de manera más rápida.

Ejemplo:

```java
CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults("backend");

Mono<String> result = circuitBreaker.run(Mono.fromSupplier(() -> {
    return callBackendService();
}), throwable -> {
    return Mono.just("Default Response");
});
```