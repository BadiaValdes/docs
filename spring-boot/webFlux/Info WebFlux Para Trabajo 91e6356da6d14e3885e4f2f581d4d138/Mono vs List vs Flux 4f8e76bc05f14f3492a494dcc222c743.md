# Mono vs List vs Flux

# Mono vs List vs Flux

En WebFlux, existen tres tipos de estructuras de datos fundamentales para manejar flujos de datos de manera asíncrona: Mono, List y Flux.

## Mono

Mono es una estructura de datos que representa un flujo de datos que consta de cero o un solo elemento. Es útil para operaciones que devuelven solo un resultado, como una consulta de base de datos o una llamada a una API externa.

Ejemplo de código que utiliza Mono:

```
public Mono<User> getUserById(String id) {
   return userRepository.findById(id);
}

```

En este ejemplo, `userRepository.findById(id)` devuelve un Mono que contiene un solo elemento de tipo `User`.

## List

List es una estructura de datos que representa un flujo de datos que consta de una lista de elementos. Es útil para operaciones que devuelven múltiples resultados, como una consulta de base de datos que devuelve varios registros.

Ejemplo de código que utiliza List:

```
public Flux<User> getAllUsers() {
   return userRepository.findAll();
}

```

En este ejemplo, `userRepository.findAll()` devuelve un Flux que contiene una lista de elementos de tipo `User`.

## Flux

Flux es una estructura de datos que representa un flujo de datos que consta de cero o más elementos. Es útil para operaciones que devuelven múltiples resultados, como una transmisión de eventos de tiempo real o una consulta de base de datos que devuelve una gran cantidad de registros.

Ejemplo de código que utiliza Flux:

```
public Flux<Notification> getNotifications() {
   return notificationService.getNotifications();
}

```

En este ejemplo, `notificationService.getNotifications()` devuelve un Flux que contiene una secuencia de elementos de tipo `Notification`.

Es importante elegir la estructura de datos adecuada para cada operación para garantizar un rendimiento óptimo y un código limpio y legible.

### Mono Examples

### Mono with Nested Mono

```
public Mono<String> getUserNameById(String id) {
    return userRepository.findById(id)
            .flatMap(user -> Mono.just(user.getName()));
}

```

In this example, `findById()` returns a `Mono<User>` object. We use the `flatMap()` operator to transform the `Mono<User>` to a `Mono<String>` that contains the user's name.

### Mono with Error Handling

```
public Mono<User> getUserById(String id) {
    return userRepository.findById(id)
            .switchIfEmpty(Mono.error(new UserNotFoundException()));
}

```

In this example, if `findById()` returns an empty `Mono`, we use the `switchIfEmpty()` operator to throw a custom exception.

### Flux Examples

### Flux with Nested Flux

```
public Flux<String> getAllUserNames() {
    return userRepository.findAll()
            .flatMap(user -> Flux.just(user.getName()));
}

```

In this example, `findAll()` returns a `Flux<User>` object. We use the `flatMap()` operator to transform each `User` object to a `Flux<String>` that contains the user's name.

### Flux with Filtering

```
public Flux<User> getUsersByAge(int age) {
    return userRepository.findAll()
            .filter(user -> user.getAge() == age);
}

```

In this example, we use the `filter()` operator to return only the users that match the specified age.

### List Examples

### List with Mapping

```
public List<String> getAllUserNames() {
    List<User> users = userRepository.findAll();
    return users.stream()
            .map(User::getName)
            .collect(Collectors.toList());
}

```

In this example, we use the `map()` operator to transform each `User` object to a `String` that contains the user's name. We then collect the transformed objects into a `List<String>`.

### List with Sorting

```
public List<User> getUsersSortedByName() {
    List<User> users = userRepository.findAll();
    users.sort(Comparator.comparing(User::getName));
    return users;
}

```

In this example, we use the `sort()` method to sort the `List<User>` by the user's name.