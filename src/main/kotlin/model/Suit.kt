package model

enum class Suit {
    HEARTS,
    DIAMONDS,
    CLUBS,
    SPADES,
    ;

    fun isRed(): Boolean = this == HEARTS || this == DIAMONDS

    fun displaySymbol(): String =
        when (this) {
            HEARTS -> "♥"
            DIAMONDS -> "♦"
            CLUBS -> "♣"
            SPADES -> "♠"
        }
}
