package uz.ttpu.specialkeywordslab.task04

inline fun command(crossinline action: () -> Unit): Runnable =
    Runnable { action() }

fun main() {
    val commands = mapOf(
        "1" to command { println("Hello!") },
        "2" to command { println("Goodbye!") },
        "3" to command { println("Time to rest.") },
    )
    print("Choose 1, 2 or 3: ")
    val chosen = commands[readln().trim()]
    if (chosen == null) println("Unknown command") else chosen.run()
}
