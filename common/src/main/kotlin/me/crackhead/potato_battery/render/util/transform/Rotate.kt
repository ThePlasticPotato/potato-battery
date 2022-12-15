package me.crackhead.potato_battery.render.util.transform

import com.mojang.math.Quaternion
import com.mojang.math.Vector3f
import net.minecraft.core.Direction


interface Rotate<Self> {
    fun multiply(quaternion: Quaternion?): Self
    fun rotate(axis: Direction, radians: Float): Self {
        return if (radians == 0f) this as Self else multiply(
            axis.step()
                .rotation(radians)
        )
    }

    fun rotate(angle: Double, axis: Direction.Axis): Self {
        val vec =
            if (axis === Direction.Axis.X) Vector3f.XP else if (axis === Direction.Axis.Y) Vector3f.YP else Vector3f.ZP
        return multiply(vec, angle)
    }

    fun rotateX(angle: Double): Self {
        return multiply(Vector3f.XP, angle)
    }

    fun rotateY(angle: Double): Self {
        return multiply(Vector3f.YP, angle)
    }

    fun rotateZ(angle: Double): Self {
        return multiply(Vector3f.ZP, angle)
    }

    fun rotateXRadians(angle: Double): Self {
        return multiplyRadians(Vector3f.XP, angle)
    }

    fun rotateYRadians(angle: Double): Self {
        return multiplyRadians(Vector3f.YP, angle)
    }

    fun rotateZRadians(angle: Double): Self {
        return multiplyRadians(Vector3f.ZP, angle)
    }

    fun multiply(axis: Vector3f, angle: Double): Self {
        return if (angle == 0.0) this as Self else multiply(axis.rotationDegrees(angle.toFloat()))
    }

    fun multiplyRadians(axis: Vector3f, angle: Double): Self {
        return if (angle == 0.0) this as Self else multiply(axis.rotation(angle.toFloat()))
    }

    fun rotateToFace(facing: Direction?): Self {
        when (facing) {
            Direction.SOUTH -> multiply(Vector3f.YP.rotationDegrees(180f))
            Direction.WEST -> multiply(Vector3f.YP.rotationDegrees(90f))
            Direction.NORTH -> multiply(Vector3f.YP.rotationDegrees(0f))
            Direction.EAST -> multiply(Vector3f.YP.rotationDegrees(270f))
            Direction.UP -> multiply(Vector3f.XP.rotationDegrees(90f))
            Direction.DOWN -> multiply(Vector3f.XN.rotationDegrees(90f))
            else -> {}
        }
        return this as Self
    }
}