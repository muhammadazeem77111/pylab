package com.example.ui.model

data class ExampleItem(
    val title: String,
    val category: String,
    val explanation: String,
    val code: String
)

data class PracticeQuestion(
    val id: Int,
    val question: String,
    val hint: String,
    val solution: String,
    val initialCode: String
)

object PythonData {
    val examples = listOf(
        // Basic Python
        ExampleItem("Hello World", "Basic Python", "Prints a message to the output console.", "print(\"Hello, World!\")"),
        ExampleItem("Variables", "Basic Python", "Stores data in variables and prints them.", "name = \"Alex\"\nage = 20\nprint(\"Name:\", name)\nprint(\"Age:\", age)"),
        ExampleItem("User Input", "Basic Python", "Takes user input and greets them.", "name = input(\"Enter your name: \")\nprint(\"Hello\", name, \"!\")"),
        ExampleItem("Arithmetic Operators", "Basic Python", "Performs basic math calculations.", "a = 15\nb = 4\nprint(\"Sum:\", a + b)\nprint(\"Product:\", a * b)\nprint(\"Division:\", a / b)"),
        ExampleItem("Even or Odd", "Basic Python", "Checks if a number is even or odd.", "num = 7\nif num % 2 == 0:\n    print(num, \"is Even\")\nelse:\n    print(num, \"is Odd\")"),

        // Conditions
        ExampleItem("if statement", "Conditions", "Executes code only when condition is true.", "score = 85\nif score >= 50:\n    print(\"Pass!\")"),
        ExampleItem("if/else", "Conditions", "Chooses between two blocks of code.", "age = 16\nif age >= 18:\n    print(\"Adult\")\nelse:\n    print(\"Minor\")"),
        ExampleItem("elif", "Conditions", "Tests multiple conditions in sequence.", "marks = 78\nif marks >= 90:\n    print(\"Grade: A\")\nelif marks >= 75:\n    print(\"Grade: B\")\nelse:\n    print(\"Grade: C\")"),
        ExampleItem("Largest of Two", "Conditions", "Finds the larger of two numbers.", "x = 25\ny = 40\nif x > y:\n    print(x, \"is larger\")\nelse:\n    print(y, \"is larger\")"),

        // Loops
        ExampleItem("for loop", "Loops", "Iterates over a sequence.", "for i in range(1, 6):\n    print(\"Number:\", i)"),
        ExampleItem("Multiplication Table", "Loops", "Prints multiplication table for a number.", "n = 5\nfor i in range(1, 11):\n    print(n, \"x\", i, \"=\", n * i)"),
        ExampleItem("Counting 1 to 10", "Loops", "Counts from 1 up to 10 using a while loop.", "count = 1\nwhile count <= 10:\n    print(count)\n    count = count + 1"),
        ExampleItem("Reverse Counting", "Loops", "Counts down from 5 to 1.", "i = 5\nwhile i > 0:\n    print(i)\n    i = i - 1"),

        // Lists
        ExampleItem("Create a List", "Lists", "Stores multiple items in a single list.", "fruits = [\"Apple\", \"Banana\", \"Cherry\"]\nprint(fruits)"),
        ExampleItem("Access List Items", "Lists", "Accesses items by their index position.", "colors = [\"Red\", \"Green\", \"Blue\"]\nprint(\"First color:\", colors[0])\nprint(\"Last color:\", colors[2])"),
        ExampleItem("Add Item", "Lists", "Appends a new item to the list.", "numbers = [10, 20, 30]\nnumbers.append(40)\nprint(numbers)"),
        ExampleItem("Loop Through List", "Lists", "Iterates through all items in a list.", "animals = [\"Dog\", \"Cat\", \"Rabbit\"]\nfor a in animals:\n    print(a)"),

        // Functions
        ExampleItem("Simple Function", "Functions", "Defines and calls a reusable function.", "def greet():\n    print(\"Welcome to Python IDE!\")\n\ngreet()"),
        ExampleItem("Function with Parameters", "Functions", "Passes arguments into a function.", "def add(a, b):\n    print(\"Sum is\", a + b)\n\nadd(10, 20)"),
        ExampleItem("Function with Return", "Functions", "Returns a value from a function.", "def square(x):\n    return x * x\n\nresult = square(6)\nprint(\"Square is\", result)"),

        // Object-Oriented Programming (OOP)
        ExampleItem("Class", "Object-Oriented Programming", "Defines a blueprint for objects.", "class Dog:\n    species = \"Canine\"\n\nprint(Dog.species)"),
        ExampleItem("Object", "Object-Oriented Programming", "Creates an instance of a class.", "class Dog:\n    species = \"Canine\"\n\ndog1 = Dog()\nprint(dog1.species)"),
        ExampleItem("__init__() Constructor", "Object-Oriented Programming", "Initializes object attributes upon creation.", "class Person:\n    def __init__(self, name, age):\n        self.name = name\n        self.age = age\n\np = Person(\"Ali\", 15)\nprint(p.name, p.age)"),
        ExampleItem("Instance Variables", "Object-Oriented Programming", "Variables specific to each object instance.", "class Car:\n    def __init__(self, brand):\n        self.brand = brand\n\nc = Car(\"Toyota\")\nprint(c.brand)"),
        ExampleItem("Methods", "Object-Oriented Programming", "Functions defined inside a class.", "class Student:\n    def __init__(self, name, age):\n        self.name = name\n        self.age = age\n\n    def show(self):\n        print(\"Name:\", self.name)\n        print(\"Age:\", self.age)\n\ns1 = Student(\"Ali\", 15)\ns1.show()"),
        ExampleItem("Encapsulation", "Object-Oriented Programming", "Restricts access to methods and attributes.", "class Account:\n    def __init__(self, balance):\n        self.__balance = balance\n\n    def get_balance(self):\n        return self.__balance\n\nacc = Account(1000)\nprint(\"Balance:\", acc.get_balance())"),
        ExampleItem("Single Inheritance", "Object-Oriented Programming", "Inherits properties from a parent class.", "class Animal:\n    def speak(self):\n        print(\"Animal speaks\")\n\nclass Cat(Animal):\n    def meow(self):\n        print(\"Meow\")\n\nc = Cat()\nc.speak()\nc.meow()"),
        ExampleItem("Multiple Inheritance", "Object-Oriented Programming", "Inherits from multiple parent classes.", "class A:\n    def method_a(self):\n        print(\"Method A\")\n\nclass B:\n    def method_b(self):\n        print(\"Method B\")\n\nclass C(A, B):\n    pass\n\nc = C()\nc.method_a()\nc.method_b()"),
        ExampleItem("Method Overriding", "Object-Oriented Programming", "Overrides a parent method in child class.", "class Parent:\n    def show(self):\n        print(\"Parent show\")\n\nclass Child(Parent):\n    def show(self):\n        print(\"Child show\")\n\nobj = Child()\nobj.show()"),
        ExampleItem("Polymorphism", "Object-Oriented Programming", "Allows different classes to have methods with same name.", "class Dog:\n    def sound(self):\n        print(\"Bark\")\n\nclass Cat:\n    def sound(self):\n        print(\"Meow\")\n\ndef make_sound(animal):\n    animal.sound()\n\nmake_sound(Dog())\nmake_sound(Cat())"),

        // Exception Handling
        ExampleItem("Basic try / except", "Exception Handling", "Catches runtime errors gracefully.", "try:\n    print(10 / 0)\nexcept:\n    print(\"An error occurred\")"),
        ExampleItem("ValueError", "Exception Handling", "Handles invalid value conversions.", "try:\n    x = int(\"abc\")\nexcept ValueError:\n    print(\"Caught ValueError: invalid integer\")"),
        ExampleItem("ZeroDivisionError", "Exception Handling", "Handles division by zero.", "try:\n    print(5 / 0)\nexcept ZeroDivisionError:\n    print(\"Cannot divide by zero\")"),
        ExampleItem("Multiple except blocks", "Exception Handling", "Catches different exceptions separately.", "try:\n    x = int(\"10\")\n    print(x / 0)\nexcept ValueError:\n    print(\"Value error\")\nexcept ZeroDivisionError:\n    print(\"Zero division error\")"),
        ExampleItem("try / except / else", "Exception Handling", "Executes else block when no exception occurs.", "try:\n    res = 10 / 2\nexcept ZeroDivisionError:\n    print(\"Error\")\nelse:\n    print(\"Success! Result:\", res)"),
        ExampleItem("try / except / finally", "Exception Handling", "Executes finally block regardless of errors.", "try:\n    print(10 / 2)\nexcept ZeroDivisionError:\n    print(\"Error\")\nfinally:\n    print(\"Always executed\")"),
        ExampleItem("Input validation", "Exception Handling", "Validates user input safely.", "try:\n    age = int(input(\"Enter age: \"))\n    print(\"Age is\", age)\nexcept ValueError:\n    print(\"Please enter a valid number.\")"),
        ExampleItem("Raising an exception", "Exception Handling", "Manually raises an exception using raise.", "def set_age(age):\n    if age < 0:\n        raise ValueError(\"Age cannot be negative\")\n    print(\"Age set to\", age)\n\ntry:\n    set_age(-5)\nexcept ValueError as e:\n    print(\"Caught:\", e)"),

        // Tkinter GUI Programming
        ExampleItem("First Tkinter Window", "Tkinter GUI Programming", "Creates a basic Tkinter window.", "import tkinter as tk\nroot = tk.Tk()\nroot.title(\"First Window\")\nroot.geometry(\"300x200\")\nroot.mainloop()"),
        ExampleItem("Label Example", "Tkinter GUI Programming", "Displays text using a Label widget.", "import tkinter as tk\nroot = tk.Tk()\nlabel = tk.Label(root, text=\"Hello Tkinter!\")\nlabel.pack()\nroot.mainloop()"),
        ExampleItem("Button Example", "Tkinter GUI Programming", "Creates a clickable button.", "import tkinter as tk\nroot = tk.Tk()\ndef click():\n    print(\"Button clicked\")\nbtn = tk.Button(root, text=\"Click Me\", command=click)\nbtn.pack()\nroot.mainloop()"),
        ExampleItem("Entry Box Example", "Tkinter GUI Programming", "Creates a text input field.", "import tkinter as tk\nroot = tk.Tk()\nentry = tk.Entry(root)\nentry.pack()\nroot.mainloop()"),
        ExampleItem("Button + Label", "Tkinter GUI Programming", "Updates label text when button is clicked.", "import tkinter as tk\nroot = tk.Tk()\nlabel = tk.Label(root, text=\"Type something\")\nlabel.pack()\nentry = tk.Entry(root)\nentry.pack()\ndef show():\n    val = entry.get()\n    label.config(text=val)\nbtn = tk.Button(root, text=\"Submit\", command=show)\nbtn.pack()\nroot.mainloop()"),
        ExampleItem("Login Form", "Tkinter GUI Programming", "Simple login form UI with labels and entries.", "import tkinter as tk\nroot = tk.Tk()\ntk.Label(root, text=\"Username\").pack()\ne1 = tk.Entry(root)\ne1.pack()\ntk.Label(root, text=\"Password\").pack()\ne2 = tk.Entry(root, show=\"*\")\ne2.pack()\ntk.Button(root, text=\"Login\").pack()\nroot.mainloop()"),
        ExampleItem("Simple Calculator GUI", "Tkinter GUI Programming", "Calculator interface with input fields and button.", "import tkinter as tk\nroot = tk.Tk()\ntk.Label(root, text=\"Simple Calculator\").pack()\ne1 = tk.Entry(root)\ne1.pack()\ne2 = tk.Entry(root)\ne2.pack()\ntk.Button(root, text=\"Add\").pack()\nroot.mainloop()"),
        ExampleItem("Checkbutton Example", "Tkinter GUI Programming", "Creates a checkbox widget.", "import tkinter as tk\nroot = tk.Tk()\nvar = tk.IntVar()\ncb = tk.Checkbutton(root, text=\"Accept terms\", variable=var)\ncb.pack()\nroot.mainloop()"),
        ExampleItem("Radiobutton Example", "Tkinter GUI Programming", "Creates radio button selection group.", "import tkinter as tk\nroot = tk.Tk()\nvar = tk.StringVar(value=\"Male\")\ntk.Radiobutton(root, text=\"Male\", variable=var, value=\"Male\").pack()\ntk.Radiobutton(root, text=\"Female\", variable=var, value=\"Female\").pack()\nroot.mainloop()"),
        ExampleItem("Listbox Example", "Tkinter GUI Programming", "Displays a list of items.", "import tkinter as tk\nroot = tk.Tk()\nib = tk.Listbox(root)\nib.insert(1, \"Python\")\nib.insert(2, \"Java\")\nib.pack()\nroot.mainloop()"),
        ExampleItem("Frame Example", "Tkinter GUI Programming", "Groups widgets inside a frame.", "import tkinter as tk\nroot = tk.Tk()\nf = tk.Frame(root)\nf.pack()\ntk.Label(f, text=\"Inside Frame\").pack()\nroot.mainloop()"),
        ExampleItem("pack() Example", "Tkinter GUI Programming", "Arranges widgets using pack geometry manager.", "import tkinter as tk\nroot = tk.Tk()\ntk.Button(root, text=\"Top\").pack(side=\"top\")\ntk.Button(root, text=\"Bottom\").pack(side=\"bottom\")\nroot.mainloop()"),
        ExampleItem("grid() Example", "Tkinter GUI Programming", "Arranges widgets in a 2D table grid.", "import tkinter as tk\nroot = tk.Tk()\ntk.Label(root, text=\"Row 0\").grid(row=0, column=0)\ntk.Label(root, text=\"Row 1\").grid(row=1, column=0)\nroot.mainloop()"),
        ExampleItem("place() Example", "Tkinter GUI Programming", "Places widgets at absolute x, y coordinates.", "import tkinter as tk\nroot = tk.Tk()\ntk.Button(root, text=\"At 50, 50\").place(x=50, y=50)\nroot.mainloop()"),
        ExampleItem("Messagebox Example", "Tkinter GUI Programming", "Displays pop-up message dialogs.", "import tkinter as tk\nfrom tkinter import messagebox\nroot = tk.Tk()\ndef show_msg():\n    messagebox.showinfo(\"Title\", \"Hello Messagebox\")\ntk.Button(root, text=\"Show Info\", command=show_msg).pack()\nroot.mainloop()")
    )

    val practiceQuestions = listOf(
        PracticeQuestion(1, "Print 'Hello World'.", "Use the print function with the text string.", "print(\"Hello World\")", "# Write your code here\nprint(\"Hello World\")"),
        PracticeQuestion(2, "Take a student's name as input and print a greeting.", "Use input() and print().", "name = input(\"Enter your name: \")\nprint(\"Hello\", name)", "name = input(\"Enter your name: \")\nprint(\"Hello\", name)"),
        PracticeQuestion(3, "Add two numbers (e.g. 12 and 18) and print the sum.", "Assign two variables and print their sum.", "a = 12\nb = 18\nprint(a + b)", "a = 12\nb = 18\nprint(a + b)"),
        PracticeQuestion(4, "Find the square of a number (e.g. 9).", "Multiply the number by itself or use ** 2.", "num = 9\nprint(\"Square:\", num * num)", "num = 9\nprint(\"Square:\", num * num)"),
        PracticeQuestion(5, "Check whether a number is even or odd.", "Use modulo operator % 2 == 0.", "n = 14\nif n % 2 == 0:\n    print(\"Even\")\nelse:\n    print(\"Odd\")", "n = 14\nif n % 2 == 0:\n    print(\"Even\")\nelse:\n    print(\"Odd\")"),
        PracticeQuestion(6, "Check whether a number is positive or negative.", "Compare number with 0 using if/else.", "val = -5\nif val >= 0:\n    print(\"Positive\")\nelse:\n    print(\"Negative\")", "val = -5\nif val >= 0:\n    print(\"Positive\")\nelse:\n    print(\"Negative\")"),
        PracticeQuestion(7, "Find the largest of two numbers.", "Use if x > y else.", "x = 45\ny = 60\nif x > y:\n    print(x)\nelse:\n    print(y)", "x = 45\ny = 60\nif x > y:\n    print(x)\nelse:\n    print(y)"),
        PracticeQuestion(8, "Print numbers from 1 to 10 using a for loop.", "Use for i in range(1, 11): print(i)", "for i in range(1, 11):\n    print(i)", "for i in range(1, 11):\n    print(i)"),
        PracticeQuestion(9, "Print multiplication table of 7 up to 10.", "Use a for loop with range(1, 11).", "n = 7\nfor i in range(1, 11):\n    print(n, \"x\", i, \"=\", n * i)", "n = 7\nfor i in range(1, 11):\n    print(n, \"x\", i, \"=\", n * i)"),
        PracticeQuestion(10, "Calculate factorial of 5 using a loop.", "Multiply numbers from 1 to 5 in a loop.", "fact = 1\nfor i in range(1, 6):\n    fact = fact * i\nprint(\"Factorial of 5 is\", fact)", "fact = 1\nfor i in range(1, 6):\n    fact = fact * i\nprint(\"Factorial is\", fact)"),
        PracticeQuestion(11, "Count items in a list.", "Use len() function on a list.", "items = [10, 20, 30, 40, 50]\nprint(\"Count:\", len(items))", "items = [10, 20, 30, 40, 50]\nprint(\"Count:\", len(items))"),
        PracticeQuestion(12, "Find the largest number in a list of numbers.", "Loop through list and track maximum.", "nums = [12, 45, 22, 89, 33]\nlargest = nums[0]\nfor n in nums:\n    if n > largest:\n        largest = n\nprint(\"Largest:\", largest)", "nums = [12, 45, 22, 89, 33]\n# Find largest"),
        PracticeQuestion(13, "Create a function to add two numbers.", "Use def add(a, b): return a + b", "def add(nums_a, nums_b):\n    return nums_a + nums_b\n\nprint(add(15, 25))", "def add(a, b):\n    # Return sum\n    pass"),
        PracticeQuestion(14, "Calculate percentage marks from obtained and total.", "percentage = (obtained / total) * 100", "obtained = 420\ntotal = 500\nper = (obtained / total) * 100\nprint(\"Percentage:\", per, \"%\")", "obtained = 420\ntotal = 500\n# Calculate percentage"),
        PracticeQuestion(15, "Reverse counting from 10 to 1.", "Use while loop or range with step.", "i = 10\nwhile i > 0:\n    print(i)\n    i = i - 1", "i = 10\nwhile i > 0:\n    print(i)\n    i = i - 1"),
        // OOP Practice Questions
        PracticeQuestion(16, "Create a Student class with name and age attributes.", "Use class Student: def __init__(self, name, age):", "class Student:\n    def __init__(self, name, age):\n        self.name = name\n        self.age = age\n\ns = Student(\"Sara\", 20)\nprint(s.name, s.age)", "class Student:\n    pass"),
        PracticeQuestion(17, "Create a Car class with a start method.", "Define class Car with def start(self): print('Car started')", "class Car:\n    def start(self):\n        print(\"Car started\")\n\nc = Car()\nc.start()", "class Car:\n    pass"),
        PracticeQuestion(18, "Demonstrate single inheritance with Animal and Dog.", "Define class Dog(Animal):", "class Animal:\n    def eat(self):\n        print(\"Eating\")\nclass Dog(Animal):\n    def bark(self):\n        print(\"Barking\")\nd = Dog()\nd.eat()\nd.bark()", "class Animal:\n    pass"),
        PracticeQuestion(19, "Demonstrate method overriding.", "Override parent method in child class.", "class Parent:\n    def show(self):\n        print(\"Parent\")\nclass Child(Parent):\n    def show(self):\n        print(\"Child\")\nChild().show()", "class Parent:\n    pass"),
        // Exception Handling Practice Questions
        PracticeQuestion(20, "Handle invalid integer conversion using try/except.", "Wrap int() inside try and catch ValueError.", "try:\n    x = int(\"xyz\")\nexcept ValueError:\n    print(\"Invalid integer\")", "try:\n    x = int(\"xyz\")\nexcept:\n    pass"),
        PracticeQuestion(21, "Handle division by zero using ZeroDivisionError.", "Wrap division in try/except.", "try:\n    print(10 / 0)\nexcept ZeroDivisionError:\n    print(\"Division by zero error\")", "try:\n    print(10 / 0)\nexcept:\n    pass"),
        PracticeQuestion(22, "Use try / except / finally.", "Add finally block that always runs.", "try:\n    print(\"Processing\")\nfinally:\n    print(\"Done\")", "try:\n    print(\"Processing\")\nfinally:\n    pass"),
        // Tkinter Practice Questions
        PracticeQuestion(23, "Create a basic Tkinter window.", "Import tkinter and call Tk().", "import tkinter as tk\nroot = tk.Tk()\nroot.title(\"My App\")\n# root.mainloop()", "import tkinter as tk\nroot = tk.Tk()\n"),
        PracticeQuestion(24, "Add a Label and Button to Tkinter window.", "Use tk.Label and tk.Button with pack().", "import tkinter as tk\nroot = tk.Tk()\ntk.Label(root, text=\"Welcome\").pack()\ntk.Button(root, text=\"Click\").pack()\n# root.mainloop()", "import tkinter as tk\nroot = tk.Tk()\n")
    )
}
