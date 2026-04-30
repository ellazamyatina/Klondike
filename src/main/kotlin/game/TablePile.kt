package game

import model.Card
import model.Rank

class TablePile : Pile() {
    /** we can place no cards but the KING in the empty table pile
     * also card can be placed iff the top card is greater for 1 and
     * has another color**/

    override fun canPlace(card: Card): Boolean {
        val top = topCard()
        if (top == null) return card.rank == Rank.KING
        return card.rank.value == top.rank.value - 1 &&
            card.suit.isBlack() != top.suit.isBlack()
    }

    // extra function for getting some cards from the pile
    fun getTopCards(count: Int): List<Card> {
        if (count > cards.size) return emptyList()
        return cards.takeLast(count)
    }
}
