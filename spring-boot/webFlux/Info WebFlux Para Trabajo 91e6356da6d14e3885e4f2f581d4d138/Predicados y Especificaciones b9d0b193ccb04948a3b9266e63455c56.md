# Predicados y Especificaciones

No se puede hablar de especificaciones sin mencionar el uso de predicados. Anteriormente se vio que los predicados son la forma que se tiene de filtrar datos en una consulta; es decir, los And, Or, etc. Las especificaciones son un conjunto de criterios (predicados) que son utilizados para filtrar datos obtenidos directamente de la base de datos. De forma general, se puede decir que son filtros reutilizables y extensibles en toda la aplicación. Para lograr esto, debemos crear la interfaz Specification (concepto surgido en Domain Drive Design o DDD). La creación de esta interfaz consiste en dos líneas de código:

```java
public interface Specification<T> {
Predicate toPredicate(Root<T> root, CriteriaQuery query, CriteriaBuilder cb);
}
```

De esta forma, su uso puede ser llevado a cualquier entidad dentro de la aplicación:

```java
public CustomerSpecifications {

  public static Specification<Customer> customerHasBirthday() {
    return new Specification<Customer> {
      public Predicate toPredicate(Root<T> root, CriteriaQuery query, CriteriaBuilder cb) {
        return cb.equal(root.get(Customer_.birthday), today);
      }
    };
  }

  public static Specification<Customer> isLongTermCustomer() {
    return new Specification<Customer> {
      public Predicate toPredicate(Root<T> root, CriteriaQuery query, CriteriaBuilder cb) {
        return cb.lessThan(root.get(Customer_.createdAt), new LocalDate.minusYears(2));
      }
    };
  }
}
```

Las líneas anteriores a simple vista pueden parecer sencillas, pero te dotas formas, necesitamos mencionar el uso de cada parámetro que se está utilizando en la función:

- `Root`
    - Este funciona como el objeto que será llamado desde la base de datos. Míralo como si fuera una entrada de la base de datos que será utilizada para la comparación. Mediante este, podemos acceder a los valores del objeto que estamos manejando; ejemplo de esto está la siguiente línea: `root.get("fullName")`. En este caso, estamos accediendo al parámetro `fullName` dentro del objeto `Customer`.
- `CriteriaQuery`
    - Nos permitirá realizar operaciones propias de una base de datos o mejor dicho, operaciones SQL. Como son `equals`, `like`, `less_than`, etc. Como su nombre lo indica si lo traducimos, sería el criterio a utilizar para comparar.
    - Este vendría siendo el predicado a utilizar para la consulta.
- `CriteriaBuilder`
    - Este último se encarga de conformar el predicado generar. Es decir, utilizar el `And`, `Or`, etc. de ser necesario. Su función principal es la creación del predicado a utilizar mediante la unión de los diferentes `criteria query` creados.

Para hacer uso de estas especificaciones de JPA, debemos ir a nuestro repositorio y hacer que extienda de `JpaSpecificationExecutor<T>` y de esta forma no hay que hacer más nada; solo resta ver un ejemplo:

```java
customerRepository.findAll(hasBirthday());
customerRepository.findAll(isLongTermCustomer());
```

Como se puede observar, aquí se está llamando al método predefinido de `findAll` de JPA pero a la vez se le está pasando por parámetros `hasBirthday()`. Esto es solo posible gracias a haber extendido de `JpaSpecificationExecutor` y su función principal es la de permitir pasar como parámetros los predicados a las funciones de JPA.

Ahora, esto se podría haber realizado utilizando una simple `query` y no está mal pensar así. Pero no se lograría esa atomicidad requerida para poder crear combinaciones de predicados que puedan sernos útiles en diferentes escenarios. 

Ya sabemos como crear especificaciones atómicas para utilizar predicados de filtrados únicos. Pero, todavía no hemos abordado como utilizar dichas especificaciones juntas. Pues, la clase `Specifications` posee métodos generales que permiten concatenar mediante `and` y `or` nuestras pequeñas especificaciones:

```java
customerRepository.findAll(where(customerHasBirthday()).and(isLongTermCustomer()));
```

Aunque es un acercamiento práctico, es un poco complejo a la hora de entender (la forma de escribir las consultas). Debido a ello, se creó un paquete llamado **`Querydsl`** que permite realizar todo lo que vimos anteriormente, pero de una forma más cercana al lenguaje humano. Para utilizarlo, solo necesitamos añadir la librería a nuestro `pom.xml` y comenzar a utilizarlo en nuestro sistema. 

Veamos a continuación como funciona esta librera. Comencemos con el repositorio:

```java
public interface CustomerRepository extends JpaRepository<Customer>, QueryDslPredicateExecutor {
  // Your query methods here
}
```

Observando lo anterior, no ha cambiado prácticamente nada en nuestro sistema. En vez de heredar de `JpaSpecificationExecutor` estamos trabajando con `QueryDslPredicateExecutor`. Por lo que el cambio no es muy brusco hasta el momento. Donde si veremos gran diferencia es en el momento de hacer la llamada al `findAll`por ejemplo:

```java
QCustomer customer = QCustomer.customer;
LocalDate today = new LocalDate();
BooleanExpression customerHasBirthday = customer.birthday.eq(today);
BooleanExpression isLongTermCustomer = customer.createdAt.lt(today.minusYears(2));
customerRepository.findAll(customerHasBirthday.and(isLongTermCustomer));
```

Podemos crear una clase aparte de ser necesario para refactorizar el código. Pero la idea es visible desde las líneas anteriores. En este caso creamos los predicados de una forma más parecida a hablar en inglés que codificada y la composición de estos, se realiza de forma más simplificada.