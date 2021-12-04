import javafx.application.Platform
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.thread

class ColorHuntPaletteLoader(paletteList: PaletteList) : PaletteLoader(paletteList) {

    private var step = 0
    private val loading:AtomicBoolean = AtomicBoolean(false)

    init {
        paletteList.canDelete = false
        paletteList.scrollPane.vvalueProperty().addListener { observable, oldValue, newValue ->
            onScroll(oldValue,newValue)
        }
    }

    override fun load() {
        loading.set(true)
        scrape()
    }

    fun scrape() {
        thread {
            val connection = Jsoup.connect("https://colorhunt.co/php/feed.php")
            connection.data("step", "$step")
            connection.data("sort", "new")
            connection.data("tags", "")
            connection.data("timeframe", "30")

            val document: Document = connection.post()
            val json = document.body().text()

            val codes = parseJson(json)
            for (c in codes) {
                val colors = parseCode(c)
                val name = getTags(c)
                Platform.runLater {
                    onPaletteLoaded(Palette(name, colors))
                }
            }
            loading.set(false)
        }
    }

    private fun parseJson(json: String): List<String> {
        val data = Json.decodeFromString<Array<Map<String, String>>>(json)
        return data.mapNotNull { it["code"] }
    }

    private fun getTags(code: String): String {
        val connection = Jsoup.connect("https://colorhunt.co/php/single.php")
        connection.data("single", code)
        val document = connection.post()
        val data = Json.decodeFromString<Array<Map<String, String>>>(document.body().text())
        return data[0]["tags"]!!
    }

    private fun parseCode(code: String): List<Color> {
        val colors = mutableListOf<Color>()
        for (i in code.indices step 6) {
            val hex = code.subSequence(i, i + 6).toString()
            colors.add(Color.fromHex(hex))
        }
        return colors
    }

    private fun onScroll(oldValue: Number, newValue: Number){
        if (newValue.toDouble() == 1.0 && oldValue.toDouble() < newValue.toDouble())
        {
            if(loading.compareAndSet(false,true))
            {
                step++
                scrape()
            }
        }
    }
}