package game

import model.Card
import model.Suit
import model.Move
import model.createShuffledPile

class Game{

    // make all piles for the game

    val tableau  = List(7) {TablePile()}
    val foundations = Suit.entries.map{ FoundationPile(it) }
    val stock = StockPile()
    val waste = WastePile()

    // move history
    private val moveHistory = mutableListOf<Move>()

    fun initialize(){
        val deck = createShuffledPile()
        var index = 0


        // put shuffled cards for 7 table piles
        for (col in 0..6){
            repeat(col+1){row->
                val card = deck[index++]

                // the top card is face up (row == col)
                tableau[col].addCard(card.copy(isFaceUp = (row == col)))
            }

        }

        // remainder located in the Stock Pile
        while(index < deck.size){
            stock.addCard(deck[index++].copy(isFaceUp = false))
        }
    }

    // make move, return true if the move is valid and done

    fun makeMove(move: Move): Boolean{

        // empty move
        if (move.cards.isEmpty()) return false

        // the target card for move
        val bottomCard = move.cards.last()
        if(!move.toPile.canPlace(bottomCard)) return false

        // check if the card from tableau piles
        // check the new card will be opened

        val wasSourceTableau = move.fromPile is TablePile
        var revealedNewCard = false

        move.cards.forEach {move.fromPile.removeTop()}

        // open new card in the Tableau
        if (wasSourceTableau && !move.fromPile.isEmpty()) {
            val newTop = move.fromPile.topCard()
            if (newTop != null && !newTop.isFaceUp) {
                move.fromPile.removeTop()
                move.fromPile.addCard(newTop.copy(isFaceUp = true))
                revealedNewCard = true
            }
        }

        // add new card, bottom card should be placed first
        move.cards.reversed().forEach { move.toPile.addCard(it) }

        // save move
        val moveWithState = Move(
            fromPile = move.fromPile,
            toPile = move.toPile,
            cards = move.cards,
            wasSourceTableau = wasSourceTableau,
            revealedNewCard = revealedNewCard,
        )

        // save for undo
        moveHistory.add(moveWithState)
        return true
    }

    // undo the last move
    fun undo(): Boolean {
        if (moveHistory.isEmpty()) return false

        val lastMove = moveHistory.removeAt(moveHistory.lastIndex)
        lastMove.cards.forEach {lastMove.fromPile.addCard(it)}

        if (lastMove.wasSourceTableau && lastMove.revealedNewCard){
            val top = lastMove.fromPile.topCard()
            if (top!= null && top.isFaceUp){
                lastMove.fromPile.removeTop()
                lastMove.fromPile.addCard(top.copy(isFaceUp = false))
            }
        }
        repeat(lastMove.cards.size) { lastMove.toPile.removeTop()}
        return true
    }

    fun isGameWon(): Boolean = foundations.all {it.isComplete()}

    // function for open new cards in the tableau
    private fun flipNewTopIfTableau(pile:Pile){
        if (pile is TablePile && !pile.isEmpty()){
            val top: Card? = pile.topCard()
            if (top!= null && !top.isFaceUp){
                pile.removeTop()
                pile.addCard(top.copy(isFaceUp = true))
            }
        }
    }

}
