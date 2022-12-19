package me.crackhead.potato_battery.render

import com.mojang.blaze3d.vertex.PoseStack
import me.crackhead.potato_battery.render.util.AngleHelper.horizontalAngle
import me.crackhead.potato_battery.render.util.AngleHelper.verticalAngle
import me.crackhead.potato_battery.render.util.TransformStack
import me.crackhead.potato_battery.util.VecHelper.rotateCentered
import net.minecraft.core.Direction
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.material.Material
import net.minecraft.world.phys.Vec3
import org.apache.commons.lang3.tuple.Pair
import java.util.function.Function


abstract class SocketBoxTransform {
    protected var scale: Float = .4f

    protected abstract fun getLocalOffset(state: BlockState?): Vec3?
    protected abstract fun rotate(state: BlockState?, ms: PoseStack?)
    open fun testHit(state: BlockState?, localHit: Vec3): Boolean {
        val offset = getLocalOffset(state) ?: return false
        return localHit.distanceTo(offset) < scale / 2
    }

    fun transform(state: BlockState, ms: PoseStack) {
        val position = getLocalOffset(state) ?: return
        ms.translate(position.x, position.y, position.z)
        rotate(state, ms)
        ms.scale(scale, scale, scale)
    }

    open fun shouldRender(state: BlockState): Boolean {
        return state.material != Material.AIR && getLocalOffset(state) != null
    }

    protected fun rotateHorizontally(state: BlockState, vec: Vec3?): Vec3 {
        var yRot = 0f
        if (state.hasProperty(BlockStateProperties.FACING)) yRot =
            horizontalAngle(state.getValue(BlockStateProperties.FACING))
        if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) yRot = horizontalAngle(
            state.getValue(
                BlockStateProperties.HORIZONTAL_FACING
            )
        )
        return rotateCentered(vec!!, yRot.toDouble(), Direction.Axis.Y)
    }

    val fontScale: Float
        get() = 1 / 64f

    abstract class Dual(var isFirst: Boolean) : SocketBoxTransform() {

        override fun testHit(state: BlockState?, localHit: Vec3): Boolean {
            val offset = getLocalOffset(state) ?: return false
            return localHit.distanceTo(offset) < scale / 3.5f
        }

        companion object {
            fun makeSlots(factory: Function<Boolean?, out Dual?>): Pair<SocketBoxTransform, SocketBoxTransform> {
                return Pair.of(factory.apply(true), factory.apply(false))
            }
        }
    }

    abstract class Sided : SocketBoxTransform() {
        var side = Direction.UP
            protected set

        fun fromSide(direction: Direction): Sided {
            side = direction
            return this
        }

        override fun getLocalOffset(state: BlockState?): Vec3 {
            var location = southLocation
            location = rotateCentered(location!!, horizontalAngle(side).toDouble(), Direction.Axis.Y)
            location = rotateCentered(location, verticalAngle(side).toDouble(), Direction.Axis.X)
            return location
        }

        protected abstract val southLocation: Vec3?

        override fun rotate(state: BlockState?, ms: PoseStack?) {
            val yRot = horizontalAngle(side) + 180
            val xRot: Float = if (side == Direction.UP) 90f else if (side == Direction.DOWN) 270f else 0.toFloat()
            TransformStack.cast(ms)
                ?.rotateY(yRot.toDouble())
                ?.rotateX(xRot.toDouble())
        }

        override fun shouldRender(state: BlockState): Boolean {
            return super.shouldRender(state) && isSideActive(state, side)
        }

        override fun testHit(state: BlockState?, localHit: Vec3): Boolean {
            return isSideActive(state, side) && super.testHit(state, localHit)
        }

        protected open fun isSideActive(state: BlockState?, direction: Direction?): Boolean {
            return true
        }
    }
}