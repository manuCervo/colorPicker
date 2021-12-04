import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.control.ScrollPane
import javafx.scene.control.TextField
import javafx.scene.layout.VBox
import java.util.regex.Pattern


class PaletteList {
    lateinit var container: VBox
    lateinit var scrollPane: ScrollPane
    lateinit var searchTextField: TextField
    private val items = mutableListOf<PaletteItem>()
    private val searchWords: List<String>
        get() {
            return searchTextField.text.split(Pattern.compile(" +")).filter { it.isNotEmpty() }
        }


    var canDelete: Boolean = true
        set(value) {
            field = value
            for (i in items) {
                i.deleteButton.isVisible = value
                i.deleteButton.isManaged = value
            }
        }


    var palettes = mutableListOf<Palette>()
        set(value) {
            field = value
            for (p in palettes) {
                add(p)
            }
        }

    fun initialize() {
        searchTextField.textProperty().addListener { _, _, newValue ->
            for (i in items) {
                i.root.isVisible = testSearch(i.palette.name)
                i.root.isManaged = i.root.isVisible
            }
        }
    }

    fun load(): Parent {
        val loader = FXMLLoader(javaClass.classLoader.getResource("palettelist.fxml"))
        loader.setController(this)
        return loader.load()
    }

    fun add(p: Palette) {
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
        item.deleteButton.isVisible = canDelete
        item.deleteButton.isManaged = canDelete

        item.root.isVisible = testSearch(p.name)
        item.root.isManaged = item.root.isVisible
    }

    var onPaletteSelected: (p: Palette) -> Unit = {}

    private fun testSearch(name: String): Boolean {
        return searchWords.isEmpty() || searchWords.all {
            name.contains(it, true)
        }
    }
}
