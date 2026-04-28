package game

import model.Rank
import model.Card
import model.Suit
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse

class TablePileTest {
    private val pile = TablePile()
    @Test
    fun `only can place the KING in the empty cell`(){
        val king = Card(Rank.KING, Suit.CLUBS, true)
        val notKing = Card(Rank.FOUR, Suit.DIAMONDS, true)
        assertTrue(pile.canPlace(king))
        assertFalse(pile.canPlace(notKing))
    }
    @Test
    fun `can make sequence`(){
        val card1 = Card(Rank.KING, Suit.HEARTS, true)
        assertTrue(pile.canPlace(card1))
        pile.addCard(card1)

        val card2 = Card(Rank.QUEEN, Suit.CLUBS, true)
        assertTrue(pile.canPlace(card2))
        pile.addCard(card2)

        val card3 = Card(Rank.JACK, Suit.DIAMONDS, true)
        assertTrue(pile.canPlace(card3))
        pile.addCard(card3)

        assertEquals(3, pile.size())
    }
}