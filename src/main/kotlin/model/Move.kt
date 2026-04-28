package model

import game.Pile

data class Move(
    val fromPile: Pile, // TablePile, StockPile and so on
    val toPile: Pile,
    val cards: List<Card>,
) {
    override fun toString(): String =
        "Move ${cards.size} card(s) from ${fromPile::class.simpleName} to " + // for good output, simpleName returns name of the current Pile
            "${toPile::class.simpleName}"
}
