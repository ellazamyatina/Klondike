package session

data class Player(
    val name: String
) {
    var score: Int = 0
    var wins: Int = 0
    var totalMoves: Int = 0

    override fun toString(): String = "$name (Score: $score, Wins: $wins"
}
