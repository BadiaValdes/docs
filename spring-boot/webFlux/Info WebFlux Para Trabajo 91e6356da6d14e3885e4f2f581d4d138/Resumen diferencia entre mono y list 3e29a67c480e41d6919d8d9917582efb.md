# Resumen diferencia entre mono y list

Mono y List son dos tipos de colecciones utilizados en Webflux. La principal diferencia es que Mono representa un valor único que puede ser emitido de manera asíncrona, mientras que List representa una colección de valores que se emiten de manera síncrona.

Ejemplo:

```java
Mono<String> greetingMono = Mono.just("Hello, world!");

List<String> greetingList = Arrays.asList("Hello", "world!");
```