package game

import model.Card

class StockPile: Pile() {

    /** take a card from the top and return
     * the face-up one  for the Waste Pile**/

    fun drawCard(): Card? {
        val card = removeTop()
        return card?.copy(isFaceUp = true)
    }

    /** return cards from the Waste Pile for the Stock Pile
     * and change isFaceUp**/

    fun resetFromWaste(wasteCards: List<Card>){
        wasteCards.reversed().forEach{card
            -> addCard(card.copy(isFaceUp = false))
        }
    }
}