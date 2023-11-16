# Resumen de inversión de control

La inversión de control es un patrón de diseño que se utiliza para reducir la dependencia entre los componentes de una aplicación. En lugar de que un componente dependa directamente de otro, se utiliza una capa intermedia que se encarga de la comunicación entre los componentes. Aquí hay un ejemplo de cómo utilizar la inversión de control en un proyecto:

Ejemplo:

```java
@Component
public class UserComponent {
    @Autowired
    private UserService userService;

    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
}
```