package model

class Card(
    val rank: Rank,
    val suit: Suit,
    var isFaceUp: Boolean = false
) {
    fun flip() {
        isFaceUp = !isFaceUp
    }

    fun color(): String = if (suit.isRed()) "RED" else "BLACK"

    override fun toString(): String = if (isFaceUp) "${rank.displaySymbol()}${suit.displaySymbol()}" else "🂠" // redefine for good output
}
