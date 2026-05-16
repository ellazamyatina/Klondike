package javafx

import game.Game
import game.Pile
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.*
import javafx.stage.Stage
import model.Card
import model.Rank
import model.Suit

class GameWindow(private val main: Main) { // link of the controller
    private val root = BorderPane() // divide the window in 5 frames
    private val statusLabel = Label("...") // status bar
    private val gameTable = Pane() // play fields

    // function for making window
    fun show(stage: Stage) { // take OS window as reference
        stage.apply {
            title = "Klondike Solitaire"
            scene = Scene(root, 1000.0, 700.0) // make scene
            show()
        }
        setupUI()
    }

    // function for setting up UI
    private fun setupUI() {
        val topPanel = HBox(10.0).apply {
            style = "-fx-padding: 10; -fx-background-color: #1e3c1b;"
            alignment = Pos.CENTER_LEFT

            // every button call main function
            children += Button("New Game").apply { setOnAction { main.startNewGame() } }
            children += Button("Draw").apply { setOnAction { main.handleDraw() } }
            children += Button("Undo").apply { setOnAction { main.handleUndo() } }
        }

        gameTable.style = "-fx-background-color: #2d5a15;"

        val bottomPanel = HBox().apply {
            style = "-fx-padding: 10; -fx-background-color: #1e3c1b;"
            alignment = Pos.CENTER
            children += statusLabel.apply { style = "-fx-text-fill: white; -fx-font-size: 14px;" }
        }

        // setting top, center and bottom of the border plane
        root.top = topPanel
        root.center = gameTable
        root.bottom = bottomPanel
    }

    // function for updating status
    fun updateStatus(msg: String) {
        statusLabel.text = msg
    }

    // MAIN function, run after every move
    fun renderGame(game: Game, selectedCards: List<Card>?, selectedPile: Pile?) {
        gameTable.children.clear() // redraw

        val W = 70.0 // just coordinates
        val H = 100.0
        val GAP = 15.0

        // 1. rendering Stock(Pile)
        if (game.stock.getCardsCopy().isNotEmpty()) {
            val back = Card(Rank.ACE, Suit.SPADES, isFaceUp = false)
            val v = CardView(back, false) { _, _ -> main.onStockClick() } // call main function
            v.layoutX = 40.0
            v.layoutY = 40.0
            gameTable.children.add(v)
        }

        // 2. rendering Waste
        if (game.waste.getCardsCopy().isNotEmpty()) {
            val top = game.waste.getCardsCopy().last()
            val isSelected = selectedCards?.contains(top) == true && selectedPile === game.waste // the same object in memory
            val v = CardView(top, isSelected) { _, _ -> main.onCardClick(top, game.waste) }
            v.layoutX = 120.0
            v.layoutY = 40.0
            gameTable.children.add(v)
        }

        // 3. rendering Foundations
        val suitSymbols = listOf("♥", "♦", "♣", "♠")

        for (i in 0..3) {
            val pile = game.foundations[i]
            val fx = 500.0 + i * (W + GAP)

            // empty contour and suits
            val foundationBase = StackPane().apply {
                prefWidth = W
                prefHeight = H
                style = "-fx-border-color: rgba(255,255,255,0.3); -fx-border-width: 1;"
                layoutX = fx
                layoutY = 40.0
                setOnMouseClicked { main.onFoundationClick(i) }
                children += Label(suitSymbols[i]).apply {
                    style = "-fx-font-size: 35px; -fx-text-fill: rgba(255,255,255,0.4); -fx-font-weight: bold;"
                    alignment = Pos.CENTER
                }
            }
            gameTable.children.add(foundationBase)

            // rendering top card
            if (pile.getCardsCopy().isNotEmpty()) {
                val top = pile.getCardsCopy().last()
                val isSelected = selectedCards?.contains(top) == true && selectedPile === pile
                val v = CardView(top, isSelected) { _, _ -> main.onFoundationClick(i) }
                v.layoutX = fx
                v.layoutY = 40.0
                gameTable.children.add(v)
            }
        }

        // 4. rendering Tableau
        for (col in 0..6) {
            val pile = game.tableau[col]
            var y = 160.0
            val tx = 40.0 + col * (W + GAP)

            for (card in pile.getCardsCopy()) {
                val isSelected = selectedCards?.contains(card) == true && selectedPile === pile
                val v = CardView(card, isSelected) { clickedCard, _ ->
                    main.onCardClick(clickedCard, pile)
                }
                v.layoutX = tx
                v.layoutY = y
                gameTable.children.add(v)
                y += if (card.isFaceUp) 25.0 else 15.0
            }

            // empty pile for click
            if (pile.isEmpty()) {
                val empty = Pane().apply {
                    prefWidth = W
                    prefHeight = H
                    layoutX = tx
                    layoutY = 160.0
                    setOnMouseClicked { main.onTableauClick(col) }
                }
                gameTable.children.add(empty)
            }
        }
    }
}
