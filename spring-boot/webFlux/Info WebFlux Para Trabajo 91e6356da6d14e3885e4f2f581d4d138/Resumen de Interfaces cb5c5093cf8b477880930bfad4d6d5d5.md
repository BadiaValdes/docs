# Resumen de Interfaces

Las interfaces se utilizan para definir un conjunto de métodos que deben ser implementados por una clase concreta. Se suelen utilizar para definir contratos que deben ser implementados por diferentes clases, permitiendo una mayor flexibilidad y modularidad en el diseño de las aplicaciones.

Ejemplo:

```java
public interface UserRepository {
    Mono<User> findById(String id);
    Flux<User> findAll();
}

public class InMemoryUserRepository implements UserRepository {
    private List<User> users = new ArrayList<>();

    public Mono<User> findById(String id) {
        return Mono.justOrEmpty(users.stream()
            .filter(user -> user.getId().equals(id))
            .findFirst()
            .orElse(null));
    }

    public Flux<User> findAll() {
        return Flux.fromIterable(users);
    }
}
```