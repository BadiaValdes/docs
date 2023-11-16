# Resumen diferencia entre map y flatMap

Map y flatmap son dos métodos utilizados en los streams de Java para transformar los elementos de una colección. La principal diferencia es que map devuelve un nuevo stream con los elementos transformados, mientras que flatmap devuelve un stream plano de los elementos transformados.

Ejemplo:

```java
List<String> words = Arrays.asList("Hello", "World");

List<String[]> letters = words.stream()
    .map(word -> word.split(""))
    .collect(Collectors.toList());

List<String> flatLetters = words.stream()
    .flatMap(word -> Arrays.stream(word.split("")))
    .collect(Collectors.toList());
```