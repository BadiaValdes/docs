# Resumen Test Unitarios

Para realizar pruebas unitarias en Java, se suelen utilizar frameworks como JUnit y Mockito. Estos frameworks proporcionan funciones para crear y ejecutar pruebas de manera automatizada, y para simular el comportamiento de objetos y métodos utilizando objetos mock.

Ejemplo:

```java
@Test
public void testAdd() {
    Calculator calculator = new Calculator();
    int result = calculator.add(2, 3);
    assertEquals(5, result);
}

@Mock
private UserRepository userRepository;

@InjectMocks
private UserController userController;

@Before
public void setup() {
    MockitoAnnotations.initMocks(this);
}

@Test
public void testGetAllUsers() {
    when(userRepository.findAll()).thenReturn(Flux.just(new User("John"), new User("Mary")));
    Flux<User> result = userController.getAllUsers();
    StepVerifier.create(result)
        .expectNext(new User("John"))
        .expectNext(new User("Mary"))
        .verifyComplete();
}
```