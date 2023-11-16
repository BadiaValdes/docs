# Manejo de errores webFlux

Para los que han trabajado con Spring Boot, el manejo de errores con WebFlux no le será muy difícil; no solo porque es similar al mencionado anteriormente, sino también intuitivo a la hora de utilizar. Comencemos, para aquellos que no saben como declarar un error con java, creando un error personalizado:

```java
public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException(Long id) {
        super("Post:" + id +" is not found.");
    }
}
```

El código anterior define un error llamado `PostNotFoundException` que extiende del error `RuntimeException`. De esta forma, es posible declarar una serie de errores que pueden heredar de distintas excepciones. Ahora, veremos como utilizar estos errores dentro de WebFlux; ojo, estos son errores para ser lanzados por las funciones:

```java
@GetMapping(value = "/{id}")
public Mono<Post> get(@PathVariable(value = "id") Long id) {
    return this.posts.findById(id).switchIfEmpty(Mono.error(new PostNotFoundException(id)));
}
```

De esta forma estamos diciendo que si no es posible encontrar por id el objeto, se lance un error mediante `Mono` (devuelve un solo valor). Ahora, si queremos manejar todos los errores que sean lanzados de forma global, podemos utilizar el decorador `RestControllerAdvice` sobre una clase externa. 

Dicha clase tendrá como único objetivo manejar todos los datos que son enviados mediante la aplicación y decidir que devolver según el error capturado:

```java
@RestControllerAdvice
@Slf4j
class RestExceptionHandler {
    @ExceptionHandler(PostNotFoundException.class)
    ResponseEntity postNotFound(PostNotFoundException ex) {
        log.debug("handling exception::" + ex);
        return notFound().build();
    }
}
```

El controlador global de excepciones solo es valido (de esta forma) si estamos utilizando un acercamiento Web en nuestro código. En caso de utilizar el `RouterFunction`, se hace necesario modificar la estructura del controlador:

```java
@Bean
public WebExceptionHandler exceptionHandler() {
    return (ServerWebExchange exchange, Throwable ex) -> {
        if (ex instanceof PostNotFoundException) {
            exchange.getResponse().setStatusCode(HttpStatus.NOT_FOUND);
            return exchange.getResponse().setComplete();
        }
        return Mono.error(ex);
    };
}
}
```

## OnError

Aunque los ejemplos anteriores son útiles para el trabajo con errores dentro de webFlux, no son la única forma que tenemos para lograr este cometido. Uno de estos métodos es el uso de la función ***`onErrorReturn`,*** que nos permite devolver valores estáticos cuando ocurra un error:****

```java
public Mono<ServerResponse> handleRequest(ServerRequest request) {
    return sayHello(request)
      .onErrorReturn("Hello Stranger")
      .flatMap(s -> ServerResponse.ok()
        .contentType(MediaType.TEXT_PLAIN)
        .bodyValue(s));
}
```

Seguido, tenemos el ***`onErrorResume`,*** que permite decidir que curso de acción tomar cuando ocurra un error; entre estas acciones se encuentran la capacidad de:****

- Devolver un valor de error. Similar al caso anterior, pero se devolvería otro `Flux` o `Mono`.

```java
public Mono<ServerResponse> handleRequest(ServerRequest request) {
    return sayHello(request)
      .flatMap(s -> ServerResponse.ok()
        .contentType(MediaType.TEXT_PLAIN)
        .bodyValue(s))
      .onErrorResume(e -> Mono.just("Error " + e.getMessage())
        .flatMap(s -> ServerResponse.ok()
          .contentType(MediaType.TEXT_PLAIN)
          .bodyValue(s)));
}
```

- Tomar una ruta distinta mediante el llamado a otra función. Dicha función debe ser `Flux` o `Mono`.

```java
public Mono<ServerResponse> handleRequest(ServerRequest request) {
    return sayHello(request)
      .flatMap(s -> ServerResponse.ok()
        .contentType(MediaType.TEXT_PLAIN)
        .bodyValue(s))
      .onErrorResume(e -> sayHelloFallback()
        .flatMap(s -> ServerResponse.ok()
        .contentType(MediaType.TEXT_PLAIN)
        .bodyValue(s)));
}
```

- Capturar y lanzar otro error.

```java
public Mono<ServerResponse> handleRequest(ServerRequest request) {
    return ServerResponse.ok()
      .body(sayHello(request)
      .onErrorResume(e -> Mono.error(new NameRequiredException(
        HttpStatus.BAD_REQUEST,
        "username is required", e))), String.class);
}
```

## Bibliografia

[https://hantsy.github.io/spring-reactive-sample/web/exception.html](https://hantsy.github.io/spring-reactive-sample/web/exception.html)

[https://www.baeldung.com/spring-webflux-errors](https://www.baeldung.com/spring-webflux-errors)

[https://medium.com/codex/spring-web-and-webflux-exception-handling-best-practices-b2c3cd7e3acf](https://medium.com/codex/spring-web-and-webflux-exception-handling-best-practices-b2c3cd7e3acf)

[https://github.com/artemptushkin/spring-web-exception-handling](https://github.com/artemptushkin/spring-web-exception-handling)