package game

import model.Rank
import model.Suit
import model.Card
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertNull

class PileTest{
    private class TestPile: Pile()
    @Test
    fun `test the empty pile`(){
        val pile = TestPile()
        assertEquals(0, pile.size())
        assertTrue(pile.isEmpty())
        assertNull(pile.topCard())
    }
    @Test
    fun `check output`(){
        val pile = TestPile()
        val card = Card(Rank.JACK, Suit.DIAMONDS, isFaceUp = true)
        pile.addCard(card)
        assertEquals("Pile size = 1, top = J♦", pile.toString())
    }
}