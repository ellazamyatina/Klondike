package ui

import game.Game
import model.Move

class GamePresenter {
    private var game: Game? = null
    fun start() {
        Display.printTitle()
        Display.printHelp()

        var playing = true
        while (playing) {
            val command = InputHandler.readCommand()
            val currentGame = game

            when (command) {
                is Command.Help -> Display.printHelp()

                is Command.Quit -> {
                    Display.printMessage("Thank you for the game!")
                    playing = false
                }

                is Command.Start -> {
                    val newGame = Game()
                    newGame.initialize()
                    game = newGame
                    Display.printMessage("The new game was started")
                }

                is Command.Undo -> {
                    if (currentGame == null) {
                        Display.printMessage("Start the game first!")
                    } else if (currentGame.undo()) {
                        Display.printMessage("Move was undone!")
                    } else {
                        Display.printMessage("Nothing to undo!")
                    }
                }

                is Command.Draw -> {
                    if (currentGame == null) {
                        Display.printMessage("Start the game first!")
                    } else {
                        if (currentGame.stock.isEmpty() && !currentGame.waste.isEmpty()) {
                            val wasteCards = currentGame.waste.clearAndReturn()
                            currentGame.stock.resetFromWaste(wasteCards)
                            Display.printMessage("The pile was reshuffled!")
                        }
                        val card = currentGame.stock.drawCard()
                        if (card != null) {
                            currentGame.waste.addCard(card)
                            Display.printMessage("Your card: $card")
                        } else {
                            Display.printMessage("The pile is empty!")
                        }
                    }
                }

                is Command.MoveFromWaste -> {
                    if (currentGame == null) {
                        Display.printMessage("Start the game first!")
                    } else {
                        val wasteCard = currentGame.waste.topCard()
                        if (wasteCard == null) {
                            Display.printMessage("Waste is empty!")
                        } else {
                            val toPile = if (command.toFoundation) {
                                currentGame.foundations.getOrNull(command.toPileIndex)
                            } else {
                                currentGame.tableau.getOrNull(command.toPileIndex)
                            }
                            if (toPile != null) {
                                val move = Move(currentGame.waste, toPile, listOf(wasteCard))
                                if (currentGame.makeMove(move)) Display.printMessage("Move done!")
                                else Display.printMessage("Wrong move!")
                            } else {
                                Display.printMessage("Invalid pile number!")
                            }
                        }
                    }
                }

                is Command.Move -> {
                    if (currentGame == null) {
                        Display.printMessage("Start the game first!")
                    } else {
                        val fromPile = currentGame.tableau.getOrNull(command.from)
                        val toPile = currentGame.tableau.getOrNull(command.to)
                        if (fromPile != null && toPile != null) {
                            val cards = fromPile.getTopCards(command.count)
                            if (cards.isNotEmpty()) {
                                val move = Move(fromPile, toPile, cards)
                                if (currentGame.makeMove(move)) Display.printMessage("Move done!")
                                else Display.printMessage("Wrong move!")
                            } else Display.printMessage("No cards to move")
                        } else Display.printMessage("Wrong tableau number (0-6)")
                    }
                }

                is Command.MoveToFoundation -> {
                    if (currentGame == null) {
                        Display.printMessage("Start the game first!")
                    } else {
                        val fromPile = currentGame.tableau.getOrNull(command.fromTableauIndex)
                        val toPile = currentGame.foundations.getOrNull(command.foundationIndex)
                        if (fromPile != null && toPile != null) {
                            val card = fromPile.topCard()
                            if (card != null && card.isFaceUp) {
                                val move = Move(fromPile, toPile, listOf(card))
                                if (currentGame.makeMove(move)) Display.printMessage("Move done!")
                                else Display.printMessage("Wrong move!")
                            } else Display.printMessage("No face-up card to move")
                        } else Display.printMessage("Invalid pile number")
                    }
                }

                is Command.AddPlayer -> {
                    Display.printMessage("Player '${command.name}' added")
                }
            }

            // Отрисовка и проверка победы
            if (currentGame != null && command !is Command.Help && command !is Command.Quit) {
                Display.printGame(currentGame)
                if (currentGame.isGameWon()) {
                    Display.printMessage("You win!")
                    playing = false
                }
            }
        }
    }
}