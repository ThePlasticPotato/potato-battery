package me.crackhead.potato_battery.render.util

import com.mojang.math.Quaternion
import com.mojang.math.Vector3f
import me.crackhead.potato_battery.mixin.accessors.GameRendererAccessor
import net.minecraft.client.Minecraft
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.Vec3i
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.DoubleTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.Tag
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.util.Mth
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.block.Mirror
import net.minecraft.world.phys.Vec3
import java.util.*


object VecHelper {
    val CENTER_OF_ORIGIN = Vec3(.5, .5, .5)
    fun rotate(vec: Vec3, rotationVec: Vec3): Vec3 {
        return rotate(vec, rotationVec.x, rotationVec.y, rotationVec.z)
    }

    fun rotate(vec: Vec3, xRot: Double, yRot: Double, zRot: Double): Vec3 {
        return rotate(rotate(rotate(vec, xRot, Direction.Axis.X), yRot, Direction.Axis.Y), zRot, Direction.Axis.Z)
    }

    fun rotateCentered(vec: Vec3, deg: Double, axis: Direction.Axis): Vec3 {
        val shift = getCenterOf(BlockPos.ZERO)
        return rotate(vec.subtract(shift), deg, axis)
            .add(shift)
    }

    fun rotate(vec: Vec3, deg: Double, axis: Direction.Axis): Vec3 {
        if (deg == 0.0) return vec
        if (vec === Vec3.ZERO) return vec
        val angle = (deg / 180f * Math.PI).toFloat()
        val sin = Mth.sin(angle).toDouble()
        val cos = Mth.cos(angle).toDouble()
        val x = vec.x
        val y = vec.y
        val z = vec.z
        if (axis === Direction.Axis.X) return Vec3(x, y * cos - z * sin, z * cos + y * sin)
        if (axis === Direction.Axis.Y) return Vec3(x * cos + z * sin, y, z * cos - x * sin)
        return if (axis === Direction.Axis.Z) Vec3(x * cos - y * sin, y * cos + x * sin, z) else vec
    }

    fun mirrorCentered(vec: Vec3, mirror: Mirror?): Vec3 {
        val shift = getCenterOf(BlockPos.ZERO)
        return mirror(vec.subtract(shift), mirror)
            .add(shift)
    }

    fun mirror(vec: Vec3, mirror: Mirror?): Vec3 {
        if (mirror == null || mirror == Mirror.NONE) return vec
        if (vec === Vec3.ZERO) return vec
        val x = vec.x
        val y = vec.y
        val z = vec.z
        if (mirror == Mirror.LEFT_RIGHT) return Vec3(x, y, -z)
        return if (mirror == Mirror.FRONT_BACK) Vec3(-x, y, z) else vec
    }

    fun lookAt(vec: Vec3, fwd: Vec3): Vec3 {
        var fwd = fwd
        fwd = fwd.normalize()
        var up = Vec3(0.0, 1.0, 0.0)
        val dot = fwd.dot(up)
        if (Math.abs(dot) > 1 - 1.0E-3) up = Vec3(0.0, 0.0, (if (dot > 0) 1 else -1.toDouble()) as Double)
        val right = fwd.cross(up)
            .normalize()
        up = right.cross(fwd)
            .normalize()
        val x = vec.x * right.x + vec.y * up.x + vec.z * fwd.x
        val y = vec.x * right.y + vec.y * up.y + vec.z * fwd.y
        val z = vec.x * right.z + vec.y * up.z + vec.z * fwd.z
        return Vec3(x, y, z)
    }

    fun isVecPointingTowards(vec: Vec3, direction: Direction): Boolean {
        return Vec3.atLowerCornerOf(direction.normal)
            .dot(vec.normalize()) > 0.125 // slight tolerance to activate perpendicular movement actors
    }

    fun getCenterOf(pos: Vec3i): Vec3 {
        return if (pos == Vec3i.ZERO) CENTER_OF_ORIGIN else Vec3.atLowerCornerOf(pos)
            .add(.5, .5, .5)
    }

    fun offsetRandomly(vec: Vec3, r: Random, radius: Float): Vec3 {
        return Vec3(
            vec.x + (r.nextFloat() - .5f) * 2 * radius, vec.y + (r.nextFloat() - .5f) * 2 * radius,
            vec.z + (r.nextFloat() - .5f) * 2 * radius
        )
    }

    fun axisAlingedPlaneOf(vec: Vec3): Vec3 {
        var vec = vec
        vec = vec.normalize()
        return Vec3(1.0, 1.0, 1.0).subtract(Math.abs(vec.x), Math.abs(vec.y), Math.abs(vec.z))
    }

    fun axisAlingedPlaneOf(face: Direction): Vec3 {
        return axisAlingedPlaneOf(Vec3.atLowerCornerOf(face.normal))
    }

    fun writeNBT(vec: Vec3): ListTag {
        val listnbt = ListTag()
        listnbt.add(DoubleTag.valueOf(vec.x))
        listnbt.add(DoubleTag.valueOf(vec.y))
        listnbt.add(DoubleTag.valueOf(vec.z))
        return listnbt
    }

    fun writeNBTCompound(vec: Vec3): CompoundTag {
        val compoundTag = CompoundTag()
        compoundTag.put("V", writeNBT(vec))
        return compoundTag
    }

    fun readNBT(list: ListTag): Vec3 {
        return if (list.isEmpty()) Vec3.ZERO else Vec3(list.getDouble(0), list.getDouble(1), list.getDouble(2))
    }

    fun readNBTCompound(nbt: CompoundTag): Vec3 {
        return readNBT(nbt.getList("V", Tag.TAG_DOUBLE.toInt()))
    }

    fun write(vec: Vec3, buffer: FriendlyByteBuf) {
        buffer.writeDouble(vec.x)
        buffer.writeDouble(vec.y)
        buffer.writeDouble(vec.z)
    }

    fun read(buffer: FriendlyByteBuf): Vec3 {
        return Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble())
    }

    fun voxelSpace(x: Double, y: Double, z: Double): Vec3 {
        return Vec3(x, y, z).scale((1 / 16f).toDouble())
    }

    fun getCoordinate(pos: Vec3i, axis: Direction.Axis): Int {
        return axis.choose(pos.x, pos.y, pos.z)
    }

    fun getCoordinate(vec: Vec3, axis: Direction.Axis): Float {
        return axis.choose(vec.x, vec.y, vec.z).toFloat()
    }

    fun onSameAxis(pos1: BlockPos, pos2: BlockPos, axis: Direction.Axis): Boolean {
        if (pos1 == pos2) return true
        for (otherAxis in Direction.Axis.values()) if (axis !== otherAxis) if (getCoordinate(
                pos1,
                otherAxis
            ) != getCoordinate(pos2, otherAxis)
        ) return false
        return true
    }

    fun clamp(vec: Vec3, maxLength: Float): Vec3 {
        return if (vec.length() > maxLength) vec.normalize()
            .scale(maxLength.toDouble()) else vec
    }

    fun lerp(p: Float, from: Vec3, to: Vec3): Vec3 {
        return from.add(
            to.subtract(from)
                .scale(p.toDouble())
        )
    }

    fun slerp(p: Float, from: Vec3, to: Vec3): Vec3 {
        val theta = Math.acos(from.dot(to))
        return from.scale(Mth.sin(1 - p) * theta)
            .add(to.scale(Mth.sin((theta * p).toFloat()).toDouble()))
            .scale((1 / Mth.sin(theta.toFloat())).toDouble())
    }

    fun clampComponentWise(vec: Vec3, maxLength: Float): Vec3 {
        return Vec3(
            Mth.clamp(vec.x, -maxLength.toDouble(), maxLength.toDouble()),
            Mth.clamp(vec.y, -maxLength.toDouble(), maxLength.toDouble()),
            Mth.clamp(vec.z, -maxLength.toDouble(), maxLength.toDouble())
        )
    }

    fun project(vec: Vec3, ontoVec: Vec3): Vec3 {
        return if (ontoVec == Vec3.ZERO) Vec3.ZERO else ontoVec.scale(vec.dot(ontoVec) / ontoVec.lengthSqr())
    }

    fun intersectSphere(origin: Vec3, lineDirection: Vec3, sphereCenter: Vec3?, radius: Double): Vec3? {
        var lineDirection = lineDirection
        if (lineDirection == Vec3.ZERO) return null
        if (lineDirection.length() != 1.0) lineDirection = lineDirection.normalize()
        val diff = origin.subtract(sphereCenter)
        val lineDotDiff = lineDirection.dot(diff)
        val delta = lineDotDiff * lineDotDiff - (diff.lengthSqr() - radius * radius)
        if (delta < 0) return null
        val t = -lineDotDiff + Math.sqrt(delta)
        return origin.add(lineDirection.scale(t))
    }

    // https://forums.minecraftforge.net/topic/88562-116solved-3d-to-2d-conversion/?do=findComment&comment=413573
    // slightly modified
    fun projectToPlayerView(target: Vec3, partialTicks: Float): Vec3 {
        /*
		 * The (centered) location on the screen of the given 3d point in the world.
		 * Result is (dist right of center screen, dist up from center screen, if < 0,
		 * then in front of view plane)
		 */
        val ari = Minecraft.getInstance().gameRenderer.mainCamera
        val camera_pos = ari.position
        val camera_rotation_conj = ari.rotation()
            .copy()
        camera_rotation_conj.conj()
        val result3f = Vector3f(
            (camera_pos.x - target.x).toFloat(),
            (camera_pos.y - target.y).toFloat(),
            (camera_pos.z - target.z).toFloat()
        )
        result3f.transform(camera_rotation_conj)

        // ----- compensate for view bobbing (if active) -----
        // the following code adapted from GameRenderer::applyBobbing (to invert it)
        val mc = Minecraft.getInstance()
        if (mc.options.bobView) {
            val renderViewEntity = mc.getCameraEntity()
            if (renderViewEntity is Player) {
                val playerentity = renderViewEntity
                val distwalked_modified = playerentity.walkDist
                val f = distwalked_modified - playerentity.walkDistO
                val f1 = -(distwalked_modified + f * partialTicks)
                val f2 = Mth.lerp(partialTicks, playerentity.oBob, playerentity.bob)
                val q2 = Quaternion(Vector3f.XP, Math.abs(Mth.cos(f1 * Math.PI.toFloat() - 0.2f) * f2) * 5.0f, true)
                q2.conj()
                result3f.transform(q2)
                val q1 = Quaternion(Vector3f.ZP, Mth.sin(f1 * Math.PI.toFloat()) * f2 * 3.0f, true)
                q1.conj()
                result3f.transform(q1)
                val bob_translation = Vector3f(
                    Mth.sin(f1 * Math.PI.toFloat()) * f2 * 0.5f,
                    -Math.abs(Mth.cos(f1 * Math.PI.toFloat()) * f2), 0.0f
                )
                bob_translation.set(bob_translation.x(), -bob_translation.y(), bob_translation.z()) // this is weird but hey, if it works
                result3f.add(bob_translation)
            }
        }

        // ----- adjust for fov -----
        val fov = (mc.gameRenderer as GameRendererAccessor).`pb$callGetFov`(ari, partialTicks, true) as Float
        val half_height = mc.window
            .guiScaledHeight.toFloat() / 2
        val scale_factor = half_height / (result3f.z() * Math.tan(Math.toRadians((fov / 2).toDouble())).toFloat())
        return Vec3(
            (-result3f.x() * scale_factor).toDouble(),
            (result3f.y() * scale_factor).toDouble(),
            result3f.z().toDouble()
        )
    }

    fun bezier(
        p1: Vec3,
        p2: Vec3,
        q1: Vec3,
        q2: Vec3,
        t: Float
    ): Vec3 {
        val v1 = lerp(t, p1, q1)
        val v2 = lerp(t, q1, q2)
        val v3 = lerp(t, q2, p2)
        val inner1 = lerp(t, v1, v2)
        val inner2 = lerp(t, v2, v3)
        return lerp(t, inner1, inner2)
    }

    fun bezierDerivative(p1: Vec3, p2: Vec3, q1: Vec3, q2: Vec3, t: Float): Vec3 {
        return p1.scale((-3 * t * t + 6 * t - 3).toDouble())
            .add(q1.scale((9 * t * t - 12 * t + 3).toDouble()))
            .add(q2.scale((-9 * t * t + 6 * t).toDouble()))
            .add(p2.scale((3 * t * t).toDouble()))
    }

    fun intersectRanged(p1: Vec3, q1: Vec3, p2: Vec3, q2: Vec3, plane: Direction.Axis): DoubleArray? {
        val pDiff = p2.subtract(p1)
        val qDiff = q2.subtract(q1)
        val intersect = intersect(p1, q1, pDiff.normalize(), qDiff.normalize(), plane) ?: return null
        if (intersect[0] < 0 || intersect[1] < 0) return null
        return if (intersect[0] > pDiff.length() || intersect[1] > qDiff.length()) null else intersect
    }

    fun intersect(p1: Vec3, p2: Vec3, r: Vec3, s: Vec3, plane: Direction.Axis): DoubleArray? {
        var p1 = p1
        var p2 = p2
        var r = r
        var s = s
        if (plane === Direction.Axis.X) {
            p1 = Vec3(p1.y, 0.0, p1.z)
            p2 = Vec3(p2.y, 0.0, p2.z)
            r = Vec3(r.y, 0.0, r.z)
            s = Vec3(s.y, 0.0, s.z)
        }
        if (plane === Direction.Axis.Z) {
            p1 = Vec3(p1.x, 0.0, p1.y)
            p2 = Vec3(p2.x, 0.0, p2.y)
            r = Vec3(r.x, 0.0, r.y)
            s = Vec3(s.x, 0.0, s.y)
        }
        val qminusp = p2.subtract(p1)
        val rcs = r.x * s.z - r.z * s.x
        if (Mth.equal(rcs, 0.0)) return null
        val rdivrcs = r.scale(1 / rcs)
        val sdivrcs = s.scale(1 / rcs)
        val t = qminusp.x * sdivrcs.z - qminusp.z * sdivrcs.x
        val u = qminusp.x * rdivrcs.z - qminusp.z * rdivrcs.x
        return doubleArrayOf(t, u)
    }
}