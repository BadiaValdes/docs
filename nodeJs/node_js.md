# NodeJs Annotations

## MultiThread

En las nuevas versiones de node, podemos utilizar el trabajo multihilos mediante los workers. A los workers se le pasa por parámetros un script a ejecutar y los datos a enviar a dicho scrpit. A continuación veamos como crear un worker:

```javascript
// Import
const {Worker} = requiere('worker_threads');

// Create
const worker = new Worker('./worker.js', { workerData })
```

Además, para usar los worker correctamente, debemos modificar las funciones de comportamiento. Estas funciones están compuestas por un comportamiento y un callback. La primera muestra el estado de un worker ('error', 'exit', 'message', etc) y el segundo es lo que se programará cuando el worker esté en alguno de estos estados:

```js
// On error
worker.on('error', (err) => { throw err }) 
// On message
worker.on('message', (msg) => { 
    primes = primes.concat(msg)
});
```

## Process

Un proceso es un programa que se está ejecutando en el sistema operativo. Como programa posee su propia memoria y no puede acceder a la memoria de otros. Además posee un puntero que indica que tarea se está ejecutando del proceso y solo se puede ejecutar una tarea a la vez. 

Debido a esto, si se ejecutan tres programas a la vez, el mismo sistema operativo decidirá el orden de ejecución. Por lo que se puede decir que los procesos se ejecutan de forma concurrente. Es decir, puede que parezcan que se están ejecutando a la vez, pero el sistema operativo lo que hace es intercambiar las prioridades de cada uno a medida que se ejecutan.

## Thread

Los hilos son como los procesos, tienen su propio apuntador a la tarea que les corresponde y solo pueden ejecutar un programa a la vez (en este caso un script). A diferencia de los procesos, los hilos no tienen memoria pripia asignada; en cambio utilizan la memoria del proceso general. Es decir, una aplicación tiene 4 hilos y el sistema le asigna 250 mb; la idea es que esos 4 hilos se ejecuten dentro de esos 250 mb y no tengan que estar esperando a que uno termine para comenzar el otro.

Además, los diferentes hilos pueden comunicarse entre ellos para la transmisión de información. De esta forma no es necesario que se solicite más memoria de la ya asignada al proceso actual. Ahora, si se tiene una operación multihilos en un procesador de un solo core, el proceso de ejecución sería similar el visto en el apartado de procesos; los hilos se intercambiarán entre ellos (el sistem operativo lo hace) para poser usar el core disponible. Esto aplica si utilizamos más hilos que cores tenemos en nuestra pc.

## Nodejs and Threads

Como se puede ver al principio de este documento, nodejs se apoya en la librería `worker_threads` para poder dividir el trabajo en hilos. Pero hay que tener en cuenta una serie de factores que puden disminuir la eficiencia de esta libraría y de node en general. 

La primera es que no se debe usar hilos extras para el proceso de lectura de archivos ya que por defecto este proceso se ejecuta en hilos. Para poder entender esto, debemos hablar de los hilos por defecto que posee node (sin usar `worker_threads`):

- 1 hilo para el programa principal
- 4 hilos para la lectura de archivos
- 2 hilos proporcionados por el motor v8

En resumidas cuentas, usar hilos extras para operaciones que ya poseen hilos propios puede afectar el rendimiento de la aplicación.

## Pasar parametros

Vamos a ver un ejemplo para pasar parámetros a una app ejecutada mediante node:

> Programa
```js
const proccessName = process.argv.slice(2)[0] ?? '';
```
- process.argv: Me permite acceder a los argumentos que se le pasen a la app.
- slice: me permite dividir el arreglo que pasan por parámetro. (2) indica que tomaremos el segundo elemento en adelante.

> Pasar el parámetro
```bash
node proccess.js A &
```
- A: es el parametro que estamos enviando mediante consola.
- &: permite ejecutar la app en background y poder seguir trabajando en la consola.

# Bibliografía
- https://www.digitalocean.com/community/tutorials/how-to-use-multithreading-in-node-js
- https://medium.com/@mohllal/node-js-multithreading-a5cd74958a67
- https://hackernoon.com/backend-development-101-prime-numbers-and-multi-threading-g42j3uex
- https://sathishsuresh.medium.com/multi-threading-in-node-js-using-worker-threads-13976e5213bd


