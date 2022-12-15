package me.crackhead.potato_battery.api.wire

import net.minecraft.world.item.DyeColor
import net.minecraft.world.phys.Vec3

interface Socket {

    val connectedSocket: DefaultSocket?

    /**
     * The position of the socket relative from the middle of the block
     */
    val pos: Vec3
    val color: DyeColor

    fun connect(other: DefaultSocket)
    fun disconnect()
}