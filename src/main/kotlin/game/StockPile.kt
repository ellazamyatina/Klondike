package game

import model.Card

class StockPile : Pile() {
    /** take a card from the top and return
     * the face-up one  for the Waste Pile**/

    fun drawCard(): Card? {
        val card = removeTop()
        card?.flip()
        return card
    }

    /** return cards from the Waste Pile for the Stock Pile
     * and change isFaceUp**/

    fun resetFromWaste(wasteCards: List<Card>) {
        wasteCards.reversed().forEach { card ->
            card.isFaceUp = false
            addCard(card)
        }
    }
}
