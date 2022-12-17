package me.crackhead.potato_battery.render.outliner

import com.mojang.blaze3d.vertex.PoseStack
import me.crackhead.potato_battery.render.SuperRenderTypeBuffer
import net.minecraft.util.Mth
import net.minecraft.world.phys.AABB


open class ChasingAABBOutline(bb: AABB) : AABBOutline(bb) {
    var targetBB: AABB
    var prevBB: AABB

    init {
        prevBB = bb.inflate(0.0)
        targetBB = bb.inflate(0.0)
    }

    fun target(target: AABB) {
        targetBB = target
    }

    override fun tick() {
        prevBB = bb!!
        setBounds(interpolateBBs(bb!!, targetBB, .5f))
    }

    override fun render(ms: PoseStack, buffer: SuperRenderTypeBuffer, pt: Float) {
        renderBB(ms, buffer, interpolateBBs(prevBB, bb!!, pt))
    }

    companion object {
        private fun interpolateBBs(current: AABB, target: AABB, pt: Float): AABB {
            return AABB(
                Mth.lerp(pt.toDouble(), current.minX, target.minX),
                Mth.lerp(pt.toDouble(), current.minY, target.minY), Mth.lerp(pt.toDouble(), current.minZ, target.minZ),
                Mth.lerp(pt.toDouble(), current.maxX, target.maxX), Mth.lerp(pt.toDouble(), current.maxY, target.maxY),
                Mth.lerp(pt.toDouble(), current.maxZ, target.maxZ)
            )
        }
    }
}