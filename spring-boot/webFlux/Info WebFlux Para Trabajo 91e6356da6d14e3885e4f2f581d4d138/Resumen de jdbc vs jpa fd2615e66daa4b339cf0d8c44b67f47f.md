# Resumen de jdbc vs jpa

## IA

JdbcTemplate y JPA son dos opciones para acceder a una base de datos en una aplicación Spring. JdbcTemplate es una opción más rápida y de menor nivel, mientras que JPA es una opción más fácil de utilizar y de más alto nivel. Aquí hay un ejemplo de cómo utilizar JdbcTemplate en un proyecto:

Example:

```java
@Autowired
private JdbcTemplate jdbcTemplate;

public List<User> getAllUsers() {
    return jdbcTemplate.query("SELECT * FROM users", new BeanPropertyRowMapper<>(User.class));
}
```

## Mio

Código JPA:

```java
public interface CustomerRepository extends JpaRepository<Customer, Long> {

  Customer findByEmailAddress(String emailAddress);

  List<Customer> findByLastname(String lastname, Sort sort);

  Page<Customer> findByFirstname(String firstname, Pageable pageable);
}
```

Como se puede observar arriba, usar JPA es bastante gratificante debido a que no se deben escribir líneas de código prácticamente, ya que con solo declarar el nombre de la función tendremos nuestro resultado de  la base de datos. A pesar de ser bastante fácil de utilizar, posee dos grandes desventajas:

- Los métodos para realizar peticiones a la base de datos pueden aumentar sin control.
- Los criterios de búsqueda utilizados son fijos e inmutables.

Para evitar estos problemas es posible utilizar los predicados para la construcción paso a paso del query a realizar a base de datos. En un principio se puede pensar en utilizar el Criteria Builder cuyo código es el siguiente:

```java
LocalDate today = new LocalDate();

CriteriaBuilder builder = em.getCriteriaBuilder();
CriteriaQuery<Customer> query = builder.createQuery(Customer.class);
Root<Customer> root = query.from(Customer.class);

Predicate hasBirthday = builder.equal(root.get(Customer_.birthday), today);
Predicate isLongTermCustomer = builder.lessThan(root.get(Customer_.createdAt), today.minusYears(2);
query.where(builder.and(hasBirthday, isLongTermCustomer));
em.createQuery(query.select(root)).getResultList();
```

Aunque conveniente para crear queries a la base de datos. Su forma de estructurarse es un poco compleja y a la vez difícil de leer. Debido a esto, surgen las especificaciones. Ver archivo de especificaciones.

JDBC

Java Database Connectivity es una API para java que define la forma de acceder del cliente a la base de datos.

Ventajas:

- Procesador de consultas SQL.
- Buen rendimiento con gran cantidad de datos.
- Bueno para las aplicaciones que necesiten respuestas rápidas.
- Bueno cuando se quiere lograr un control total sobre la aplicación.
- Es mucho más rápido que JPA.

Desventajas:

- Lleva demasiada programación por detrás comparado con JPA.
- No existe la encapsulación como en JPA.
- Difícil implementación cuando se utiliza la arquitectura MVC.
- Las consultas deben ser específicas para el gestor de base de datos.

Para utilizar JDBC en el proyecto, es necesario crear primero un componente de configuración en spring boot:

```java
@Configuration
@ComponentScan("com.baeldung.jdbc")
public class SpringJdbcConfig {
    @Bean
    public DataSource mysqlDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/springjdbc");
        dataSource.setUsername("guest_user");
        dataSource.setPassword("guest_password");

        return dataSource;
    }
}
```

## JdbcTemplate

JDBC por detrás posee diferentes formas de trabajar. Comencemos hablando de `JdbcTemplate`. 

Esta es la API principal que provee la librería; ya que a partir de esta, podemos acceder a las funcionalidades más comunes que se usan en un trabajo. Comencemos con un ejemplo sencillo de conteo de datos:

```java
int result = jdbcTemplate.queryForObject(
    "SELECT COUNT(*) FROM EMPLOYEE", Integer.class);
```

Como podemos observar, utilizamos una instancia de `jdbcTemplate` para acceder a ejecutador de query por decirlo de alguna forma (`queryForObject`). Dentro, le pasamos como primer parámetro nuestra query (en este caso un select con count) y seguido la clase correspondiente al tipo de datos a devolver.

En caso de que queramos acutalizar un dato en la BD, utilizaremos la siguiente consulta:

```java
public int addEmplyee(int id) {
    return jdbcTemplate.update(
      "INSERT INTO EMPLOYEE VALUES (?, ?, ?, ?)", id, "Bill", "Gates", "USA");
}
```

Aunque no muy diferente a la anterior, pero vemos algo extraño. En donde van los valores vemos un símbolo de `?` y posteriormente más parámetros de los que deberían estar. Esto sucede porque estamos utilizando el `?` como placeholder para datos; se podría decir que son variables dentro del query. Toda variable debe recibir un valor, por lo que los siguientes parámetros a insertar son los valores en orden correspondientes a los datos (**EN ORDEN**) del objeto en la base de datos.

## namedParameterJdbcTemplate

Se hace un poco complicado no?? Tener variables que pueden confundir al programador. Bueno, no hay problema con esto. Además del `jdbcTemplate` se tiene el  `namedParameterJdbcTemplate` que permite estructurar el query con nombres de variables en vez de `?`. A continuación veremos un ejemplo para un solo parámetro; por decirlo de alguna forma, random:

```java
// Uso de variables nombradas
SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("id", 1);
return namedParameterJdbcTemplate.queryForObject(
  "SELECT FIRST_NAME FROM EMPLOYEE WHERE ID = :id", namedParameters, String.class);
```

Antes de poder ir directamente a la query como se hizo en ejemplos anteriores, aquí se tuvo que crear un objeto que guarde el mapeo de variables. Es decir, creo una variable llamada id y su valor va a ser 1, en este caso. Posteriormente llamo al `namedParameterJdbcTemplate.queryForObject` y paso por parámetros lo que ya conocemos  pero en este caso, podemos variar la estructura del query. Anteriormente debería haber creado la variable con un `?`, pero aquí puedo llamarla directamente como `:id`. Muy importante a tener en cuenta es que el nombre de la variable que use en el query, debe ser la misma que puse en el objeto de mapeo.

Como segundo ejemplo de este caso, tendremos una desestructuración del `queryForObject` para un mejor manejo de los parámetros y en vez de utilizar variables como parámetros, utilizaremos un objeto:

```java
//Uso de variables nombradas con objetos
Employee employee = new Employee();
employee.setFirstName("James");

String SELECT_BY_ID = "SELECT COUNT(*) FROM EMPLOYEE WHERE FIRST_NAME = :firstName";

SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(employee);
return namedParameterJdbcTemplate.queryForObject(
  SELECT_BY_ID, namedParameters, Integer.class);
```

Primero se creó un objeto de tipo `Employee` y se llenaron los datos necesarios para el query. Seguido, se declaró el query a utilizar y al final del mismo, la variable que se deberá utilizar. Como debieron haber observado ya, el nombre de la variable del query es `:firstName` y la propiedad del objeto que fue llenada es `firstName` también; por lo que, al igual que el caso anterior, debe ser el mismo nombre. Por último hacemos la llamada y terminamos.

Antes de terminar con el pequeño ejemplo, debemos realizar otra acción. En este caso estaremos viendo como mapear los datos provenientes de JDBC a objetos propios de JAVA. Comencemos primero creando una clase que se encargará de todo el procedimiento de asignación de datos:

```java
// Mapeo de query a objeto
public class EmployeeRowMapper implements RowMapper<Employee> {
    @Override
    public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
        Employee employee = new Employee();

        employee.setId(rs.getInt("ID"));
        employee.setFirstName(rs.getString("FIRST_NAME"));
        employee.setLastName(rs.getString("LAST_NAME"));
        employee.setAddress(rs.getString("ADDRESS"));

        return employee;
    }
}
```

Porqué hacemos esto? Bueno, las peticiones provenientes de la base de datos son planas por así decirlo, solo texto. Para poder utilizar los resultados devueltos por estas en nuestras aplicación, debemos asignar los datos pedidos a un objeto o variables. En el caso anterior se creó un objeto `Employee` que almacenará todos los datos que se extraigan de la BD. El método de mapeo, deberá recibir dos datos; el primero `rs` hace referencia al resultado obtenido de la base de datos y el segundo al número de fila que se devuelven `rowNum`. La idea general es usar `rs` `(rs.getInt("ID"))` para asignar los datos al objeto. 

El método creado, debe estar dentro de una clase como se mencionó anteriormente. Y a su vez, dicha clase debe implementar la interfaz de `RowMapper<T>` para obtener acceso al método de mapeo.

Conociendo esto, solo nos resta ver como sería su uso dentro de nuestra aplicación:

```java
String query = "SELECT * FROM EMPLOYEE WHERE ID = ?";
Employee employee = jdbcTemplate.queryForObject(
  query, new Object[] { id }, new EmployeeRowMapper());
```

## Manejo de errores

Si hablamos de una librería que accede directamente a base de datos, estaremos hablando de errores también. Pero no errores en el código, sino errores de acceso a la base de datos ya sea por problemas de conexión o datos malformados. Por defecto, JDBC utiliza como excepción principal *`DataAccessException`* para manejar todos los casos posibles. Además, es posible crear nuestras propias excepciones mediante la extensión de la clase `SQLErrorCodeSQLExceptionTranslator`; a continuación  veremos un ejemplo de un error personalizado:

```java
public class CustomSQLErrorCodeTranslator extends SQLErrorCodeSQLExceptionTranslator {
    @Override
    protected DataAccessException
      customTranslate(String task, String sql, SQLException sqlException) {
        if (sqlException.getErrorCode() == 23505) {
          return new DuplicateKeyException(
            "Custom Exception translator - Integrity constraint violation.", sqlException);
        }
        return null;
    }
}
```

Aunque esto viene como anillo al dedo para personalizar los posibles errores que encontremos, no es algo que podamos llamar de forma global. Para utilizarlo, es necesario pasarlo como parámetro de `jdbcTemplate`:

```java
CustomSQLErrorCodeTranslator customSQLErrorCodeTranslator =
  new CustomSQLErrorCodeTranslator();
jdbcTemplate.setExceptionTranslator(customSQLErrorCodeTranslator);
```

## JDBC en modo facil

Viendo todo hasta el momento, parece mucho código y un poco pesado de utilizar. Bueno, si no eres de los que le gusta tirar mucho código SQL, existe una variante llamada ****`SimpleJdbc`** encargada de facilitarnos la vida. No, no va a hacer la magia que nos permite JPA, pero si nos hace la vida un poco más sencilla. A continuación veremos diferentes escenarios donde se puede utilizar:

**Insertar**:

```java
public int addEmplyee(Employee emp) {
		SimpleJdbcInsert simpleJdbcInsert = 
		  new SimpleJdbcInsert(dataSource).withTableName("EMPLOYEE");
    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put("ID", emp.getId());
    parameters.put("FIRST_NAME", emp.getFirstName());
    parameters.put("LAST_NAME", emp.getLastName());
    parameters.put("ADDRESS", emp.getAddress());

    return simpleJdbcInsert.execute(parameters);
}
```

Para insertar, primero es necesario crear una instancia de `SimpleJdbcInsert` y pasarle como parámetro la conexión a base de datos y posteriormente la tabla a utilizar. Ya después crearamos un `HashMap` para los parámetros y mediante `SimpleJdbcInsert` ejecutariamos la query de insertar.

**Llamar**:

```java
public Employee getEmployeeUsingSimpleJdbcCall(int id) {
		SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dataSource)
		                     .withProcedureName("READ_EMPLOYEE");   
		SqlParameterSource in = new MapSqlParameterSource().addValue("in_id", id);
    Map<String, Object> out = simpleJdbcCall.execute(in);

    Employee emp = new Employee();
    emp.setFirstName((String) out.get("FIRST_NAME"));
    emp.setLastName((String) out.get("LAST_NAME"));

    return emp;
}
```

Mismo procedimiento que insertar, excepto que esta vez utilizaremos una llamada directa a base de datos.

# Bibliografía

[Spring JDBC Tutorial | Baeldung](https://www.baeldung.com/spring-jdbc-jdbctemplate)

[https://github.com/rkDeependra/Spring-JDBC-Demo](https://github.com/rkDeependra/Spring-JDBC-Demo)

[Spring JDBC Example  | DigitalOcean](https://www.digitalocean.com/community/tutorials/spring-jdbc-example)