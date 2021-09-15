import kotlin.math.abs
import kotlin.math.pow

class Color {
    companion object {
        fun fromRGB(r: Int, g: Int, b: Int): Color {
            val c = Color()
            c.r = r
            c.g = g
            c.b = b
            return c
        }

        fun fromHSV(h: Int, s: Double, v: Double): Color {
            val c = Color()
            c.h = h
            c.s = s
            c.v = v
            return c
        }

        fun fromHex(hex:String): Color {
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

    val relativeLuminance: Double
        get() {
            val (r, g, b) = rgbNormalized.run {
                Triple(
                    if (first <= 0.03928) {
                        first / 12.92
                    } else {
                        ((first + 0.055) / 1.055).pow(2.4)
                    },
                    if (second <= 0.03928) {
                        second / 12.92
                    } else {
                        ((second + 0.055) / 1.055).pow(2.4)
                    },
                    if (third <= 0.03928) {
                        third / 12.92
                    } else {
                        ((third + 0.055) / 1.055).pow(2.4)
                    }
                )
            }
            return 0.2126 * r + 0.7152 * g + 0.0722 * b
        }

    var hex: String
        get() {
            return Integer.toHexString(red) + Integer.toHexString(green) + Integer.toHexString(blue)
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

    val complementary: Color
        get() {
            return fromRGB(255 - red, 255 - green, 255 - blue)
        }

    val triadic: Array<Color>
        get() {
            return arrayOf(
                fromHSV(wrapHue(hue + 120), saturation, value),
                fromHSV(wrapHue(hue - 120), saturation, value)
            )
        }

    val tetradic: Array<Color>
        get() {
            return arrayOf(
                fromHSV(wrapHue(hue + 30), saturation, value),
                fromHSV(wrapHue(hue + 210), saturation, value),
                fromHSV(wrapHue(hue - 180), saturation, value)
            )
        }

    val splitComplementary: Array<Color>
        get() {
            return arrayOf(
                fromHSV(wrapHue(hue + 150), saturation, value),
                fromHSV(wrapHue(hue - 150), saturation, value)
            )
        }

    val analogous: Array<Color>
        get() {
            return arrayOf(
                fromHSV(wrapHue(hue - 60), saturation, value),
                fromHSV(wrapHue(hue - 30), saturation, value),
                fromHSV(wrapHue(hue + 30), saturation, value),
                fromHSV(wrapHue(hue + 60), saturation, value)
            )
        }

    val monochromatic: Array<Color>
        get() {
            return arrayOf(
                fromHSV(hue, saturation, wrapValue(value - 0.2)),
                fromHSV(hue, saturation, wrapValue(value - 0.4)),
                fromHSV(hue, saturation, wrapValue(value - 0.6)),
                fromHSV(hue, saturation, wrapValue(value - 0.8))
            )
        }

    private fun rgbToHsv(r: Int, g: Int, b: Int): Triple<Int, Double, Double> {
        val r1: Double = r.toDouble() / 255.0
        val g1: Double = g.toDouble() / 255.0
        val b1: Double = b.toDouble() / 255.0

        val cMax: Double = maxOf(r1, g1, b1)
        val cMin: Double = minOf(r1, g1, b1)

        val delta: Double = cMax - cMin

        var h: Double = 0.0
        if (delta != 0.0) {
            when (cMax) {
                r1 -> {
                    h = 60 * (((g1 - b1) / delta) % 6)
                }
                g1 -> {
                    h = 60 * (((b1 - r1) / delta) + 2)
                }
                b1 -> {
                    h = 60 * (((r1 - g1) / delta) + 4)
                }
            }
        }

        h = wrapHue(h)

        val s: Double
        s = if (cMax == 0.0) {
            0.0
        } else {
            delta / cMax
        }

        val v = cMax

        return Triple(h.toInt(), s, v)
    }


    private fun hsvToRgb(h: Int, s: Double, v: Double): Triple<Int, Int, Int> {
        val c: Double = v * s
        val x: Double = c * (1.0 - abs(((h / 60.0) % 2) - 1.0))
        val m: Double = v - c

        val r1: Double
        val g1: Double
        val b1: Double
        when (h) {
            in 0..59 -> {
                r1 = c
                g1 = x
                b1 = 0.0
            }
            in 60..119 -> {
                r1 = x
                g1 = c
                b1 = 0.0
            }
            in 120..179 -> {
                r1 = 0.0
                g1 = c
                b1 = x
            }
            in 180..239 -> {
                r1 = 0.0
                g1 = x
                b1 = c
            }
            in 240..299 -> {
                r1 = x
                g1 = 0.0
                b1 = c
            }
            in 300..359 -> {
                r1 = c
                g1 = 0.0
                b1 = x
            }
            else -> {
                r1 = 0.0
                g1 = 0.0
                b1 = 0.0
            }
        }
        return Triple(
            ((r1 + m) * 255).toInt(),
            ((g1 + m) * 255).toInt(),
            ((b1 + m) * 255).toInt()
        )
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

    private fun wrapHue(hue: Double): Double {
        return when {
            hue >= 360 -> hue - 360.0
            hue < 0 -> hue + 360.0
            else -> hue
        }
    }

    private fun wrapHue(hue: Int): Int {
        return when {
            hue >= 360 -> hue - 360
            hue < 0 -> hue + 360
            else -> hue
        }
    }

    private fun wrapValue(value: Double): Double {
        return when {
            value > 1.0 -> value - 1.0
            value < 0 -> value + 1.0
            else -> value
        }
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