# Resumen Interfaces y clases abstractas

Las interfaces y las clases abstractas son herramientas que se utilizan para definir un conjunto de métodos o propiedades que deben ser implementados por una clase concreta. La diferencia principal es que las clases abstractas pueden contener métodos concretos, mientras que las interfaces no. Se suele utilizar una clase abstracta cuando se quiere definir una implementación básica que puede ser extendida por otras clases, y una interfaz cuando se quiere definir un contrato que debe ser implementado por diferentes clases.

Ejemplo:

```java
public interface Shape {
    double getArea();
    double getPerimeter();
}

public abstract class AbstractShape implements Shape {
    protected int sides;

    public int getSides() {
        return sides;
    }

		public abstract double getPerimeter();
}

public class DShape extends AbstractShape {
	public DShape() {
		super();
	}

	public double getPerimeter(){
		return 2.2;
	}
}
```