# IEnumerable vs IEnumerable<T> vs IList vs List

- IEnumerable

This interface is pretty similar to the Iterable interface in `JAVA`. But in this case, you need to implement an interface and a class. Why? The former, allows you to use the `foreach` statement. The latter is the class implementation that the `foreach` uses to know the next element in the iterable. Let's see an example:

> Is important to say that c# allows you to use `foreach` with every iterable object, regardless if the object's class implements IEnumerable or not. This behavior can only be used in C#.

```c#

// Base class
public class Person
{
    public Person(string fName, string lName)
    {
        this.firstName = fName;
        this.lastName = lName;
    }

    public string firstName;
    public string lastName;
}

// Controller class
public class People : IEnumerable // Implements IEnumerable
{
    private Person[] _people;
    public People(Person[] pArray)
    {
        _people = new Person[pArray.Length];

        for (int i = 0; i < pArray.Length; i++)
        {
            _people[i] = pArray[i];
        }
    }

// Implementation for the GetEnumerator method.
    IEnumerator IEnumerable.GetEnumerator()
    {
       return (IEnumerator) GetEnumerator();
    }

// This method calls the class that implements the IEnumerator interface
    public PeopleEnum GetEnumerator()
    {
        return new PeopleEnum(_people);
    }
}


 // When you implement IEnumerable, you must also implement IEnumerator.
public class PeopleEnum : IEnumerator
{
    public Person[] _people;

    // Enumerators are positioned before the first element
    // until the first MoveNext() call.
    int position = -1;

// Constructor
    public PeopleEnum(Person[] list)
    {
        _people = list;
    }

    // Check if you can make a movement in the list
    public bool MoveNext()
    {
        position++;
        return (position < _people.Length);
    }

// Reset the iterable
    public void Reset()
    {
        position = -1;
    }

    object IEnumerator.Current
    {
        get
        {
            return Current;
        }
    }

// Get The current value
    public Person Current
    {
        get
        {
            try
            {
                return _people[position];
            }
            catch (IndexOutOfRangeException)
            {
                throw new InvalidOperationException();
            }
        }
    }
}
  


```

- IEnumerable<T>

This is a `type-safe` version of IEnumerable. This is an interface that inherits from IEnumerable, so, the class that implements this interface also needs to implement all methods from IEnumerable.