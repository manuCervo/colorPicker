import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.input.MouseButton
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import java.io.File

class PaletteItem {

    lateinit var root: HBox
    lateinit var paletteNameLabel: Label
    lateinit var colorContainer: HBox
    lateinit var deleteButton: Button

    var palette: Palette = Palette("palette", listOf())
        set(value) {
            field = value
            paletteNameLabel.text = palette.name
            for (c in palette.colors) {
                colorContainer.children.add(Pane().also {
                    it.style = "-fx-background-color:#${c.hex}"
                })
            }
        }

    var onClick: () -> Unit = {}

    fun load(): Parent {
        val loader = FXMLLoader(javaClass.classLoader.getResource("paletteItem.fxml"))
        loader.setController(this)
        return loader.load()
    }

    fun initialize() {
        root.setOnMouseClicked {
            if (it.button == MouseButton.PRIMARY) {
                onClick()
            }
        }

        deleteButton.setOnMouseClicked {
            if(it.button == MouseButton.PRIMARY)
            {
                File(Globals.palettesDir,palette.name).delete()
                onDeleted()
            }
        }
    }

    var onDeleted:()->Unit = {}
}