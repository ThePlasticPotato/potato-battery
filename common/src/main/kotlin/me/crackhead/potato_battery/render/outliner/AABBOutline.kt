package me.crackhead.potato_battery.render.outliner

import com.mojang.blaze3d.vertex.PoseStack
import me.crackhead.potato_battery.render.RenderTypes
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.core.Direction
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3


open class AABBOutline(val bb: AABB) : Outline() {

    override fun render(ms: PoseStack, buffer: MultiBufferSource, pt: Float) {
        renderBB(ms, buffer, bb)
    }

    fun renderBB(ps: PoseStack, buffer: MultiBufferSource, bb: AABB) {
        var bb = bb
        val projectedView = Minecraft.getInstance().gameRenderer.mainCamera.position
        var noCull = bb.contains(projectedView)
        bb = bb.inflate(if (noCull) -1 / 128.0 else 1 / 128.0)
        noCull = noCull or params.disableCull
        val xyz = Vec3(bb.minX, bb.minY, bb.minZ)
        val Xyz = Vec3(bb.maxX, bb.minY, bb.minZ)
        val xYz = Vec3(bb.minX, bb.maxY, bb.minZ)
        val XYz = Vec3(bb.maxX, bb.maxY, bb.minZ)
        val xyZ = Vec3(bb.minX, bb.minY, bb.maxZ)
        val XyZ = Vec3(bb.maxX, bb.minY, bb.maxZ)
        val xYZ = Vec3(bb.minX, bb.maxY, bb.maxZ)
        val XYZ = Vec3(bb.maxX, bb.maxY, bb.maxZ)
        var start = xyz
        renderAACuboidLine(ps, buffer, start, Xyz)
        renderAACuboidLine(ps, buffer, start, xYz)
        renderAACuboidLine(ps, buffer, start, xyZ)
        start = XyZ
        renderAACuboidLine(ps, buffer, start, xyZ)
        renderAACuboidLine(ps, buffer, start, XYZ)
        renderAACuboidLine(ps, buffer, start, Xyz)
        start = XYz
        renderAACuboidLine(ps, buffer, start, xYz)
        renderAACuboidLine(ps, buffer, start, Xyz)
        renderAACuboidLine(ps, buffer, start, XYZ)
        start = xYZ
        renderAACuboidLine(ps, buffer, start, XYZ)
        renderAACuboidLine(ps, buffer, start, xyZ)
        renderAACuboidLine(ps, buffer, start, xYz)
        renderFace(ps, buffer, Direction.NORTH, xYz, XYz, Xyz, xyz, noCull)
        renderFace(ps, buffer, Direction.SOUTH, XYZ, xYZ, xyZ, XyZ, noCull)
        renderFace(ps, buffer, Direction.EAST, XYz, XYZ, XyZ, Xyz, noCull)
        renderFace(ps, buffer, Direction.WEST, xYZ, xYz, xyz, xyZ, noCull)
        renderFace(ps, buffer, Direction.UP, xYZ, XYZ, XYz, xYz, noCull)
        renderFace(ps, buffer, Direction.DOWN, xyz, Xyz, XyZ, xyZ, noCull)
    }

    protected fun renderFace(
        ms: PoseStack, buffer: MultiBufferSource, direction: Direction,
        p1: Vec3, p2: Vec3, p3: Vec3, p4: Vec3,
        noCull: Boolean
    ) {
        if (params.faceTexture == null) return
        val faceTexture = params.faceTexture!!.location
        val alphaBefore = params.alpha

        params.alpha = if (direction == params.highlightedFace && params.hightlightedFaceTexture != null) 1f else 0.5f

        val builder = buffer.getBuffer(RenderTypes.solidOutline) //RenderTypes.getOutlineTranslucent(faceTexture, !noCull)

        val axis = direction.axis
        val uDiff = p2.subtract(p1)
        val vDiff = p4.subtract(p1)
        val maxU = Math.abs(if (axis === Direction.Axis.X) uDiff.z else uDiff.x).toFloat()
        val maxV = Math.abs(if (axis === Direction.Axis.Y) vDiff.z else vDiff.y).toFloat()

        putQuadUV(ms, builder, p1, p2, p3, p4, 0f, 0f, maxU, maxV, Direction.UP)
        params.alpha = alphaBefore
    }
}