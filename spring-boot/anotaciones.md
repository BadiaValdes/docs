# Named Query

Nos permite declarar a nivel de clase una consulta (Query) que evite la repetición de datos. Ejemplo:

`@NamedQuery(name = "discounted-products", query = "select product from Product product where product.discounted IS NOT NULL")`

# Deferencia entre @Autowired, @Resource y @Inject 
(https://www.baeldung.com/spring-annotations-resource-inject-autowire)

- **@Resource**
    - (By name) Se utiliza para buscar recursos como los beans. El valor a utilizar es el mismo nombre que se le haya asignado al `@Bean`
    - (By Type) La segunda opción es mediante tipo y su acción es buscar un `@Bean` con el mismo tipo de dato.
    - (By Qualifier) Cuando tengamos más de un bean podemos utilizar el qualifier para seleccionar uno en específico.

- **@Inject**
    - 
