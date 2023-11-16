# Resumen de clean code

Clean Code es un conjunto de prácticas y principios para escribir código mantenible, legible y escalable. Aquí hay un ejemplo de cómo se puede aplicar Clean Code en un proyecto:

```
- Utilizar nombres de variables, funciones y clases descriptivos y significativos.
- Escribir funciones cortas y específicas que hagan una sola cosa.
- Evitar el código duplicado y refactorizar el código existente para eliminar la complejidad innecesaria.
- Comentar el código solo cuando sea necesario y evitar comentarios redundantes o informativos.
- Utilizar pruebas unitarias y pruebas de integración para garantizar la calidad del código y evitar errores.
```

Ejemplos:

1. Nombres significativos

Los nombres de las variables, funciones y clases deben ser descriptivos y significativos.

arduino

```java
// Ejemplo de nombres poco significativos

public void x(int a, int b) {
    int c = a + b;
    System.out.println(c);
}

// Ejemplo de nombres significativos

public void sumarDosNumeros(int num1, int num2) {
    int resultado = num1 + num2;
    System.out.println(resultado);
}
```

1. Funciones pequeñas

Las funciones deben ser cortas y hacer una sola cosa.

typescript

```java
// Ejemplo de función larga

public void crearUsuario(String nombre, String apellido, int edad, String telefono, String correo) {
    // Lógica para crear un usuario
    // Lógica para enviar un email de confirmación
    // Lógica para guardar el usuario en la base de datos
}

// Ejemplo de función pequeña

public void crearUsuario(String nombre, String apellido, int edad) {
    // Lógica para crear un usuario
}

public void enviarEmailDeConfirmacion() {
    // Lógica para enviar un email de confirmación
}

public void guardarUsuarioEnBaseDeDatos() {
    // Lógica para guardar el usuario en la base de datos
}
```

1. Comentarios útiles

Los comentarios deben ser útiles y explicar el porqué del código, no el qué.

arduino

```java
// Ejemplo de comentario inútil

public int sumar(int num1, int num2) {
    // Suma dos números
    return num1 + num2;
}

// Ejemplo de comentario útil

// Calcula el total de la compra incluyendo el impuesto
public double calcularTotal(double subtotal, double impuesto) {
    double total = subtotal + (subtotal * impuesto);
    return total;
}
```

1. Formato consistente

El código debe tener un formato consistente para facilitar la lectura.

arduino

```java
// Ejemplo de formato inconsistente

public void imprimirResultado(){
int resultado=3+4;
System.out.println("El resultado es: "+resultado);}

// Ejemplo de formato consistente

public void imprimirResultado() {
    int resultado = 3 + 4;
    System.out.println("El resultado es: " + resultado);
}
```

1. Evitar código duplicado

El código duplicado debe ser evitado para facilitar el mantenimiento.

```java
// Ejemplo de código duplicado

public void imprimirNombreCompleto(String nombre, String apellido) {
    System.out.println(nombre + " " + apellido);
}

public void imprimirNombreYApellido(String nombre, String apellido) {
    System.out.println(nombre + " " + apellido);
}

// Ejemplo de código sin duplicación

public void imprimirNombreCompleto(String nombre, String apellido) {
    System.out.println(nombre + " " + apellido);
}

public void imprimirNombre(String nombre) {
    System.out.println(nombre);
}
```