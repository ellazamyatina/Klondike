package model

data class Card(val rank: Rank, val suit: Suit, val isFaceUp: Boolean = false){
    fun flip(): Card = copy(isFaceUp = !isFaceUp)                             // make a new face-up card, use copies
    fun color(): String = if (suit.isRed()) "RED" else "BLACK"
    override fun toString(): String = if (isFaceUp) "${rank.displaySymbol()}${suit.displaySymbol()}" else "🂠" // redefine for good output

}
