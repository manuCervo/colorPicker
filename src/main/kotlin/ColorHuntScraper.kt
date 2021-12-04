import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.jsoup.Jsoup
import org.jsoup.nodes.Document



class ColorHuntScraper {
    fun scrape():List<Palette>
    {
        val connection = Jsoup.connect("https://colorhunt.co/php/feed.php")
        connection.data("step","0")
        connection.data("sort","new")
        connection.data("tags","")
        connection.data("sort","new")

        val document: Document = connection.post()
        val json = document.body().text()

        val palettes = mutableListOf<Palette>()
        val codes = parseJson(json)
        for (c in codes)
        {
            val colors = parseCode(c)
            val name = getTags(c)
            palettes.add(Palette(name,colors))
        }
        return palettes
    }

    private fun parseJson(json:String):List<String>
    {
        val data = Json.decodeFromString<Array<Map<String,String>>>(json)
        return data.map { it["code"]!! }
    }

    private fun getTags(code:String):String{
        val connection = Jsoup.connect("https://colorhunt.co/php/single.php")
        connection.data("single",code)
        val document = connection.post()
        val data = Json.decodeFromString<Array<Map<String,String>>>(document.body().text())
        return data[0]["tags"]!!
    }

    private fun parseCode(code:String):List<Color>
    {
        val colors = mutableListOf<Color>()
        for (i in code.indices step 6)
        {
            val hex = code.subSequence(i,i+6).toString()
            colors.add(Color.fromHex(hex))
        }
        return colors
    }
}