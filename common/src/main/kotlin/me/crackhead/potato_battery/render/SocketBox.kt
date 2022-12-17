package me.crackhead.potato_battery.render

import com.mojang.blaze3d.vertex.PoseStack
import me.crackhead.potato_battery.render.outliner.ChasingAABBOutline
import net.minecraft.client.Minecraft
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import me.crackhead.potato_battery.render.SocketBoxTransform.Sided
class SocketBox(bb: AABB) : ChasingAABBOutline(bb) {

    protected var labelOffset: Vec3 = Vec3.ZERO
    protected var passiveColor = 0
    protected var highlightColor = 0
    var isPassive = false

    protected var pos: BlockPos? = null
    protected var transform: SocketBoxTransform? = null
    protected var blockState: BlockState? = null

    fun SocketBox(bb: AABB, pos: BlockPos) {
        this.bb = bb
        this.pos = pos
        blockState = Minecraft.getInstance().level?.getBlockState(pos)
    }

    fun transform(transform: SocketBoxTransform?): SocketBox {
        this.transform = transform
        return this
    }

    fun withColors(passive: Int, highlight: Int): SocketBox {
        passiveColor = passive
        highlightColor = highlight
        return this
    }

    fun passive(passive: Boolean): SocketBox {
        isPassive = passive
        return this
    }

    override fun render(ms: PoseStack, buffer: SuperRenderTypeBuffer, pt: Float) {
        val hasTransform = transform != null
        if (transform is Sided && params.highlightedFace != null) (transform as Sided).fromSide(
            params.highlightedFace!!
        )
        if (hasTransform && !transform!!.shouldRender(blockState!!)) return
        ms.pushPose()
        ms.translate(pos!!.x.toDouble(), pos!!.y.toDouble(), pos!!.z.toDouble())
        if (hasTransform) transform!!.transform(blockState, ms)
        transformNormals = ms.last()
            .normal()
            .copy()
        params.colored(if (isPassive) passiveColor else highlightColor)
        super.render(ms, buffer, pt)
        val fontScale = if (hasTransform) -transform!!.fontScale else -1 / 64f
        ms.scale(fontScale, fontScale, fontScale)
        ms.pushPose()
        ms.popPose()
        if (!isPassive) {
            ms.pushPose()
            ms.translate(17.5, -.5, 7.0)
            ms.translate(labelOffset.x, labelOffset.y, labelOffset.z)

            ms.popPose()
        }
        ms.popPose()
    }


}