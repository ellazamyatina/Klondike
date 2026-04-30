package ui

sealed class Command {
    object Help : Command()

    object Quit : Command()

    object Start : Command()

    object Undo : Command()

    object Draw : Command()

    data class AddPlayer(
        val name: String,
    ) : Command()

    data class Move(
        val from: Int,
        val to: Int,
        val count: Int = 1,
    ) : Command()

    data class MoveToFoundation(
        val fromTableauIndex: Int,
        val foundationIndex: Int,
    ) : Command()

    data class MoveFromWaste(
        val toPileIndex: Int,
        val toFoundation: Boolean = false,
    ) : Command()

    companion object {
        fun parse(input: String): Command {
            val parts =
                input
                    .trim()
                    .lowercase()
                    .split(" ")
                    .filter { it.isNotEmpty() }
            if (parts.isEmpty()) return Help

            return when (parts[0]) {
                "help" -> Help
                "quit" -> Quit
                "start" -> Start
                "undo" -> Undo
                "draw" -> Draw
                "add" -> if (parts.size > 1) AddPlayer(parts[1]) else Help
                "move" -> {
                    if (parts.size >= 3) {
                        val from = parts[1].toIntOrNull() ?: return Help
                        val to = parts[2].toIntOrNull() ?: return Help
                        val count = parts.getOrNull(3)?.toIntOrNull() ?: 1
                        if (to >= 100) MoveToFoundation(from, to - 100) else Move(from, to, count)
                    } else {
                        Help
                    }
                }
                "mw" -> { // move waste
                    if (parts.size >= 2) {
                        val to = parts[1].toIntOrNull() ?: return Help
                        if (to >= 100) {
                            MoveFromWaste(to - 100, toFoundation = true)
                        } else {
                            MoveFromWaste(to, toFoundation = false)
                        }
                    } else {
                        Help
                    }
                }
                else -> Help
            }
        }
    }
}
