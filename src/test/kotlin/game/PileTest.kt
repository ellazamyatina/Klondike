package game

import model.Card
import model.Rank
import model.Suit
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class PileTest {
    private class TestPile : Pile()

    @Test
    fun `test the empty pile`() {
        val pile = TestPile()
        assertEquals(0, pile.size())
        assertTrue(pile.isEmpty())
        assertNull(pile.topCard())
    }

    @Test
    fun `check output`() {
        val pile = TestPile()
        val card = Card(Rank.JACK, Suit.DIAMONDS, isFaceUp = true)
        pile.addCard(card)
        assertEquals("Pile size = 1, top = J♦", pile.toString())
    }
}
