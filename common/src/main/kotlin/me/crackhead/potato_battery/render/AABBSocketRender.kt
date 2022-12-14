package me.crackhead.potato_battery.render

import com.mojang.blaze3d.vertex.PoseStack
import me.crackhead.potato_battery.render.outliner.AABBOutline
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3

open class AABBSocketRender(bb: AABB) : AABBOutline(bb) {
    var passiveColor = 0
    var highlightColor = 0xFFFFFF
    val isPassive get() = !SocketRenderer.isHighlighted(this)

    // Label
    var fontScale = -1 / 64f
    var labelOffset: Vec3 = Vec3.ZERO

    init {
        params.disableCull()
        params.faceTexture = SpecialTextures.CHECKERED
    }

    open fun transform(ps: PoseStack) {

    }

    override fun render(ps: PoseStack, buffer: MultiBufferSource, pt: Float) {
        ps.pushPose()
        transform(ps)
        transformNormals = ps.last()
            .normal()
            .copy()
        params.colored(if (isPassive) passiveColor else highlightColor)
        super.render(ps, buffer, pt)
        val fontScale = fontScale
        ps.scale(fontScale, fontScale, fontScale)
        if (!isPassive) {
            ps.pushPose()
            ps.translate(17.5, -.5, 7.0)
            ps.translate(labelOffset.x, labelOffset.y, labelOffset.z)
            // TODO render label?
            ps.popPose()
        }
        ps.popPose()
    }
}