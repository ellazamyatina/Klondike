package game

import model.Card

class WastePile: Pile() {
    fun clearAndReturn(): List<Card>{
        val allCards = getCardsCopy()
        cards.clear()
        return allCards
    }
}