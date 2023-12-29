# Project Structure

- Controllers -> Store all Controller Classes
- obj -> Project properties
- Properties -> Launch project setting
- appsettings.json -> project configurations
- Program.cs -> Main program
- proy1.csproj -> Global SDK declaration
- WeatherForecast -> Model

# Controllers Inheritance

All controllers class must extend from `ControllerBase`, let's see and example:

``` c#
public class WeatherForecastController : ControllerBase
```

# Decorators

- `[ApiController]` -> Declare a class as API controller.
- `[Route("[controller]")]` -> Creates a route to a class
- `[HttpGet(Name = "GetWeatherForecast")]` -> Declare de HTTP Method to be used
    > [HttpGet]

    > [HttpPost]

    > [HttpPut]

    > [HttpPut]

    > [HttpDelete]

    > [HttpHead]

    > [HttpPatch]
- 