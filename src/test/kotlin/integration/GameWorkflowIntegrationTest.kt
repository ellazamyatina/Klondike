package integration

import game.Game
import model.Card
import model.Move
import model.Rank
import model.Suit
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class GameWorkflowIntegrationTest {
    private val game = Game()

    @Test
    fun `full game initialization workflow`() {
        game.initialize()

        assertEquals(7, game.tableau.size)
        assertEquals(4, game.foundations.size)
        assertNotNull(game.stock)
        assertNotNull(game.waste)

        assertEquals(28, game.tableau.sumOf { it.size() })
        assertEquals(24, game.stock.size())
        assertEquals(0, game.waste.size())
        assertEquals(0, game.foundations.sumOf { it.size() })

        assertTrue(game.tableau.all { it.topCard()?.isFaceUp == true })
    }

    @Test
    fun `draw card workflow from stock to waste`() {
        game.initialize()

        val initialStockSize = game.stock.size()
        val initialWasteSize = game.waste.size()

        val drawn = game.drawCardToWaste()

        assertTrue(drawn)
        assertEquals(initialStockSize - 1, game.stock.size())
        assertEquals(initialWasteSize + 1, game.waste.size())
    }

    @Test
    fun `undo move workflow restores game state`() {
        game.initialize()

        game.drawCardToWaste()
        val wasteSizeAfterDraw = game.waste.size()
        val stockSizeAfterDraw = game.stock.size()

        assertTrue(game.undo())

        assertEquals(wasteSizeAfterDraw - 1, game.waste.size())
        assertEquals(stockSizeAfterDraw + 1, game.stock.size())
    }

    @Test
    fun `move between tableau piles workflow`() {
        game.initialize()

        val sourcePile = game.tableau[6]
        val targetPile = game.tableau[0]

        targetPile.addCard(Card(Rank.KING, Suit.SPADES, true))

        val movableCards = mutableListOf<Card>()
        while (!sourcePile.isEmpty() && sourcePile.topCard()?.isFaceUp == true) {
            val card = sourcePile.topCard()
            if (card != null) {
                movableCards.add(card)
            }
            sourcePile.removeTop()
        }

        movableCards.reversed().forEach { sourcePile.addCard(it) }

        if (movableCards.isNotEmpty() && targetPile.canPlace(movableCards.last())) {
            val move = Move(sourcePile, targetPile, movableCards)
            val result = game.makeMove(move)

            if (result) {
                assertTrue(targetPile.size() >= movableCards.size)
            }
        }
    }

    @Test
    fun `game won condition workflow`() {
        game.initialize()

        assertFalse(game.isGameWon())

        for (foundation in game.foundations) {
            for (rank in Rank.entries) {
                foundation.addCard(Card(rank, foundation.targetSuit, true))
            }
        }

        assertTrue(game.isGameWon())
    }
}
