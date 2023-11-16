# Resumen de solid

Los principios SOLID son un conjunto de principios de diseño de 
software que se enfocan en la creación de código escalable, modular y 
mantenible. Aquí hay un ejemplo de cómo se pueden aplicar los principios
 SOLID en un proyecto:

- Principio de Responsabilidad Única (SRP): Cada clase o función debe tener una sola responsabilidad.
- Principio de Abierto/Cerrado (OCP): El código debe ser abierto a la extensión pero cerrado a la modificación.
- Principio de Sustitución de Liskov (LSP): Las clases hijas deben
poder ser utilizadas en lugar de las clases padre sin cambiar el
comportamiento del programa.
- Principio de Segregación de Interfaz (ISP): Las interfaces deben ser específicas y no tener más métodos de los necesarios.
- Principio de Inversión de Dependencia (DIP): Las clases de nivel
superior no deben depender de las clases de nivel inferior, sino que
deben depender de las abstracciones.

Ejemplos de códigos en java:

1. Principio de Responsabilidad Única (SRP)

Cada clase o función debe tener una sola responsabilidad.

java

```java
// Ejemplo de violación del principio SRP

public class User {
    public void createUser() {
        // Lógica para crear un usuario
        // Lógica para enviar un email de confirmación
    }
}

// Ejemplo de cumplimiento del principio SRP

public class User {
    public void createUser() {
        // Lógica para crear un usuario
    }
}

public class EmailSender {
    public void sendEmail() {
        // Lógica para enviar un email de confirmación
    }
}
```

1. Principio de Abierto/Cerrado (OCP)

El código debe ser abierto a la extensión pero cerrado a la modificación.

typescript

```java
// Ejemplo de violación del principio OCP

public class User {
    public void createUser(String type) {
        if (type.equals("admin")) {
            // Lógica para crear un usuario administrador
        } else if (type.equals("regular")) {
            // Lógica para crear un usuario regular
        }
    }
}

// Ejemplo de cumplimiento del principio OCP

public interface UserCreator {
    void createUser();
}

public class AdminUserCreator implements UserCreator {
    public void createUser() {
        // Lógica para crear un usuario administrador
    }
}

public class RegularUserCreator implements UserCreator {
    public void createUser() {
        // Lógica para crear un usuario regular
    }
}

public class User {
    private UserCreator userCreator;

    public User(UserCreator userCreator) {
        this.userCreator = userCreator;
    }

    public void createUser() {
        userCreator.createUser();
    }
}
```

1. Principio de Sustitución de Liskov (LSP)

Las clases hijas deben poder ser utilizadas en lugar de las clases padre sin cambiar el comportamiento del programa.

arduino

```java
// Ejemplo de violación del principio LSP

public class Rectangle {
    private int width;
    private int height;

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getArea() {
        return width * height;
    }
}

public class Square extends Rectangle {
    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        super.setHeight(width);
    }

    @Override
    public void setHeight(int height) {
        super.setWidth(height);
        super.setHeight(height);
    }
}

// Ejemplo de cumplimiento del principio LSP

public interface Shape {
    int getArea();
}

public class Rectangle implements Shape {
    private int width;
    private int height;

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getArea() {
        return width * height;
    }
}

public class Square implements Shape {
    private int side;

    public void setSide(int side) {
        this.side = side;
    }

    public int getArea() {
        return side * side;
    }
}
```

1. Principio de Segregación de Interfaz (ISP)

Las interfaces deben ser específicas y no tener más métodos de los necesarios.

java

```java
// Ejemplo de violación del principio ISP

public interface User {
    void createUser();
    void updateUser();
    void deleteUser();
    void sendEmail();
}

// Ejemplo de cumplimiento del principio ISP

public interface CreateUser {
    void createUser();
}

public interface UpdateUser {
    void updateUser();
}

public interface DeleteUser {
    void deleteUser();
}

public interface SendEmail {
    void sendEmail();
}

public class User implements CreateUser, UpdateUser, DeleteUser {
    public void createUser() {
        // Lógica para crear un usuario
    }

    public void updateUser() {
        // Lógica para actualizar un usuario
    }

    public void deleteUser() {
        // Lógica para eliminar un usuario
    }
}

public class EmailSender implements SendEmail {
    public void sendEmail() {
        // Lógica para enviar un email
    }
}
```

1. Principio de Inversión de Dependencia (DIP)

Las clases de nivel superior no deben depender de las clases de nivel inferior, sino que deben depender de las abstracciones.

java

```java
// Ejemplo de violación del principio DIP

public class User {
    private MySqlDatabase mySqlDatabase;

    public User() {
        this.mySqlDatabase = new MySqlDatabase();
    }

    public List<User> getAllUsers() {
        return mySqlDatabase.getAllUsers();
       }
}

public class MySqlDatabase {
    public List<User> getAllUsers() {
        // Lógica para obtener todos los usuarios desde la base de datos MySQL
    }
}

// Ejemplo de cumplimiento del principio DIP

public interface Database {
    List<User> getAllUsers();
}

public class MySqlDatabase implements Database {
    public List<User> getAllUsers() {
        // Lógica para obtener todos los usuarios desde la base de datos MySQL
    }
}

public class User {
    private Database database;

    public User(Database database) {
        this.database = database;
    }

    public List<User> getAllUsers() {
        return database.getAllUsers();
    }
}

```