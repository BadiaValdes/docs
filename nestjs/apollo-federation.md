# Proyecto de prueba de Nestjs

## Crear una app de nestjs

En Nestjs puedes crear una app global que contenga diferentes aplicaciones. De esta forma, se pueden compartir entre las diferentes aplicaciones los paquetes instalados. Para crear una aplicación, podemos utilizar el siguiente comando:

- `nest g app` -> Dentro de la misma carpeta

> Por favor, siga las instrucciones de la consola. Puedes ejecutar este comando por cada app a crear dentro de la carpeta y nestjs se encargará de gestionar todo por ti.

Para crear un recurso dentro de cualquiera de las aplicaciones, ejecutamos el comando `nest g resource name` en la carpeta principal y antes de hacer cualquier otra cosa preguntará en que aplicación vas a crear el recurso.

## Paquetes a instalar para apollo federation

- @apollo/gateway
- @apollo/subgraph
- @nestjs/apollo
- @nestjs/graphql
- @apollo/server
- apollo-server-express
- graphql

## Habilitar la federación 

En vez de inicializar `GraphQLModule` de la forma acostumbrada, cambiamos a esta implementación:

```ts
GraphQLModule.forRoot<ApolloFederationDriverConfig>(
    {
        driver: ApolloFederationDriver,
        autoSchemaFile: {
        federation: 2
        }
    }
)
```