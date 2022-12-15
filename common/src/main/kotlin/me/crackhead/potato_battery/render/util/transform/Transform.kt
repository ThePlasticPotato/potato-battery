package me.crackhead.potato_battery.render.util.transform

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Matrix3f
import com.mojang.math.Matrix4f
import com.mojang.math.Quaternion
import net.minecraft.core.Direction


interface Transform<Self : Transform<Self>?> : Translate<Self>, Rotate<Self>, Scale<Self> {
	fun mulPose(pose: Matrix4f?): Self
	fun mulNormal(normal: Matrix3f?): Self
	fun transform(pose: Matrix4f?, normal: Matrix3f?): Self {
		mulPose(pose)
		return mulNormal(normal)
	}

	fun transform(stack: PoseStack): Self {
		val last = stack.last()
		return transform(last.pose(), last.normal())
	}

	fun rotateCentered(axis: Direction, radians: Float): Self {
		translate(.5, .5, .5)?.rotate(axis, radians)
			?.translate(-.5, -.5, -.5)
		return this as Self
	}

	fun rotateCentered(q: Quaternion?): Self {
		translate(.5, .5, .5)?.multiply(q)
			?.translate(-.5, -.5, -.5)
		return this as Self
	}
}