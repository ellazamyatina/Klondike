package game

import model.Card
import model.Rank

class TablePile : Pile() {
    /** we can place no cards but the KING in the empty table pile
     * also card can be placed iff the top card is greater for 1 and
     * has another color**/

    override fun canPlace(card: Card): Boolean {
        if (cards.isEmpty()) {
            return card.rank == model.Rank.KING
        }

        val top = cards.last()
        val isRed = (card.suit.name == "HEARTS" || card.suit.name == "DIAMONDS")
        val topIsRed = (top.suit.name == "HEARTS" || top.suit.name == "DIAMONDS")
        val differentColor = isRed != topIsRed
        val nextRank = top.rank.ordinal == card.rank.ordinal + 1

        return differentColor && nextRank
    }

    // extra function for getting some cards from the pile
    fun getTopCards(count: Int): List<Card> {
        if (count > cards.size) return emptyList()
        return cards.takeLast(count).reversed()
    }
}
