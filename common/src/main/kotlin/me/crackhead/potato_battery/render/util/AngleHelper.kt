package me.crackhead.potato_battery.render.util

import net.minecraft.core.Direction
import net.minecraft.util.Mth


object AngleHelper {
    fun horizontalAngle(facing: Direction): Float {
        if (facing.axis.isVertical) return 0f
        var angle = facing.toYRot()
        if (facing.axis === Direction.Axis.X) angle = -angle
        return angle
    }

    fun verticalAngle(facing: Direction): Float {
        return if (facing == Direction.UP) -90f else if (facing == Direction.DOWN) 90f else 0f
    }

    fun rad(angle: Double): Float {
        return if (angle == 0.0) 0f else (angle / 180 * Math.PI).toFloat()
    }

    fun deg(angle: Double): Float {
        return if (angle == 0.0) 0f else (angle * 180 / Math.PI).toFloat()
    }

    fun angleLerp(pct: Double, current: Double, target: Double): Float {
        return (current + getShortestAngleDiff(current, target) * pct).toFloat()
    }

    fun getShortestAngleDiff(current: Double, target: Double): Float {
        var current = current
        var target = target
        current = current % 360
        target = target % 360
        return (((target - current) % 360 + 540) % 360 - 180).toFloat()
    }

    fun getShortestAngleDiff(current: Double, target: Double, hint: Float): Float {
        val diff = getShortestAngleDiff(current, target)
        return if (Mth.equal(Math.abs(diff), 180f) && Math.signum(diff) != Math.signum(hint)) {
            diff + 360 * Math.signum(hint)
        } else diff
    }
}