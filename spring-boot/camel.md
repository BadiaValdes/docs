# Apache Camel

Apache Camel es un framework de integración de código abierto que permite a los desarrolladores conectar fácilmente diferentes sistemas y aplicaciones.

# Conceptos técnicos de Camel

- Mensaje (*message*)
    - Entidad utilizada para la comunicación del sistema
- Intercambio (*exchange*)
    - Encapsula el mensaje (desde el body hasta el header) y permite la interacción interna en el sistema.
- Contexto de camel (*camel context*)
    - Componente de camel que permite el acceso a servicios como las rutas, endpoints, etc.
- Rutas (*routes*)
    - Permite el trabajo entre el cliente y el servidor de forma independiente. Se crean mediante el lenguaje DSL y se encargan del encadenamiento de funciones (procesos).
- Lenguaje específico de dominio (*domain specific lenguage (DSL)*)
    - Los procesos y los endpoints son juntados mediante el lenguaje DSL que son concevidos mediante la rutas.
    - DSL se adapta segun al lenguaje o framework que se esté utilizando
- Procesadores (*processors*)
    - Los procesadores pueden realizar cambio sobre los mensajes encapsulados en los *exchange*.
    - Como funciona la relacion entre procesadores, rutas y mensajes:
        - Las rutas son unidades lógicas que conectan
        - procesos, encargados de procesar
        - los mensajes
- Componente (*component*)
    - Son extensiones de apache camel. Se podría decir que son las piezas que permiten a camel integrarse con diferentes tecnologías para trabajar.
- *Endpoint*
    - Son los puntos que permiten la coneción entre sistemas.
- Producidores (*producer*)
    - Componente que se encargar de crear y enviar los mensajes a los endpoints.
- Consumidores (*consumer*)
    - Componente que recibe los mensajes creados por los productores y los envian a los procesadores.

# Creando nuestra app

## Qué debemos instalar?

> Estaremos utilizando https://start.spring.io/ para crea nuestro proyecto

- Apache Camel
- Spring Web
- Spring Data JPA
- H2 database (Si queremos una bd ligera. Base de datos en memoria)
- PostgresSQL Driver (Si queremos bd en postgres. En este caso no usamos la de arriba)

# Bibliografia

## General
- https://lucian-davitoiu.medium.com/a-camel-project-example-with-java-beans-and-osgi-blueprint-in-karaf-5dc172e09829
- https://stackabuse.com/example-apache-camel-with-blueprint/

# Anotaciones

## Formas de las rutas

Hasta ahora he visto dos formas de trabajar con las rutas. Mediante DSL (JAVA) o mediante Blueprint (XML). La primera la vimos anteriormente; en este apartado veremos la de XML (explicaremos las distintas etiquetas):

* Todos los componentes deben llevar un id.

> Etiqueta bean

```xml
<bean class="org.apache.activemq.camel.component.ActiveMQComponent" id="beanAMQ">
    <property name="brokerURL" value="${amq.broker.url}"/>
    <property name="userName" value="${amq.username}"/>
    <property name="password" value="${amq.password}"/>
    <property name="usePooledConnection" value="true"/>
</bean>
```

Esta etiqueta se utiliza para hacer referencia a una función dentro de una clase de java:
    - class: la dirección de la clase a utilizar
    - id: el nombre que se le da al bean

Dentro tiene una serie de `properties` que definen los datos necesarios para que la clase trabaje correctamente. En este caso estamos definiendo las propiedades `brokerURL`, `userName`, entre otros.

> Etiqueta `cxf:rsClient`

```xml
<cxf:rsClient address="[{endpoint.url.meta4}]"
    id="endpointWsConsultarRut"
    password="[{wsejecutivo.integracion.metacuatro.basic.auth.pass}]" username="[{wsejecutivo.integracion.metacuatro.basic.auth.user}]">
    <cxf:providers>
        <ref component-id="jsonProvider"/>
    </cxf:providers>
</cxf:rsClient>
```

El rsClient es un conector a un servicio externo, el address hace referencia a la `url` que vamos a estar llamando. En este caso estamos usando una variable declarada en el archivo properties, por lo que para acceder a ella usamos `[{nombreDeLaProp}]`.

Como datos extras le pasamos el password y el username; de esta forma podrá autenticarse sin problemas. El proximo paso es declarar el provider. El provider hace referencia a la clase que se va a utilizar para la tranformación del objeto. Esta clase es añadida en la próxima etiqueta:

- `<bean class="org.apache.cxf.jaxrs.provider.json.JSONProvider" id="jsonProvider"/>`

Aquí podemos ver que en el `provider` llamamos a la clase mediante `component-id`; y el valor del mismo es el dato declarado en el `id` del `bean` que hace referencia a la clase `JSON` a utilizar para la tranformación.

> Etiqueta `cxf:cxfEndpoint`

```xml
 <cxf:cxfEndpoint address="/WSEjecutivo" id="epEjecutivoSOAP"
        serviceClass="cl.coopeuch.integracion.wsejecutivo.wsdl.WSEjecutivoPortType" wsdlURL="etc/wsdl/WSEjecutivo.wsdl"/>
```

Esta etiqueta nos permite definir un enpoint de acceso mediante camel. Mediante `address` decimos el punto de entrada hacia el servicio. `ServiceClass` se encarga de definir que clase interfaz dentro de nuestros archivos `wsdl` que se encargará de atender la peticion. Y `wsdlUrl` apunta hacia el archivo que permite conformar los datos de respuesta.

> Etiqueta `cxf:rsServer`

```xml
<cxf:rsServer address="/RSEjecutivo" id="epEjecutivoREST" serviceClass="cl.coopeuch.integracion.wsejecutivo.rest.EjecutivoRest"/>
```

El cxf

> Ejemplo con bean de acceso a la base de datos:

```xml
<bean class="org.apache.commons.dbcp.BasicDataSource"
    destroy-method="close" id="dataSourceSqlServer">
    <property name="driverClassName" value="net.sourceforge.jtds.jdbc.Driver"/>
    <property name="url" value="[{jdbc.sqlserver.url.ejecutivo}]/Payroll;charset=iso_1"/>
    <property name="username" value="[{jdbc.sqlserver.username.ejecutivo}]"/>
    <property name="password" value="[{jdbc.sqlserver.password.ejecutivo}]"/>
    <property name="timeBetweenEvictionRunsMillis" value="[{jdbc.sqlserver.timeBetweenEvictionRunsMillis.ejecutivo}]"/>
    <property name="numTestsPerEvictionRun" value="[{jdbc.sqlserver.numTestsPerEvictionRun.ejecutivo}]"/>
    <property name="minEvictableIdleTimeMillis" value="[{jdbc.sqlserver.minEvictableIdleTimeMillis.ejecutivo}]"/>
    <property name="maxActive" value="[{jdbc.sqlserver.maxActive.ejecutivo}]"/>
</bean>
```

> Ejemplo de acceso a clases dentro de nuestro sistema:

```xml
<bean class="cl.coopeuch.integracion.util.RouteFacade" id="beanFacade"/>
<bean class="cl.coopeuch.integracion.util.ValidarLlave" id="validarLlave"/>
```

En el atributo class podemos ver como la dirección apunta ha un archivo que contiene una clase. Por ejemplo, RouteFacade se puede ver de la siguiente forma:

```java
package cl.coopeuch.integracion.util;
public class RouteFacade {
    public void iniciarExcepcion(Exchange exchange) throws NumberFormatException, Exception {

		Excepcion excepcion = RouteExcepcion.crear(exchange, CODIGONOENCONTRADO, ORIGENINICIAR, CANTIDADERRORES);
		Response response = new Response();
		response.setError(excepcion);
		exchange.getOut().setBody(response);

	}
}
```

Necesitamos que la `instancia` a esa clase se cree mediante el `bean` para poder acceder a datos como `Exchange exchange`.

