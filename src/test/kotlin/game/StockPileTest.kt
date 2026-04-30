package game

import model.Suit
import model.Card
import model.Rank
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse

class StockPileTest {
    private val stock = StockPile()
    @Test
    fun `drawCard flip card`(){
        stock.addCard(Card(Rank.ACE, Suit.SPADES))
        val card = stock.drawCard()
        assertEquals(Rank.ACE, card?.rank)
        assertTrue(card?.isFaceUp ?: false)
    }
    @Test
    fun `resetFromWaste puts face-down cards and returns them in the reverse order`(){
        val wasteCards = listOf(
            Card(Rank.FOUR, Suit.SPADES, true),
            Card(Rank.EIGHT, Suit.SPADES, true)
        )
        stock.resetFromWaste(wasteCards)
        assertEquals(2, stock.size())
        assertFalse(stock.topCard()?.isFaceUp ?: true)

        val drawn = stock.drawCard()
        assertEquals(Rank.FOUR, drawn?.rank)

    }
}