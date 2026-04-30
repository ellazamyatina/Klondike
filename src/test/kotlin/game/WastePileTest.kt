package game

import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.assertEquals
import model.Card
import model.Rank
import model.Suit

class WastePileTest: Pile() {
    private val waste = WastePile()
    @Test
    fun `clearAndReturn returns all cards`(){
        waste.addCard(Card(Rank.FOUR, Suit.SPADES, true))
        waste.addCard(Card(Rank.JACK, Suit.HEARTS, true))
        val returned = waste.clearAndReturn()
        assertEquals(2, returned.size)
        assertTrue(waste.isEmpty())

    }

}