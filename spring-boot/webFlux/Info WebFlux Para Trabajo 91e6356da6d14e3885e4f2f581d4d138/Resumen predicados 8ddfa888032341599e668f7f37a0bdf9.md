# Resumen predicados

Un predicado es una función que toma un objeto como entrada y devuelve un valor booleano que indica si se cumple una condición determinada. Los predicados se utilizan en muchos contextos, como por ejemplo en la búsqueda y filtrado de elementos en una colección.

Ejemplo:

```java
List<String> names = Arrays.asList("John", "Mary", "Peter", "Jane");

Predicate<String> startsWithJ = name -> name.startsWith("J");
List<String> jNames = names.stream().filter(startsWithJ).collect(Collectors.toList());
```