import javafx.scene.input.MouseButton
import javafx.scene.layout.Pane

class ColorList(private val container: Pane, private val onColorSelected: (Color) -> Unit) {
    private val colorList = mutableListOf<Color>()
    val colors:List<Color>
    get() {
        return colorList
    }

    fun add(color: Color) {
        colorList.add(color)
        val colorViewer = ColorViewer()
        val root = colorViewer.load()
        container.children.add(root)
        colorViewer.onclick = { mouseEvent, c ->
            mouseEvent?.let {
                if (it.button == MouseButton.PRIMARY) {
                    onColorSelected(c)
                } else {
                    container.children.remove(root)
                    colorList.remove(colorViewer.color)
                }
            }
        }
        colorViewer.color = color
    }

    fun clear()
    {
        container.children.clear()
        colorList.clear()
    }
}