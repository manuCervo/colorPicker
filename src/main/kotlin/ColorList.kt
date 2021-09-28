import javafx.scene.input.MouseButton
import javafx.scene.layout.Pane

class ColorList(private val container: Pane, private val onColorSelected: (Color) -> Unit) {

    fun add(color: Color) {
        val colorViewer = ColorViewer()
        val root = colorViewer.load()
        container.children.add(root)
        colorViewer.onclick = { mouseEvent, c ->
            mouseEvent?.let {
                if (it.button == MouseButton.PRIMARY) {
                    onColorSelected(c)
                } else {
                    container.children.remove(root)
                }
            }
        }
        colorViewer.color = color
    }
}