package me.crackhead.potato_battery.api.wire

import net.minecraft.core.BlockPos
import net.minecraft.world.item.DyeColor
import net.minecraft.world.phys.Vec3

interface Socket {

    val connectedSocket: DefaultSocket?

    /**
     * The position of the socket in world coordinates
     */
    val pos: Vec3
    val blockPos: BlockPos // The block position of the socket

    fun connect(other: DefaultSocket)
    fun disconnect()
}