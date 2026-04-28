package game

import model.Suit
import model.Rank
import model.Card

/** target suit has to be as the same as the  foundation pile requires it
 * also check if foundation pile is complete**/

class FoundationPile(private val targetSuit: Suit): Pile() {
    fun canPlace(card: Card): Boolean{
        if (card.suit != targetSuit) return false
        val top = topCard()
        if (top == null) return card.rank == Rank.ACE
        return card.rank.value == top.rank.value + 1
    }
    fun isComplete(): Boolean{
        return size() == 13
    }
}