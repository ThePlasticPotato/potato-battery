package me.crackhead.potato_battery.render.util

import com.mojang.blaze3d.vertex.PoseStack
import me.crackhead.potato_battery.render.util.transform.TStack
import me.crackhead.potato_battery.render.util.transform.Transform


interface TransformStack : Transform<TransformStack?>, TStack<TransformStack?> {
    companion object {
        fun cast(stack: PoseStack?): TransformStack? {
            return stack as TransformStack?
        }
    }
}