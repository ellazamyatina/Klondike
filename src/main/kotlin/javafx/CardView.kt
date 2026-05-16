package javafx

import javafx.scene.image.Image
import javafx.scene.image.ImageView
import model.Card

class CardView(
    private val card: Card,
    isSelected: Boolean,
    private val onClick: (Card, CardView) -> Unit
) : ImageView() {

    init {
        // setting properties of card
        fitWidth = 70.0
        fitHeight = 100.0
        isPreserveRatio = true
        isSmooth = true

        updateImage()

        if (isSelected) {
            style = "-fx-border-width: 3; -fx-border-radius: 5;"
        }
        // send instructions to the Main.kt
        setOnMouseClicked { onClick(card, this) }
    }

    // function for loading right picture of the card
    private fun updateImage() {
        val name = if (card.isFaceUp) {
            "${card.rank}_${card.suit}.png"
        } else {
            "back.png"
        }

        val url = javaClass.getResource("/cards/$name")
        image = Image(url!!.toExternalForm(), 70.0, 100.0, true, true)
        // true , true parameters - preloading and smoothing
    }
}
