import javafx.application.Platform
import javafx.fxml.FXMLLoader
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.Slider
import javafx.scene.control.TextField
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.layout.FlowPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane

class MainController {
    lateinit var redSlider: Slider
    lateinit var blueSlider: Slider
    lateinit var greenSlider: Slider

    lateinit var hueSlider: Slider
    lateinit var saturationSlider: Slider
    lateinit var valueSlider: Slider

    lateinit var redLabel: Label
    lateinit var greenLabel: Label
    lateinit var blueLabel: Label

    lateinit var hueLabel: Label
    lateinit var saturationLabel: Label
    lateinit var valueLabel: Label

    lateinit var colorPane: Pane

    lateinit var complementaryHBox: HBox
    lateinit var tetradicHBox: HBox
    lateinit var triadicHBox: HBox
    lateinit var splitComplementaryHBox: HBox
    lateinit var analogousHBox: HBox
    lateinit var monochromaticHBox: HBox

    lateinit var pickButton: Button

    lateinit var rgbTextField: TextField
    lateinit var normalizedRgbTextField: TextField
    lateinit var hexTextField: TextField
    lateinit var hsvTextField: TextField

    lateinit var colorsContainer: FlowPane

    lateinit var colorList: ColorList

    private val complementaryColorViewer: ColorViewer = ColorViewer(this::onColorViewerClicked)
    private val tetradicColorViewers = ColorViewerArray(3, this::onColorViewerClicked)
    private val triadicColorViewers = ColorViewerArray(2, this::onColorViewerClicked)
    private val splitComplementaryColorViewers = ColorViewerArray(2, this::onColorViewerClicked)
    private val analogousColorViewers = ColorViewerArray(4, this::onColorViewerClicked)
    private val monochromaticColorViewers = ColorViewerArray(4, this::onColorViewerClicked)

    private var ignoreListeners = false

    private val mainColor: Color = Color.fromRGB(0, 0, 0)

    private val screenColorPicker: ScreenColorPicker = ScreenColorPicker()
    private fun onRgbSliderUpdate(r: Int? = null, g: Int? = null, b: Int? = null) {
        if (!ignoreListeners) {
            r?.run { mainColor.r = r }
            g?.run { mainColor.g = g }

            b?.run { mainColor.b = b }
            onColorUpdate(updateRgbSliders = false)
        }
    }

    private fun onHsvSliderUpdate(h: Int? = null, s: Double? = null, v: Double? = null) {
        if (!ignoreListeners) {
            h?.run { mainColor.h = h }
            s?.run { mainColor.s = s }
            v?.run { mainColor.v = v }
            onColorUpdate(updateHsvSliders = false)
        }
    }


    fun initialize() {

        colorPane.setOnMouseClicked(this::onMainColorClicked)

        redSlider.valueProperty()
            .addListener { _, _, newValue: Number -> onRgbSliderUpdate(r = newValue.toInt()) }

        greenSlider.valueProperty()
            .addListener { _, _, newValue: Number -> onRgbSliderUpdate(g = newValue.toInt()) }

        blueSlider.valueProperty()
            .addListener { _, _, newValue: Number -> onRgbSliderUpdate(b = newValue.toInt()) }

        hueSlider.valueProperty()
            .addListener { _, _, newValue: Number -> onHsvSliderUpdate(h = newValue.toInt()) }

        saturationSlider.valueProperty()
            .addListener { _, _, newValue: Number -> onHsvSliderUpdate(s = newValue.toDouble() / 100.0) }

        valueSlider.valueProperty()
            .addListener { _, _, newValue: Number -> onHsvSliderUpdate(v = newValue.toDouble() / 100.0) }

        rgbTextField.textProperty()
            .addListener { _, _, newValue ->
                if (!ignoreListeners) {
                    try {
                        mainColor.rgb = parseTriple(newValue)
                            .let {
                                Triple(
                                    it.first.toInt(),
                                    it.second.toInt(),
                                    it.third.toInt()
                                )
                            }
                        onColorUpdate(updateRgbTextField = false)
                    } catch (e: NumberFormatException) {
                    } catch (e: IndexOutOfBoundsException) {
                    }
                }
            }

        hsvTextField.textProperty()
            .addListener { _, _, newValue ->
                if (!ignoreListeners) {
                    try {
                        mainColor.hsv = parseTriple(newValue)
                            .let {
                                Triple(
                                    it.first.toInt(),
                                    it.second / 100.0,
                                    it.third / 100.0
                                )
                            }
                        onColorUpdate(updateHsvTextField = false)
                    } catch (e: NumberFormatException) {
                    } catch (e: IndexOutOfBoundsException) {
                    }
                }
            }

        hexTextField.textProperty()
            .addListener { _, _, newValue ->
                if (!ignoreListeners) {
                    try {
                        mainColor.hex = newValue.removePrefix("#")
                        onColorUpdate(updateHexTextField = false)
                    } catch (e: NumberFormatException) {
                    } catch (e: IndexOutOfBoundsException) {
                    }
                }
            }

        normalizedRgbTextField.textProperty()
            .addListener { _, _, newValue ->
                if (!ignoreListeners) {
                    try {
                        mainColor.rgbNormalized = parseTriple(newValue)
                            .let {
                                Triple(
                                    it.first,
                                    it.second,
                                    it.third
                                )
                            }
                        onColorUpdate(updateNormalizedRgbTextField = false)
                    } catch (e: NumberFormatException) {
                    } catch (e: IndexOutOfBoundsException) {
                    }
                }
            }


        pickButton.setOnMouseClicked { event ->
            if (event.button == MouseButton.PRIMARY && !screenColorPicker.isRunning) {
                screenColorPicker.start { r, g, b ->
                    mainColor.rgb = Triple(r, g, b)
                    Platform.runLater {
                        onColorUpdate()
                    }
                }
            }
        }

        val loader = FXMLLoader(javaClass.classLoader.getResource("colorviewer.fxml"))
        loader.setController(complementaryColorViewer)
        complementaryHBox.children.add(loader.load())

        tetradicColorViewers.load(tetradicHBox)
        triadicColorViewers.load(triadicHBox)
        splitComplementaryColorViewers.load(splitComplementaryHBox)
        analogousColorViewers.load(analogousHBox)
        monochromaticColorViewers.load(monochromaticHBox)

        colorList = ColorList(colorsContainer) { color ->
            mainColor.rgb = color.rgb
            onColorUpdate()
        }
        onColorUpdate()
    }


    private fun onColorUpdate(
        updateRgbSliders: Boolean = true,
        updateHsvSliders: Boolean = true,
        updateRgbTextField: Boolean = true,
        updateHsvTextField: Boolean = true,
        updateNormalizedRgbTextField: Boolean = true,
        updateHexTextField: Boolean = true
    ) {
        colorPane.style = "-fx-background-color: #${mainColor.hex};"
        redLabel.text = "${mainColor.r}"
        greenLabel.text = "${mainColor.g}"
        blueLabel.text = "${mainColor.b}"

        hueLabel.text = "${mainColor.h}"
        saturationLabel.text = "${(mainColor.s * 100).toInt()}"
        valueLabel.text = "${(mainColor.v * 100).toInt()}"

        complementaryColorViewer.color = complementary(mainColor)
        tetradicColorViewers.colors = tetradic(mainColor)
        triadicColorViewers.colors = triadic(mainColor)
        analogousColorViewers.colors = analogous(mainColor)
        monochromaticColorViewers.colors = monochromatic(mainColor)
        splitComplementaryColorViewers.colors = splitComplementary(mainColor)

        ignoreListeners = true
        if (updateRgbSliders) {
            redSlider.value = mainColor.r.toDouble()
            greenSlider.value = mainColor.g.toDouble()
            blueSlider.value = mainColor.b.toDouble()
        }

        if (updateHsvSliders) {
            hueSlider.value = mainColor.h.toDouble()
            saturationSlider.value = mainColor.s * 100
            valueSlider.value = mainColor.v * 100
        }

        if (updateRgbTextField) {
            rgbTextField.text = mainColor.rgb.toString()
        }

        if (updateNormalizedRgbTextField) {
            normalizedRgbTextField.text = mainColor.rgbNormalized.let {
                Triple(
                    it.first.roundTo(2),
                    it.second.roundTo(2),
                    it.third.roundTo(2)
                )
            }.toString()
        }

        if (updateHexTextField) {
            hexTextField.text = "#${mainColor.hex}"
        }

        if (updateHsvTextField) {
            hsvTextField.text = mainColor.hsv.let {
                Triple(
                    it.first,
                    (it.second * 100).toInt(),
                    (it.third * 100).toInt()
                )
            }.toString()
        }

        ignoreListeners = false
    }

    private fun onMainColorClicked(event: MouseEvent?) {
        event?.let {
            if (it.button == MouseButton.PRIMARY) {
                addColor(mainColor)
            }
        }
    }

    private fun onColorViewerClicked(event: MouseEvent?, color: Color) {
        event?.let {
            if (it.button == MouseButton.SECONDARY) {
                mainColor.rgb = color.rgb
                onColorUpdate()
            } else if (it.button == MouseButton.PRIMARY) {
                addColor(color)
            }
        }
    }

    private fun addColor(color: Color) {
        colorList.add(color.clone())
    }

    private fun parseTriple(s: String): Triple<Double, Double, Double> {

        return s.replace(" ", "")
            .removePrefix("(")
            .removeSuffix(")")
            .split(",")
            .let {
                Triple(
                    it[0].toDouble(),
                    it[1].toDouble(),
                    it[2].toDouble()
                )
            }
    }
}