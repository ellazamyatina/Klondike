package system

import game.Game
import model.Card
import model.Move
import model.Rank
import model.Suit
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GameEndToEndSystemTest {
    private val game = Game()

    @Test
    fun `simulated complete game to victory`() {
        game.initialize()

        var movesPerformed = 0

        fun performMove(
            from: Any,
            to: Any,
            cards: List<Card>,
        ): Boolean {
            val move = Move(from as game.Pile, to as game.Pile, cards)
            val result = game.makeMove(move)
            if (result) movesPerformed++
            return result
        }

        game.drawCardToWaste()
        val aceHearts = Card(Rank.ACE, Suit.HEARTS, true)
        game.waste.addCard(aceHearts)
        val heartsFoundation = game.foundations.first { it.targetSuit == Suit.HEARTS }
        performMove(game.waste, heartsFoundation, listOf(aceHearts))

        game.drawCardToWaste()
        val twoHearts = Card(Rank.TWO, Suit.HEARTS, true)
        game.waste.addCard(twoHearts)
        performMove(game.waste, heartsFoundation, listOf(twoHearts))

        game.drawCardToWaste()
        val threeHearts = Card(Rank.THREE, Suit.HEARTS, true)
        game.waste.addCard(threeHearts)
        performMove(game.waste, heartsFoundation, listOf(threeHearts))

        game.drawCardToWaste()
        val fourHearts = Card(Rank.FOUR, Suit.HEARTS, true)
        game.waste.addCard(fourHearts)
        performMove(game.waste, heartsFoundation, listOf(fourHearts))

        game.drawCardToWaste()
        val fiveHearts = Card(Rank.FIVE, Suit.HEARTS, true)
        game.waste.addCard(fiveHearts)
        performMove(game.waste, heartsFoundation, listOf(fiveHearts))

        game.drawCardToWaste()
        val sixHearts = Card(Rank.SIX, Suit.HEARTS, true)
        game.waste.addCard(sixHearts)
        performMove(game.waste, heartsFoundation, listOf(sixHearts))

        game.drawCardToWaste()
        val sevenHearts = Card(Rank.SEVEN, Suit.HEARTS, true)
        game.waste.addCard(sevenHearts)
        performMove(game.waste, heartsFoundation, listOf(sevenHearts))

        game.drawCardToWaste()
        val eightHearts = Card(Rank.EIGHT, Suit.HEARTS, true)
        game.waste.addCard(eightHearts)
        performMove(game.waste, heartsFoundation, listOf(eightHearts))

        game.drawCardToWaste()
        val nineHearts = Card(Rank.NINE, Suit.HEARTS, true)
        game.waste.addCard(nineHearts)
        performMove(game.waste, heartsFoundation, listOf(nineHearts))

        game.drawCardToWaste()
        val tenHearts = Card(Rank.TEN, Suit.HEARTS, true)
        game.waste.addCard(tenHearts)
        performMove(game.waste, heartsFoundation, listOf(tenHearts))

        game.drawCardToWaste()
        val jackHearts = Card(Rank.JACK, Suit.HEARTS, true)
        game.waste.addCard(jackHearts)
        performMove(game.waste, heartsFoundation, listOf(jackHearts))

        game.drawCardToWaste()
        val queenHearts = Card(Rank.QUEEN, Suit.HEARTS, true)
        game.waste.addCard(queenHearts)
        performMove(game.waste, heartsFoundation, listOf(queenHearts))

        game.drawCardToWaste()
        val kingHearts = Card(Rank.KING, Suit.HEARTS, true)
        game.waste.addCard(kingHearts)
        performMove(game.waste, heartsFoundation, listOf(kingHearts))

        assertTrue(heartsFoundation.isComplete())
        assertEquals(13, heartsFoundation.size())

        val diamondsFoundation = game.foundations.first { it.targetSuit == Suit.DIAMONDS }
        for (rank in Rank.entries) {
            game.drawCardToWaste()
            val card = Card(rank, Suit.DIAMONDS, true)
            game.waste.addCard(card)
            performMove(game.waste, diamondsFoundation, listOf(card))
        }
        assertTrue(diamondsFoundation.isComplete())

        val clubsFoundation = game.foundations.first { it.targetSuit == Suit.CLUBS }
        for (rank in Rank.entries) {
            game.drawCardToWaste()
            val card = Card(rank, Suit.CLUBS, true)
            game.waste.addCard(card)
            performMove(game.waste, clubsFoundation, listOf(card))
        }
        assertTrue(clubsFoundation.isComplete())

        val spadesFoundation = game.foundations.first { it.targetSuit == Suit.SPADES }
        for (rank in Rank.entries) {
            game.drawCardToWaste()
            val card = Card(rank, Suit.SPADES, true)
            game.waste.addCard(card)
            performMove(game.waste, spadesFoundation, listOf(card))
        }
        assertTrue(spadesFoundation.isComplete())

        assertTrue(game.isGameWon())
        assertTrue(movesPerformed > 50)
    }

    @Test
    fun `game can be lost not won`() {
        game.initialize()

        var possibleMoves = 0

        for (i in 0..100) {
            game.drawCardToWaste()

            val wasteCard = game.waste.topCard()
            if (wasteCard != null) {
                for (foundation in game.foundations) {
                    if (foundation.canPlace(wasteCard)) {
                        val move = Move(game.waste, foundation, listOf(wasteCard))
                        if (game.makeMove(move)) {
                            possibleMoves++
                        }
                    }
                }

                for (tableau in game.tableau) {
                    if (tableau.canPlace(wasteCard)) {
                        val move = Move(game.waste, tableau, listOf(wasteCard))
                        if (game.makeMove(move)) {
                            possibleMoves++
                        }
                    }
                }
            }
        }

        assertFalse(game.isGameWon())
        assertTrue(possibleMoves > 0)
    }

    @Test
    fun `undo after win returns to incomplete state`() {
        game.initialize()

        val aceHearts = Card(Rank.ACE, Suit.HEARTS, true)
        game.waste.addCard(aceHearts)
        val heartsFoundation = game.foundations.first { it.targetSuit == Suit.HEARTS }
        game.makeMove(Move(game.waste, heartsFoundation, listOf(aceHearts)))

        assertEquals(1, heartsFoundation.size())

        game.undo()

        assertEquals(0, heartsFoundation.size())
        assertEquals(1, game.waste.size())
    }

    @Test
    fun `multiple undo operations maintain consistency`() {
        game.initialize()

        repeat(5) {
            game.drawCardToWaste()
        }

        val movesCount = game.movesCount

        repeat(5) {
            game.undo()
        }

        assertEquals(0, game.movesCount)
        assertEquals(24, game.stock.size())
        assertEquals(0, game.waste.size())
    }

    @Test
    fun `game with no valid moves can still proceed via stock`() {
        game.initialize()

        val initialMoves = game.movesCount

        while (game.stock.size() > 0 && game.movesCount < initialMoves + 3) {
            game.drawCardToWaste()
        }

        assertTrue(game.movesCount > initialMoves)
    }
}
