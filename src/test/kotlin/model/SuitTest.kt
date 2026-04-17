package model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SuitTest {
    @Test
    fun `isRed returns true for HEARTS and DIAMONDS else false`() {
        assertTrue(Suit.HEARTS.isRed())
        assertTrue(Suit.DIAMONDS.isRed())
        assertFalse(Suit.SPADES.isRed())
        assertFalse(Suit.CLUBS.isRed())
    }

    @Test
    fun `symbol returns correct unicode characters`() {
        assertEquals("♥", Suit.HEARTS.displaySymbol())
        assertEquals("♦", Suit.DIAMONDS.displaySymbol())
        assertEquals("♠", Suit.SPADES.displaySymbol())
        assertEquals("♣", Suit.CLUBS.displaySymbol())
    }

    @Test
    fun `card can't be red and black at the same time`() {
        for (suit in Suit.entries) {
            assertTrue(suit.isRed() != suit.isBlack())
        }
    }
}
