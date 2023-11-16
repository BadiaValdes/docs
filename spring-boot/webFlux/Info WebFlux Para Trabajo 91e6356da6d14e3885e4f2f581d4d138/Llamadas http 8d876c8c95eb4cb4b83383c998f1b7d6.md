# Llamadas http

- RestTemplate
- WebClient
- [@RestController](https://twitter.com/RestController)
- Spring HttpClient
- Apache HttpClient
- OkHttp
- Feign

## Comparación de llamadas HTTP en Spring Boot

Existen varias opciones para realizar llamadas HTTP en una aplicación de Spring Boot. A continuación, se presenta una comparación de algunas de las opciones más populares:

- **RestTemplate**: Es una opción popular en aplicaciones de Spring Boot. Es fácil de usar y tiene una amplia documentación. Sin embargo, no es tan flexible como otras opciones y no admite de manera nativa llamadas asíncronas.
- **WebClient**: Es una opción más moderna que RestTemplate y es preferible si se necesitan llamadas asíncronas. Es más rápido y escalable que RestTemplate, pero puede ser más difícil de usar.
- **@RestController**: Es una anotación específica de Spring que se utiliza para crear controladores REST. Es fácil de usar y es ideal para aplicaciones simples. Sin embargo, puede ser menos flexible que otras opciones si se necesitan características más avanzadas.
- **Spring HttpClient**: Es una biblioteca de Spring que se utiliza para realizar llamadas HTTP. Es fácil de usar y tiene una amplia documentación. Sin embargo, no es tan flexible como otras opciones.
- **Apache HttpClient**: Es una biblioteca popular para realizar llamadas HTTP en Java. Es altamente configurable y flexible, pero puede ser más difícil de usar que otras opciones.
- **OkHttp**: Es una biblioteca de llamadas HTTP de código abierto para Java. Es fácil de usar y tiene una amplia documentación. Además, es más rápido y escalable que la mayoría de las opciones. Sin embargo, no es una opción nativa de Spring Boot.
- **Feign**: Es una biblioteca de llamadas HTTP de código abierto para Java que utiliza anotaciones de interfaz para definir las llamadas HTTP. Es fácil de usar y es una opción nativa de Spring Boot. Sin embargo, puede ser menos flexible que otras opciones si se necesitan características más avanzadas.

En general, la elección de una opción de llamada HTTP dependerá de los requisitos específicos de la aplicación. RestTemplate es una buena opción para aplicaciones simples, mientras que WebClient es ideal para llamadas asíncronas. OkHttp es una buena opción si se necesita un alto rendimiento. Feign es una buena opción si se busca una integración nativa de Spring Boot.

# Llamadas http

- RestTemplate
    - Ejemplo de código:
    
    ```
    RestTemplate restTemplate = new RestTemplate();
    String result = restTemplate.getForObject("<https://example.com/api/resource>", String.class);
    
    ```
    
- WebClient
    - Ejemplo de código:
    
    ```
    WebClient webClient = WebClient.create();
    Mono<String> result = webClient.get()
        .uri("<https://example.com/api/resource>")
        .retrieve()
        .bodyToMono(String.class);
    
    ```
    
- [@RestController](https://twitter.com/RestController)
    - Ejemplo de código:
    
    ```
    @RestController
    public class ExampleController {
        @GetMapping("/resource")
        public String getResource() {
            return "Resource";
        }
    }
    
    ```
    
- Spring HttpClient
    - Ejemplo de código:
    
    ```
    HttpClient httpClient = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("<https://example.com/api/resource>"))
        .build();
    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    String result = response.body();
    
    ```
    
- Apache HttpClient
    - Ejemplo de código:
    
    ```
    CloseableHttpClient httpClient = HttpClients.createDefault();
    HttpGet request = new HttpGet("<https://example.com/api/resource>");
    CloseableHttpResponse response = httpClient.execute(request);
    String result = EntityUtils.toString(response.getEntity());
    
    ```
    
- OkHttp
    - Ejemplo de código:
    
    ```
    OkHttpClient client = new OkHttpClient();
    Request request = new Request.Builder()
        .url("<https://example.com/api/resource>")
        .build();
    Response response = client.newCall(request).execute();
    String result = response.body().string();
    
    ```
    
- Feign
    - Ejemplo de código:
    
    ```
    @FeignClient(name = "resourceClient", url = "<https://example.com>")
    public interface ResourceClient {
        @GetMapping("/api/resource")
        String getResource();
    }
    
    ```
    

En general, la elección de una opción de llamada HTTP dependerá de los requisitos específicos de la aplicación. RestTemplate es una buena opción para aplicaciones simples, mientras que WebClient es ideal para llamadas asíncronas. OkHttp es una buena opción si se necesita un alto rendimiento. Feign es una buena opción si se busca una integración nativa de Spring Boot.