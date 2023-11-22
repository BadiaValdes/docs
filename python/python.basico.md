# Tutorial python básico

Este tutorial estará dirigido a explicar la forma de trabajar con python directamente. Pasaremos desde como crear variables hasta como crear clases.

## Un poco de python

Python es un lenguaje de programación de alto nivel, interpretado y fácil de aprender. Fue creado por Guido van Rossum en la década de 1990 y se ha convertido en uno de los lenguajes más populares en la actualidad.

Python se destaca por su sintaxis clara y legible, lo que facilita la escritura y comprensión del código. Es un lenguaje multiparadigma, lo que significa que permite programar utilizando diferentes estilos, como programación orientada a objetos, programación funcional y programación procedural.

Una de las características más destacadas de Python es su amplia biblioteca estándar, que ofrece una gran cantidad de módulos y funciones para realizar tareas comunes, como manipulación de archivos, acceso a bases de datos, procesamiento de texto, entre otros. Esto hace que sea muy eficiente y rápido desarrollar aplicaciones en Python.

*Generado por Chat GPT*

## Comencemos

> Este documento es una mera recopilación de programación básica con python, por lo que solo tratará temas sencillos. Si quiere indagar más en el tema, puede dirigirse a la siguiente página: https://docs.python.org/3/tutorial/index.html 

**Descargar python**

Para descargar python acceda al siguiente link y siga las indicaciones de la página para su instalación:

- https://www.python.org/downloads/

### Variables

> Python posee variables sin tipado. Es decir, una variable lo mismo puede almacenar un string que un int.
> Ojo, no existe el punto y coma.

```py
variableName = 'd'
```

Esto quiere decir que si queremos declarar una lista, debemos realizar la asignación de la próxima forma:

```py
variableName = []
```

### Estructuras de flujo

> Ojo, python no usa los {} para delimitar código. En cambio usa identación así que cuidado como escriben su código. 

**If**
```python
x = 500
if x < 0:
    print('TEXT HERE')
elif x == 0:
    print('SECOND TEXT HERE')
else:
    print('FINAL')
```

**For**
```python
words=['wordA', 'wordB', 'wordC']
for w in words:
    print(w)
```

> Para ver el tamaño de un arreglo o un string usa len(variable)

*Iterar sobre más de un elemento*
```python
users = {'Hans': 'active', 'Éléonore': 'inactive', 'ELON': 'active'}
active_users = {}
for user, status in users.items():
    if status == 'active':
        active_users[user] = status
```

> Si queremos eliminar un elemento de un arreglo, debemos utilizar la palabra reservada `del` -> `del user[user]`

*For hasta n*

Pero que pasa si quiero iterar un ciclo normal, o sea, un for de toda la vida, no un foreach. Para ese caso tenemos qu utilizar la operación range:

```python
for i in range(5)
    print(i)
```

> Range va de 0 hasta el número que definas -1. Además puedes definir el paso, si quieres que sea de 10, de 3 en 3. Este sería un tercer parámetro. Por lo tanto el range quedaría así: `range(inicio, fin, paso)`

Los ciclos for también pueden tener un `else` dentro de la estructura y esto permitirá ejecutar un procedimiento una vez que se termine por completo el ciclo.

*For simplificado*

```py
dat = [x for x in range(6)]
```

En español sería de la siguiente forma:

- [dato_a_devolver `for` dat_iterado `in` dato_a_iterar]

Además, puede agregar una condicional al final mediante un if:

- [dato_a_devolver `for` dat_iterado `in` dato_a_iterar `if` condición] 

Algo mayor a esto debe ser programado en un for completo

*Iterar sobre dos arreglos a la vez*

```py
questions = ['name', 'quest', 'favorite color']
answers = ['lancelot', 'the holy grail', 'blue']
for dat1, dat2 in zip(questions, answers):
    print(q,a) # IT 1 -> name lancelot
```

*Iterar en reversa*
```py
for i in reverse(range(5)):
    print(i) # IT 1 -> 4
```

**While**

```python
a,b = 0, 1 #Esto es asignación de variables también, a = 0 y b = 1
while a < 10:
    print(a)
    a,b = b, a+b
```

En los ciclos `while` y `for` es posible romper o pasar a la próxima iteración mediante dos palabras claves:
- `brake`: Permite romper por completo el ciclo. En otras palabras, detiene su ejecución.
- `continue`: Pasa a la próxima iteración, cerrando por completo la actual.

**match** -> Old `switch`

```py
def htt_error(status):
    match status:
        case 400:
            return 'A'
        case _: # Default
            return 'B'
```

Para combinar varios elementos en un solo case, puedes hacer: `case 401 | 402`. Es decir, separar las condiciones de un mismo caso mediante `|`.

### ENUMS

```python
class Color(Enum):
    RED='red'
```

### Funciones

```py
def nombre_funcion(datos):
    # Lógica aquí dentro
```

Si quieres utilizar datos por defectos en los parámetros puedes hacer lo siguiente:

```py
def nombre_funcion(datos='valor'):
    # Lógica aquí dentro
```

Como un extra podemos definir que es posible pasar los datos a la función ya sea llamando a la palabra clave:

```py
nombre_funcion('val')
nombre_funcion(datos='val')
```

Ahora, si queremos definir la forma en que se llamarán los parámetros, podemos utilizar `*` o `/` para definir si lo queremos por palabras claves o por posición respectivamente. Ejemplo:

```py

# Función solo palabras claves
def fun_kwd(*, arg):
    # lógica

# Función solo posición
def fun_position(arg, /)
    # lógica

# Combinando
def combinado(position, /, normal, *, key_words)
```

### Estructuras de datos

**List**
```py
variable = []
```

> Las listas pueden ser tratadas como una pila.

**List como Queue** -> Las listas directamente no so eficientes para crear colas.

```py
from collections import deque
queue = deque(['A','B','C'])    
queue.append('D') # Añade un elemento
queue.popleft() · Extrae el elemento de la izquierda
```

**Tuples** -> Recuerden que las tuplas son datos inmutables

```py
tuple_data = 1, 2, 'azul' # las tuplas se separan por comas
```

**SETS** -> Datos no ordenados y no repetidos

```py
set_data = set() # Crear un set vacio
set_with_data = {'dato1','dato2','dato3'}
set_data_from_string = set('aaaabzxd') # -> {'a','b','z','x','d'}
```

Los Sets son conjuntos, por lo que se pueden realizar operaciones matemáticas entre ellos como:
- `a - b` -> datos que existan en `a` y no en `b`
- `a | b` -> Unión de los datos
- `a & b` -> Intercesión de los datos
- `a ^ b` -> Contrario de la unión

**Diccionarios** -> Pareja de clave - valor

```py
dictionary = {'a':1,'b':2}
dictionary_construct = dict([('a',1),('b',2)]) # Crear diccionarios mediante parámetros.
```

Para recorrer mediante un ciclo for un diccionario y obtener el valor y la llave, debemos utilizar la siguiente estructura:

```py
knights = {'gallahad': 'the pure', 'robin': 'the brave'}
for key, value in knights.items():
    print(key, value)
```

### Crear paquetes

Pon dentro de la carpeta donde estarán los scripts del paquete un archivo vacío llamado `__init__.py`. Ya con eso tenemos un paquete.

### Forma String

Para incrustar variables dentro de un String podemos utilizar el siguiente código:

```py
a = 'Hola'
b = 'Rusia'
print(f'{a} madre patria, {b}')
```

Usando la letra `f` frente a un texto, podemos agregar variables en el cuerpo y python será capaz de sustituirla por su valor en tiempo de ejecución.

Si no nos gusta la f, podemos utilizar:
```py
a = 'Hola'
b = 'Rusia'
print('{} madre patria, {}'.format(a,b))
```

Para más información sobre como dar formato a los textos, vea la documentación: https://docs.python.org/3/reference/lexical_analysis.html#f-strings

### Archivos

```py
with open('FILE', encoding="TYPE") as variable:
    read_data = variable.read()
```

O puedes usar:

```py
variable = open('FILE', 'MODO' ,encoding="TYPE")
variable.close() # para cerrar la lectura del archivo
```

Los modos que puedes utilizar son:
- `w` -> escritura
- `r` -> lectura
- `a` -> Añadir datos al final del documento
- `r+` -> lectura/escritura

### JSON

*Convertir a json*

```py
import json # importamos la librería json para trabajar
x = {'a':1}
json.dumps(x) # Convertir a json
json.dumps(x, f) # Convertir a json y guardarlo a un archivo abierto.
```

*Convertir a objeto*

```py
import json # importamos la librería json para trabajar
x = json.load(f) # F es un archivo abierto para leer o puede ser un dato recibido
```

### Try Catch

```py
try:
    # Lógica
    break
except Error: # Este sería el catch
    # Lógica del error
else:
    # Logica si se ejecutra el try y no pasa por el catch
finally:
    # do after try or catch
```

### Lanzar excepción

```py
    raise ERROR_NAME('data')
```

### Clases

```py
class NombreClase:
    # Datos
    i = 1 # Variable compartida entre los objetos creados de la clase

    # Constructor (no necesario)
    def __init__(self, argumentos): # Los argumentos no son necesarios
        self.data = [] # Es una variable que su valor es único para cada objeto
        # También con self podemos hacer referencia a las variables de clases
        self.i = 20

    def fun(self): # todas las funciones deben tener self para hacer referencia a la clase
        return i
```

Para crear un objeto de la clase:

```py
x = NombreClase() # Ya con esto tenemos una instancia de la clase
```

*Herencia*

```py
class BaseClass:
    i = 0

    def fun(self, var):
        print(var)

class DerivedClass(BaseClass): # Puedes heredad de diferentes clases
    
    # Puedes sobrescribir los métodos de la clase padre
    def fun(self, var):
        print(var)
        print(var)

```

*Abstracción*

Por defecto, python no posee abstracción por defecto, por lo que debemos utilizar un tipo de herencia para marcar una clase como abstracta. A continuación veremos el código y su explicación:

```py
from abc import ABC, abstractmethod # ABC es la clase que define la Abstracción

class Polygon(ABC):

    @abstractmethod # Define un método como abstracto
    def n_sides(self):
        pass

class Triangle(Polygon):
    def n_sides(self):
        print("3")
```

En caso de que la clase padre tenga un método concreto y el hijo necesite llamarlo, sería de la siguiente forma:

```py
from abc import ABC, abstractmethod

class Base(ABC):
    def meth(self):
        print("Hello")

class Child(Base):
    def meth(self):
        super().meth()
        print("World")
```

# Extra

Si te gusta tener toda la documentación de diferentes lenguajes de programación al alcance de un click, puedes acceder al siguiente link:

- https://devdocs.io/

En este sitio podrás encontrar la documentación oficial de diversos lenguajes de programación y frameworks de desarrollo. Como un extra, el sitio permite almacenar en el navegador la documentación para que sea accedida de forma offline.