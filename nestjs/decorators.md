# Como crear decoradores

> Ejemplo global decorador de parámetro
```js
import { createParamDecorator, ExecutionContext } from '@nestjs/common';

export const GetUser = createParamDecorator(
  (data: unknown, ctx: ExecutionContext) => {
    const request = ctx.switchToHttp().getRequest(); // Obtenemos el request de la petición.
    return request.user?.email; // Del request tomamos el usuario
  },
);
```

Destripemos lo anterior:
- `createParamDecorator` -> Nos permite crear un decorador. Específicamente este lo usaremos en los parámetros de las funciones. Recibe como parámetros una función con dos argumentos.
- `data` -> Primer argumento y hace referencia a cualquier dato extra que se quiera enviar al decorador.
- `ctx` -> Es el `ExecutionContext` o contexto de ejecución. Es un objeto que almacena toda la información de la ejecución de una transacción. Nestjs se encarga de asignar los valores correspondientes a este objeto.
- `ctx.switchToHttp().getRequest()` -> Este método nos permite obtener la información del request de la petición HTTP realizada.
- `request.user` -> Intentamos acceder al objeto usuario que es añadido a la petición mediante el middleware de Passport para la autenticación.

> Cómo se debería utilizar

```js
getAgentProfile(@GetUser() userEmail: string)
```

De forma automática, el valor de `userEmail` será el devuelto por el decorador.