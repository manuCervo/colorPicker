import kotlin.math.abs

fun complementary(color: Color): Color {
    return Color.fromRGB(255 - color.r, 255 - color.g, 255 - color.b)
}

fun triadic(color: Color): Array<Color> {
    return arrayOf(
        Color.fromHSV(wrapHue(color.h + 120), color.s, color.v),
        Color.fromHSV(wrapHue(color.h - 120), color.s, color.v)
    )
}

fun tetradic(color: Color): Array<Color> {
    return arrayOf(
        Color.fromHSV(wrapHue(color.h + 30), color.s, color.v),
        Color.fromHSV(wrapHue(color.h + 210), color.s, color.v),
        Color.fromHSV(wrapHue(color.h - 180), color.s, color.v)
    )
}

fun splitComplementary(color: Color): Array<Color> {
    return arrayOf(
        Color.fromHSV(wrapHue(color.h + 150), color.s, color.v),
        Color.fromHSV(wrapHue(color.h - 150), color.s, color.v)
    )
}

fun analogous(color: Color): Array<Color> {
    return arrayOf(
        Color.fromHSV(wrapHue(color.h - 60), color.s, color.v),
        Color.fromHSV(wrapHue(color.h - 30), color.s, color.v),
        Color.fromHSV(wrapHue(color.h + 30), color.s, color.v),
        Color.fromHSV(wrapHue(color.h + 60), color.s, color.v)
    )
}

fun monochromatic(color: Color): Array<Color> {
    return arrayOf(
        Color.fromHSV(color.h, color.s, wrapValue(color.v - 0.2)),
        Color.fromHSV(color.h, color.s, wrapValue(color.v - 0.4)),
        Color.fromHSV(color.h, color.s, wrapValue(color.v - 0.6)),
        Color.fromHSV(color.h, color.s, wrapValue(color.v - 0.8))
    )
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

fun rgbToHsv(r: Int, g: Int, b: Int): Triple<Int, Double, Double> {
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

fun hsvToRgb(h: Int, s: Double, v: Double): Triple<Int, Int, Int> {
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