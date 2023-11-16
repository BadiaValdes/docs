# Resumen Manejo de excepciones webflux

## IA

En Webflux, se utilizan los operadores onErrorResume y onErrorReturn para manejar excepciones de manera asincrónica. Estos operadores permiten definir un comportamiento específico que se debe ejecutar en caso de que se produzca una excepción.

Ejemplo:

```java
@GetMapping("/users/{id}")
public Mono<User> getUserById(@PathVariable("id") String id) {
    return userRepository.findById(id)
        .switchIfEmpty(Mono.error(new UserNotFoundException()));
}

@ControllerAdvice
public class UserControllerAdvice {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.notFound().build();
    }
}
```