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

### Etiqueta bean

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

### Etiqueta `cxf:rsClient`

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

### Etiqueta `cxf:cxfEndpoint`

```xml
 <cxf:cxfEndpoint address="/WSEjecutivo" id="epEjecutivoSOAP"
        serviceClass="cl.coopeuch.integracion.wsejecutivo.wsdl.WSEjecutivoPortType" wsdlURL="etc/wsdl/WSEjecutivo.wsdl"/>
```

Esta etiqueta nos permite definir un enpoint de acceso mediante camel. Mediante `address` decimos el punto de entrada hacia el servicio. `ServiceClass` se encarga de definir que clase interfaz dentro de nuestros archivos `wsdl` que se encargará de atender la peticion. Y `wsdlUrl` apunta hacia el archivo que permite conformar los datos de respuesta.

### Etiqueta `cxf:rsServer`

```xml
<cxf:rsServer address="/RSEjecutivo" id="epEjecutivoREST" serviceClass="cl.coopeuch.integracion.wsejecutivo.rest.EjecutivoRest"/>
```

El cxfEndpoint se encargaba de exponer nuestro servicio mediante SOAP. En este caso estamos creando un endpoint pero de tipo Rest. De la misma forma que `address` decide la dirección de entrada del servicio `SOAP`, decidirá el destino de entrada de las operaciones `REST`. Seguido podemos ver el atributo `id` que es básico para todas las etiquetas y un poco después logramos ver `serviceClass`. Este último, se encarga de apuntar a una interfáz con las declaraciones necesarias para exponer nuestra `API REST`.

### Ejemplo con bean de acceso a la base de datos:

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

### Ejemplo de acceso a clases dentro de nuestro sistema:

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

### Camel Context

```xml
<camelContext id="context-wsejecutivo" xmlns="http://camel.apache.org/schema/blueprint">
<!-- route -->
</camelContext>
```
Este fragmento es el encapsulador de todas las declaraciones de rutas dentro de camel. Es decir, dentro del mismo comenzaremos a declarar todas las rutas de Camel que se encargaran de enrutar los mensajes y aplicar los procesadores pertinentes.

### Data Format

```xml
<dataFormats>
    <json id="JacksonReq" library="Jackson" unmarshalTypeName="cl.coopeuch.integracion.wsejecutivo.wsdl.ConsultarRequest"/>
    <json id="JacksonResp" include="NON_NULL" library="Jackson" unmarshalTypeName="cl.coopeuch.integracion.wsejecutivo.wsdl.Response"/>
    <json id="JacksonRenovarReq" library="Jackson" unmarshalTypeName="cl.coopeuch.integracion.wsejecutivo.wsdl.ConsultarEECRequest"/>
    <json id="JacksonRenovarResp" include="NON_NULL"
        library="Jackson" unmarshalTypeName="cl.coopeuch.integracion.wsejecutivo.wsdl.ConsultarEECResponse"/>
    <json id="JacksonFault" include="NON_NULL" library="Jackson" unmarshalTypeName="cl.coopeuch.integracion.wsejecutivo.wsdl.Excepcion"/>
</dataFormats>
```

Este pequeño fragmento de código que se encuentra antes de la declaración de la primer ruta, nos permite declarar todos los archivos de tranformación a utilizar dentro de las diferentes rutas. Las etiquetas dentro de `dataFormats` son de tipo `json` y tienen los siguientes atributos:
- `library` -> Librería a utilizar para el proceso de tranformación. En este caso están usando `Jackson`.
- `include` -> Con esta podemos decidir si incluimos un tipo de valor en específico. En algunos casos ponemos que solo se incluyan los datos no nulos (`NON_NULL`).
- `unmarshalTypeName` -> El `unmarshal` es el proceso de llevar de datos recibidos desde la red (binarios o textos por ejemplo) a un objeto de tipo java. En este caso se utiliza para apuntar a la clase que se encarga de realizar la conversión.

- `marshal` -> Es la operación contraria a `unmarshal`; llevamos de objeto de java a binario o texto.

### Ruta (`route`)

```xml
<route id="ConsultaEjecutivo-Ruta-SOAP">
<!-- operaciones -->
</route>
```

La etiqueta `route` es la encargada en envolver todas las operaciones que se realizarán ensobre una ruta. Su objetivo principal es de contenedor. Como atributo tiene un `id` que identifica a esa etiqueta como única. Dentro de la misma comenzaremos a escribir las siguientes etiquetas.

### Desde (`from`)

```xml
<from id="ConsultaEjecutivo-From-SOAP" uri="cxf:bean:epEjecutivoSOAP"/>
```

La etiqueta from define la ruta que se debe escuchar. Es decir, si una petición llega al sistema por la ruta `rouge1` y tenemos un from con `uri` `rouge1`, todas las operaciones que se declaren dentro de la ruta atenderan esa petición. En este caso la `URI` hace referencia el punto de acceso `SOAP` declarado en el `bean` `cxfEndpoint`.

La uri no necesariamente debe ser una clase o endpoint que defina un punto de acceso. También puede ser otra ruta declarada en el archivo de `camel`. Estos casos se ven cuando se posee una operación compleja y se desea dividir el comportamiento en diferentes partes que se realicen de forma encadenada. Ejemplo:
- `uri="direct:ejecutivo"` -> esta ruta atenderá todas las llamadas realizadas a la uri con el mismo nombre.

### Hacia (`to`)

```xml
<to id="ConsultaEjecutivo-To-REST" uri="direct:ejecutivo"/>
```

Esta etiqueta define a que ruta se debe llamar dentro de un flujo. La `uri` puede hacer referencia a un `from` como vimos anteriormente o ha un servicio externo o clase. 

Varios ejemplos son:

- A un archivo xsl

```xml
<to id="ConsultaEjecutivo-To-RPG-Input" uri="xslt://etc/xsl/in/IOC709001I_consultar.xsl?saxon=true"/>
```

En este caso la uri apunta a un archivo `xsl` (de tranformación) dentro de nuestros archivos locales.

- Hacia una cola de eventos

```xml
<to id="ConsultaEjecutivo-To-AMQ" uri="beanAMQ:{{amq.queue.temp.int}}?preserveMessageQos=true&amp;jmsMessageType=Text&amp;replyToType=Temporary&amp;synchronous=true&amp;exchangePattern=InOut&amp;deliveryPersistent=false&amp;replyToDeliveryPersistent=false"/>       
```

- Hacia un procedimiento almacenado

```xml
<to id="GuardarFuncionarios-SQL-Buscar" uri="sql-stored:classpath:etc/sql/pa_guardarejecutivo.sql?dataSource=dataSourceSqlServer"/>        
```

### Hacia ruta dinámica (`toD`)

```xml
<toD id="_toDListarPorProducto" uri="{{endpoint.url.meta4}}?throwExceptionOnFailure=false"/>
```

Esta etiqueta es utilizada cuando queremos utilizar una `uri` dinámica; o mejor dicho, una `uri` que contanga un dato variable como es el caso del endpoint en el ejemplo anterior.

### Crear Propiedad (`setProperty`)

```xml
<setProperty id="_setProperty3" propertyName="operationName">
    <simple>${header.operationName}</simple>
</setProperty>
```

La etiqueta `setProperty` viene siendo el equivalente a la creación de variables en código java. El atributo `propertyName` define el nombre que llevará la variable o propiedad y la etiqueta `simple` encapsulada, es la encargada de definir el dato a guardar. En este caso estamos accediendo al varlo del `header` del mensaje que viaja por la ruta.

Es importante aclarar que esta funcionalidad no se limita solo a asignaciones sencillas, también se pueden asignar datos modificados mediante operaciones `xml`.

### Crear propiedad desde xml (`xpath`)

Partiendo del jemplo anterior del `setProperty` vamos a abtraernos y pensar que el dato a obtener lo encontraremos dentro de un `XML`. En estos casos etiquetas como `simple` o acceso a objetos no nos funcionan; por lo que necesitamos poseer un acceso directo a las variables declaradas dentro del `XML`. Para este caso utilizamos la etiqueta `xpath`:

```xml
<xpath resultType="String">//apellidos</xpath>
```

La etiqueta recibe el atributo `resultType` que define el tipo de dato que se devolverá. Dentro de la misma mediante el uso de `//` llamamos a la variable interna; en el ejemplo anterior es apellidos.

### Try pero en xml (`doTry`)

```xml
<doTry id="_doTry1">
<!-- operaciones -->
</doTry>
```

La etiqueta `doTry` es el equivalente XML al `try` propuesto por los lenguajes de programación. Su función es similar, ejecutar un código y capturar cualquier fallo que exista dentro para su posterior tratado.

### Catch pero con xml (`doCatch`)

```xml
<doCatch id="inicioDoCatch">
    <exception>java.lang.Exception</exception>
    <setProperty id="inicioPropertyCodigo" propertyName="codigoError">
        <simple>1</simple>
    </setProperty>
    <to id="toInicioExcepcion" uri="direct:excepcion"/>
</doCatch>
```

Esta etiqueta es la encargada de capturar cualquier error que exista dentro de la etiqueta `doTry`. Su funcionamiento es similar al catch de java y por lo tanto, debemos declarar una serie de pasos dentro para manejar el error.

- `exception` -> Define la excepción que se estará capturando
- `setProperty` -> Como vimos anteriormente es para declarar una propiedad o modificarla
- `to` -> Como se vio anteriormente, el to me permite invocar otra ruta dentro de camel (uno de sus usos).

Es importante decir que esta etiqueta va dentro del `doTry`; a continuación veremos un ejemplo:

```xml
<doTry id="_doTry1">
    <doCatch id="inicioDoCatch">
        <exception>java.lang.Exception</exception>
        <setProperty id="inicioPropertyCodigo" propertyName="codigoError">
            <simple>1</simple>
        </setProperty>
        <to id="toInicioExcepcion" uri="direct:excepcion"/>
    </doCatch>
</doTry>
```

### El switch de xml camel (`choice`)

```xml
<choice id="inicioChoiceOperacion">
<!-- condiciones -->
</choice>
```

Esta etiqueta es un encapsulador de condiciones. Funciona similar a un if o a un switch; puede ser visto de cualquiera de las dos formas. Es el encargado de definir las diferentes rutas que puede seguir una petición endependencia de los criterios de comparación.

### La condición (`when`)

```xml
<when id="inicioWhenOperacionConsultar">
    <simple>${header.operationName} == 'consultar'</simple>
    <setBody id="ConsultaEjecutivo-SetBody">
        <simple>$simple{body.get(0)}</simple>
    </setBody>
    <to id="ConsultaEjecutivo-To-SOAP" uri="direct:ejecutivo"/>
</when>
```

La etiqueta `when` sigue siendo un encapsulador para una sola condición. La idea es encapsular dentro de la misma:
- la condición que se debe cumplir
- las operaciones a realizar si se cumple

En este caso se esta realizando la comparación mediante la etiqueta `simple` donde podemos ver que pregunta si la operacoines dentro del header `${header.operationName}` es igual al valor `consultar`. Posteriormente se realiza la acción `setBody` que tiene como objetivo modificar el valor del cuerpo del mensaje. En este caso estamos seteando el primer valor del body anterior. Por último tenemos una llamada a otra ruta mediante el `to`.

### En caso contrario (`otherwise`)

```xml
<otherwise id="inicioOtherwise">
    <throwException
        exceptionType="java.lang.Exception"
        id="throwExceptionOperacionInvalida" message="La operacion solicitada es invalida"/>
</otherwise>
```

Esta etiqueta nos permite definir un caso base por si no se cumple ninguno de los anteriores. Por lo que la etiqueta `choice` funciona más como un `switch`. Dentro declaramos la etiqueta:
- `throwException` -> Nos permite lanzar una excepción. Recibe como atributos:
    - `exceptionType` -> Tipo de excepción a lanzar.
    - `message` -> Mensaje a enviar.

### Unión de `choice`, `when`, `otherwise`

```xml
<choice id="inicioChoiceOperacion-REST">
    <when id="inicioWhenOperacionConsultar-REST">
        <simple>${header.operationName} == 'consultar'</simple>
        <unmarshal id="unmarshall_entrada" ref="JacksonReq"/>
        <to id="ConsultaEjecutivo-To-REST" uri="direct:ejecutivo"/>
    </when>
    <otherwise id="inicioOtherwise-REST">
        <throwException exceptionType="java.lang.Exception"
            id="throwExceptionOperacionInvalida-REST" message="La operacion solicitada es invalida"/>
    </otherwise>
</choice>
```

### Transformar (`Marshal`)

```xml
<marshal id="ConsultaEjecutivo-Marshal">
    <jaxb contextPath="cl.coopeuch.integracion.wsejecutivo.wsdl"/>
</marshal>
```

La etiqueta `marshal` nos permite realizar la tranformación de objeto java a XML; mientras que la etiqueta `jaxb` se encarga de definir el archivo que se encargará del proceso de serialización.

### Tranformar Manual (`transform`)

```xml
<transform id="ConsultaEjecutivo-Transform">
    <simple>&lt;trama&gt;${body}&lt;/trama&gt;</simple>
</transform>
```

Mientras que las etiquetas `marshal` y `unmarshal` me facilitan una tranformación semi-autmática de los datos, la etiqueta transform me permite hacer esta operación de forma manual. Es decir, si quiero añadir un dato en específico o envolver la respuesta de una acción dentro de una etiqueta (ejemplo anterior) puedo utlizar una tranformación manual y evitar crear un archivo solo para ese propósito.

### Tranformar cuerpo (`convertBodyTo`)

```xml
<convertBodyTo id="consultarEjecutivo-ConvertBodyToResponse" type="cl.coopeuch.integracion.wsejecutivo.wsdl.Response"/>
```

La función de esta etiqueta es convertir todos los datos del cuerpo del mensaje a una estructura designada por la propiedad `type`. Es decir, el `convertBodyTo` cambiaría de `int` a `string`; en el ejemplo puesto anteriormente, convertiríamos el cuepro completo para que sigha la estructura declarada en el wsdl.

Aunque parezca a primera vista similar a `marshal`, su similitud no podría ser menor. Esta última se encarga de tranformar los datos en diferentes formatos como son `JSON` o `XML`. Mientras que `convertBodyTo` convierte el tipo de datos.

### Mostrando logs (`log`)

```xml
<log id="_log2" message="SW ${header.sw}"/>
```

En el caso que en medio de una ejecución queramos comprobar el valor de alguna variable podemos utilizar la etiqueta `log` que mediante el atributo `message` nos permite sacar datos por la consola

### Eliminar los datos del encabezado (`removeHeaders`)

```xml
<removeHeaders id="ConsultaEjecutivo-RemoveHeaders" pattern="*"/>
```

Esta etiqueta se encarga de borrar todas las propiedades declaradas en el header dado un patrón a seguir.

### Añadir datos al header (`setHeader`)

```xml
<setHeader headerName="Exchange.HTTP_METHOD" id="consultarRutSetHeaderMethod">
    <constant>GET</constant>
</setHeader>
```

En este caso vamos a intentar añadir datos al header del mensaje. Especificamente se está modificange el valor de `HTTP_METHOD` y se le está asignando un valor constante (valor predefinido) `GET`.

### Realizar copia del mensaje (`wireTap`)

Esta etiqueta la utilizaremos cuando queramos que otra ruta de camel utilice nuestro mensaje actual pero no modifique los valores del original. Es decir, se clonaría el mensaje, uno seguiría el flujo principal de la ruta mientras que el otro es enviado a otra ruta dentro de camel y cualquier cambio que se realice sobre él, no afectará al mensaje original.

```xml
<wireTap id="guardarCacheTo" uri="direct:guardarCacheBD"/>
```

La pripiedad `uri` de esta etiqueta define hacia donde se debe dirigir la información clonada.

### Detener la propagación (`stop`)

```xml
<stop id="Exception-Stop"/>
```

Supongamos que tenemos varios `when` dentro de un `choice` y posteriormente otra serie de pasos a ejecutar. Supongamos que el mensaje llegó a un `when` que debería terminar la ejecución completa de la ruta. En este caso debemos utilizar la etiqueta `stop` ya que su función es terminar con la propagación de un mensaje dentro de las rutas de `camel`.

