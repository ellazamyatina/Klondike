package model

import game.Pile

data class Move(
    // TablePile, StockPile and so on
    val fromPile: Pile,
    val toPile: Pile,
    val cards: List<Card>,
    val wasSourceTableau: Boolean = false,
    // did the new card opened
    val revealedNewCard: Boolean = false,
) {
    override fun toString(): String =
        "Move ${cards.size} card(s) from ${fromPile::class.simpleName} to " +
            "${toPile::class.simpleName}"
}
