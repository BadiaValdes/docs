# Beginning tutorial

## Import Package

- `using Package_Name;`

## Create namespace

```c#
namespace NAMESPACE_NAME {
    public class Program {

    }
}
```

## Main Function

The main function allows you to run code on after startup

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

## OutPut Format

- Currency

```c#
Console.WriteLine("Currency : {0:c}", 23.455)
```

That way of treat the string remind me the pipes in angular. The `c` is a pipe of currency. This means that the value that will be show in the console will be formated using the currency standard.

- fill the number with zeros at the beggining

```c#
Console.WriteLine("Currency : {0:d4}", 23)
```

This formater will add zeros before the number. The amount of zeros is `4` (in this case) - the number, so the final value will be: `0023`

- Fix decimal places

```c#
Console.WriteLine("Currency : {0:f3}", 23.123456)
```

The `f3` allows only 3 decimal digits in a number. So the above example will be printed as we can see now: `23.123`. If I want to change the number of decimal values to show, the only thing to do is replace 3 with another number. But will round the number.

 - Add decimal zeros

```c#
Console.WriteLine("Currency : {0:n4}", 23.12)
```

The `n#` formater will add zeros after the point value of a number. The amount of zeros will be the `#` (in this case 4) - the amount of  numbers after the comma. This case will be: `23.1200`

## Get Console input value

- `Console.ReadLine();`

## Data Type

- `String` -> Data text
- `bool` -> True or false value
- `int` -> Integer values
- `long` -> Bigger than int without decimal points
- `decimal` -> a number with decimal point 
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

## Data type convertion (parse)

> `boolean`

```c#
bool boolFromStr = bool.Parse("true") // The string value must be true or false
```

> `int`

```c#
int intFromStr = int.Parse("100") // The string must be expresed has number
```

> `string`

```c#
string strVal = intFromStr.toString();
```

## Explicit convertion

```c#
Console.WriteLine($"Data with lost in convertion {(int)dbNumber}")
```

In this type of convertion we always going to lose some data from the original value.

## Implicit convertion

This way of work is like, I don't want to convert the value, let the engine do that for me:

```c#
long data = intValue;
```

## Template String

- `Console.WriteLine($"Helo {world}")`

The $ symbol represents a template string. And the {} represent a variable inside a string chain.

- `Console.Write("Biggest Integer: {0}", ...args)`

In this case, the template string follows an array aproach. We put inside the quotes all our variable placeholders `{}` with a number inside `{0}`. That number represents the position taken from the args params.

## String operations

- Length

Get the length of a string

`stringValue.Length`

- Contains

Check if a string contains a specific value.

`stringValue.Contains("is")` -> returns true or false

- IndexOf

Check position of an element

`stringValue.IndexOf("is")` -> return -1 if the element dosn't exist, otherwise returns a number above -1.

- Remove

Remove a substring from a string

`stringValue.Remove(10, 6)` -> Remove from position 10, 6 values

- Add String

Insert a string value inside another string

`stringValue.Insert(10, "short ")` -> Insert a string in the positon X

- Repleace String

`stringValue.Replace("string", "sentence")` -> Repleace the word `string` with `sentence`.

- Compare String

`stringValue.Compare("A", "B")` -> Compare two string

If with want to add the ignoreCase to the string comparation, wee need to write:

`stringValue.Compare("A", "B", StringComparison.OrdinalIgnoreCase)` -> Compare two string

The values returned by this function are:

- < 0 if String1 preceeds String2
- = 0 if both are equals
- \> 0 if String2 preceeds String1

- Scape Characters

Use " inside a String.

`Console.WriteLine(@"Hola mundo \n")` -> This will print the \n instead a new line 
 

## Comments

There are two types of comments:

1. Line comment: `// hellow world`
2. Multi line comment: `/* */`