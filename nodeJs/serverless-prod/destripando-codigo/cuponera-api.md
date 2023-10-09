# Cuponera

Este documento está dedicado a ver los códigos dentro del proyecto Cuponera. Cuponera es una aplicación de venta de comida online. Escrita en `Javascript` con `Nodejs` y adaptada para trabajar con la arquitectura `serverless` de AWS. Veamos como está distribuida la app:

![Cuponera Estructure](./assets/cuponera-structura.png)

- `Api`: Aquí dentro están almacenadas la lógica de la aplicación.
    - `Controladores`: Son funciones que nos facilitaran el trabajo con las lambdas. Algunas de las funciones aquí dentro se encargarán de manipular las peticiones como se hace en el patrón MVC.
    - `Entidades`: Se definen las estructuras de los objetos que se van a utilizar.
    - `Helpers`: Métodos reutilizables en toda la aplicación.
    - `Lambdas`: Son todas las funciones encargadas de ejecutarse en el servidor de aws.
    - `Middlewares`: Los middlewares son funciones que se ejecutan antes o después de una petición HTTP. Se encargan de agregar, comprobar o extraer información de las peticiones.
    - `Routes`: Aquí se declaran las rutas HTTP del servidor. Es como declarar las rutas REST de nuestra APP.
    - `Swagger`: Dentro de esta carpeta esta la documentación de la app, además posee un sistema de rutas aparte de `Routes`.
    - `Validators`: Clases que apoyan la validación de los datos.
- `Resources`: Aquí se declaran en archivos `yml` los recursos (tablas de dynamo) que se utilizarán en la app.
- `Utils`: Se declaran las constantes del proyecto y las estructuras de los response usados en el controlador y las peticiones http.

## Archivos de configuración

- `buildspec.yml`: Aquí se definen los pasos para la construcción de la app. Este archivo le permite a `AWS` saber que pasos debe seguir para la instalación de dependencias y despliegue. Este archivo se lo presentaremos a continuación y describiremos un poco como funciona:

```yml
version: 0.2
run-as: root # Como debe ser ejecutado este archivo
phases: # Fases por la que pasa el archivo
  install: # Cuando se va a instalar
    runtime-versions: # Versiones a utilizar de los paquetes 
      nodejs: 16 # Version de node js
    commands: # Comandos a ejecutar durante la instalación
      - echo Installing Serverless... # Echo es para mostrar mensajes en consola
      - npm install -g serverless@latest # Instalar global serverless en aws
      #- serverless upgrade --major
  pre_build: # Antes de compilar el archivo
    commands: # Comandos
        - echo Installing source NPM dependencies... # Mensaje a mostrar en consola
        - npm install # Instalar dependencias del package.json
        - npm install # Lo mismo, dicen que si no lo hacemos doble, el sistema hace kaput
  build: # Fase de construcción (Desplegar nuestro serverless)
    commands: # Comandos
        - echo Starting deploy on branch ${STAGE} # Mostramos donde estamos y el stage que estamos utilizando
        - serverless deploy --stage ${STAGE} # Desplegamos nuestra aplicación serverless con nuestro estado actual
        - serverless prune -n 4 --stage ${STAGE} # Prune nos permite eliminar cualquier despliegue viejo de la app.

cache: # usamos el cache para evitar repetir archivos ya existentes que sean importantes
  paths: # Le decimos el camino del archivo o carpeta que queremos dejar en cache
    - node_modules # En este caso estamos apuntando a la carpeta node modules.
```

- `handler.js`: Este archivo se utiliza para exportar o más bien declarar la aplicación `express` con las rutas. Veamos este archivo por completo (Claro vamos a separarlo):

> Importación: 

```js
const handler = require("serverless-express/handler"); // Desde la dependencia serverless-express tomamos la clase handler encargada de ejecutar o mejor dicho, manejar nuestra aplicación de express.
const express = require("serverless-express/express"); // Nos permite acceder a las funcionalidades de express. Esta función será más adlenate utilizada para crear la app.
const bodyParser = require("body-parser"); // Nos permite definir la forma a utilizar en el cuerpo de las peticiones. Más adelante veremos como utilizarlo.
const cors = require("cors"); // Nos permite configurar el acceso de los CORS de nuestra app.
const helmet = require("helmet"); // Esta importación no es utilizada en el código pero la librería helmet se encarga de asegurar peticiones http en express

const path = require("path"); // Nos permite acceder al path de la app. De esta forma accedemos a archivos.

const swgUI = require("swagger-ui-express"); // Permite añadir la documentación swagger a express
const swgJsDoc = require("swagger-jsdoc"); //  Este nos permite obtener los archivos js que tengan solo anotaciones swagger y convertirlos en nuestra documentación
const swgSpec = { // Esto es la información general de swagger.
  definition: {
    openapi: "3.0.0",
    info: {
      title: "Cuponera Digital",
      version: "1.0.0",
    },
  },
  apis: [`${path.join(__dirname, "./api/swagger/*.js")}`], // Definimos el path donde están los archivos js para utilizar en la documentación.
};

//const fileupload = require("express-fileupload");
const multer = require("multer"); // Multer es un paquete que nos ayuda con la subida de archivos
const storage = multer.memoryStorage(); // Usamos memory storage de multer para realizar salvas en nuestra memoria hasta subirlas a la nube.
const upload = multer({ storage: storage }); // Usamos las dos variables anteriores y creamos nuestra instancia de multer con el valor del storage.
```

> Rutas

```js
const indexRoutes = require("./api/routes/index");
const configuracionesRoutes = require("./api/routes/configuraciones");
const campanasRoutes = require("./api/routes/campanas");
const promocionesRoutes = require("./api/routes/promociones");
const reglasRoutes = require("./api/routes/reglas");
const restriccionesRoutes = require("./api/routes/restricciones");
const uploadRoutes = require("./api/routes/upload");
const usuariosRoutes = require("./api/routes/usuarios");
const cuponRoutes = require("./api/routes/cupon");
const transaccionesRoutes = require("./api/routes/transacciones");
const redencionesPromocionesRoutes = require("./api/routes/redencionesPromociones");
const redencionesMarcasRoutes = require("./api/routes/redencionesMarcas");
const tiendasRoutes = require("./api/routes/tiendas");
const starbucks = require("./api/routes/starbucks");
const contadorEntidades = require("./api/routes/contadorEntidades");
const wow = require("./api/routes/wow");
const { required } = require("./api/entidades/reglas");
```

Este archivo es bastante sencillo y se explica por si solo, menos la última variable. Las 16 primeras variables son importaciones de las diferentes rutas que poseemos en nuestra aplicación. Cada uno de esas importaciones es una exportación de la variable `router`. Dicha variable pertenece al ecosistema de express y nos permite crear rutas HTTP, no usables hasta que las agreguemos a nuestra aplicación, lo veremos más adelante.

La última importación (no utilizada) nos permite acceder a una entidad encargada de validar los datos de entrada, decidiendo el tipo de dato a recibir y la estructura. Es algo extraño no utilizarlo, pero vale la pena mencionarlo.

> Nuestra App

```js
const app = express(); // Esto lo mencionamos anteriormente, almacenando en una variable la función express, nos permite crear una instancia de este servidor.

// App.use nos permite agregar comportamiento a express. En este caso estamos agregando a express una restricción con el bodyParser
app.use(
  bodyParser.json({
    limit: "4096kb", // Limitamos el tamaño de los datos en formato json que lleguen a la aplicación
  })
);
app.use(
  bodyParser.urlencoded({
    extended: false, // Similar pero estas modificando el uso de la codificación de la url
  })
);
app.use(
  cors({ // En este caso estamos diciéndole a express que utilice una configuración específica para el manejo de los cors.
    maxAge: 604800000,
  })
);


// De la misma forma que podemos definir configuraciones de express con use, podemos decirle que rutas utilizar y como utilizarlas.
app.use("/index", indexRoutes); // Por cada ruta tenemos un endpoint, en este caso /index y la importación de una variable de tipo Router.
app.use("/configuraciones", configuracionesRoutes);
app.use("/campanas", campanasRoutes);
app.use("/promociones", promocionesRoutes);
app.use("/reglas", reglasRoutes);
app.use("/restricciones", restriccionesRoutes);
app.use("/upload", uploadRoutes);
app.use("/usuarios", usuariosRoutes);
app.use("/cupon", cuponRoutes);
app.use("/transacciones", transaccionesRoutes);
app.use("/redencionesXpromociones", redencionesPromocionesRoutes);
app.use("/redencionesXmarcas", redencionesMarcasRoutes);
app.use("/tiendas", tiendasRoutes);
app.use("/starbucks", starbucks);
app.use("/contadorEntidades", contadorEntidades);
app.use("/wow", wow);

app.use("/api-docs", swgUI.serve, swgUI.setup(swgJsDoc(swgSpec)));

// Ahora estamos creando una función middleware. Es decir, los use que tengan una función directamente como parámetro, será utilizada como middleware
app.use((req, res, next) => {
  const err = new Error("Not Found");
  console.log(req.path);
  err.status = 404;
  next(err);
});
app.use(async (err, req, res) => {
  // set locals, only providing error in de
  res.locals.message = err.message;
  res.locals.error = process.env.stage === "dev" ? err : {};
  // render the error page
  res.status(err.status || 500);
  res.json(err);
});
```

> Export handler

```js
module.exports = {
  handler: handler(app),
};
```

Por último utilizamos la variable `handler` para crear un envoltorio alrededor de nuestra `app` de `express`. Este envoltorio permite encapsular la lógica de `express` dentro de nuestra aplicación `serverless`. De esta forma nos olvidamos de cualquier problema de compatibilidad que pueda existir entre las dos tecnologías.

- `Serverless.js`

Ya hemos vistos en tutorial anteriores como funciona la configuración de express, pero hasta el momento no hemos visto ninguna configuración `serverless` `ready to prod`. En este apartado antes de entrar a los códigos de la app, veamos como está compuesto el archivo `serverless`:

> Providers

``` yml
provider: # Declaramos los proveedores 
  name: aws # Nombre del proveedor
  runtime: nodejs16.x # Version de node para ejecutar serverless
  stage: ${opt:stage, 'dev'} # Definimos el stage de nuestra app. Si no se le pasa por variable de entorno ningún dato, utilizamos dev por defecto
  apiGateway: # Configuramos la api gateway
    shouldStartNameWithService: true # Esta opción nos permite definir si queremos que los nombres de nuestras rutas posean los nombres de nuestros servicios. Por ejemplo mi-servicio. Será nombrado mi-servicio-api-dev; el dev es por el stage que estemos utilizando.
    binaryMediaTypes: # Definimos el Media Type para los archivos binarios. En este caso usamos multipart/form-data debido a que necesitamos subir los archivos.
      - "multipart/form-data"
  tracing: # Si queremos llevar un log de las acciones que se realizan en el api gateway y en los lambdas
    apiGateway: true
    lambda: true
  timeout: 28 # Definimos el time out general para las operaciones
  region: ${opt:region, "eu-west-1"} # Definimos la región sobre la que estaremos trabajando. Recuerda que las regiones son imperante para trabajar en aws
  memorySize: 1024 # Definimos el tamaño máximo de la memoria a utilizar por la app. Mientras mayor sea, más se nos cobrará.
  logRetentionInDays: 30 # Definimos cuanto tiempo queremos que nuestros logs estén almacenados. 
  environment: # Definimos las variables de entorno a utilizar dentro de nuestra app
    STAGE: ${self:provider.stage} # En este caso estamos definiendo la variable de entorno STAGE y este va a buscar su valor dentro del archivo serverless, específicamente dentro de provider y posteriormente en stage -> stage: ${opt:stage, 'dev'}
    REGION: ${self:provider.region}
    # Hay más variables de entorno, pero por seguridad no las mostraremos
  lambdaHashingVersion: 20201221 # Esto define que versión de lambdas se estará utilizando.
  iamRoleStatements: # Le damos los roles para el acceso a los datos
     - Effect: Allow # Permitimos las acción
       Action: "*" # Definimos las acciones
       Resource: "*" # Definimos los recursos sobre los que realizaremos las acciones.
```

> Funciones

Esta sección la vamos a dividir en diferentes partes para poder entenderla bien. Comencemos con la exposición de nuestra `REST API` de `express` mediante `serverless`.

```yml
functions:
  app:
    handler: handler.handler
    events:
      - http:
          path: /{proxy+}
          method: "any"
          cors: true
```

Dentro del apartado de funciones: 
- declaramos una función llamada `app`
- en la propiedad `handler`, le decimos que utilice el archivo `handler` y dentro de dicho archivo, la propiedad exportada `handler`. De esta forma estamos utilizando los `endpoints` de `express` declarados y manejándolos mediante funciones `lambda`.
- el `path` nos permite definir la ruta de acceso a la función. En este caso estamos definiendo una ruta dinámica, ya que cualquier petición que llegue a nuestro endpoint será manejada por dicha función. esto se debe al uso de `{proxy+}`.
- `method` nos permite definir si aceptamos peticiones `get`, `put`, `post`, etc. En este caso definimos que puede ser cualquier tipo de petición.
- `cors` indica que existirá una comprobación de este tipo de cabecera.

Seguidamente vamos a ver una conexión realizada a `websocket`:

> WebSockets es un protocolo de comunicación bidireccional en tiempo real que se utiliza para establecer una conexión persistente entre un cliente y un servidor a través de una única conexión TCP. A diferencia del protocolo HTTP tradicional, que sigue un modelo de solicitud-respuesta, WebSockets permite una comunicación continua y en tiempo real entre el cliente y el servidor.


```yml
websocket:
  handler: api/lambdas/websocket.handler
  events:
    - websocket:
        route: $connect
    - websocket:
        route: $disconnect
    - websocket:
        route: $default
```

En este caso estamos realizando una conexión a un lambda que será disparado (estará escuchando los eventos) de tipo `webSocket`. Dentro de cada evento de `webSocket`, es decir:
  - `$connect` -> Hace referencia a ejecutar la función cuando se conecte un usuario
  - `$disconnect` -> Hace referencia a ejecutar la función cuando se desconecte un usuario
  - `$default` -> Hace referencia a ejecutar la función cuando el usuario realice una acción dentro de la app.

Seguimos bajando en la configuración y vamos a ver ahora una función que **incluye** **otro** **archivo** para ejecutarse:

> En el código esto es una ruta de prueba

```yml
  generaCuponesMultiples:
    handler: api/lambdas/generaCuponesMultiples.handler
    timeout: 900
    package:
      include:
        - api/lambdas/generaCuponesMultiples.js
    events:
      - http:
          path: cuponesMultiples
          method: post
          integration: AWS
          request:
            parameters:
              headers:
                "X-Amz-Invocation-Type": true
```

Veamos solamente las opciones nuevas que distan de las vistas anteriormente:
- `timeout`: En los providers teníamos una opción de `timeout` que podía ser utilizada por todas las funciones que declaremos. Pero es posible sobrescribir ese comportamiento. En este caso estamos definiendo un nuevo `timeout` solo para esta función.
- `package`: Nos permite definir que otros paquetes podemos utilizar dentro de nuestra función. Recuerden que las `funciones` de aws están dormidas hasta que sean ejecutadas, por lo que para utilizar cualquier otro archivo dentro de la ejecución, necesitamos importarlo.
- `events.http.integration`: Este parámetro de integración define que la petición realizada hacia ese endpoint será manejado por el propio `aws`.
- `request`: Dentro definimos que datos deberían estar dentro de la petición.
  - `paremeters`: Define los parámetros que debe poseer el `request`.
    - `headers`: podemos listar todos los parámetros dentro de header que deben estar incluidos en el `request`.

> Es importante decir que no todas las funciones lambdas que declaremos, deben tener un evento para ser ejecutadas. Ya que algunas serán utilizadas por otras lambdas o dentro de una máquina de estado y no necesitamos exponerlas a un evento en específico.
> Ejemplo:
>
> ```
>  preGeneraCupones:
>    handler: api/lambdas/preGeneraCupones.handler
>    timeout: 900
>    package:
>      include:
>        - api/lambdas/preGeneraCupones.js
> ```

Pasemos a ver como crear funciones programables en el tiempo (`schedules` o `cronjobs`):

El uso de este tipo de eventos viene dado por la necesidad de crear tareas automatizadas. O mejor dicho, tareas que se ejecuten cada cierto tiempo.

```yml
 mantenimientos:
    handler: api/lambdas/mantenimientos.handler
    package:
      include:
        - api/lambdas/mantenimientos.js
    events:
      - schedule:
          rate: cron(8 8 * * ? *)
          enabled: true
```

Dejemos de lado el `handler` y el `package` que ya conocemos como funciona. Vamos a centrarnos en `schedule`. Este tipo de evento permite definir un intervalo de tiempo para la ejecución de una función. El atributo `rate` no permite definir el tiempo que se utilizará para cronometrar la función; `cron` sería la función para cronometrar y los valores que posee dentro nos permite definir el intervalo de ejecución. Para generar un intervalo sin rompernos tanto la cabeza, podemos dirigirnos a: https://crontab.cronhub.io/. 

Según ese sitio web, la función se ejecutará todos los días a las 8 y 8 AM.

Las demás funciones dentro de este apartado son similares a las vistas anteriormente, así que pasemos al próximo punto de configuración. Las `step functions`. Pondremos un ejemplo de como se declara la step function y explicaremos las líneas de código, pero para la creación de una, les pedimos que usen la interfaz gráfica proporcionada por `aws`; si tiene duda de como utilizarla, por favor vea el siguiente tutorial (realizado también por nuestro equipo): https://github.com/BadiaValdes/docs/blob/doc/nodeJs/serverless-prod/step-function.md

```yml
stepFunctions: # Declaración de las step functions
  stateMachines: # Declaración de las máquinas de estado
    CuponStateMachineExpress: # Nombre de la máquina de estado
      name: CuponStateMachineExpress-${self:provider.stage} # El mismo nombre, pero será usado por AWS
      type: EXPRESS # Existen dos tipos de máquinas de estado. Ver el link anterior
      events: # Evento que la desencadena
        - http:
            path: /v1/{action} # path para llegar al evento
            method: ANY # Cualquier método http
            action: StartSyncExecution # Cuando se acceda a la función inicial del step function comience una ejecución síncrona
            authorizer: # Autorización para ejecutar la función
              name: autorizador
              type: token
              identitySource: method.request.header.Authorization
            request: # El request que va a recibir la máquina de estado
              parameters: # Definición de los parámetros
                paths:
                  action: true
              template: lambda_proxy # Plantilla que se va a utilizar
            response: # Definimos la forma de realizar el response de la maquina de estado
              template: # La plantilla a utilizar para dar la respuesta
                application/json: |
                  #set( $body = $util.parseJson($input.json('$.output')) )
                  $body

      tracingConfig: # Dar seguimiento a la máquina de estado
        enabled: true
      loggingConfig: # Que vamos a mandar mantener en los logs
        level: ALL
        includeExecutionData: true 
        destinations: # Esos logs a quien pertenecen
          - Fn::GetAtt: [StateMachineLogGroup, Arn]

      definition: # Definimos la máquina de estado
        Comment: A description of my state machine # Comentario
        StartAt: ErrorHandler # Estado inicial (Nombre del estado)
        States: # Estados
          ErrorHandler: # Nombre del estado a crear
            Type: Parallel # Tipo, el parallel nos permite ejecutar multiples funciones o acciones de forma simultánea.
            Branches: # Ramificaciones del estado paralelo.
              - StartAt: ValidarInput # Initial state of a branch
                States: # estados de la primera rama
                  ValidarInput: # Nombre del primer estado
                    Type: Task # Tipo de estado
                    Resource: # Recurso a utilizar. En este caso es la función validar input
                      Fn::GetAtt: [validarInput, Arn] # Ojo etas funciones deben estar declaradas en el apartado de functions. Aunque no utilicen ningún event
                    Next: ObtenerTienda # Próximo estado

                  ObtenerTienda: # Nombre de estado
                    Type: Task # Tipo
                    Resource: arn:aws:states:::dynamodb:getItem # Recurso a utilizar. En este caso está usando dynamoDb para realizar una búsqueda
                    Parameters: # Parámetros a utilizar en dinamo db
                      TableName: ${self:provider.environment.MANAGEMENT_TABLE}
                      Key: # Claves a utilizar (OJO en dynamo existe la clave y todos los demás datos. En este casos se declaró una PK como llave primaria por así decirle y un segundo valor para la búsqueda que es el SK.)
                        pk:
                          S.$: $.body.tiendaSk
                        sk:
                          S.$: $.body.ceco
                    OutputPath: $ # Define que parte de la información utilizada será enviada al próximo estado.
                    Next: ValidarTienda # Proxima función
                    ResultPath: "$.body.tienda" # Específicamente se va a guardar en la variable tienda

                  ValidarTienda: #Lo mismo que el estado Validar input
                    Type: Task
                    Resource:
                      Fn::GetAtt: [validarTienda, Arn]
                    Next: ObtenerCupon

                  ObtenerCupon: # Lo mismo que Obtener Tienda
                    Type: Task 
                    Resource: arn:aws:states:::dynamodb:getItem
                    Parameters:
                      TableName: ${self:provider.environment.MANAGEMENT_TABLE}
                      Key:
                        pk:
                          S.$: $.body.pk
                        sk:
                          S.$: $.body.sk
                    OutputPath: $
                    Next: ExisteCupon
                    ResultPath: "$.body.cupon"
                  
                  ExisteCupon: # Ejecutar método para comprobar que el cupón es correcto
                    Type: Task
                    Resource:
                      Fn::GetAtt: [existeCupon, Arn]
                    Next: ObtenerPromo

                  ObtenerPromo: # Obtener de la base de dato el cupon
                    Type: Task
                    Resource: arn:aws:states:::dynamodb:getItem
                    Parameters:
                      TableName: ${self:provider.environment.MANAGEMENT_TABLE}
                      Key:
                        pk:
                          S.$: States.Format('{}®PR', $.body.pais)
                        sk:
                          S.$: $.body.cupon.promocion.sk
                    Next: ValidarCupon # Validar el cupón
                    ResultPath: $.body.promocion
                    OutputPath: $

                  ValidarCupon:
                    Type: Task
                    Resource:
                      Fn::GetAtt: [validarCupon, Arn]
                    End: true # Define el fin de la maquina de estado

            Catch: # Capturar el error de la ejecución paralela
              - ErrorEquals:
                  - States.ALL
                Next: PassError # Si existe un error enviarlo a otro método
                ResultPath: $.error
            Next: ArrayToSingle # Si la ejecución paralela se realizó correctamente, entonces pasar a este paso

          PassError: # Estado de manipulación del error
            Type: Pass # ESte estado es de pasada nada más. No se manipula nada.
            OutputPath: $.error # Que parte de los datos que tenemos de la función pasará al siguiente paso, en este caso solo el error.
            Next: MensajeError # Próximo en la cadena

          MensajeError: # Estado para manejar el mensaje de error a enviar
            Type: Task
            Resource:
              Fn::GetAtt: [mensajeError, Arn] # Este usa una función
            End: true

          ArrayToSingle: # Este nos permite modificar los datos de salida. 
            Type: Pass # Solo eso, es un paso intermedio
            InputPath: $[0] # De todo el arreglo devuelto de la ejecución paralela, me quedo con el primero
            ResultPath: $ # Donde vamos a almacenar esos datos. Repetimos, el $ significa el path completo a utilizar
            Next: RedimirValidar # Proximo paso

          RedimirValidar: # EStado IF
            Type: Choice # Nos permite crear bifurcaciones en una maquina de estado según una condición dada. Podemos tener cuantas opciones queramos.
            Choices: # Aquí declaramos las opciones
              - Variable: $.path # Dentro del los datos de la petición, busca la variable path
                StringEquals: ${self:provider.environment.VALENDPOINT} # Compara la variable path con un valor 
                Next: RetornarCuponValido # QUien en el proximo estado
              - Variable: $.path # Lo mismo que arriba
                StringEquals: ${self:provider.environment.REDEMPENDPOINT}
                Next: RedimirCupon

          RedimirCupon: # Llamar a función para realizar una operación.
            Type: Task
            Resource:
              Fn::GetAtt: [redimirCupon, Arn]
            End: true
            Catch: # Si existe un error, realizar el siguiente paso en vez del declarado anteriormente
              - ErrorEquals:
                  - States.ALL
                Next: PassError
                ResultPath: $

          RetornarCuponValido:
            Type: Task
            Resource:
              Fn::GetAtt: [retornaCupon, Arn]
            End: true # Fin de la máquina de estado
```

Todo el código anterior pertenece a la siguiente máquina de estado:

![Maquina de estado](./assets/stepfunctions_graph.png)


Terminada la declaración de los `step functions`, podemos dar paso a la declaración de los recursos a utilizar por nuestra app. Todos los recursos a utilizar se declaran bajo la opción de `resources`:

```yml
resources: # Declaramos los recursos a utilizar en este apartado
  Resources: # Nos permite definir nuestros recursos (Recuerde que los recursos se piden en el momento que se despliegue nuestro serverless)
    cuponerabucket: # Nombre del recurso
      Type: AWS::S3::Bucket # Tipo del recurso a utilizar. En este caso estamos definiendo un bucket de S3
      Properties: # Propiedades
        BucketName: ${self:service}-bucket-${self:provider.stage} # Nombre del bucket a utilizar
        PublicAccessBlockConfiguration: # Configuramos el bloqueo e acceso publico al bucket
          BlockPublicAcls: false
          BlockPublicPolicy: false

    cuponeraCatalogTable: "${file(./resources/cuponera-catalog.yml):cuponera-catalog}" # Definimos la ruta hacia donde está nuestro otro recurso declarado. De esta forma, podemos organizar mejor el trabajo.

    cuponeraManagementTable: "${file(./resources/cuponera-management.yml):cuponera-management}" # Lo mismo de arriba

    StateMachineLogGroup: # Solicitamos el acceso de las máquinas de estados a los logs.
      Type: AWS::Logs::LogGroup # Definimos el tipo de recurso solicitado. Logs en este caso
      Properties: # Propiedades a utilizar dentro 
        LogGroupName: # Definimos como se va a llamar los logs solicitados
          !Join [
            "/",
            ["stepfunctions-${self:provider.stage}", CuponStateMachineExpress],
          ]

    handlerStateMachineRole: # Este recurso es para definir los permisos
      Type: AWS::IAM::Role # Este tipo de Rol es para permisos
      Properties: # Propiedades del recurso
        RoleName: handlerStateMachineRole-${self:provider.region}-${self:provider.stage} # Nombre
        Path: / # Donde se encontrará almacenado
        AssumeRolePolicyDocument: # El encargado de dar los permisos
          Statement:
            - Effect: Allow
              Principal:
                Service:
                  - states.amazonaws.com
              Action:
                - sts:AssumeRole
        Policies: # Los permisos que son necesarios
          - PolicyName: statePolicy # Nombre del permiso
            PolicyDocument: # Documento para los permisos
              Version: "2012-10-17" # Version de los permisos
              Statement: # Lo que declara el documento de permisos
                - Effect: Allow # Permite o Deniega
                  Action: # Acción a realizar o sobre que se va a realizar
                    - states:* # Todas las acciones de las máquinas de estado
                  Resource: # Recursos a los que se tendrá acceso
                    - arn:aws:states:* # Sobre las maquinas de estado

                - Effect: Allow
                  Action:
                    - lambda:* # Todas las acciones de los lambdas
                  Resource: "*" # Sobre todos los recursos disponibles

                - Effect: Allow
                  Action:
                    - dynamodb:* # Acciones de base de datos
                  Resource: "*" # Sobre todos los recursos disponibles

                - Effect: Allow
                  Action:
                    - logs:* # Acciones de loggeo
                  Resource: "*"

                - Effect: Allow
                  Action:
                    - ses:* # Envío de mail mediante amazon
                  Resource: "*"
```

Ahora nos toca ver los `plugins` utilizados dentro de este proyecto:

```yml
plugins:
  - serverless-offline # Necesario para ejecutar serverless en la pc local
  - serverless-express # Nos permite usar express junto a serverless
  - serverless-content-encoding # Nos permite manipular las propiedades de las peticiones
  - serverless-prune-plugin # Este plugin nos permite borrar despliegues viejos realizados
  - serverless-deployment-bucket # Nos permite crear un bucket de S3
  - serverless-step-functions # Permite ejecutar los step functions
```

Algunos plugins que tenemos son configurables. En un apartado llamado `custom`:

```yml
custom: 
  serverless-offline: # Propiedades configurables de serverless
    httpPort: 7000 # Puerto de ejecución de serverless
    lambdaPort: 7003 # Puerto de ejecución de los lambdas
    host: 0.0.0.0 
    useChildProcesses: true
    allowCache: true
  contentEncoding: # Como va a modificar el contenido de las peticiones
    minimumCompressionSize: 0 # Compresión de las peticiones
```

# Código

Ahora veamos el código dentro de la carpeta `API` (Lo más importante). Comencemos por la carpeta `helpers` que son funciones auxiliares para utilizar dentro de nuestra app.

> Helpers

- `axios.js`

Este archivo posee dos funciones dentro `AxiosGenerico` y `AxiosGET`. El primero se encarga de realizar peticiones de forma genérica, ya que podemos definir el `método`, `url`, `url base`, `datos a enviar` y `encabezado`. El segundo solo realiza una petición `GET` sencilla.

- `compararObjetos.js`

Como su nombre lo indica, nos permite comparar objeto contra objeto o array contra array. El primer método llamado `ObjCompare` compara dos objetos para verificar si son idénticos. El segundo `ArrayObjectCompare` hace lo mismo, pero recorre dos arreglos invocando por cada par el método `ObjCompare`.

- `errorOfValidateHelper.js`

Maneja los mensajes de errores de las validaciones.

- `makeId.js`

Posee 3 métodos de generación de ID. Estos se generan a partir de un tamaño que le pasemos por parámetros. Los métodos son:
 **makeId** -> Crea un ID con mayúsculas, minúsculas y números
 **MAKEID** -> Crea un ID con mayúsculas y números
 **MAKEIDALL** -> Lo mismo de arriba

- `orderItems.js`

Posee dos métodos (`sortItemsByFechaCreadoDesc` y `sortItemsByFechaHoraDesc`). Ambos se encargan de ordenar un arreglo. El primero mediante la fecha de creado y el segundo mediante la hora.

- `s3.js`

Funciones de acceso a S3. Tenemos tres funciones, `getBuckets` para obtener todos los `buckets` de `aws`. `UploadToBucket` para subir archivos a nuestro `bucket`.Terminando con `uploadBase64ToBucket` para subir archivos en `base64` a nuestro `s3`.

- `sendMail.js`

Nos brinda métodos para el envío de emails mediante `aws` `SESV2`

- `validateHelper.js`

Provee funciones para validar los `request` de `express`.




