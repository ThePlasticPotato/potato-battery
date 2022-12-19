package me.crackhead.potato_battery.api.wire

import net.minecraft.core.BlockPos
import net.minecraft.world.phys.AABB

interface Socket {

    val connectedSocket: Socket?

    val blockPos: BlockPos // The block position of the socket
    val aabb: AABB // in world coordinates

    fun connect(other: Socket)
    fun disconnect()
}