# Resumen MicroServicios

Los microservicios son una arquitectura de software que consiste en la creación de pequeños servicios independientes y altamente especializados, que se comunican entre sí mediante una API. Cada microservicio se enfoca en una tarea específica y se ejecuta en su propio proceso, lo que permite una mayor escalabilidad, flexibilidad y modularidad en el diseño de las aplicaciones.

# Spring Cloud

> Framework for cloud apps
Makes easy the MS construction and configuration
> 

// FOR MORE INFO: [https://www.baeldung.com/spring-cloud-bootstrapping](https://www.baeldung.com/spring-cloud-bootstrapping)

## Componentes

### Server de configuración (Config Server)

- Permite centralizar archivos de configuración para todos los MS
- Su premisa es un lugar para manejarlos a todos.
- Se debe crear un repositorio local (git) en la PC donde estará el servidor
- Hacemos que spring cloud apunte a esa carpeta

### Discovery (EUREKA SERVER)

- Permite que los microservicios se encuentren entre ellos
- Este se usa para permitir encontrar los diferentes ms sin importar que dirección ip tengan.
- Para activar el servidor de eureka, debemos añadir @EnableEurekaServer arriba de la clase principal.
- Para decirle que utilice el sistema de configuración, debemos añadir al archivo boostrap.properties:
-> spring.cloud.config.name=discovery // Decide que archivo utilizar de configuracion
-> spring.cloud.config.uri=http://localhost:8081 // Dirección del servicio de configuración
- Ahora solo debemos crear un archivo llamado discovery.properties en el repositorio de configuración.

### Gateway

- Permite definir un proxy de entrada a los MS donde se pueden manejar configuraciones globales como la autenticación.

## Patrones más comunes en los MS:

1. **API Gateway**: Un API Gateway es un único punto de entrada para todas las solicitudes de los clientes que las dirige al microservicio adecuado en función de la solicitud. También puede realizar autenticación y otras cuestiones transversales.
2. **Service Registry**: Un registro de servicios es un registro central que mantiene una lista de todos los microservicios disponibles y sus direcciones de punto final. Esto permite a otros microservicios descubrir y comunicarse entre sí sin hardcoding las direcciones de punto final.
3. **Service Discovery**: El descubrimiento de servicios es el proceso de descubrir automáticamente microservicios disponibles en un sistema distribuido. Esto puede hacerse a través de un Registro de Servicios u otros mecanismos, como DNS o multicast.
4. **Circuit Breaker**: Un Circuit Breaker es un patrón que previene los fallos en cascada en un sistema distribuido, automáticamente automáticamente el circuito y volviendo a un comportamiento por defecto cuando un microservicio no está disponible o experimenta una alta latencia.
5. **Event Sourcing**: Event Sourcing es un patrón en el que el estado de un microservicio se deriva de una secuencia de eventos que se han registrado a lo largo del tiempo. Esto permite una fácil auditoría, rollback, y repetición de eventos.
6. **Command Query Responsibility Segregation (CQRS)**: CQRS es un patrón en el que las operaciones de lectura y escritura de un microservicio se separan en modelos diferentes. Esto permite una mejor escalabilidad, rendimiento y flexibilidad.
7. **Saga**: Una Saga es un patrón que gestiona transacciones transacciones en un sistema distribuido dividiéndolas en una serie de transacciones más pequeñas e independientes que pueden ser revertidas o compensadas si alguna de ellas falla.
8. **Bulkhead**: Un Bulkhead es un patrón que aísla fallos en un sistema distribuido particionando o agrupando microservicios en grupos o hilos separados. Esto evita que los fallos se extiendan por el sistema y causen tiempos de inactividad o problemas de problemas de rendimiento.
9. **Sidecar**: Un Sidecar es un patrón en el que un proceso o contenedor separado o contenedor se despliega junto a un microservicio para proporcionar funcionalidad adicional, como descubrimiento de servicios, balanceo de carga o seguridad.