package session

import game.Game
import java.io.File

class SessionManager {
    var currentPlayer: Player? = null
    private var currentGame: Game? = null
    private val registryFile = File("players_registry.txt")

    fun login(name: String) {
        currentPlayer = Player(name)
        loadPlayerData()
    }

    fun startNewGame(): Game {
        currentGame = Game()
        currentGame?.initialize()
        return currentGame!!
    }

    fun getActiveGame(): Game? = currentGame

    fun reportWin() {
        currentPlayer?.let {
            it.wins++
            it.score += 100
            it.totalMoves += currentGame?.movesCount ?: 0
            savePlayerData()
        }
    }

    private fun savePlayerData() {
        val player = currentPlayer ?: return

        val lines = mutableListOf<String>()
        var updated = false
        if (registryFile.exists()) {
            registryFile.readLines().forEach { line ->
                val parts = line.split("|")
                if (parts.size == 4 && parts[0] == player.name) {
                    lines.add("${player.name}|${player.score}|${player.wins}|${player.totalMoves}")
                    updated = true
                } else {
                    lines.add(line)
                }
            }
        }

        if (!updated) {
            lines.add("${player.name}|${player.score}|${player.wins}|${player.totalMoves}")
        }

        registryFile.writeText(lines.joinToString("\n"))
    }

    private fun loadPlayerData() {
        val player = currentPlayer ?: return
        if (!registryFile.exists()) return

        registryFile.readLines().forEach { line ->
            val parts = line.split("|")
            if (parts.size == 3 && parts[0] == player.name) {
                player.score = parts[1].toIntOrNull() ?: 0
                player.wins = parts[2].toIntOrNull() ?: 0
                player.totalMoves = parts[3].toIntOrNull() ?: 0
            }
        }
    }
}
