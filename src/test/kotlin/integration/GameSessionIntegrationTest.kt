package integration

import model.Card
import model.Move
import model.Rank
import model.Suit
import session.SessionManager
import java.io.File
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class GameSessionIntegrationTest {
    private val sessionManager = SessionManager()
    private val testRegistryFile = File("test_players_registry.txt")

    @BeforeTest
    fun setUp() {
        sessionManager.login("TestPlayer")
    }

    @AfterTest
    fun tearDown() {
        testRegistryFile.delete()
    }

    @Test
    fun `sessionManager creates and initializes new game`() {
        val game = sessionManager.startNewGame()

        assertNotNull(game)
        assertEquals(28, game.tableau.sumOf { it.size() })
        assertEquals(24, game.stock.size())
    }


    @Test
    fun `game move count updates correctly with session`() {
        val game = sessionManager.startNewGame()

        val aceHearts = Card(Rank.ACE, Suit.HEARTS, true)
        game.waste.addCard(aceHearts)
        val heartsFoundation = game.foundations.first { it.targetSuit == Suit.HEARTS }

        val move = Move(game.waste, heartsFoundation, listOf(aceHearts))
        game.makeMove(move)

        assertEquals(1, game.movesCount)

        val activeGame = sessionManager.getActiveGame()
        assertNotNull(activeGame)
        assertEquals(1, activeGame.movesCount)
    }

    @Test
    fun `multiple game sessions maintain separate state`() {
        val game1 = sessionManager.startNewGame()
        val initialMoves1 = game1.movesCount

        val aceHearts = Card(Rank.ACE, Suit.HEARTS, true)
        game1.waste.addCard(aceHearts)
        val heartsFoundation = game1.foundations.first { it.targetSuit == Suit.HEARTS }
        game1.makeMove(Move(game1.waste, heartsFoundation, listOf(aceHearts)))

        val game2 = sessionManager.startNewGame()

        assertEquals(1, game1.movesCount)
        assertEquals(initialMoves1, game2.movesCount)
    }

    @Test
    fun `win reporting updates player stats correctly`() {
        val player = sessionManager.currentPlayer
        assertNotNull(player)

        val game = sessionManager.startNewGame()
        game.movesCount = 50

        for (suit in Suit.entries) {
            val foundation = game.foundations.first { it.targetSuit == suit }
            for (rank in Rank.entries) {
                foundation.addCard(Card(rank, suit, true))
            }
        }

        assertTrue(game.isGameWon())

        sessionManager.reportWin()

        assertEquals(1, player.wins)
        assertEquals(100, player.score)
        assertEquals(50, player.totalMoves)
    }
}