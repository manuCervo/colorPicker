class Color private constructor() {
    companion object {
        fun fromRGB(r: Int, g: Int, b: Int): Color {
            return Color().apply {
                this.r = r
                this.g = g
                this.b = b
            }
        }

        fun fromHSV(h: Int, s: Double, v: Double): Color {
            return Color().apply {
                this.h = h
                this.s = s
                this.v = v
            }
        }

        fun fromHex(hex: String): Color {
            return Color().apply {
                this.hex = hex
            }
        }
    }

    private var red = 0
    var r: Int
        set(value) {
            red = value
            updateFromRGB()
        }
        get() {
            return red
        }

    private var green = 0
    var g: Int
        set(value) {
            green = value
            updateFromRGB()
        }
        get() = green

    private var blue = 0
    var b: Int
        set(value) {
            blue = value
            updateFromRGB()
        }
        get() = blue

    private var hue = 0
    var h: Int
        set(value) {
            hue = value
            updateFromHSV()

        }
        get() = hue

    private var saturation: Double = 0.0
    var s: Double
        set(value) {
            saturation = value
            updateFromHSV()
        }
        get() = saturation

    private var value: Double = 0.0
    var v: Double
        set(value) {
            this.value = value
            updateFromHSV()
        }
        get() = this.value

    var rgb: Triple<Int, Int, Int>
        get() {
            return Triple(red, green, blue)
        }
        set(value) {
            r = value.first
            g = value.second
            b = value.third
        }

    var hsv: Triple<Int, Double, Double>
        get() {
            return Triple(hue, saturation, value)
        }
        set(value) {
            h = value.first
            s = value.second
            v = value.third
        }

    var hex: String
        get() {
            fun addZero(h: String): String {
                return if (h.length == 1) {
                    "0$h"
                } else {
                    h
                }
            }
            return addZero(Integer.toHexString(red)) +
                    addZero(Integer.toHexString(green)) +
                    addZero(Integer.toHexString(blue))
        }
        set(value) {
            r = Integer.parseInt(value.subSequence(0, 2).toString(), 16)
            g = Integer.parseInt(value.subSequence(2, 4).toString(), 16)
            b = Integer.parseInt(value.subSequence(4, 6).toString(), 16)
        }

    var rgbNormalized: Triple<Double, Double, Double>
        get() {
            return Triple(red.toDouble() / 255.0, green.toDouble() / 255.0, blue.toDouble() / 255.0)
        }
        set(value) {
            r = (value.first * 255).toInt()
            g = (value.second * 255).toInt()
            b = (value.third * 255).toInt()
        }

    private fun updateFromRGB() {
        val hsv: Triple<Int, Double, Double> = rgbToHsv(r, g, b)
        hue = hsv.first
        saturation = hsv.second
        this.value = hsv.third
    }

    private fun updateFromHSV() {
        val rgb: Triple<Int, Int, Int> = hsvToRgb(h, s, this.v)
        red = rgb.first
        green = rgb.second
        blue = rgb.third
    }

    fun clone(): Color {
        return Color().apply {
            this.red = this@Color.red
            this.green = this@Color.green
            this.blue = this@Color.blue
            this.hue = this@Color.hue
            this.saturation = this@Color.saturation
            this.value = this@Color.value
        }
    }
}