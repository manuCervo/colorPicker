import javafx.scene.control.Tab

class PaletteLoaderController {
    lateinit var savedPalettesTab: Tab
    lateinit var colorHuntPalettesTab: Tab

    private val userPaletteList: PaletteList = PaletteList()
    private val colorHuntPaletteList = PaletteList()

    fun initialize() {
//        val directory: File = File(Globals.palettesDir)
//        val fileNames: Array<File> = directory.listFiles() ?: arrayOf()
//        val palettes = mutableListOf<Palette>()
//        for (f in fileNames) {
//            val paletteName = f.name
//            val colors = mutableListOf<Color>()
//            val reader: FileReader = FileReader(f)
//            val lines = reader.readLines()
//            for (l in lines) {
//                try {
//                    colors.add(Color.fromHex(l))
//                } catch (e: NumberFormatException) {
//                }
//            }
//            reader.close()
//            if (colors.size > 0) {
//                palettes.add(Palette(paletteName, colors))
//            }
//        }
        savedPalettesTab.content = userPaletteList.load()
        userPaletteList.onPaletteSelected = { onPaletteSelected(it) }
        SavedPaletteLoader(userPaletteList).load()

        colorHuntPalettesTab.content = colorHuntPaletteList.load()
        colorHuntPaletteList.onPaletteSelected = { onPaletteSelected(it) }
        ColorHuntPaletteLoader(colorHuntPaletteList).load()
    }

    var onPaletteSelected: (palette: Palette) -> Unit = {}
}

