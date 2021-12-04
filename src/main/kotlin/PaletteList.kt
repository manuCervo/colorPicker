import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.control.Control
import javafx.scene.control.ScrollPane
import javafx.scene.control.TextField
import javafx.scene.layout.VBox


class PaletteList {
    lateinit var container: VBox
    lateinit var scrollPane: ScrollPane
    lateinit var searchTextField: TextField
    var palettes = mutableListOf<Palette>()
        set(value) {
            field = value
            for (p in palettes) {
                val item = PaletteItem().also { items.add(it) }
                container.children.add(item.load())
                item.palette = p
                item.onClick = {
                    onPaletteSelected(p)

                }
                item.onDeleted = {
                    palettes.remove(item.palette)
                    items.remove(item)
                    container.children.remove(item.root)
                }
            }
        }
    private val items = mutableListOf<PaletteItem>()

    fun initialize() {
        searchTextField.textProperty().addListener { _, _, newValue ->

            for (i in items) {
                i.root.isVisible = i.palette.name.contains(newValue)
                i.root.isManaged = i.root.isVisible
            }
        }
    }

    fun load(): Parent {
        val loader = FXMLLoader(javaClass.classLoader.getResource("palettelist.fxml"))
        loader.setController(this)
        return loader.load()
    }

    var onPaletteSelected: (p: Palette) -> Unit = {}
}
