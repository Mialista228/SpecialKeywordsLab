package uz.ttpu.specialkeywordslab.task11

typealias Validator = (String) -> Boolean
typealias Predicate<T> = (T) -> Boolean

val notBlank: Validator = { it.isNotBlank() }
val isEmail: Validator = { '@' in it && '.' in it.substringAfter('@') }
val isStrongPassword: Validator = { it.length >= 8 && it.any(Char::isDigit) }

fun check(label: String, input: String, validator: Validator) {
    println("$label: ${if (validator(input)) "OK" else "INVALID"}")
}

fun <T> countMatching(items: List<T>, rule: Predicate<T>): Int = items.count(rule)

fun main() {
    print("E-mail: ")
    val email = readln().trim()
    print("Password: ")
    val password = readln()
    check("E-mail", email, isEmail)
    check("Password", password, isStrongPassword)
    check("Not blank", email) { it.isNotBlank() }
    val plain: (String) -> Boolean = isEmail
    println("Used as a plain function type: ${plain(email)}")
    println("Valid e-mails in the list: ${countMatching(listOf(email, "x@y.uz", "bad"), isEmail)}")
}
