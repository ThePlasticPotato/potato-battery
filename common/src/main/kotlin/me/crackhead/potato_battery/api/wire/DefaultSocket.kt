package me.crackhead.potato_battery.api.wire

import net.minecraft.core.BlockPos
import net.minecraft.world.phys.AABB


class DefaultSocket(override val blockPos: BlockPos, override val aabb: AABB) : Socket {

    private var _connectedSocket: Socket? = null
    override val connectedSocket: Socket? get() = _connectedSocket

    override fun connect(other: Socket) {
        _connectedSocket = other
    }

    override fun disconnect() {
        _connectedSocket = null
    }

    override fun hashCode(): Int {
        return blockPos.hashCode()
    }
}