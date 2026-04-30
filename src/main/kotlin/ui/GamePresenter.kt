package ui

import game.Game
import model.Move

class GamePresenter {
    private lateinit var game: Game

    fun start() {
        Display.printTitle()
        Display.printHelp()

        var playing = true
        while (playing) {
            val input = Display.prompt()
            val command = Command.parse(input)

            when (command) {
                is Command.Help -> Display.printHelp()

                is Command.Quit -> {
                    Display.printMessage("Thank you for the game!")
                    playing = false
                }

                is Command.Start -> {
                    game = Game()
                    game.initialize()
                    Display.printMessage("The new game was started")
                }

                is Command.Undo -> {
                    if (::game.isInitialized) {
                        if (game.undo()) {
                            Display.printMessage("Move was undone!")
                        } else {
                            Display.printMessage("Nothing to undo!")
                        }
                    } else {
                        Display.printMessage("Start the game first!")
                    }
                }

                is Command.Draw -> {
                    if (::game.isInitialized) {
                        if (game.stock.isEmpty() && !game.waste.isEmpty()) {
                            val wasteCards = game.waste.clearAndReturn()
                            game.stock.resetFromWaste(wasteCards)
                            Display.printMessage("The pile was reshuffled!")
                        }

                        val card = game.stock.drawCard()
                        if (card != null) {
                            game.waste.addCard(card)
                            Display.printMessage("Your card: $card")
                        } else {
                            Display.printMessage("The pile is empty!")
                        }
                    } else {
                        Display.printMessage("Start the game first!")
                    }
                }

                is Command.MoveFromWaste -> {
                    if (::game.isInitialized) {
                        val wasteCard = game.waste.topCard()
                        if (wasteCard == null) {
                            Display.printMessage("Waste is empty!")
                        } else {
                            val toPile =
                                if (command.toFoundation) {
                                    game.foundations.getOrNull(command.toPileIndex)
                                } else {
                                    game.tableau.getOrNull(command.toPileIndex)
                                }

                            if (toPile != null) {
                                val move = Move(game.waste, toPile, listOf(wasteCard))
                                if (game.makeMove(move)) {
                                    Display.printMessage("Move done!")
                                } else {
                                    Display.printMessage("Wrong move!")
                                }
                            } else {
                                Display.printMessage("Invalid pile number!")
                            }
                        }
                    }
                }

                is Command.Move -> {
                    if (::game.isInitialized) {
                        val fromPile = game.tableau.getOrNull(command.from)
                        val toPile = game.tableau.getOrNull(command.to)

                        if (fromPile != null && toPile != null) {
                            val cards = fromPile.getTopCards(command.count)
                            if (cards.isNotEmpty()) {
                                val move = Move(fromPile, toPile, cards)
                                if (game.makeMove(move)) {
                                    Display.printMessage("Move done!")
                                } else {
                                    Display.printMessage("Wrong move!")
                                }
                            } else {
                                Display.printMessage("No cards to move")
                            }
                        } else {
                            Display.printMessage("Wrong tableau number (0-6)")
                        }
                    }
                }

                is Command.MoveToFoundation -> {
                    if (::game.isInitialized) {
                        val fromPile = game.tableau.getOrNull(command.fromTableauIndex)
                        val toPile = game.foundations.getOrNull(command.foundationIndex)

                        if (fromPile != null && toPile != null) {
                            val card = fromPile.topCard()
                            if (card != null && card.isFaceUp) {
                                val move = Move(fromPile, toPile, listOf(card))
                                if (game.makeMove(move)) {
                                    Display.printMessage("Move done!")
                                } else {
                                    Display.printMessage("Wrong move!")
                                }
                            } else {
                                Display.printMessage("No face-up card to move")
                            }
                        } else {
                            Display.printMessage("Invalid pile number")
                        }
                    }
                }

                is Command.AddPlayer -> {
                    Display.printMessage("Player '${command.name}' added")
                }
            }

            if (::game.isInitialized && command !is Command.Help && command !is Command.Quit) {
                Display.printGame(game)

                if (game.isGameWon()) {
                    Display.printMessage("You win! ")
                    playing = false
                }
            }
        }
    }
}
