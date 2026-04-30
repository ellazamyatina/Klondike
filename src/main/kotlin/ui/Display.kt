package ui

import game.Game

object Display {
    fun printTitle() {
        println("||==||  KLONDIKE SOLITAIRE  ||==||  ")
    }

    fun printHelp() {
        println("\nAvailable commands:")
        println("  start        - New Game")
        println("  draw         - Take the card from the pile")
        println("  mw <to>      - Place from the Waste Pile (tableau: 0-6, founds: 100-103)")
        println("  move <from> <to> [num] - Replace card(s)")
        println("  undo         - Undo the last move")
        println("  add <name>   - Add a new player")
        println("  quit         - Exit")
        println("  help         - Help information")
    }

    fun printGame(game: Game) {
        println("\n" + "─".repeat(40))

        print("Stock: ${game.stock.size()} | ")
        val wasteTop = game.waste.topCard()
        println("Waste: ${wasteTop?.toString() ?: "-"}")

        print("Foundations: ")
        game.foundations.forEachIndexed { i, f ->
            val top = f.topCard()
            print("[$i: ${top?.toString() ?: "·"}] ")
        }
        println()

        println("\nTableau:")
        val maxRows = game.tableau.maxOfOrNull { it.size() } ?: 0

        for (row in 0 until maxRows) {
            print("  ")
            game.tableau.forEachIndexed { col, pile ->
                val cards = pile.getCardsCopy()
                if (row < cards.size) {
                    val card = cards[row]
                    val display =
                        if (card.isFaceUp) {
                            card.toString()
                        } else {
                            "🂠"
                        }
                    print("$display     ")
                } else {
                    print("      ")
                }
            }
            println()
        }
        println("─".repeat(40))
    }

    fun printMessage(msg: String) {
        println(msg)
    }

    fun prompt(): String {
        print("\n>")
        return readln().trim() ?: ""
    }
}
