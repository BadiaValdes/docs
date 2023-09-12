# ELK, Spring Boot, and Filebeat

En las aplicaciones web normalmente tenemos un archivo encargado de almacenar los logs. En la arquitectura de microservicios este acercamiento es un poco engorroso ya que cada aplicación posee su propio archivo de logs. Buscar errores entro de esta arquitectura puede ser una total pesadilla, pero gracias a Elastic Stack, podemos tener un control centralizado de todos los logs de la aplicación y en tiempo real, ver lo que sucede a lo largo de la cadena de interacciones de nuestras apps.

## ELK

Elastic Stack es un grupo de aplicaciones de codigo abierto diseñadas para tomar datos de cualquier tipo y formato para posteriormente buscar, analizar y visualizar en tiempo real. Este stack está compuesto por las siguientes aplicaciones: 

- Elasticsearch -> encargado de almacenar los datos en formato json. Usado mayormente para almacenar logs.
- Logstash -> Encargado de darle sentido a la información que llega mediante beats u otra aplicación.
- kibana -> Plataforma de análisis y visualización
- Beats -> Intermediario para recolectar los datos de los MS y enviarlos a logstash o elastic. Filebeat es una de las funcionalidades que tiene esta app.

![Alt text](img/diagram-elastic-stack.png)

## Spring Boot + Logger

En Spring Boot tenemos la herramiente `slf4j` para manejar los logs. Esta en si, es una fachada que nos permite utilizar distintos sistemas para la creación de logs. Esta dependencia nos permitirá de forma sencilla crear los logs de nuestra aplicación de forma sencilla e intuitiva y así lograr que Beats sea capaz de capturar los logs de forma eficiente. Comencemos con la forma de declarar una instancia de `Logger` en nuestra aplicación:

```java
private final Logger log = LoggerFactory.getLogger(CLASSNAME.class);
```

En caso que queramos evitar la creacion manual de la instncia de `Logger`, podemos apoyarnos en una anotación de la librería de Lombok: `@Slf4j`. Sin importar de la forma que lo hagamos, `Logger` nos brinda cinco niveles de logs:

- trace
- debug
- info
- warn
- error

Un ejemplo en código de los mismos es:

```java
log.trace("Logging at TRACE level");
log.debug("Logging at DEBUG level");
log.info("Logging at INFO level");
log.warn("Logging at WARN level");
log.error("Logging at ERROR level");

log.debug("Found {} results", list.size()); // <- Ejemplo con parámetros
```

Un sistema de logs en una arquitectura monolótica no pasa de unas líneas extras de código comparado con lo anterior; pero hay que tener en cuenta que cuando estemos trabajando con microservicios, este comportamiento se complica. Realizar el debug de microservicios sin conocer exactamente donde ocurrió el error puede ser un gran problema, pero Spring Cloud nos brinda una dependencia muy util para solucionar este problema. `spring-cloud-sleuth` es una dependencia que nos permite enriquecer nuestros log con un `trace id` y un `span id`; encargados de indicar quien desencadenó el log y hasta donde llegó. Para instalar esta dependecia dentro de nuestra aplicaciones, debemos instalar las siguientes dependencias:

> Esta dependencia está **depreciada** actualmente (solo funcionan hasta la version 2.5 de spring boot)
```xml
<!-- Poner esto en el dependencyManagement me dio error de compatibilidad con jakarta asi que la quite -->
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-sleuth</artifactId>
            <version>${spring-cloud-sleuth.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-sleuth</artifactId>
        <version>${spring-cloud-sleuth.version}</version> <!-- Para compensar, agregue esto aqui -->
    </dependency>
</dependencies>
```

> En cambio, puede usar la siguiente dependencia:

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>io.micrometer</groupId>
            <artifactId>micrometer-tracing-bom</artifactId>
            <version>1.0.4</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

> Es posible que esta dependencia entre en conflicto con la configuración automática de las versiones de spring cloud. Para ello se recomienda especificar las versiones a utilizar.

Con esta dependencia instalada, solo nos resta preparar los archivos de log para trabajar. Pero, por defecto los archivos de log están en **texto** y nuestro objetivo es utilizarlos en **Elasticsearch** que guarda los documentos en formato json. Para esto, debemos ir a nuestra carpeta resource (dirección por defecto que utilizará `Slf4j`) y crear un archivo llamado `logback-spring.xml`. Comencemos con un ejemplo sencill; simplemente obtendremos el log en formato json:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <springProperty scope="context" name="application_name" source="spring.application.name"/>
    <appender name="jsonConsoleAppender" class="ch.qos.logback.core.ConsoleAppender">
        <encoder class="net.logstash.logback.encoder.LogstashEncoder"/>
    </appender>
    <root level="INFO">
        <appender-ref ref="jsonConsoleAppender"/>
    </root>  
</configuration>
```

El resultado final de una consulta a un enpoint que invoca a un log info es la siguiente:
```json
{
  "@timestamp": "2023-08-21T15:29:07.2220964-04:00",
  "@version": "1",
  "message": "Resolving eureka endpoints via configuration",
  "logger_name": "com.netflix.discovery.shared.resolver.aws.ConfigClusterResolver",
  "thread_name": "AsyncResolver-bootstrap-executor-0",
  "level": "INFO",
  "level_value": 20000,
  "application_name": "items"
}
```

A pesar de lo bien que se vea, faltan algunas informaciones bastante importante como son el **traceId** y el **spanId**. Para agregar estas propiedades, debemos escribir la siguiente información en **properties.yml**:

```yml
logging:
  pattern:
    level:%5p [${spring.application.name:},%X{traceId:-},%X{spanId:-}]
```

Ahora vamos a una configuración de log mucho más ambiciosa. En este caso estaremos dando mucha más información de la que teníamos con la configuración antigua:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <springProperty scope="context" name="application_name" source="spring.application.name"/>
    <appender name="jsonConsoleAppender" class="ch.qos.logback.core.ConsoleAppender">
        <encoder class="net.logstash.logback.encoder.LoggingEventCompositeJsonEncoder">
            <providers>
                <timestamp>
                    <timeZone>UTC</timeZone>
                </timestamp>
                <version/>
                <logLevel/>
                <message/>
                <loggerName/>
                <threadName/>
                <context/>
                <pattern>
                    <omitEmptyFields>true</omitEmptyFields>
                    <pattern>
                        {
                            "trace": {
                               "traceId": "%X{traceId:-NONE}",
                               "spanId": "%X{spanId:-NONE}",
                               "parentId": "%X{parentId:-NONE}"
                            }
                        }
                    </pattern>
                </pattern>
                <mdc>
                    <excludeMdcKeyName>traceId</excludeMdcKeyName>
                    <excludeMdcKeyName>spanId</excludeMdcKeyName>
                    <excludeMdcKeyName>parentId</excludeMdcKeyName>
                    <excludeMdcKeyName>spanExportable</excludeMdcKeyName>
                    <excludeMdcKeyName>X-B3-TraceId</excludeMdcKeyName>
                    <excludeMdcKeyName>X-B3-SpanId</excludeMdcKeyName>
                    <excludeMdcKeyName>X-B3-ParentSpanId</excludeMdcKeyName>
                    <excludeMdcKeyName>X-Span-Export</excludeMdcKeyName>
                </mdc>
                <stackTrace/>
            </providers>
        </encoder>
    </appender>
    <root level="INFO">
        <appender-ref ref="jsonConsoleAppender"/>
    </root>    
</configuration>
```

Esto da como resultado:

```json
{
  "@timestamp": "2023-08-21T20:49:45.5833536Z",
  "@version": "1",
  "level": "INFO",
  "message": "Info log in logInfo method",
  "logger_name": "com.poc.microservice.items.controller.ItemController",
  "thread_name": "http-nio-0.0.0.0-8089-exec-6",
  "application_name": "items",
  "trace": {
    "traceId": "64e3cde9409908cb36f6d4c5187fe8d4",
    "spanId": "36f6d4c5187fe8d4",
    "parentId": "NONE"
  }
}
```

Teiendo ya la configuración de los logs de nuestro sistema, podemos dar paso a la configuración del entorno de docker para ELK.

## ELK y Docker

El trabajo con ELK no es muy complicado, simplemente utilizamos el docker compose brindado por una de estas tres opciones:

- [github](https://github.com/deviantony/docker-elk/blob/main/docker-compose.yml)
- [elastic](https://www.elastic.co/blog/getting-started-with-the-elastic-stack-and-docker-compose)
- [docker-compose](./docker-compose.yml)

## ELK y Filebeat

La idea general del docker compose que podemos encontrar en la sección anterior es levantar cuatro servicios. 

- elasticsearch
    - Este servicio se encarga de recibir los logs de diferentes fuentes. Una de ellas es logstash.
- logstash
    - Este servicio tiene como objetivo principal la recepción, modificación y filtrado de los logs; para posteriormente enviarlos a elasticsearch.
    - Para modificar el comportamiento de envío de datos debemos crear un archivo de comandos (pipeline): [Configuración_LogStash](./extra/logstash.conf)
- filebeat
    - Este servicio es bastante interesante. Surge como un recopilador de logs de contenedores docker. O sea, nuestras aplicaciones docker pueden desprenderse de la configuración de envío de logs a un endpoint en específico y concentrarse solamente en el trabajo que se les fue asigando. Entonces, filebeat se encarga de acceder a dichos contenedores y escuchar cualquier acción de tipo "log" que se ejecute; la captura y la envía a otro servicio como logstash.
- kibana
    - Último servicio del stack ELK y es el encargado de presentar todos los logs recopilados en nuestras aplicaciones. Estos datos son mostrados al cliente mediante un dashboard que recoge en diferentes gráficos los datos envíados por los logs.
    
# Conclusiones

Espero que este pequeño tutorial haya sido de su agrado. Si encuentran algún error o demora, no duden enviar un mensaje por github para realizar los cambios pertinentes. No dejen de aprender y nos vemos pronto.

# Bibliografia
- https://salithachathuranga94.medium.com/integrate-elk-stack-into-spring-boot-application-ae38a6371f86
- https://howtodoinjava.com/spring-cloud/elk-stack-tutorial-example/
- https://bigbear.ai/blog/solution-for-distributed-tracing-with-spring-boot-and-elk/
- https://javatechonline.com/how-to-monitor-spring-boot-microservices-using-elk-stack/
- https://www.javainuse.com/elasticsearch/filebeat-elk
- ** https://cassiomolin.com/2019/06/30/log-aggregation-with-spring-boot-elastic-stack-and-docker/ 
- ** https://github.com/cassiomolin/log-aggregation-spring-boot-elastic-stack
- https://auth0.com/blog/spring-boot-logs-aggregation-and-monitoring-using-elk-stack/
- https://github.com/zhaoqingchris/springboot-elk-filebeat-example
- https://github.com/npalma9006/elastic-stack
- https://www.adictosaltrabajo.com/2016/06/02/analisis-de-logs-con-kibana/
- https://spring.io/blog/2022/10/12/observability-with-spring-boot-3
- https://stackoverflow.com/questions/65688048/cant-see-traceid-and-spanid-in-log-for-sleuth
- https://discuss.elastic.co/t/volume-mapped-filebeat-yml-permissions-from-docker-on-windows-host/91893


## Notas

- Cuando estamos trabajando con ELK y creamos el archivo docker compose; lo normal es limitar la cantidad de RAM que consumen. En mi primer intento puse todos los servicios en 250 y esto hizo que LogStash se reiniciara. Por lo que subirlo a 500 no resolvió del todo el problema ya que explotó cuando se hicieron muchas peticiones de forma consecutiva. Terminamos podiendole **1gb** a todos los contenedores de ELK.

## Problemas y soluciones

### Problema:

Tenemos un contenedor con la ram limitada a 250 mb. Vemos que el contenedor no levanta y docker muestra en el estado lo siguiente:

- exited 137

Este error hace referencia a que el servicio (contenedor) está solicitando más ram de la asignada por lo que no se puede ejecutar.

### Solución:

Aumentar el tamaño de la ram. En este caso se aumento 250 mb más. Pero si el software pertenece a un stack como es ELK, podemos ver cuanto consumen los demas servicios y después utilizar esa cantidad.

### Problema

Cuando levantamos por primera vez Filebeat en docker, nos mostró el siguiente error:

```bash
error loading config file: config file ("filebeat.yml") can only be writable by the owner but the permissions are "-rwxrwxrwx" (to fix the permissions use: 'chmod go-w /usr/share/filebeat/filebeat.yml')
```

Esto nos indica que el archivo donde está la información de configuración de filebeat tiene todos los permisos del sistema y no debería ser así.

### Solución

La solución a este problema está en poner la línea de código que veremos a continuación en el docker compose:

entrypoint: "filebeat -e -strict.perms=false"

### Problema:

```bash
Caused by: org.elasticsearch.action.search.SearchPhaseExecutionException: Search rejected due to missing shards [[.kibana_7.17.10_001][0]]. Consider using `allow_partial_search_results` setting to bypass this error.
```

Este problema se presentó cuando levantamos Elasticsearch si haber eliminado por completo el volumen; por lo que información antigua se quedó almacenada.

### Solución

Eliminar manualmente la carpeta de la computadora vinculada al volumen de elsaticsearch.