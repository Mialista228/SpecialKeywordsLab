LAB 5 SPECIAL KEYWORDS

Task 1

1. Attempts and Null Returns
Attempts reported: 3 attempts are made ("abc", "200", and "35").
Why null? "abc" fails to parse into an integer (toIntOrNull() returns null), and "200" is outside the allowed range (0..120), causing takeIf to return null.
2. Bytecode with inline
Are they there? No. Because ask is marked inline, the compiler copies the code directly into main, eliminating the separate ask(...) function call and Ref.IntRef.
3. Bytecode without inline
What appeared? A standard function call to ask(...) and a Ref.IntRef object.
Why Ref.IntRef? Without inline, the lambda becomes a separate object. Since lambdas cannot normally mutate local primitive variables from an outer function, Kotlin wraps attempts in a Ref.IntRef container to allow modification.
4. Answer to your friend
Inlining only pays off for small utility functions with lambdas to eliminate call overhead and object allocations.
Marking a 200-line function inline causes massive code bloat (duplicating the bytecode at every call site), which can actually slow down your program due to instruction cache misses.

Task 2

1. The Red Line & Error Message
•	Parameter named: reminder
•	What it suggests: The compiler says an inline parameter cannot be stored or passed around as a real object (e.g., added to a list).
2. Running with "Write report"
What is printed and in what order?
Saved: Write report (from save())
Pending reminders: 1 (printed in main)
Reminder: finish 'Write report' (when pending.forEach { it() } runs later)
3. Storing save with pending.add(save)
What does the compiler say? It gives the same error for save: an inline parameter cannot be stored.
Why can it be called, but not stored? save() is executed immediately inside the function while it's running, which requires no object. Storing it in a list requires a persistent object that outlives the function call.
4. Where reminder Lives & Inlining
Where does it live? In an object (because marking it noinline forces Kotlin to allocate a real object for it).
What would be missing if it were inlined? There would be no object instance to put into the pending list, because inlined code is just raw text copied directly into the call site.

Task 3

1. findFirst vs. findAll
Why findFirst prints one line: It uses a bare return, which immediately exits the entire findFirst function upon finding the first match.
Why findAll prints several lines: It uses return@forEach (a labeled return), which only exits the current loop iteration and lets the function continue checking the rest of the list for all matches.
2. return@forEach vs. continue
What return@forEach does: It skips the rest of the current lambda block and jumps to the next item in the collection, behaving identically to continue in a standard for loop.
3. Skipped Lines & "Search finished"
Line never printed: println("No contact contains '$query'") is skipped if a match is found because the return exits the function early.
Why "Search finished" prints: It is called in main() after findFirst and findAll complete execution and return control back to main.
4. each vs. standard forEach
The error: A bare return is not allowed inside findWithEach's lambda because each is a regular (non-inlined) function.
The difference: Standard forEach is an inline function, meaning its lambda code is physically copied into the call site, allowing a bare return to exit the outer function. each compiles the lambda as a separate object, trapping the return inside it.
5. Completing the Rule
The rule: A bare return is allowed in a lambda when the lambda is passed to an inline function.

Task 4

1. The Red Line & Error Message
Parameter named: action
What it suggests: The compiler suggests adding the crossinline modifier because the lambda is called indirectly inside a Runnable object rather than directly.
2. Running the Program
With 2: Prints Goodbye!
With 9: Prints Unknown command
3. Adding return to the First Command
The error: 'return' is not allowed here.
Why it's forbidden here vs. allowed in Task 3: In Task 3, forEach executed the lambda directly inside the function body, allowing a non-local return to exit the outer function. Here, action() is wrapped inside a Runnable object and called later when .run() is invoked. A return inside a separate object cannot jump out of main().

Task 5

1. Execution Output & Input Behavior
First run (10, abc, 25.5, done):
Output:
Plaintext
'abc' is not a number, skipped
Items: 2, total: 35.5
Second run (10, abc, 20000, 5):
Output:
Plaintext
'abc' is not a number, skipped
Price 20000.0 is too large, stopping
Items: 1, total: 10.0
Is 5 ever read? No. The break statement inside the let lambda immediately exits the while(true) loop when 20000 is encountered, so the program never reaches the prompt to read 5.
2. Lambdas, Functions, and Loops
The continue statement: Located inside the lambda passed to run. It skips the rest of the current iteration of the outer while (true) loop.
The break statement: Located inside the lambda passed to let. It exits the outer while (true) loop completely.
3. Replacing run with orSkip
The error: 'break' or 'continue' is not allowed here (or non-local break/continue is not allowed).
The difference: Standard Kotlin standard library functions like run are inline, allowing non-local break and continue jumps to control the outer loop. Your custom orSkip function is a regular (non-inlined) function, so jumps inside its lambda are forbidden.
4. Making orSkip inline
Does the error disappear? Yes.
Lecture rule: Non-local jumps (break, continue, return) are allowed in a lambda only when the function accepting the lambda is marked inline and calls the lambda directly.

Task 6

1. Variables and Null Results
Which variables are null? age, height, and subscribed are null because parsing fails ("x" isn't a valid integer, "1,75" uses a comma instead of a dot for a double, and "yes" is rejected by toBooleanStrictOrNull, which strictly requires "true" or "false").
Why is city not null? An empty line still reads as an empty string (""), which successfully casts to String rather than returning null.
2. Removing reified
The two errors: The compiler complains that T is erased and cannot be used.
Where T is used: In T::class and as T? (type casting).
3. Testing readValue<Char>
Does it compile? Yes, it compiles without any errors.
When do you get the problem? At run time when the function executes.
Which line creates the message? The else -> error(...) line, because Char::class is not handled in the when branches.

Task 7

1. Running with 10 apples 2.5 7 pears 0.75
Where tokens go:
Ints: [10, 7]
Doubles: [2.5, 0.75]
Words: [apples, pears]
Why 7 is an Int: parseToken checks toIntOrNull() first before toDoubleOrNull(). Since "7" successfully parses as an integer, it stops and returns an Int.
2. Removing reified
The error: 'is' check for a type parameter has restricted semantics and the type parameter is erased.
What 'erased type' means: Due to Java/Kotlin bytecode compatibility, generic type information (T) is thrown away (erased) at compile time. At runtime, the program normally has no idea what T actually is, making item is T impossible unless reified is used to copy the concrete type directly into the code.
3. Using filterIsInstance<Int>()
Is the output the same? Yes.
The two modifiers: filterIsInstance is marked with inline and reified.
1.	Adding Support for Booleans
Running with true 5 false x:
Printing items.onlyOfType<Boolean>() outputs: [true, false]

Task 8

1. Running the Program
What is printed for price: 98.88 (based on the snippet's code this - (1 + VAT_RATE)).
What is printed for words: 4 (the double space splits into an empty string, but isNotBlank() filters it out, leaving the, quick, brown, and fox).
2. Inline Placement
Which is marked inline as a whole? withVat (inline val Double.withVat).
Which is marked only on its getter? wordCount (val String.wordCount ... inline get()).
Do both work the same way? Yes, both result in their accessor code being inlined at the call site.
3. Adding inline val taxRate
The error: 'inline' property cannot have a backing field.
Why? Inline properties cannot store state or have backing fields because they don't generate real property instances; their accessor code is just copied directly into the code wherever they are used.

Task 9

1. The Red Line & Error Message
The error: The compiler complains that a public inline function cannot access a private declaration because public inline functions are copied into other modules where private variables are invisible.
2. Changing private to internal
Is the error gone? No. internal is still restricted and not visible enough for a public inline function across module boundaries.
3. Adding @PublishedApi & Running with yes/no
Does it compile? Yes.
When is (building the report...) printed? Only when you run it with yes.
Why? Because the message is passed as a lambda (() -> String), meaning it uses lazy evaluation and is only executed inside the if (debugEnabled) block when logging is actually enabled.
4. Changing debug to a Normal Function
What is printed when run with no? (building the report...) is printed, even though debug mode is disabled.
Why? In a regular function, arguments are evaluated before the function runs (eager evaluation). The inline lambda version avoids building the message entirely when debug is off, saving CPU cycles.

Task 10

1. Rejected Lines & Checks
Rejected lines: bad, Bobur x, and any line without exactly two parts separated by a space or where the grade is not a valid integer.
Which check: parts.size != 2 || grade == null.
2. IDE Hover & Long Type
IDE type shown for book: Gradebook (or expanded as MutableMap<String, MutableList<Int>>).
The long type: MutableMap<String, MutableList<Int>>.
3. Changing to MutableMap<String, MutableList<Int>>
Does it still work? Yes, perfectly.
What it says about aliases: Type aliases do not create brand-new distinct types; they are purely interchangeable alternative names for the exact same underlying type.

Task 11

1. Interoperability and Plain Types
Where a plain lambda is given to a Validator: check("Not blank", email) { it.isNotBlank() }
Where a Validator is stored in a plain function type: val plain: (String) -> Boolean = isEmail
Why both compile: Type aliases do not create new distinct types; they are just alternative names for the exact same underlying type, meaning they are completely interchangeable everywhere.
2. Room Demo & Unit Safety
Why the compiler does not complain: Both Meters and Feet are aliases for Double. To the compiler, describeRoom(feet) is just passing a plain Double to a function expecting a Double.
What could protect you: A value class (inline class with @JvmInline value class Meters(val value: Double)), which enforces strong type safety at compile time so you can't accidentally mix up units.

Task 12

1. Default Output and println(a)
What println(a) prints: Something like Contact@4a574795 (class name plus a hexadecimal hash of its memory address).
Which class and method it comes from: The toString() method inherited from the root Any class.
2. Equality (== vs ===)
Why a == b is false: Because by default, equals() compares object references rather than internal data properties, treating two distinct object instances in memory as different.
What == calls: It calls the equals() method.
What === compares: It compares referential equality (whether a and b point to the exact same object in memory).
3. Overriding toString, equals, and hashCode
What changed in each of the four output lines:
1.	a: Prints clean text representation via toString() (e.g., Ali (991234567)).
2.	a == b: Becomes true because equals now compares the phone property instead of memory addresses.
3.	a === b: Stays false because they are still two separate object instances in memory.
4.	b in book: Becomes true because hashSetOf uses equals and hashCode to find matching entries.
4. Removing hashCode Override
What b in book prints: It becomes unreliable or prints false randomly, even though a == b is true.
Why hashCode must agree with equals: Hash-based collections (HashSet, HashMap) use hashCode() to find the correct bucket instantly. If two equal objects have different hash codes, the collection looks in the wrong bucket and fails to find the item.
5. Parameter Any? and Type Check
Why equals takes Any? instead of Contact?: Because equals overrides the signature defined in the root Any class, which must accept any arbitrary type of object for compatibility.
What other is Contact gives when other is null: It safely evaluates to false without throwing a ClassCastException or NullPointerException.

Task 13

1. Handling ratio
Which branch handles ratio: The else branch, because ratio is a Double (1.5) and there is initially no explicit branch for Double.

2. Results of val asText = value as String for Each Key
volume (7): Throws a ClassCastException (java.lang.Integer cannot be cast to java.lang.String).

darkMode (true): Throws a ClassCastException (java.lang.Boolean cannot be cast to java.lang.String).

username ("Dilnoza"): No problem, successfully assigns the string.

theme (null): Throws a NullPointerException because a non-null type cast (as String) cannot cast a null value.

ratio (1.5): Throws a ClassCastException (java.lang.Double cannot be cast to java.lang.String).

3. Comparing as? String vs. as String?
as? String (Safe cast): Returns null safely for both volume and theme instead of crashing.

as String? (Cast to nullable type):

For volume (Int), it still performs a strict type check and throws a ClassCastException.

For theme (null), it succeeds and returns null because null is allowed by the nullable target type String?.

4. typeName() on Every Value
Why it can be called on every value: Because in Kotlin, Any is the root of the class hierarchy for all non-null types (primitives like Int are boxed to objects when referenced as Any), meaning an extension on Any is available everywhere.

Output for the additions:

println(5.typeName()) prints Int

println("x".typeName()) prints String
