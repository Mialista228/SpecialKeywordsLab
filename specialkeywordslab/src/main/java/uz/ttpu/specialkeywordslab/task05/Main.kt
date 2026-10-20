package uz.ttpu.specialkeywordslab.task05

fun main() {
    val prices = mutableListOf<Double>()
    println("Enter prices, one per line. Type 'done' to finish.")
    while (true) {
        val line = readln().trim()
        if (line == "done") break
        val price = line.toDoubleOrNull() ?: run {
            println("'$line' is not a number, skipped")
            continue
        }
        price.takeIf { it > 10_000 }?.let {
            println("Price $it is too large, stopping")
            break
        }
        prices.add(price)
    }
    println("Items: ${prices.size}, total: ${prices.sum()}")
}

fun orSkip(text: String?, onNull: () -> Double): Double = text?.toDoubleOrNull() ?: onNull()