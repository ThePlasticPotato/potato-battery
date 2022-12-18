package me.crackhead.potato_battery.render.outliner

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.math.Matrix3f
import me.crackhead.potato_battery.render.SpecialTextures
import me.crackhead.potato_battery.render.SuperRenderTypeBuffer
import me.crackhead.potato_battery.render.util.AngleHelper
import me.crackhead.potato_battery.render.util.Color
import me.crackhead.potato_battery.render.util.TransformStack
import me.crackhead.potato_battery.render.util.VecHelper
import net.minecraft.client.renderer.LightTexture
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.core.Direction
import net.minecraft.util.Mth
import net.minecraft.world.phys.Vec3
import java.util.*


abstract class Outline {
    var params: OutlineParams
        protected set
    protected var transformNormals
            : Matrix3f? = null

    init {
        params = OutlineParams()
    }

    abstract fun render(ms: PoseStack, buffer: SuperRenderTypeBuffer, pt: Float)
    open fun tick() {}
    fun renderCuboidLine(ms: PoseStack, buffer: SuperRenderTypeBuffer, start: Vec3?, end: Vec3) {
        val diff = end.subtract(start)
        val hAngle: Float = AngleHelper.deg(Mth.atan2(diff.x, diff.z))
        val hDistance = diff.multiply(1.0, 0.0, 1.0)
            .length().toFloat()
        val vAngle: Float = AngleHelper.deg(Mth.atan2(hDistance.toDouble(), diff.y)) - 90
        ms.pushPose()
        if (start != null) {
            TransformStack.cast(ms)
                ?.translate(start)
                ?.rotateY(hAngle.toDouble())?.rotateX(vAngle.toDouble())
        }
        renderAACuboidLine(ms, buffer, Vec3.ZERO, Vec3(0.0, 0.0, diff.length()))
        ms.popPose()
    }

    fun renderAACuboidLine(ms: PoseStack, buffer: SuperRenderTypeBuffer, start: Vec3, end: Vec3) {
        var start = start
        var end = end
        val lineWidth = params.getLineWidth()
        if (lineWidth == 0f) return
        val builder: VertexConsumer = buffer.getBuffer(RenderType.solid()) //replace with custom render type later
        var diff = end.subtract(start)
        if (diff.x + diff.y + diff.z < 0) {
            val temp = start
            start = end
            end = temp
            diff = diff.scale(-1.0)
        }
        val extension = diff.normalize()
            .scale((lineWidth / 2).toDouble())
        var plane: Vec3 = VecHelper.axisAlingedPlaneOf(diff)
        var face = Direction.getNearest(diff.x, diff.y, diff.z)
        val axis = face.axis
        start = start.subtract(extension)
        end = end.add(extension)
        plane = plane.scale((lineWidth / 2).toDouble())
        val a1 = plane.add(start)
        val b1 = plane.add(end)
        plane = VecHelper.rotate(plane, -90.0, axis)
        val a2 = plane.add(start)
        val b2 = plane.add(end)
        plane = VecHelper.rotate(plane, -90.0, axis)
        val a3 = plane.add(start)
        val b3 = plane.add(end)
        plane = VecHelper.rotate(plane, -90.0, axis)
        val a4 = plane.add(start)
        val b4 = plane.add(end)
        if (params.disableNormals) {
            face = Direction.UP
            putQuad(ms, builder, b4, b3, b2, b1, face)
            putQuad(ms, builder, a1, a2, a3, a4, face)
            putQuad(ms, builder, a1, b1, b2, a2, face)
            putQuad(ms, builder, a2, b2, b3, a3, face)
            putQuad(ms, builder, a3, b3, b4, a4, face)
            putQuad(ms, builder, a4, b4, b1, a1, face)
            return
        }
        putQuad(ms, builder, b4, b3, b2, b1, face)
        putQuad(ms, builder, a1, a2, a3, a4, face.opposite)
        var vec = a1.subtract(a4)
        face = Direction.getNearest(vec.x, vec.y, vec.z)
        putQuad(ms, builder, a1, b1, b2, a2, face)
        vec = VecHelper.rotate(vec, -90.0, axis)
        face = Direction.getNearest(vec.x, vec.y, vec.z)
        putQuad(ms, builder, a2, b2, b3, a3, face)
        vec = VecHelper.rotate(vec, -90.0, axis)
        face = Direction.getNearest(vec.x, vec.y, vec.z)
        putQuad(ms, builder, a3, b3, b4, a4, face)
        vec = VecHelper.rotate(vec, -90.0, axis)
        face = Direction.getNearest(vec.x, vec.y, vec.z)
        putQuad(ms, builder, a4, b4, b1, a1, face)
    }

    fun putQuad(
        ms: PoseStack, builder: VertexConsumer, v1: Vec3, v2: Vec3, v3: Vec3, v4: Vec3,
        normal: Direction?
    ) {
        putQuadUV(ms, builder, v1, v2, v3, v4, 0f, 0f, 1f, 1f, normal)
    }

    fun putQuadUV(
        ms: PoseStack, builder: VertexConsumer, v1: Vec3, v2: Vec3, v3: Vec3, v4: Vec3, minU: Float,
        minV: Float, maxU: Float, maxV: Float, normal: Direction?
    ) {
        putVertex(ms, builder, v1, minU, minV, normal)
        putVertex(ms, builder, v2, maxU, minV, normal)
        putVertex(ms, builder, v3, maxU, maxV, normal)
        putVertex(ms, builder, v4, minU, maxV, normal)
    }

    protected fun putVertex(ms: PoseStack, builder: VertexConsumer, pos: Vec3, u: Float, v: Float, normal: Direction?) {
        putVertex(ms.last(), builder, pos.x.toFloat(), pos.y.toFloat(), pos.z.toFloat(), u, v, normal)
    }

    protected fun putVertex(
        pose: PoseStack.Pose,
        builder: VertexConsumer,
        x: Float,
        y: Float,
        z: Float,
        u: Float,
        v: Float,
        normal: Direction?
    ) {
        val rgb: Color = params.rgb
        if (transformNormals == null) transformNormals = pose.normal()
        var xOffset = 0
        var yOffset = 0
        var zOffset = 0
        if (normal != null) {
            xOffset = normal.stepX
            yOffset = normal.stepY
            zOffset = normal.stepZ
        }
        builder.vertex(pose.pose(), x, y, z)
            .color(
                rgb.redAsFloat,
                rgb.greenAsFloat,
                rgb.blueAsFloat,
                rgb.alphaAsFloat * params.alpha
            )
            .uv(u, v)
            .overlayCoords(OverlayTexture.NO_OVERLAY)
            .uv2(params.lightMap)
            .normal(pose.normal(), xOffset.toFloat(), yOffset.toFloat(), zOffset.toFloat())
            .endVertex()
        transformNormals = null
    }

    class OutlineParams {
        var faceTexture: Optional<SpecialTextures>
        var hightlightedFaceTexture: Optional<SpecialTextures>
        var highlightedFace: Direction? = null
            protected set
        protected var fadeLineWidth: Boolean
        var disableCull = false
        var disableNormals = false
        var alpha: Float
        var lightMap: Int
        var rgb: Color
        private var lineWidth: Float

        init {
            hightlightedFaceTexture = Optional.empty<SpecialTextures>()
            faceTexture = hightlightedFaceTexture
            alpha = 1f
            lineWidth = 1 / 32f
            fadeLineWidth = true
            rgb = Color.WHITE
            lightMap = LightTexture.FULL_BRIGHT
        }

        // builder
        fun colored(color: Int): OutlineParams {
            rgb = Color(color, false)
            return this
        }

        fun colored(c: Color): OutlineParams {
            rgb = c.copy()
            return this
        }

        fun lightMap(light: Int): OutlineParams {
            lightMap = light
            return this
        }

        fun lineWidth(width: Float): OutlineParams {
            lineWidth = width
            return this
        }

        fun withFaceTexture(texture: SpecialTextures?): OutlineParams {
            faceTexture = Optional.ofNullable<SpecialTextures>(texture)
            return this
        }

        fun clearTextures(): OutlineParams {
            return withFaceTextures(null, null)
        }

        fun withFaceTextures(texture: SpecialTextures?, highlightTexture: SpecialTextures?): OutlineParams {
            faceTexture = Optional.ofNullable<SpecialTextures>(texture)
            hightlightedFaceTexture = Optional.ofNullable<SpecialTextures>(highlightTexture)
            return this
        }

        fun highlightFace(face: Direction?): OutlineParams {
            highlightedFace = face
            return this
        }

        fun disableNormals(): OutlineParams {
            disableNormals = true
            return this
        }

        fun disableCull(): OutlineParams {
            disableCull = true
            return this
        }

        // getter
        fun getLineWidth(): Float {
            return if (fadeLineWidth) alpha * lineWidth else lineWidth
        }
    }
}