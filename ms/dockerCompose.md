# Docker Composes

Docker Compose es una herramienta que permite definir y ejecutar aplicaciones Docker multi-contenedor. Permite definir la configuración de los servicios, redes y volúmenes necesarios para ejecutar una aplicación en diferentes contenedores de Docker. Con Docker Compose, se puede definir la estructura de la aplicación en un archivo YAML y luego utilizar un solo comando para crear y ejecutar todos los contenedores necesarios para la aplicación. Esto facilita la gestión de aplicaciones complejas que requieren múltiples contenedores y simplifica el proceso de despliegue y escalado de las aplicaciones. (Sacado de ChatGPT)

## Extendiendo de archivos docker

En algunas ocaciones tenemos ya archivos docker definidos que no queremos tocar ya sea por miedo al cambio o para evitar redundancia de datos. En este tipo de casos pordemos utilizar un docker-compose nuevo que su misión principal es extender de otros archivos del mismo tipo y añadir o sobreescribir algunas configuraciones. Para ello, vamos a guiarnos por el siguiente ejemplo:

> Archivo base para crear una base de datos
```yml
version: '3.8'
services:
  ms_item_db:
    image: postgres:15.1-alpine
    restart: always
    environment:
      POSTGRES_USER: ${POSTGRES_USER}
      POSTGRES_PASSWORD: ${POSTGRES_PASSWORD}   
      APP_DB_USER: ${APP_DB_USER}   
      APP_DB_PASS: ${APP_DB_PASS}   
      APP_DB_NAME: ${APP_DB_NAME}  
    ports:
      - '5433:5432'
    volumes: 
      - db_item:/var/lib/postgresql/data
      - ./scripts/:/docker-entrypoint-initdb.d/    
volumes:
  db_item:
    driver: local
```
> Extender de ese archivo base y agregar configuraciones
```yml
  ms_item_db:
    extends:
      service: ms_item_db
      file: ./database/postgres/docker-compose.yml
    networks:
      - poc-test
    restart: on-failure
```

El nombre que le pongamos al servicio dentro de `docker compose` puede variar, pero el que se debe mantener inmutable es el que está siendo utilizado por la etiqueta `service` dentro de `extends`, ya que hace referencia al servicio que estamos extendiendo. Seguidamente se debe declarar la propiedad `file` que permite apuntar hacia el `docker compose` que queramos utililzar. Por último podemos sobreescribir o agregar nuevas proiedades; en este caso se agregaron las propiedades `network` y `restart`.

Hay que tener en cuenta dos cosas. El extends solo trae la configuraci'on especificada del docker compose. Es decir, si se usan variables de entorno dentro de archivo original, el archivo extendido debe tener un archivo .env donde aparezcan esas mismas variables de entorno. El caso de los volumenes es igual. El valumen que se declare en el servicio, debe ser declararado en el docker compose que extiende. Lo mismo sucede con cualquier archivo externo que sea utilizado en el proceso.

## Variables de entorno con valores por defecto y mensajes de error

Ahora veamos como podemos sacar más probecho de las variables de entonro de nuestro archivo docker. Partamos del siguiente ejemplo:

```yml
environment:
    KC_DB_URL: jdbc:postgresql://${APP_DB:-keycloakdb}:${APP_DB_PORT:-5434}/${APP_DB_NAME:?Database not found}  
```


> Utilizando valor por defecto
```yml
KC_DB_URL: jdbc:postgresql://${APP_DB:-keycloakdb}:${APP_DB_PORT:-5434}
```

Como se puede observar en la variable de entorno KC_DB_URL estamos insertanto la URL hacia el servicio de postgres. En este caso vamos a conectar nuestro servicio contra postgres, pero para esta conexión estamos usando variables de entorno. Pero, no necesariamente tenemos que pasarle estos valores, ya que si estamos en el proceso de desarrollo no tenemos que usar un archivo .env. En este caso, docker compose nos permite definir un valor por defecto a la variable de entorno mediante la anotación `-VALOR`.

> Utilizando mensajes de error.
```yml
${APP_DB_NAME:?Database not found}  
```

Existen casos donde no queremos tener valores por defecto y obligar a que se usen los datos dentro de la variable de entorno. Para ellos, buscamos una alternativa. Si el dato perteneciente a la variable de entorno no existe, le mostramos un mensaje de error al usuario mediante le comando `?Mensaje de error`


# Conclusiones

Espero que este pequeño tutorial haya sido de su agrado. Si encuentran algún error o demora, no duden enviar un mensaje por github para realizar los cambios pertinentes. No dejen de aprender y nos vemos pronto.

# Bibliografía
- https://github.com/Yelp/docker-compose/blob/master/docs/extends.md
- https://serversforhackers.com/dockerized-app/compose-separated

# Notas.

- Para conectarme a un docker de postgres dentro del mismo docker compose se debe usar el puerto interno y no el que se expone. Para cambiar el puerto interno de postgres debemos agregar `command: -p PORT` al docker compose de postgres.