# Beginning tutorial

## Index
[Importar paquetes](#import-package)
[Crear Nombre de espacio](#create-namespace)
[Función principal](#main-function)
[Convención de nombres](#naming-conventions)
[Escribir en consola](#write-in-console)
[Formato de salida](#output-format)
[Obtener datos por consola](#get-console-input-value)
[Tipos de datos](#data-type)
[Parse](#data-type-conversion-parse)
[Conversión explícita](#explicit-conversion)
[Conversión implícita](#implicit-conversion)
[Template String](#template-string)
[Operaciones con string](#string-operations)
[Comentarios](#comments)
[Arreglos](#array)
[Functions](#functions)
[Loops](#loops)
[Switch](#switch)


## Import-Package

- `using Package_Name;`

## Create namespace

```c#
namespace NAMESPACE_NAME {
    public class Program {

    }
}
```

## Main Function

The main function allows you to run code after startup

```c#
namespace NAMESPACE_NAME {
    public class Program {
        static void Main(string[] args){

        }
    }
}
```

## Naming Conventions

- `Variable` -> camelCase
- `Method and Class` -> CamelCase

## Write in console

- `System.Console.Write`

## Output Format

- Currency

```c#
Console.WriteLine("Currency : {0:c}", 23.455)
```

That way of treating the string reminds me of the pipes in angular. The `c` is a pipe of currency. This means that the value that will be shown in the console will be formatted using the currency standard.

- fill the number with zeros at the beginning

```c#
Console.WriteLine("Currency : {0:d4}", 23)
```

This format will add zeros before the number. The amount of zeros is `4` (in this case) - the number, so the final value will be: `0023`

- Fix decimal places

```c#
Console.WriteLine("Currency : {0:f3}", 23.123456)
```

The `f3` allows only 3 decimal digits in a number. So the above example will be printed as we can see now: `23.123`. If I want to change the number of decimal values to show, the only thing to do is replace 3 with another number. But will round the number.

 - Add decimal zeros

```c#
Console.WriteLine("Currency : {0:n4}", 23.12)
```

The `n#` format will add zeros after the point value of a number. The number of zeros will be the `#` (in this case 4) - numbers after the comma. This case will be: `23.1200`

## Get Console input value

- `Console.ReadLine();`

## Data Type

- `String` -> Data text
- `bool` -> True or false value
- `int` -> Integer values
- `long` -> Bigger than int without decimal points
- `decimal` -> a number with a decimal point 
- `double` -> decimal value but with precision loss
- `float` -> like double but with less precision
- `Type []` -> Array of a specific type

- Other data types
- `byte` -> 8 bit is like 0 to 255
- `char` -> 16 bit a character
- `sbyte` -> 8 bits but only numbers from 128 to 127
- `short` -> 16 bits, numbers from - 32 768 to 32 767
- `uint` -> 32 bits, 0 to 4 294 967 295
- `ulong` -> 64 bits 0 to 18 446 744 073 709 551 615
- `ushort` -> 16 bits 0 to 65 535 

## Data type conversion (parse)

> `boolean`

```c#
bool boolFromStr = bool.Parse("true") // The string value must be true or false
```

> `int`

```c#
int intFromStr = int.Parse("100") // The string must be exprese has number
```

> `string`

```c#
string strVal = intFromStr.toString();
```

## Explicit conversion

```c#
Console.WriteLine($"Data with lost in conversion {(int)dbNumber}")
```

In this type of conversion, we always going to lose some data from the original value.

## Implicit conversion

This way of work is like, I don't want to convert the value, let the engine do that for me:

```c#
long data = intValue;
```

## Template String

- `Console.WriteLine($"Hello {world}")`

The $ symbol represents a template string. And the {} represents a variable inside a string chain.

- `Console.Write("Biggest Integer: {0}", ...args)`

In this case, the template string follows an array approach. We put inside the quotes all our variable placeholders `{}` with a number inside `{0}`. That number represents the position taken from the args params.

## String operations

- Length

Get the length of a string

`stringValue.Length`

- Contains

Check if a string contains a specific value.

`stringValue.Contains("is")` -> returns true or false

- IndexOf

Check the position of an element

`stringValue.IndexOf("is")` -> returns -1 if the element doesn't exist, otherwise returns a number above -1.

- Remove

Remove a substring from a string

`stringValue.Remove(10, 6)` -> Remove from position 10, 6 values

- Add String

Insert a string value inside another string

`stringValue.Insert(10, "short ")` -> Insert a string in the position X

- Replace String

`stringValue.Replace("string", "sentence")` -> Replace the word `string` with `sentence`.

- Compare String

`stringValue.Compare("A", "B")` -> Compare two string

If with want to add the ignoreCase to the string comparison, we need to write:

`stringValue.Compare("A", "B", StringComparison.OrdinalIgnoreCase)` -> Compare two string

The values returned by this function are:

- < 0 if String1 precedes String2
- = 0 if both are equals
- \> 0 if String2 precedes String1

- Scape Characters

Use " inside a String.

`Console.WriteLine(@"Hola mundo \n")` -> This will print the \n instead a new line 
 
 ## Comments

There are two types of comments:

1. Line comment: `// hello world`
2. Multi-line comment: `/* */`

## Array

- Typed array

```c#
string[] customer = new string[7];
```

- Typed array with values

```c#
string[] customer = {"Nathan", "Archival", "Bastian"};
```

- Object Array

```c#
object[] customer = {"Nathan", 2, new Data("castillo", 1996)};
```

- Multidimensional array O Matrix Array

```c#
string[,] customer = new string[2,2];
```

- Multidimensional array O Matrix Array with Values

```c#
string[,] customer = new string[2,2] {{"camilo","ascuas"},{"arnulo","chicho"}};
```

## Functions

```c#
public void PrintArray(data){}
```
 
## Loops

- For

```c#
foreach(int k in Data){}
for(int i = 0; i < n; i++){}
```

- While
```c#
while(i<2){}
```

- doWhile
```c#
do {} while(data);
```

## Switch

```c#
switch (data){
    case 1:
    case 2:
        Console.WriteLine("Algo");
        break;
    default:
        Console.WriteLine("Default")
        break;
}
```

## Try-Catch

```c#
try{}
catch(DataType var){}
finally{}
```

## Convert variables

```c#
Convert.ToDouble(var);
```

## Date Time

```c#
DateTime dat = new DateTime(YY, MM, DD);
TimeSpan time = new TimeSpan(HH, MM, SS);
```

## Enum

```c#
enum CL {
    DATA,
    DATA1
}
```

Otra forma de usar enum es crear una clave valor

```c#
enum CL : byte {
    DATA = 0,
    DATA1 = 1,
}
```

## Classes

> Allows overloading

```c#
class Animal {
    private string name;
    public string sound;

    public Animal() : this("A", "B") {} // Constructor with default values

    public Animal(string data, string data2) {
        this.name = data;
        this.sound = data2;
    }

    // Default value in method
    public Animal(string data = "A", string data2 = "B") {
        this.name = data;
        this.sound = data2;
    }

    // Set name with not number validation
    public void SetName(string name) {
        if(!name.Any(char.IsDigit)) // Any allows searching for at least one coincidence. Char.IsDigit returns is a char is a digit or not
        {
            this.name = name;
        } else {
            this.name = "No Name";
            Console.WriteLine("Name can't contain numbers")
        }
    }

    public string GetName() {
        return name;
    }

    // Allows properties creation. The property can only be created if a base value ex sound exists 
    public string Sound { // This reference a variable
        get {return sound;}
        set {
            if(value.Length > 10){
                sound = "No Sound"
            } else {
                this.sound = value;
            }
        }
    }

    // Property creation with default value. This is only possible because you have get and set without body, otherwise the system will rise an error.
    public string Owner {get; set;} = "No Owner"; // This create a variable

    // Virtual method allows children to override the behavior
    public virtual void MakeSound() {
        Console.WriteLine("Woof");
    }
}
```

- Inheritance with virtual method

```c#
public class Animal
{
    private string _owner;
    private string _name;

    public Animal()
    {
        _owner = "sad";
        _name = "Eduardo";
    }

    public string Name
    {
        get { return _name; }
        set
        {
            if (!value.Equals("Test"))
            {
                _name = value;
            }
            else
            {
                _name = "tortuga";
            }
        }
    }

    public string Owner {
        get
        {
            return _owner + "Azul";
        }
        set
        {
            _owner = value;
        }
    }

    public string Sound { get; set; } = "woof";

    public virtual void MakeSound()
    {
        Console.WriteLine($"{Sound} + Woof Woof");
    }
}

public class Cat : Animal
{
    public override void MakeSound()
    {
        Console.WriteLine($"Meow {Sound}");
    }
}
```

> Use virtual when the base class has a default behavior for a method but that one will be override in the children.

- Inheritance with abstract

```c#
public abstract class AnimalA
{
    protected AnimalA(bool magic, string spell)
    {
        this.magic = magic;
        this.spell = spell;
    }

    protected bool magic;
    protected string spell;

    public abstract void Cast();

    public bool Magic {
        get
        {
            return magic;
        }

        set
        {
            magic = value;
        }
    }

    public string Spell
    {
        get
        {
            return spell;
        }

        set
        {
            spell = value;
        }
    }
}

public class CatA : AnimalA
{
    public CatA(bool magic, string spell) : base(magic, spell)
    {
    }

    public override void Cast()
    {
        Console.WriteLine(Magic);
        
        if(Magic)
            Console.WriteLine($"The cat cast the spell {Spell}");
        else
        {
            Console.WriteLine($"The cat can't cast {Spell}");
        }
    }
}
```

> Use abstract when you want to create a class with base behavior but one or more methods needs to be override in the children classes.

- Implementation of those classes

```c#

// Virtual

Animal animal = new Animal();

animal.Owner = "Azzz";

Console.WriteLine($"The animal {animal.Name} makes {animal.Sound} and his owner is {animal.Owner}");

animal.MakeSound();

Animal cat = new Cat();

// Abstract

Console.WriteLine($"The animal {cat.Name} makes {cat.Sound} and his owner is {cat.Owner}");

cat.MakeSound();

AnimalA animalA = new CatA(true, "wafuwafu");

animalA.Cast();
```

### Diccionarios

> Store key - values pair.

```c#
Dictionary<string, string> superHeroes = new Dictionary<String, String>();
superHeroes.Add("Clark Kent", "Superman"); // Add value
superHeroes.Remove("Clark Kent") // Remove value
superHeroes.Count() // Count 
superHeroes.ContainsKey("Clark Kent") // If a key exist
superHeroes.TryGetValue("Clark Kent", out string test) // try get value an store it in a variable

// Iterate over a dictionary
foreach(KeyValuePair<string, string> item in superHeroes){
    Console.WriteLine("{0}, {1}", item.Kay, item.Value)
}
```

### QUEUE

> Queue is a data structure that follows the FIFO principle, First In First Out. Thus, the first element that enters to the queue will be the first one in been taken out

```c#
Queue queue = new Queue();
queue.Enqueue(1); // Add data to queue
queue.Dequeue(); // Takes and remove the first element
queue.Peek(); // Allows you to see the first element

object[] queueArray = queue.ToArray(); // Convert queue to array

foreach (object o in queue){} // Loop over queue -> Please don't do this
```

### Stack

> Stack is a data structure that follows the LIFO principle, Last In First Out. Thus, the first element that enters to the queue will be the last one in been taken out from the estructure

```c#
Stack stack = new Stack();
stack.Push(1); // Add data to queue
stack.Pop(); // Takes and remove the first element
stack.Peek(); // Allows you to see the first element

object[] stackArray = stack.ToArray(); // Convert queue to array

foreach (object o in stack){} // Loop over queue -> Please don't do this
```

### List

A little about lists. You know the basics, but this one is so cool.

`ZIP` -> This list method allows you to iterate over two lists at the same time and use an arrow function with a tuple as params (x,y).

`Aggregate` -> Takes all the values from an array and returns a single element. The function to be used in this method is an arrow function.

`All` -> Iterate over all list values and find if all values fulfill a condition.

`Any` -> Same as the above method, but the condition only needs to be fulfilled one time.

### Generics

> Generic is a way to create a class, variable or method that not have a specific type

```c#
class Animal {
    public string Name {get; set;}
    public Animal(string name = "No Name") {Name = name;}
    public static void GetSum<T>(ref T num1, ref T num2){ // T represent the generic type. We use ref to say that element has a referent to T class
        // Some Logic Here
    }
}
```

### IEnumerable

The interface IEnumerable allows you to iterate over a non-generic collection. Example a custom class. Let's see an example:

```c#
class Person {
    private string firstName;
    private string lastName;

    // Constructor here
}

// This allows us make a foreach over our collection
class People: IEnumerable //We are going to convert this class in an iterable
{
    private List<Person> person = new List<Person>();

    public Person this[int index]{
        get{return (Person)person[index]}
    }

    // The enumerator method implemented
    public IEnumerator GetEnumerator(){
        return animalList.GetEnumerator();
    }
}
```

### Operator overloading

This type of function allows us to change the behavior of normal operators like + or - in particular situations. For example, if you have a class of Box (length, width) you can change the behavior of + by doing:

```c#
public static Box operator +(Box box1, Box box2){
    Box boxy = new Box(){
        Length = box1.Length + box2.Length;
        Width = box1.Width + box2.Width;
    }

    return boxy;
}
```

The above method will override the behavior of box1 + box2 in the main code. Instead of the default behavior of that symbol, now when two boxes are added, a new Box will be created.

Also, you can overload explicit an implicit operator as int() or Box(). Let's see some examples:

> Explicit convert box to int
```c#
public static explicit operator int(Box b){
    return (int)(b.Length + b.Width + b.Breadth) / 3;
}
```

> Implicit convert from something (int) to Class
```c#
public static implicit operator Box(int i){
    return new Box(i,i,i);
}
```

#### LINQ -> Language Integrated Query

LinQ is a language that allows you to manipulate data on iterables. An example can be found below.

```c#
string [] dogs = {"Astro", "Scooby Doo", "Snoopy"}

// List only dogs with spaces in their names -> Scooby Doo
var dogSpaces = from dog in dogs
                where dog.Contains(" ")
                orderby dog descending
                select dog;

// The value of dogSpaces is [{"Scooby Doo"}]

// An example with animals and owners
var innerJoin = form animal in animals 
                join owner in owners on animal.AnimalID // Animal id is the owner id
                equals owner.OwnerId
                select new {OwnerName = owner.Name, AnimalName = animal.Name};
```

#### Threads

The capacity to use CPU threads in a c# program. Let's see an example:

```c#
static void Pring1(){
    for(int i = 0; i < 1000; i++){
        Console.WriteLine(i);
    }
}

static void Main(string[] args){
    Thread t = new Thread(Print1); // Create a new thread with Print1 method as value
    t.start()// Start thread
}
```

> Use sleep to suspend an execution

```c#
static void Main(string[] args){
    int num = 1;
    for (int i = 0; i < 10; i++){
        Thread.sleep(1000) // Stop the execution for a period of time
        Console.WriteLine(i)
    }
}
```

If you want to create multiple threads in a program, you can use a thread array and to start a thread immediately  `ThreadStart`. Example:

```c#
Thread[] ths = new Thread[15]

for (int i=0; i < 15; i++){
    Thread t = new Thread(new ThreadStart(SOMETHING_HERE))// You need to encapsulate the threadStart class inside the thread
    t.Name = i.ToString() // Change thread name
    ths[i] = t; // Store Thread
}
```

#### Lock

Is like a transaction. The operation will be locked until it finishes. Example:

```c#
public double Withdraw(double amt) {
    lock(BOOLEAN VARIABLE HERE){
        // Code to lock inside here
    }
}
```

#### Directory Info

This class allows you to access specific directory information.

```c#
DirectoryInfo directInfo = new DirectoryInfo("PATH TO DIRECTORY")

Directory.METHOD // Static class to play around with directories.
```

#### File

The file class allows us to interact with the PC file system. Let's see an example:

```c#
string textFilePath = @"C:\Users\MyUser\data.txt"; // Set the location
string dataToStore[] = {"BLUE","RED","GREEN"}; // Set the data to write
File.WriteAllLines(textFilePath, dataToStore); // Creates a file and write inside of it
File.ReadAllLines(textFilePath); // Reads all lines from a file
```

What happens if I want to only get the info of a file? Well, you can use `FileInfo` for this. A simple example will be provided below:

```c#
DirectoryInfo myDir = new DirectoryInf( @"C:\Users\MyUser")// Set directory path
FileInfo[] txtFiles = myDir.GetFiles("*.txt", SearchOption.AllDirectories); // Set the files. In this case, we use the method GetFiles from myDir variable. Inside, a regex is used to specify which files are allowed to be selected. At the end of the method, we need to specify the search options; in this case, we use all directories. So if the folder has a directory inside of it, the GetFiles method would iterate over those.
```

Read an array of bytes with FileStream (a way to do the above):

```c#
string textFilePath = @"C:\Users\MyUser\data.txt";
FileStream fs = File.Open(textFilePath2, FileMode.Create); // Use FileMode.Create if the document isn't present in the folder
string randString = "This string"; // Create the data to store in the file
byte[] rsByteArray = Encoding.Default.GetBytes(randString); // Convert the string data in bytes
fs.Write(rsByteArray, 0, rsByteArray.Length) // Write the bytes in file

// Now we read

byte[] fileByteArray = new byte["LENGTH HERE"];
for(int i=0; i < rsByteArray.Length; i++){ // Here they use the rsByteArray as placeholder
    fileByteArray[i] = (byte)fs.ReadByte(); // Read all bytes from file
}

// Byte to String

Encoding.Default.GetString(fileByteArray);

fs.Close(); // Is Important to close the fileStream
```

The tutorial says that is a better option to use `StreamWriter` and `StreamReader` over `FileStream`:

```c#
string textFilePath = @"C:\Users\MyUser\data2.txt";
StreamWriter sw = new StreamWriter(textFilePath); // Create a writeable file or use one
sw.Write("SOME");
sw.WriteLine("WORLD");
sw.Close(); // Always close this

// Read file
StreamReader sr = new StreamReader(textFilePath);
Convert.ToChar(sr.Peek()); // Convert toChar allows you to convert byte to character. Peek takes the first letter
sr.ReadLine(); // Reads a document line
sr.ReadToEnd(); // Reads all the elements left
```

The next step is the `BinaryReader` and `BinaryWriter`:

```c#
// Writer
string textFilePath = @"C:\Users\MyUser\data3.txt";
FileInfo datFile = new FileInfo(textFilePath); // For BinaryWriter, you need to pass the FileInfo instead of a string with the direction
BinaryWriter bw = new BinaryWriter(datFile.OpenWrite()); // Use the fileInfo in Write state
string dat = "Some here";
bw.Write(dat); // Write data inside the file
bw.Close(); // Always close

// Reader
BinaryReader  br = new BinaryReader(datFile.OpenRead());
br.ReadString(); // Read a line that will be a string. You can read the lines and parse at the same time example. br.ReadDouble for read doble data.
```

#### Serialization

Allows you to convert an object to a readable or a sort of transported state (JSON, XML, Binary to mention a few). And deserialization is the opposite, converting readable data into an object. As an example, we have the class `Animal` and that class will be marked as serializable

```c#
[Serializable()]
class Animal : ISerializable {
    // Base date Here

    // Override to String
    public override string ToString(){
        return "Blue";
    }

    // This method comes with the ISerializable interface
    public void GetObjectData(SerializationInfo info, StreamingContext context){
        info.AddValue("Name",Name); // Add as key - value pair the data to serialize.
    }

    // The deserialization
    public Animal(SerializationInfo info, StreamingContext context){
        Name = (string)info.GetValue("Name", typeof(string)); // Get a value from serializationInfo and convert it to string
    }
}
```

In the main class, we import two serialization libraries:
- `System.Runtime.Serialization.Formatters.Binary` -> Serialize to binary
- `System.Xml.Serialization` -> To XML file

> To Binary

``` c#
Animal bowser = new Animal("Bowser",45,25);
Stream stream = File.Open("FILE", FileMode.Create);
BinaryFormatter bf = new BinaryFormatter(); // The name says all.

bf.Serialize(stream, bowser); // This method takes and stream and a object. The first one is for writing propuse and the second one is the object to serialize, please notice that int the above class the serialize method was implemented.
stream.Close(); // Always close an stream

// Now read
Stream stream = File.Open("FILE", FileMode.Open);
bf = new BinaryFormatter(); 

Animal bowserRead = (Animal)bf.Deserialize(stream);
stream.close()
```

> To XML

``` c#
Animal bowser = new Animal("Bowser",45,25);
Stream stream = File.Open("FILE", FileMode.Create);
XmlSerializer xmlSer = new XmlSerializer(typeof(Animal));// Create a xml serializer of animal type

// This one is to auto open and close a file
using (TextWriter tw = new StreamWriter(@"PATH")){
    xmlSer.Serialize(tw,bowser); // Write on the text writer
}

// Deserialize
XmlSerializer xmlDeserializer = new XmlSerializer(typeof(Animal));
TextReader reader = new StreamReader(@"PATH TO FILE") // In this case you need to create a text Reader
object obj = xmlDeserializer.Deserialize(reader);
reader.close();
bowser = (Animal)obj;

// Multiple data
using(Stream fs = new FileStream(@"PATH", FileMode.Create,FileAccess.Write,FileShare.None)){
    // login here
}

```

#### Database