package game

import model.Card
import model.Rank
import model.Suit
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class WastePileTest : Pile() {
    private val waste = WastePile()

    @Test
    fun `clearAndReturn returns all cards`() {
        waste.addCard(Card(Rank.FOUR, Suit.SPADES, true))
        waste.addCard(Card(Rank.JACK, Suit.HEARTS, true))
        val returned = waste.clearAndReturn()
        assertEquals(2, returned.size)
        assertTrue(waste.isEmpty())
    }
}
