package model

/** function for creating the main shuffled pile **/

fun createShuffledPile(): List<Card>{
    val pile = mutableListOf<Card>()
    for (rank in Rank.entries){
        for (suit in Suit.entries){
            val card = Card(rank,suit)
            pile.add(card)
        }
    }
    return pile.shuffled()
}