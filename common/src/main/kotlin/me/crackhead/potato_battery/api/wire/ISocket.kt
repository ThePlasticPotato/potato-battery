package me.crackhead.potato_battery.api.wire

import net.minecraft.world.phys.Vec3

interface ISocket {

    var connectedSocket: Socket?

    var pos: Vec3

    fun connect(other: Socket)

    fun disconnnect()

    var polarity: Boolean
}