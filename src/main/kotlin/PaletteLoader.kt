import javafx.scene.control.Tab
import java.io.File
import java.io.FileReader

class PaletteLoader {
    lateinit var savedPalettesTab: Tab
    private val paletteList: PaletteList = PaletteList()

    fun initialize() {
        val directory: File = File(Globals.palettesDir)
        val fileNames: Array<File> = directory.listFiles() ?: arrayOf()
        val palettes = mutableListOf<Palette>()
        for (f in fileNames) {
            val paletteName = f.name
            val colors = mutableListOf<Color>()
            val reader: FileReader = FileReader(f)
            val lines = reader.readLines()
            for (l in lines) {
                try {
                    colors.add(Color.fromHex(l))
                } catch (e: NumberFormatException) {
                }
            }
            reader.close()
            if (colors.size > 0) {
                palettes.add(Palette(paletteName, colors))
            }
        }
        savedPalettesTab.content = paletteList.load()
        paletteList.palettes = palettes
        paletteList.onPaletteSelected = { onPaletteSelected(it) }
    }

    var onPaletteSelected: (palette: Palette) -> Unit = {}
}

