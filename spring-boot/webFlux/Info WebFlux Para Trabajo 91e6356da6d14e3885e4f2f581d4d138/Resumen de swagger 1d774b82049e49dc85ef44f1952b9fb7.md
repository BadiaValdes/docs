# Resumen de swagger

Swagger es una herramienta que se utiliza para documentar APIs de manera automática. Permite generar documentación interactiva a partir del código fuente, y proporciona una interfaz de usuario para probar y explorar las APIs.

Ejemplo:

```java
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.any())
            .paths(PathSelectors.any())
            .build();
    }
}
```