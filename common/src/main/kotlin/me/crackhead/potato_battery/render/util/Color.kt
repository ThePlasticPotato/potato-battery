package me.crackhead.potato_battery.render.util

import com.google.common.hash.Hashing
import com.mojang.math.Vector3f
import net.minecraft.util.Mth
import net.minecraft.world.phys.Vec3
import java.util.function.UnaryOperator
import javax.annotation.Nonnull


class Color {
    protected var mutable = true

    /**
     * Returns the RGB value representing this color
     * (Bits 24-31 are alpha, 16-23 are red, 8-15 are green, 0-7 are blue).
     * @return the RGB value of the color
     */
    var rGB = 0
        protected set

    @JvmOverloads
    constructor(r: Int, g: Int, b: Int, a: Int = 0xff) {
        rGB = a and 0xff shl 24 or
                (r and 0xff shl 16) or
                (g and 0xff shl 8) or (b and 0xff shl 0)
    }

    constructor(r: Float, g: Float, b: Float, a: Float) : this(
        (0.5 + 0xff * Mth.clamp(r, 0f, 1f)).toInt(),
        (0.5 + 0xff * Mth.clamp(g, 0f, 1f)).toInt(),
        (0.5 + 0xff * Mth.clamp(b, 0f, 1f)).toInt(),
        (0.5 + 0xff * Mth.clamp(a, 0f, 1f)).toInt()
    ) {
    }

    constructor(rgba: Int) {
        rGB = rgba
    }

    constructor(rgb: Int, hasAlpha: Boolean) {
        if (hasAlpha) {
            rGB = rgb
        } else {
            rGB = rgb or -0x1000000
        }
    }

    @JvmOverloads
    fun copy(mutable: Boolean = true): Color {
        return if (mutable) Color(rGB) else Color(rGB).setImmutable()
    }

    /**
     * Mark this color as immutable. Attempting to mutate this color in the future
     * will instead cause a copy to be created that can me modified.
     */
    fun setImmutable(): Color {
        mutable = false
        return this
    }

    /**
     * @return the red component in the range 0-255.
     * @see .getRGB
     */
    val red: Int
        get() = rGB shr 16 and 0xff

    /**
     * @return the green component in the range 0-255.
     * @see .getRGB
     */
    val green: Int
        get() = rGB shr 8 and 0xff

    /**
     * @return the blue component in the range 0-255.
     * @see .getRGB
     */
    val blue: Int
        get() = rGB shr 0 and 0xff

    /**
     * @return the alpha component in the range 0-255.
     * @see .getRGB
     */
    val alpha: Int
        get() = rGB shr 24 and 0xff

    /**
     * @return the red component in the range 0-1f.
     */
    val redAsFloat: Float
        get() = red / 255f

    /**
     * @return the green component in the range 0-1f.
     */
    val greenAsFloat: Float
        get() = green / 255f

    /**
     * @return the blue component in the range 0-1f.
     */
    val blueAsFloat: Float
        get() = blue / 255f

    /**
     * @return the alpha component in the range 0-1f.
     */
    val alphaAsFloat: Float
        get() = alpha / 255f

    fun asVector(): Vec3 {
        return Vec3(redAsFloat.toDouble(), greenAsFloat.toDouble(), blueAsFloat.toDouble())
    }

    fun asVectorF(): Vector3f {
        return Vector3f(redAsFloat, greenAsFloat, blueAsFloat)
    }

    fun setRed(r: Int): Color {
        return ensureMutable().setRedUnchecked(r)
    }

    fun setGreen(g: Int): Color {
        return ensureMutable().setGreenUnchecked(g)
    }

    fun setBlue(b: Int): Color {
        return ensureMutable().setBlueUnchecked(b)
    }

    fun setAlpha(a: Int): Color {
        return ensureMutable().setAlphaUnchecked(a)
    }

    fun setRed(r: Float): Color {
        return ensureMutable().setRedUnchecked((0xff * Mth.clamp(r, 0f, 1f)).toInt())
    }

    fun setGreen(g: Float): Color {
        return ensureMutable().setGreenUnchecked((0xff * Mth.clamp(g, 0f, 1f)).toInt())
    }

    fun setBlue(b: Float): Color {
        return ensureMutable().setBlueUnchecked((0xff * Mth.clamp(b, 0f, 1f)).toInt())
    }

    fun setAlpha(a: Float): Color {
        return ensureMutable().setAlphaUnchecked((0xff * Mth.clamp(a, 0f, 1f)).toInt())
    }

    fun scaleAlpha(factor: Float): Color {
        return ensureMutable().setAlphaUnchecked((alpha * Mth.clamp(factor, 0f, 1f)).toInt())
    }

    fun mixWith(other: Color, weight: Float): Color {
        return ensureMutable()
            .setRedUnchecked((red + (other.red - red) * weight).toInt())
            .setGreenUnchecked((green + (other.green - green) * weight).toInt())
            .setBlueUnchecked((blue + (other.blue - blue) * weight).toInt())
            .setAlphaUnchecked((alpha + (other.alpha - alpha) * weight).toInt())
    }

    fun darker(): Color {
        val a = alpha
        return ensureMutable().mixWith(BLACK, .25f).setAlphaUnchecked(a)
    }

    fun brighter(): Color {
        val a = alpha
        return ensureMutable().mixWith(WHITE, .25f).setAlphaUnchecked(a)
    }

    fun setValue(value: Int): Color {
        return ensureMutable().setValueUnchecked(value)
    }

    fun modifyValue(function: UnaryOperator<Int>): Color {
        val newValue = function.apply(rGB)
        return if (newValue == rGB) this else ensureMutable().setValueUnchecked(newValue)
    }

    // ********* //
    protected fun ensureMutable(): Color {
        return if (mutable) this else Color(rGB)
    }

    protected fun setRedUnchecked(r: Int): Color {
        rGB = rGB and -0xff0001 or (r and 0xff shl 16)
        return this
    }

    protected fun setGreenUnchecked(g: Int): Color {
        rGB = rGB and -0xff01 or (g and 0xff shl 8)
        return this
    }

    protected fun setBlueUnchecked(b: Int): Color {
        rGB = rGB and -0x100 or (b and 0xff shl 0)
        return this
    }

    protected fun setAlphaUnchecked(a: Int): Color {
        rGB = rGB and 0x00ffffff or (a and 0xff shl 24)
        return this
    }

    protected fun setValueUnchecked(value: Int): Color {
        rGB = value
        return this
    }

    companion object {
        val TRANSPARENT_BLACK = Color(0, 0, 0, 0).setImmutable()
        val BLACK = Color(0, 0, 0).setImmutable()
        val WHITE = Color(255, 255, 255).setImmutable()
        val RED = Color(255, 0, 0).setImmutable()
        val GREEN = Color(0, 255, 0).setImmutable()
        val SPRING_GREEN = Color(0, 255, 187).setImmutable()

        // ********* //
        fun mixColors(@Nonnull c1: Color, @Nonnull c2: Color, w: Float): Color {
            return Color(
                (c1.red + (c2.red - c1.red) * w).toInt(),
                (c1.green + (c2.green - c1.green) * w).toInt(),
                (c1.blue + (c2.blue - c1.blue) * w).toInt(),
                (c1.alpha + (c2.alpha - c1.alpha) * w).toInt()
            )
        }

        fun mixColors(@Nonnull colors: Pair<Color, Color>, w: Float): Color {
            return mixColors(colors.first, colors.second, w)
        }

        fun mixColors(color1: Int, color2: Int, w: Float): Int {
            val a1 = color1 shr 24
            val r1 = color1 shr 16 and 0xFF
            val g1 = color1 shr 8 and 0xFF
            val b1 = color1 and 0xFF
            val a2 = color2 shr 24
            val r2 = color2 shr 16 and 0xFF
            val g2 = color2 shr 8 and 0xFF
            val b2 = color2 and 0xFF
            return ((a1 + (a2 - a1) * w).toInt() shl 24) +
                    ((r1 + (r2 - r1) * w).toInt() shl 16) +
                    ((g1 + (g2 - g1) * w).toInt() shl 8) +
                    ((b1 + (b2 - b1) * w).toInt() shl 0)
        }

        fun rainbowColor(timeStep: Int): Color {
            val localTimeStep = Math.abs(timeStep) % 1536
            val timeStepInPhase = localTimeStep % 256
            val phaseBlue = localTimeStep / 256
            val red = colorInPhase(phaseBlue + 4, timeStepInPhase)
            val green = colorInPhase(phaseBlue + 2, timeStepInPhase)
            val blue = colorInPhase(phaseBlue, timeStepInPhase)
            return Color(red, green, blue)
        }

        private fun colorInPhase(phase: Int, progress: Int): Int {
            var phase = phase
            phase = phase % 6
            if (phase <= 1) return 0
            if (phase == 2) return progress
            return if (phase <= 4) 255 else 255 - progress
        }

        fun generateFromLong(l: Long): Color {
            return rainbowColor(Hashing.crc32().hashLong(l).asInt())
                .mixWith(WHITE, 0.5f)
        }
    }
}