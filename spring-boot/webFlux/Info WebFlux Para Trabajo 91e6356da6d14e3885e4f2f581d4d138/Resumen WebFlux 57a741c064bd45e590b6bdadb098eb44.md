# Resumen WebFlux

WebFlux es un módulo de Spring que permite la programación reactiva en el servidor. Esto significa que los servidores construidos con WebFlux pueden manejar eficientemente grandes volúmenes de solicitudes utilizando el modelo de concurrencia basado en hilos Netty. En lugar de utilizar el modelo de subprocesos por solicitud, WebFlux utiliza un número limitado de subprocesos para manejar varias solicitudes. Esto permite que el servidor sea escalable y responda a un mayor número de solicitudes.

WebFlux proporciona dos tipos de API: la API basada en anotaciones y la API funcional. La API basada en anotaciones utiliza anotaciones como `@Controller` y `@RequestMapping` para definir los puntos finales de la API. La API funcional utiliza funciones lambda para definir los puntos finales de la API.

Un ejemplo de un controlador WebFlux podría verse así:

```java
@RestController
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public Flux<User> getAllUsers() {
        return userRepository.findAll();
    }
}

```

En este ejemplo, el controlador tiene un método `getAllUsers()` que devuelve un `Flux` de usuarios. El método `findAll()` del repositorio de usuarios devuelve un `Flux` de usuarios que se emitirán a medida que estén disponibles. El controlador utiliza la API basada en anotaciones y la anotación `@GetMapping` para definir el punto final `/users`.

Un ejemplo más complejo de WebFlux podría incluir la integración con otras tecnologías, como MongoDB o WebSockets. Por ejemplo, un controlador que utiliza WebSockets podría verse así:

```java
@RestController
public class WebSocketController {
    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public WebSocketController(WebClient webClient, ObjectMapper objectMapper) {
        this.webClient = webClient;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/messages")
    public Flux<String> getMessages() {
        return webClient.execute(
            WebSocketClientTransport.create(webClient)
                .uri("ws://example.com/messages"),
            session -> session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .map(payload -> {
                    try {
                        return objectMapper.readValue(payload, String.class);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
        );
    }
}

```

En este ejemplo, el controlador utiliza un cliente HTTP reactiva (`WebClient`) para conectarse a un servidor WebSocket en `ws://example.com/messages`. El controlador utiliza la API basada en anotaciones y la anotación `@GetMapping` para definir el punto final `/messages`. Cuando un cliente se conecta a este punto final, el controlador establece una conexión WebSocket con el servidor y devuelve un `Flux` de mensajes recibidos. Los mensajes son decodificados utilizando un objeto `ObjectMapper` antes de ser emitidos en el `Flux` de salida.

En resumen, WebFlux es un módulo de Spring que permite la programación reactiva en el servidor. Proporciona una API basada en anotaciones y una API funcional para definir puntos finales de API. WebFlux es escalable y eficiente en el manejo de grandes volúmenes de solicitudes.