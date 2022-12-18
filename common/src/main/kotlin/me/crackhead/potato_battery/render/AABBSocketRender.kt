package me.crackhead.potato_battery.render

import com.mojang.blaze3d.vertex.PoseStack
import me.crackhead.potato_battery.render.outliner.AABBOutline
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.core.BlockPos
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3

open class AABBSocketRender(bb: AABB, val pos: BlockPos) : AABBOutline(bb) {
    var passiveColor = 0
    var highlightColor = 0
    var isPassive = false
    var blockState = Minecraft.getInstance().level!!.getBlockState(pos)

    // Label
    var fontScale = -1 / 64f
    var labelOffset: Vec3 = Vec3.ZERO

    open fun transform(ps: PoseStack) {

    }

    override fun render(ps: PoseStack, buffer: MultiBufferSource, pt: Float) {
        ps.pushPose()
        ps.translate(pos!!.x.toDouble(), pos!!.y.toDouble(), pos!!.z.toDouble())
        transform(ps)
        transformNormals = ps.last()
            .normal()
            .copy()
        params.colored(if (isPassive) passiveColor else highlightColor)
        super.render(ps, buffer, pt)
        val fontScale = fontScale
        ps.scale(fontScale, fontScale, fontScale)
        ps.pushPose()
        ps.popPose()
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