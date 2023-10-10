# SES

Amazon SES (Amazon Simple Email Service) es un servicio de envío de correo electrónico en la nube ofrecido por Amazon Web Services (AWS). Permite a los desarrolladores enviar correos electrónicos de manera escalable y confiable utilizando una API o una interfaz de usuario.

Amazon SES se utiliza principalmente para enviar correos electrónicos transaccionales, como confirmaciones de pedidos, notificaciones de envío, contraseñas restablecidas y otros mensajes automatizados. También se puede utilizar para enviar correos electrónicos de marketing y boletines informativos.

Este servicio proporciona características como la capacidad de enviar grandes volúmenes de correos electrónicos, filtrado de spam, seguimiento de entregas y estadísticas detalladas. Además, se integra con otros servicios de AWS, lo que permite a los desarrolladores crear soluciones más complejas y personalizadas.

# AWS dice

Cuando vamos al sitio de [AWS-SES](https://aws.amazon.com/es/ses/) dice de manera resumida:

> Amazon Simple Email Service (SES) le permite llegar a los clientes de forma fiable sin la necesidad de un sistema Simple Mail Transfer Protocol (SMTP) en las instalaciones.

Básicamente quieren decirnos que nos olvidemos de instalar un sistema SMTP para el envío de mensajes de nuestra app. Además, brindan esta foto mostrando la interacción global con `SES`:

![AWS SES](./assets/aws-ses.png)

Al final agregan lo siguiente casos de uso de esta herramienta:

- Automatice los mensajes transaccionales 
- Entregue correos electrónicos de marketing a nivel mundial 
- Envíe notificaciones oportunas a los clientes
- Envíe comunicaciones masivas por correo electrónico 

# Antes de comenzar

Similar a como hicimos en el estudio de [SNS](https://github.com/BadiaValdes/docs/blob/doc/nodeJs/serverless-prod/sns.md), este documento comenzará explicando su uso mediante un código en internet al cuál tendrán acceso y posteriormente se introducirá un ejemplo desarrollado por nuestro equipo. Sin más que decir, comencemos.

> Si no sabes como iniciar una app de serverless o quieres refrescar tus conocimiento, mira el siguiente documento generado por nuestro equipo:
> [Serverless](https://github.com/BadiaValdes/docs/blob/doc/nodeJs/serverless.md)


# Cómo lo uso?

Bueno, comencemos viendo la estructura que seguiremos para el archivo `serverless.yml`. Todo proyecto de serverless debe comenzar por la configuración de ese archivo:

```yml
service: serverless-deploy
provider:  
  name: aws  
  runtime: nodejs14.x  
  stage: dev  
  region: us-east-1
functions:  
    hello:    
      handler: index.handler
      events:      
       - http: 
           path: hello
           method: get
    dispatcher:
      handler: dispatch.handler
      events:
        - sns: dispatch
    invoke:
      handler: invoke.handler
      events:
        - http:
            method: get
            path: invoke
```

Bastante sencillo debería decir. Este archivo posee las configuraciones mínimas he indispensables para ejecutar nuestro código. Comencemos por `provider`, donde podemos observar la declaración de los datos más importantes de nuestra `app`:
- `name`: nombre de nuestro proveedor `serverless`, en este caso `aws`.
- `runtime`: que version de node se debe utilizar para ejecutar nuestra `app`
- `stage`: define el estado en el que estamos trabajando en el momento. En el despliegue, este valor puede ser cambiado.
- `region`: define en que región para desplegar nuestra app. Recuerden que `AWS` trabaja por regiones y el código de una región, no puede ser visto en otra.

Seguido de la propiedad `provider`, tenemos las declaraciones de nuestras funciones. La primera, `hello` viene por defecto cuando utilizamos el template base para la creación de nuestra app. La penúltima función (`dispatcher`) será ejecutada mediante la cola de mensajes de `AWS`; para más información, puede revisar el apartado de [SNS](https://github.com/BadiaValdes/docs/blob/doc/nodeJs/serverless-prod/sns.md).

---------

**Un dolor de cabeza controlado**

Uno de los grandes dolores de cabeza que trae trabajar con `AWS` es la necesidad de especificar los permisos que tiene tu `app` sobre los servicios de amazon. Esto que les vamos a mostrar, no mejora en nada ese dolor de cabeza, incluso puede hacer que empeore, pero algunas personas prefieren un dolor de cabeza controlado. Mediante el plugin `serverless-iam-roles-per-function`, podemos definir permisos de acceso directamente en nuestras funciones. Es verdad que es más trabajo, pero en entornos que se demande seguridad en las transacciones, no todas las funciones pueden tener acceso a todos los recursos.

Para instalar esta dependencia podemos utilizar el siguiente comando:
- `npm i serverless-iam-roles-per-function`

Una vez instalado podemos agregar permisos directamente en nuestras funciones:

```yml
service: serverless-deploy
plugins:
- serverless-iam-roles-per-function # Plugin instalado
provider:  
  name: aws  
  runtime: nodejs12.x  
  stage: dev  
  region: us-east-1
functions:  
    hello:    
      handler: index.handler
      events:      
       - http: 
           method: get
           path: hello
    dispatcher:
      handler: dispatch.handler
      events:
         - sns: dispatch
      iamRoleStatements: # Permiso por función
      - Effect: "Allow"
        Action:
          - ses:SendEmail
        Resource: "*"
    invoke:
      handler: invoke.handler
      events:
        - http:
            method: get
            path: invoke
      iamRoleStatements: # Permiso por función
      - Effect: "Allow"
        Action:
          - sns:Publish
        Resource: "*"
```
Podemos notar el uso de este plugin dentro de las declaraciones de nuestras funciones, específicamente bajo  la propiedad `iamRoleStatements`. Aquí, por función, definimos los permisos de acceso que pueden tener.

---------

Seguimos con el envío de mensajes. Me voy a saltar la creación de funciones y el uso de `SNS` ya que ambos pueden encontrarlos en los siguientes links:

- [SNS](https://github.com/BadiaValdes/docs/blob/doc/nodeJs/serverless-prod/sns.md)
- [Serverless](https://github.com/BadiaValdes/docs/blob/doc/nodeJs/serverless.md)

> Ojo, para desplegar en `aws` podemos utilizar el siguiente comando:
> - `serverless deploy `
> Si quieres especificar un stage de despliegue, use el siguiente comando
> - `serverless deploy --aws-profile NOMBRE_STAGE`

Para poder tener acceso a los servicios de `aws` desde nuestro código, debemos utilizar una de las siguientes librerías:
- `aws-sdk`: Es la api de `aws` que te permite utilizar cualquiera de sus servicios en tu `app`. El inconveniente es que posee muchas herramientas, lo que se convierte en más peso para el despliegue.
- `@aws-sdk/client-ses`: Esta es la versión ligera por así decirlo. Nos permite instalar exactamente la herramienta de `aws` que queremos utilizar. De esta forma solo usamos lo que necesitamos.

El proyecto que tomamos como base para documentar utiliza la segunda opción; por lo que les mostraremos el comando para instalar esta dependencia:
- `npm i @aws-sdk/client-ses`

Ya basta de instalar y prepara, vamos a ver como es el código, al final es lo que más nos interesa no?

```js
const { SESClient, SendEmailCommand } = require("@aws-sdk/client-ses");
const REGION = "us-east-1";
const sesClient = new SESClient({ region: REGION });
exports.handler = async (event) => {
    const promises = event.Records.map((record) => {
        const message = JSON.parse(record.Sns.Message);
        const params = {
            Destination: { ToAddresses: [message.to] },
            Message: {
                Body: {
                    Html: { Charset: "UTF-8", Data: message.content },
                    Text: { Charset: "UTF-8", Data: "TEXT_FORMAT_BODY" },
                },
                Subject: { Charset: "UTF-8", Data: "Sign up" },
            },
            Source: "verifiedsesemail@test.com",
        };
        sesClient
            .send(new SendEmailCommand(params))
            .then(function (data) {
                const response = {
                    statusCode: 200,
                    body: JSON.stringify(data),
                };
                return response;
            })
            .catch(function (err) {
                console.error(err, err.stack);
            });
    });
    await Promise.all(promises);
};
```

Vamos a explicar esto por bloque:

1. Constantes:

```js
const { SESClient, SendEmailCommand } = require("@aws-sdk/client-ses");
const REGION = "us-east-1";
const sesClient = new SESClient({ region: REGION });
```
 
La primera línea es una destructuración. Es decir, del script `@aws-sdk/client-ses` tomamos solamente las clases `SESClient, SendEmailCommand` de todas las disponibles. La segunda es bastante fácil, simplemente declaramos la región en la que trabajamos; ya hablamos de esto antes pero es bueno repetirlo, `aws` trabaja según las regiones, un código en la region `x` no es lo mismo que el código en la región `y`. Por últimos creamos una instancia de la clase importada `SESClient`; esta instancia nos permitirá trabajar con el servicio de `simple email` de `aws`.

2. La función:

```js
exports.handler = async (event) => {
    const promises = event.Records.map((record) => {
        const message = JSON.parse(record.Sns.Message);
        const params = {
            Destination: { ToAddresses: [message.to] },
            Message: {
                Body: {
                    Html: { Charset: "UTF-8", Data: message.content },
                    Text: { Charset: "UTF-8", Data: "TEXT_FORMAT_BODY" },
                },
                Subject: { Charset: "UTF-8", Data: "Sign up" },
            },
            Source: "verifiedsesemail@test.com",
        };
        sesClient
            .send(new SendEmailCommand(params))
            .then(function (data) {
                const response = {
                    statusCode: 200,
                    body: JSON.stringify(data),
                };
                return response;
            })
            .catch(function (err) {
                console.error(err, err.stack);
            });
    });
    await Promise.all(promises);
};
```

Dentro de la función, tenemos la siguiente línea de código `const promises = event.Records.map((record)`. Esto se utiliza debido a que los métodos internos a utilizar son asíncronos y en todo el código no hay una sola sentencia await (dentro del map, afuera si está); cuando lleguemos al final del código, explicaremos mejor esta parte. Las próximas dos líneas de código son para capturar los datos que provienen de la cola de mensajes y preparar el cuerpo del mail que vamos a enviar:

```js
const message = JSON.parse(record.Sns.Message); // Tomamos de la variable record el mensaje enviado por la cola de mensajes
const params = { 
    Destination: { ToAddresses: [message.to] },
    Message: {
        Body: {
            Html: { Charset: "UTF-8", Data: message.content },
            Text: { Charset: "UTF-8", Data: "TEXT_FORMAT_BODY" },
        },
        Subject: { Charset: "UTF-8", Data: "Sign up" },
    },
    Source: "verifiedsesemail@test.com",
};
```

La variable `param` se construye en dependencia de los datos que se quieran enviar. Dicha estructura está documentada en [AWS SES CLIENT](https://docs.aws.amazon.com/AWSJavaScriptSDK/v3/latest/clients/client-ses/classes/sendemailcommand.html); además, en [AWS SES](https://docs.aws.amazon.com/AWSJavaScriptSDK/v3/latest/clients/client-ses/) podemos encontrar otras funciones brindadas por la librería instalada para el envío de mensajes. En este caso definimos 3 valores, el destino (`Destination`), el mensaje (`Message`) con su cuerpo (`Body`) y su título (`Subject`); por último, declaramos el correo electrónico de quien lo envía (`Source`).

Ya casi terminamos, solo nos falta ver como enviamos el mensaje mediante el servicio de `aws`. Para los que hayan prestado atención, pudieron percatarse de que importamos dos clases y solo hemos creado la instancia de una. Pues están en lo correcto, la segunda clase nos permite definir los datos a enviar, vemos el código que realiza esta operación:

```js
sesClient // La instancia de la clase SESClient
    .send(new SendEmailCommand(params)) // Utilizamos la función send de nuestra instancia y le pasamos los parámetros mediante new SendEmailCommand(params)
    .then(function (data) {
        const response = {
            statusCode: 200,
            body: JSON.stringify(data),
        };
        return response;
    })
    .catch(function (err) {
        console.error(err, err.stack);
    });
```

Vale la pena aclarar el uso de la clase `SendEmailCommand`. Esta clase nos permite darle forma a los parámetros declarados anteriormente, para que el método `send` de `sesClient` pueda enviar de forma correcta los datos; además esta clase espera una instancia de `SendEmailCommand` para trabajar. Posteriormente se utiliza el encadenador `then`; este se utiliza cuando se trabaja con promesas y nos permite ejecutar funciones una vez el método anterior terminó de ejecutarse. El then o el método inicial pueden poseer error a la hora de ejecutarse, por lo que utilizamos el encadenador `catch` para capturar cualquier error que exista.

Terminando el método:

```js
await Promise.all(promises);
```

Anteriormente hablamos del uso de promesas y awaits. Bueno, en un caso normal haríamos lo siguiente:

```js
async function a() => {
    await p.call(1);
    await p.call(2);
    await p.call(3);
    await p.call(4);
    await p.call(5);
    return data;
}
```

Pero esta forma de trabajar tiene un inconveniente cuando se realizan llamadas multiples a métodos promesas. Este inconveniente es la necesidad de esperar que termine un sentencia, para poder pasar a la otra; en el ejemplo anterior, debemos esperar que termine la llamada `p.call(1)` para que `p.call(2)` comience. Para evitar este tipo de comportamientos en llamadas que pueden realizarse de forma simultánea (nada depende de ellas para trabajar posteriormente), podemos utilizar la función `all` de la clase `Promise` (`Promise.all`). Este método de la clase promise nos permite juntar varias promesas `promises` y ejecutarlas de forma simultaneas; por lo tanto, solo esperarías por la que más se demore.

Pues ya. hasta qui llegó el ejemplo tomado de internet. Solo resta poner nuestro propio ejemplo.

# Nuestro proyecto