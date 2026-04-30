package game

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse
import model.Suit
import model.Card
import model.Rank

class FoundationPileTest {
    private val heartsPile = FoundationPile(Suit.HEARTS)
    private val spadesPile = FoundationPile(Suit.SPADES)
    @Test
    fun `can place no cards but ace at the empty cell`(){
        val heartAce = Card(Rank.ACE, Suit.HEARTS, true)
        val heartKing = Card(Rank.KING, Suit.HEARTS, true)
        val diamondsAce = Card(Rank.ACE, Suit.DIAMONDS, true)
        assertTrue(heartsPile.canPlace(heartAce))
        assertFalse(heartsPile.canPlace(heartKing))
        assertFalse(spadesPile.canPlace(diamondsAce))
    }
    @Test
    fun `check if the pile is complete`(){
        for (i in 1..10){
            heartsPile.addCard(Card(Rank.entries[i-1], Suit.HEARTS, true))
        }
        assertFalse(heartsPile.isComplete())

        for (rank in Rank.entries){
            spadesPile.addCard(Card(rank, Suit.SPADES, true))
        }
        assertTrue(spadesPile.isComplete())
        assertEquals(13, spadesPile.size())
    }
}