package javafx

import game.Game
import game.Pile
import game.TablePile
import javafx.application.Application
import javafx.stage.Stage
import model.Card
import model.Move
import session.SessionManager

class Main : Application() {
    private var window: GameWindow? = null
    private var game: Game? = null
    private val session = SessionManager()
    private var selectedCards: List<Card>? = null
    private var selectedSourcePile: Pile? = null

    override fun start(stage: Stage) {
        window = GameWindow(this)
        window?.show(stage)
        session.login("Player")
        startNewGame()
    }

    fun startNewGame() {
        game = Game()
        game?.initialize()
        clearSelection()
        window?.renderGame(game!!, selectedCards, selectedSourcePile)
        window?.updateStatus("New game. Click on the card to start.")
    }

    fun onCardClick(card: Card, pile: Pile) {
        val game = game ?: return
        val window = window ?: return

        if (selectedCards != null && selectedSourcePile != null) {
            tryMove(pile)
        } else {
            selectedSourcePile = pile
            // for TablePile take all cards from clicked to the top
            if (pile is TablePile) {
                selectedCards = getStackFromCard(pile, card)
            } else {
                // for Waste, Foundation, Stock take only one card
                selectedCards = listOf(card)
            }

            window.renderGame(game, selectedCards, selectedSourcePile)
            window.updateStatus("Chosen cards: ${selectedCards?.size}. Click on the target pile.")
        }
    }

    // function for getting pile of cards from target card to the top
    private fun getStackFromCard(pile: TablePile, card: Card): List<Card> {
        val allCards = pile.getCardsCopy()
        val index = allCards.indexOfFirst { it == card && it.isFaceUp }
        return if (index >= 0 && index < allCards.size) {
            allCards.subList(index, allCards.size)
        } else {
            listOf(card)
        }
    }

    fun onFoundationClick(index: Int) {
        val game = game ?: return
        val target = game.foundations[index]
        if (selectedCards != null && selectedSourcePile != null) {
            tryMove(target)
        } else {
            window?.updateStatus("Choose the card at first.")
        }
    }

    fun onTableauClick(index: Int) {
        val game = game ?: return
        val target = game.tableau[index]
        if (selectedCards != null && selectedSourcePile != null) {
            tryMove(target)
        } else {
            window?.updateStatus("Choose the card at first.")
        }
    }

    fun onStockClick() {
        window?.updateStatus("Press 'Draw' button.")
    }

    fun handleDraw() {
        val game = game ?: return
        val window = window ?: return

        if (game.stock.isEmpty() && !game.waste.isEmpty()) {
            val cards = game.waste.clearAndReturn()
            game.stock.resetFromWaste(cards)
        } else {
            val card = game.stock.drawCard()
            if (card != null) {
                card.isFaceUp = true
                game.waste.addCard(card)
            }
        }
        clearSelection()
        window.renderGame(game, selectedCards, selectedSourcePile)
        window.updateStatus("Draw. Moves: ${game.movesCount}")
    }

    fun handleUndo() {
        val game = game ?: return
        val window = window ?: return

        if (game.undo()) {
            clearSelection()
            window.renderGame(game, selectedCards, selectedSourcePile)
            window.updateStatus("Undo. Moves: ${game.movesCount}")
        } else {
            window.updateStatus("Nothing to do. Moves: ${game.movesCount}")
        }
    }

    private fun tryMove(targetPile: Pile) {
        val cards = selectedCards ?: return
        val source = selectedSourcePile ?: return
        val game = game ?: return
        val window = window ?: return

        if (cards.isEmpty()) return

        println("Trial: ${cards.size} cards from ${source::class.simpleName} -> ${targetPile::class.simpleName}")

        val cardToPlace = cards.first()
        println("Checking the card: $cardToPlace")
        println("canPlace: ${targetPile.canPlace(cardToPlace)}")

        if (!targetPile.canPlace(cardToPlace)) {
            window.updateStatus("Can't place. Moves: ${game.movesCount}")
            return
        }

        val move = Move(source, targetPile, cards)
        if (game.makeMove(move)) {
            clearSelection()
            window.renderGame(game, selectedCards, selectedSourcePile)
            window.updateStatus("Move is done! Moves: ${game.movesCount}")
            checkWin()
        } else {
            window.updateStatus("The game cancel your move. Moves: ${game.movesCount}")
        }
    }

    private fun checkWin() {
        val game = game ?: return
        val window = window ?: return
        if (game.foundations.all { it.size() == 13 }) {
            session.reportWin()
            window.updateStatus("You're win! Your score: ${session.currentPlayer?.score}")
        }
    }

    private fun clearSelection() {
        selectedCards = null
        selectedSourcePile = null
    }
}

fun main() = Application.launch(Main::class.java)
