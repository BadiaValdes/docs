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

# Anotaciones

## Formas de las rutas

Hasta ahora he visto dos formas de trabajar con las rutas. Mediante DSL (JAVA) o mediante Blueprint (XML). La primera la vimos anteriormente; en este apartado veremos la de XML (explicaremos las distintas etiquetas):

