package model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CardTest {
    @Test
    fun `check if isFaceUp is reversed when flip`() {
        val card = Card(Rank.ACE, Suit.HEARTS)
        assertFalse(card.isFaceUp)
        card.flip()
        assertTrue(card.isFaceUp)
    }

    @Test
    fun `check if the card returns the correct color`() {
        val blackCard = Card(Rank.QUEEN, Suit.SPADES)
        assertEquals("BLACK", blackCard.color())
        val redCard = Card(Rank.FIVE, Suit.DIAMONDS)
        assertEquals("RED", redCard.color())
    }

    @Test
    fun `check the performance of the card`() {
        val card = Card(Rank.ACE, Suit.SPADES)
        assertEquals("🂠", card.toString())
        card.flip()
        assertEquals("A♠", card.toString())
    }
}
