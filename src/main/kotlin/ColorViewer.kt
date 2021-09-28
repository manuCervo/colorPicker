import javafx.animation.ScaleTransition
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.control.TextField
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane
import javafx.util.Duration


class ColorViewer(var onclick: (MouseEvent?, Color) -> Unit = { _, _ -> }) {
    lateinit var colorPane: Pane
    lateinit var rgbTextField: TextField
    lateinit var rgbNormalizedTextField: TextField
    lateinit var hexTextField: TextField
    lateinit var hsvTextField: TextField

    fun initialize() {
        colorPane.setOnMouseClicked { onclick(it, color) }
        colorPane.hoverProperty().addListener { _, _, newValue ->
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

            colorPane.style = "-fx-background-color:#${value.hex};"
            rgbTextField.text = value.rgb.toString()
            rgbNormalizedTextField.text = value.rgbNormalized.let {
                Triple(
                    it.first.roundTo(2),
                    it.second.roundTo(2),
                    it.third.roundTo(2)
                )
            }.toString()
            hexTextField.text = "#${value.hex}"
            hsvTextField.text = value.hsv.let {
                Triple(
                    it.first,
                    it.second.roundTo(2),
                    it.third.roundTo(2)
                )
            }.toString()
        }

    fun load(): Parent {
        val loader = FXMLLoader(javaClass.classLoader.getResource("colorviewer.fxml"))
        loader.setController(this)
        return loader.load()
    }
}