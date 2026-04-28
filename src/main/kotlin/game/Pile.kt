package game

import model.Card

abstract class Pile {
    protected val cards: MutableList<Card> = mutableListOf<Card>() // protected from the user

    open fun addCard(card: Card) {
        cards.add(card)
    }

    open fun removeTop(): Card? = if (cards.isNotEmpty()) cards.removeAt(cards.lastIndex) else null

    open fun topCard(): Card? = cards.lastOrNull()

    open fun isEmpty(): Boolean = cards.isEmpty()

    open fun size(): Int = cards.size

    open fun getCardsCopy(): List<Card> = cards.toList()

    open override fun toString(): String {
        val top = topCard()?.toString() ?: "empty"
        return "Pile size = ${cards.size}, top = $top"
    }
}
