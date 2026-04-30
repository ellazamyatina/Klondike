package game

import model.Card
import model.Move
import model.Rank
import model.Suit
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GameTest {
    private val game = Game()

    @Test
    fun `initialize deals correct number of cards to tableau and stock`() {
        game.initialize()

        // 28 cards in tableau
        val tableauCount = game.tableau.sumOf { it.size() }
        assertEquals(28, tableauCount)

        // 24 cards in stock
        assertEquals(24, game.stock.size())

        // all the upper cards in tableau is open
        game.tableau.forEach { pile ->
            assertTrue(pile.topCard()?.isFaceUp ?: false)
        }
    }

    @Test
    fun `initialize creates 4 foundation piles`() {
        game.initialize()
        assertEquals(4, game.foundations.size)
    }

    @Test
    fun `makeMove returns false for invalid move to foundation`() {
        game.initialize()

        val invalidCard = Card(Rank.KING, Suit.HEARTS, true)
        val move = Move(game.tableau[0], game.foundations[0], listOf(invalidCard))

        assertFalse(game.makeMove(move))
    }

    @Test
    fun `makeMove succeeds for valid move to tableau`() {
        game.initialize()

        val targetPile = game.tableau[0]
        val sourcePile = game.tableau[1]

        val kingHearts = Card(Rank.KING, Suit.HEARTS, true)
        val queenSpades = Card(Rank.QUEEN, Suit.SPADES, true)

        targetPile.addCard(kingHearts)
        sourcePile.addCard(queenSpades)

        val move = Move(sourcePile, targetPile, listOf(queenSpades))
        assertTrue(game.makeMove(move))

        assertEquals(queenSpades, targetPile.topCard())
    }

    @Test
    fun `undo reverts last move`() {
        game.initialize()

        // valid move
        val heartsFoundation = game.foundations.first { it.targetSuit == Suit.HEARTS }
        val aceOfHearts = Card(Rank.ACE, Suit.HEARTS, true)
        game.waste.addCard(aceOfHearts)

        val move = Move(game.waste, heartsFoundation, listOf(aceOfHearts))

        // make move
        assertTrue(game.makeMove(move))
        assertEquals(1, heartsFoundation.size())
        assertEquals(0, game.waste.size())

        // undo
        assertTrue(game.undo())

        // checking
        assertEquals(0, heartsFoundation.size())
        assertEquals(1, game.waste.size())
    }

    @Test
    fun `isGameWon returns true when all foundations are complete`() {
        for (foundation in game.foundations) {
            for (rank in Rank.entries) {
                foundation.addCard(Card(rank, foundation.targetSuit, true))
            }
        }
        assertTrue(game.isGameWon())
    }
}
