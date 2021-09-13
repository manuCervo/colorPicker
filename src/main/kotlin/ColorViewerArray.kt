import javafx.fxml.FXMLLoader
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane


class ColorViewerArray(size: Int, onColorViewerClicked: (event: MouseEvent?, color: Color) -> Unit) {
    private val colorViewers: Array<ColorViewer> = Array(size) { ColorViewer(onColorViewerClicked) }

    var colors: Array<Color>
        set(value) {
            for (i in value.indices) {
                colorViewers[i].color = value[i]
            }
        }
        get() {
            return colorViewers.map { it.color }.toTypedArray()
        }


    fun load(parent: Pane) {
        for (c in colorViewers) {
            parent.children.add(c.load())
        }
    }


}