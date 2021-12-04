import javafx.application.Platform
import java.io.File
import java.io.FileReader
import kotlin.concurrent.thread

class SavedPaletteLoader(paletteList: PaletteList) : PaletteLoader(paletteList) {
    override fun load() {
        thread {
            val directory: File = File(Globals.palettesDir)
            val fileNames: Array<File> = directory.listFiles() ?: arrayOf()
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
                    Platform.runLater {
                        onPaletteLoaded(Palette(paletteName, colors))
                    }
                }
            }
        }
    }
}