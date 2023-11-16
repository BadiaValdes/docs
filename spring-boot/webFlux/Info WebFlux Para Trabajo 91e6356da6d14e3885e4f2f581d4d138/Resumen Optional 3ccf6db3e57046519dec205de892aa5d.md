# Resumen Optional

Optional es una clase de Java que se utiliza para representar un valor que puede o no estar presente. Se utiliza para evitar excepciones de tipo NullPointerException al trabajar con valores nulos, y para indicar de manera explícita que un valor puede no estar presente. Se suele utilizar en combinación con las expresiones lambda y los streams.

Ejemplo:

```java
Optional<String> optionalName = Optional.ofNullable(null);

String name = optionalName.orElse("Unknown");
```