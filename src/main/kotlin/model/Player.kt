package model

data class Player(
    val player: String,
    var score: Int = 0,
    var wins: Int = 0
)