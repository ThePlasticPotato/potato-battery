/*
Inspired by the Immersive Engineering team- credit goes to them!
*/

package me.crackhead.potato_battery.api

import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.item.context.UseOnContext

class TargetingContext {
    lateinit var side: Direction
    var hitX: Float = 0.0f
    var hitY: Float = 0.0f
    var hitZ: Float = 0.0f

    fun TargetingContext(ctx: UseOnContext) {
        ctx.clickedFace; ctx.clickLocation.x ; ctx.clickLocation.y ; ctx.clickLocation.z
    }

    fun TargetingContext(side: Direction, hitX: Float, hitY: Float, hitZ: Float) {
        this.side = side
        this.hitX = hitX
        this.hitY = hitY
        this.hitZ = hitZ
    }
}