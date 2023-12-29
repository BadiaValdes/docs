# Creando un interceptor

En Nestjs los interceptors son bastantes sencillos, debido a que parten de una clase injectable que implementa la interfaz de `NestInterceptor`:

```js
import { applyDecorators, SetMetadata, UseInterceptors } from '@nestjs/common';

@Injectable()
export class MyInterceptor implements NestInterceptor {}
```

Internamente se debe sobrescribir la función siguiente:

```js
intercept(context: ExecutionContext, next: CallHandler): Observable<any> {
    // Aquí trabajamos todo lo que se hace antes de enviar la petición o sea, request
    return next.handle().pipe(map(dat => {
        // Aquí trabajamos todo lo que modificaremos en el response
    }));
}
```

Para usar el interceptor en métodos específicos solamente, podemos utilizar el decorador `@UseInterceptors`:

```js
@Get()
@UseInterceptors(MyInterceptor)
getUsers(): User[]{}
```

Si usamos el decorador a nivel de clase:

```js
@Controller()
@UseInterceptors(CustomInterceptors)
export class AppController {}
```

El interceptor será utilizado por todos los métodos de la clase.

En caso de querer utilizarlo en todo el proyecto, nos dirigimos a main y escribimos:

```js
app.useGlobalInterceptors(new MyInterceptor());
```

Pero este acercamiento evita inyectar información en nuestro interceptor, por lo que se recomienda utilizar la siguiente forma en vez de la anterior:

```js
@Module({
 imports: [],
 controllers: [AppController],
 providers: [
   AppService,
   {
     provide: APP_INTERCEPTOR, // Ponerlo como provider el modulo global
     useClass: MyInterceptor,
   },
 ],
})
export class AppModule {}
```

### Links de interés
- https://stackoverflow.com/questions/74810230/nestjs-how-can-i-access-controller-function-response-using-a-custom-decorator
- https://blog.logrocket.com/nestjs-interceptors-guide-use-cases/