import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage

class App : Application()
{
    override fun start(primaryStage: Stage?) {
        val loader = FXMLLoader(App::class.java.classLoader.getResource("main.fxml"))
        val scene = Scene(loader.load())
        primaryStage!!.scene = scene
        primaryStage.show()
    }

}

fun main(args:Array<String>)
{
    Application.launch(App::class.java,*args)
}
