import java.nio.file.Paths

object Globals {
    val userDir: String
        get() {
            return System.getProperty("user.home")
        }

    val palettesDir: String
        get() {
            return Paths.get(userDir, ".colorPalettes").toString()
        }
}