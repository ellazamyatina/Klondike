package system

import game.Game
import model.Move
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.fail

class FullGameSystemTest {
    private var game: Game? = null

    @BeforeTest
    fun setup() {
        game = Game()
        game?.initialize()
    }

    @Test
    fun `move ace from waste to foundation`() {
        // Начальное состояние
        assertEquals(0, game?.movesCount)
        assertFalse(game!!.isGameWon())

        game!!.drawCardToWaste()
        val drawnCard = game!!.waste.topCard() ?: fail("Waste should have a card after draw")

        val foundation =
            game!!.foundations.firstOrNull { it.targetSuit == drawnCard.suit }
                ?: fail("Should find foundation for suit ${drawnCard.suit}")

        if (drawnCard.isAce()) {
            val move = Move(game!!.waste, foundation, listOf(drawnCard))
            assertTrue(game!!.makeMove(move), "Move ace to foundation should succeed")
            assertEquals(1, foundation.size())
            assertEquals(drawnCard, foundation.topCard())
        }
    }

    @Test
    fun `move card stack between tableau piles`() {
        val sourceIndex = game!!.tableau.indexOfFirst { !it.isEmpty() && it.topCard()?.isFaceUp == true }
        assertTrue(sourceIndex >= 0, "Should find source tableau pile")

        val sourcePile = game!!.tableau[sourceIndex]
        val topCard = sourcePile.topCard() ?: fail("Source pile should have a card")

        val targetIndex =
            if (topCard.isKing()) {
                game!!.tableau.indexOfFirst { it.isEmpty() && it != sourcePile }
            } else {
                game!!.tableau.indexOfFirst {
                    it != sourcePile && !it.isEmpty() && it.canPlace(topCard)
                }
            }

        if (targetIndex >= 0) {
            val targetPile = game!!.tableau[targetIndex]
            val sourceSizeBefore = sourcePile.size()
            val targetSizeBefore = targetPile.size()

            val move = Move(sourcePile, targetPile, listOf(topCard))
            assertTrue(game!!.makeMove(move), "Move should succeed")

            assertEquals(sourceSizeBefore - 1, sourcePile.size())
            assertEquals(targetSizeBefore + 1, targetPile.size())
            assertEquals(topCard, targetPile.topCard())
        }
    }

    @Test
    fun `undo single move restores state`() {
        val stockBefore = game!!.stock.size()
        val wasteBefore = game!!.waste.size()
        val movesBefore = game!!.movesCount

        game!!.drawCardToWaste()
        game!!.undo()

        assertEquals(stockBefore, game!!.stock.size(), "Stock должен вернуться к исходному размеру")
        assertEquals(wasteBefore, game!!.waste.size(), "Waste должен вернуться к исходному размеру")
        assertEquals(movesBefore, game!!.movesCount, "movesCount должен восстановиться")
    }

    @Test
    fun `stock reset cycle works correctly`() {
        val initialStockSize = game!!.stock.size()
        val initialWasteSize = game!!.waste.size()

        while (!game!!.stock.isEmpty()) {
            game!!.drawCardToWaste()
        }

        assertTrue(game!!.stock.isEmpty(), "Stock should be empty after drawing all cards")
        assertEquals(initialStockSize + initialWasteSize, game!!.waste.size(), "All cards should be in waste")

        game!!.drawCardToWaste()

        assertTrue(game!!.stock.size() > 0, "Stock should be refilled after reset")
        assertTrue(game!!.waste.size() <= 1, "Waste should have at most 1 card after reset")
    }

    @Test
    fun `game wins when all foundations are complete`() {
        for (suit in model.Suit.entries) {
            val foundation = game!!.foundations.first { it.targetSuit == suit }
            for (rank in model.Rank.entries) {
                val card = findCardInGame(suit, rank) ?: continue
                if (foundation.canPlace(card)) {
                    val move = Move(game!!.waste, foundation, listOf(card))
                    game!!.makeMove(move)
                }
            }
        }
        assertTrue(game!!.foundations.all { it.isComplete() }, "All foundations should be complete")
        assertTrue(game!!.isGameWon(), "Game should report win when all foundations are full")
    }

    @Test
    fun `invalid move is rejected`() {
        val foundation = game!!.foundations[0]
        val wrongSuitCard = findCardWithSuitNot(foundation.targetSuit) ?: return

        val move = Move(game!!.waste, foundation, listOf(wrongSuitCard))
        assertFalse(game!!.makeMove(move), "Move with wrong suit should be rejected")
    }

    /** function for finding card with target rand and suit  **/
    private fun findCardInGame(
        suit: model.Suit,
        rank: model.Rank,
    ): model.Card? =
        listOf(game!!.stock, game!!.waste)
            .plus(game!!.tableau)
            .plus(game!!.foundations)
            .flatMap { it.getCardsCopy() }
            .firstOrNull { it.suit == suit && it.rank == rank }

    /**   function for finding any open card   **/
    private fun findCardWithSuitNot(excludeSuit: model.Suit): model.Card? =
        listOf(game!!.stock, game!!.waste)
            .plus(game!!.tableau)
            .plus(game!!.foundations)
            .flatMap { it.getCardsCopy() }
            .firstOrNull { it.suit != excludeSuit && it.isFaceUp }
}

private fun model.Card.isAce() = this.rank == model.Rank.ACE

private fun model.Card.isKing() = this.rank == model.Rank.KING
