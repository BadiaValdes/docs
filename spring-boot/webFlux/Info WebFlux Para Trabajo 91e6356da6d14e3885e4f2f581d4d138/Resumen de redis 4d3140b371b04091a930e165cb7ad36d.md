# Resumen de redis

Redis es una base de datos en memoria ampliamente utilizada para almacenar y recuperar datos rápidamente. Aquí hay un ejemplo de cómo utilizar Redis en un proyecto:

Ejemplo:

```java
@Autowired
private RedisTemplate<String, String> redisTemplate;

public void setValue(String key, String value) {
    redisTemplate.opsForValue().set(key, value);
}

public String getValue(String key) {
    return redisTemplate.opsForValue().get(key);
}
```