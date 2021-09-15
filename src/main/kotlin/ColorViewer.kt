import javafx.animation.ScaleTransition
import javafx.fxml.FXMLLoader
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.control.TextField
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import javafx.util.Duration
import kotlin.math.pow
import kotlin.math.roundToInt


class ColorViewer(var onclick: (MouseEvent?, Color) -> Unit = { _, _ -> }) {
    lateinit var colorPane: Pane
    lateinit var rgbTextField: TextField
    lateinit var rgbNormalizedTextField: TextField
    lateinit var hexTextField: TextField
    lateinit var hsvTextField: TextField

    private val backgroundColor = Color.fromHex("2b2b2b")

    fun initialize() {
        colorPane.setOnMouseClicked { onclick(it, color) }
        colorPane.hoverProperty().addListener { observable, oldValue, newValue ->
            val transition: ScaleTransition = ScaleTransition(Duration.millis(100.0), colorPane)
            if (newValue) {
                transition.toX = 1.1
                transition.toY = 1.1
            } else {
                transition.toX = 1.0
                transition.toY = 1.0
            }
            transition.play()
        }

    }

    var color: Color = Color.fromRGB(0, 0, 0)
        set(value) {
            field = value
            val rgbString = value.rgb.toString()
            val rgbNormalizedString =
                value.rgbNormalized.let {
                    Triple(
                        it.first.roundTo(2),
                        it.second.roundTo(2),
                        it.third.roundTo(2)
                    )
                }.toString()

            val hex = value.hex

            val hsvString = value.hsv.let {
                Triple(
                    it.first,
                    it.second.roundTo(2),
                    it.third.roundTo(2)
                )
            }.toString()

            colorPane.style = "-fx-background-color:rgb$rgbString;"
            rgbTextField.text = rgbString
            rgbNormalizedTextField.text = rgbNormalizedString
            hexTextField.text = "#$hex"
            hsvTextField.text = hsvString
        }

    fun load(): Parent {
        val loader = FXMLLoader(javaClass.classLoader.getResource("colorviewer.fxml"))
        loader.setController(this)
        return loader.load()
    }
}