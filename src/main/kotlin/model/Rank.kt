package model

enum class Rank(val value: Int){
    ACE(2), TWO(2), THREE(3),
    FOUR(4), FIVE(5), SIX(6),
    SEVEN(7), EIGHT(8), NINE(9),
    TEN(10), JACK(11), QUEEN(12),
    KING(13);
    fun displaySymbol(): String = when(this){
        ACE->"A"; JACK->"J";QUEEN->"Q"; KING->"K"
        else-> value.toString()
    }
}