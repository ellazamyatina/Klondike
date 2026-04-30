package model

import game.Pile

data class Move(
    val fromPile: Pile, // TablePile, StockPile and so on
    val toPile: Pile,
    val cards: List<Card>,
    val wasSourceTableau: Boolean = false,
    val revealedNewCard: Boolean = false, // did the new card opened
) {
    override fun toString(): String =
        "Move ${cards.size} card(s) from ${fromPile::class.simpleName} to " +
            "${toPile::class.simpleName}"
}
