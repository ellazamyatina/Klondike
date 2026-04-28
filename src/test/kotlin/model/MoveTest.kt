package model

import kotlin.test.Test
import kotlin.test.assertEquals
import game.FoundationPile
import game.TablePile

class MoveTest{
    @Test
    fun `move stores cards correctly`(){
        val fromPile = TablePile()
        val toPile = TablePile()
        val card1 = Card(Rank.FOUR, Suit.SPADES, true)
        val card2 = Card(Rank.THREE, Suit.CLUBS, true)
        val cards: List<Card> = listOf(card1, card2)

        val move = Move(fromPile, toPile, cards)
        assertEquals(2, move.cards.size)
        assertEquals(card1, move.cards[0])
        assertEquals(card2, move.cards[1])
    }
    @Test
    fun `check pretty output`(){
        val fromPile = TablePile()
        val toPile = FoundationPile(Suit.HEARTS)
        val cards = listOf(Card(Rank.JACK, Suit.DIAMONDS, true))
        val move = Move(fromPile, toPile, cards)
        assertEquals("Move 1 card(s) from TablePile to FoundationPile", move.toString())
    }


}