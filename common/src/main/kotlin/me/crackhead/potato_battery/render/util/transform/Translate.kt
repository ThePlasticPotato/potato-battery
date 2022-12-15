package me.crackhead.potato_battery.render.util.transform

import com.mojang.math.Vector3f
import net.minecraft.core.Vec3i
import net.minecraft.world.phys.Vec3


interface Translate<Self> {
    fun translate(x: Double, y: Double, z: Double): Self
    fun centre(): Self {
        return translateAll(0.5)
    }

    fun unCentre(): Self {
        return translateAll(-0.5)
    }

    fun translateAll(v: Double): Self {
        return translate(v, v, v)
    }

    fun translateX(x: Double): Self {
        return translate(x, 0.0, 0.0)
    }

    fun translateY(y: Double): Self {
        return translate(0.0, y, 0.0)
    }

    fun translateZ(z: Double): Self {
        return translate(0.0, 0.0, z)
    }

    fun translate(vec: Vec3i): Self {
        return translate(vec.x.toDouble(), vec.y.toDouble(), vec.z.toDouble())
    }

    fun translate(vec: Vec3): Self {
        return translate(vec.x, vec.y, vec.z)
    }

    fun translate(vec: Vector3f): Self {
        return translate(vec.x().toDouble(), vec.y().toDouble(), vec.z().toDouble())
    }

    fun translateBack(vec: Vec3): Self {
        return translate(-vec.x, -vec.y, -vec.z)
    }

    fun translateBack(x: Double, y: Double, z: Double): Self {
        return translate(-x, -y, -z)
    }

    fun translateBack(vec: Vec3i): Self {
        return translate(-vec.x.toDouble(), -vec.y.toDouble(), -vec.z.toDouble())
    }

    /**
     * Translates this object randomly by a very small amount.
     * @param seed The seed to use to generate the random offsets.
     * @return `this`
     */
    fun nudge(seed: Int): Self {
        var randomBits = seed.toLong() * 31L * 493286711L
        randomBits = randomBits * randomBits * 4392167121L + randomBits * 98761L
        val xNudge = (((randomBits shr 16 and 7L).toFloat() + 0.5f) / 8.0f - 0.5f) * 0.004f
        val yNudge = (((randomBits shr 20 and 7L).toFloat() + 0.5f) / 8.0f - 0.5f) * 0.004f
        val zNudge = (((randomBits shr 24 and 7L).toFloat() + 0.5f) / 8.0f - 0.5f) * 0.004f
        return translate(xNudge.toDouble(), yNudge.toDouble(), zNudge.toDouble())
    }
}