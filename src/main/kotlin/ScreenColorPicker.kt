import org.jnativehook.GlobalScreen
import org.jnativehook.mouse.NativeMouseEvent
import org.jnativehook.mouse.NativeMouseInputListener
import java.awt.MouseInfo
import java.awt.Robot
import java.util.logging.Level
import java.util.logging.Logger

class ScreenColorPicker {
    private var stopped = true
    val isRunning
        get() = !stopped

    init {
        val logger: Logger = Logger.getLogger(GlobalScreen::class.java.`package`.name)
        logger.level = Level.WARNING
        logger.useParentHandlers = false
        GlobalScreen.registerNativeHook()
    }

    private fun getColor(): Triple<Int, Int, Int> {
        val (x: Int, y: Int) = MouseInfo.getPointerInfo().location.let { Pair(it.x, it.y) }
        val robot: Robot = Robot()
        return robot.getPixelColor(x, y).let {
            Triple(it.red, it.green, it.blue)
        }
    }

    fun start(onUpdate: (Int, Int, Int) -> Unit) {
        GlobalScreen.addNativeMouseListener(mouseListener)
        stopped = false
        Thread {
            while (!stopped) {
                val (r: Int, g: Int, b: Int) = getColor()
                onUpdate(r, g, b)
                Thread.sleep(10)
            }
        }.start()
    }

    fun stop() {
        stopped = true
        GlobalScreen.removeNativeMouseListener(mouseListener)
    }

    private val mouseListener = object : NativeMouseInputListener {
        override fun nativeMouseClicked(p0: NativeMouseEvent?) {
            stop()
        }

        override fun nativeMousePressed(p0: NativeMouseEvent?) {}

        override fun nativeMouseMoved(p0: NativeMouseEvent?) {}

        override fun nativeMouseReleased(p0: NativeMouseEvent?) {}

        override fun nativeMouseDragged(p0: NativeMouseEvent?) {}

    }
}