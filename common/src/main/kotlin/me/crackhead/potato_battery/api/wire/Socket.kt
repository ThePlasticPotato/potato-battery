package me.crackhead.potato_battery.api.wire

import com.mojang.math.Vector3d
import net.minecraft.world.item.DyeColor
import net.minecraft.world.phys.Vec3
import kotlin.math.roundToInt


class Socket(override val pos: Vec3, override val color: DyeColor) : ISocket {

    private var _connectedSocket: Socket? = null
    override val connectedSocket: Socket? get() = _connectedSocket


    override fun connect(other: Socket) {
        this._connectedSocket = other
    }

    override fun disconnnect() {
        this._connectedSocket = null
    }
}