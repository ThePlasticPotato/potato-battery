package me.crackhead.potato_battery.api.wire

import net.minecraft.world.item.DyeColor
import net.minecraft.world.phys.Vec3


class DefaultSocket(override val pos: Vec3, override val color: DyeColor) : Socket {

    private var _connectedSocket: DefaultSocket? = null
    override val connectedSocket: DefaultSocket? get() = _connectedSocket


    override fun connect(other: DefaultSocket) {
        this._connectedSocket = other
    }

    override fun disconnect() {
        this._connectedSocket = null
    }
}