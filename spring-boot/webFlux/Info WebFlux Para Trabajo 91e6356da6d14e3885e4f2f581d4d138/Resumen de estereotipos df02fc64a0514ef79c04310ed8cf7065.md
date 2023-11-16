# Resumen de estereotipos

Los estereotipos de Spring son anotaciones que se utilizan para simplificar la configuración de la aplicación y reducir la cantidad de código repetitivo. Aquí hay un ejemplo de cómo utilizar los estereotipos de Spring en un proyecto:

Ejemplo:

```java
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
```