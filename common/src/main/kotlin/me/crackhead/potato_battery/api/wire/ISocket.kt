package me.crackhead.potato_battery.api.wire

import net.minecraft.world.phys.Vec3

interface ISocket {
    var isOccupied : Boolean

    var connectedSocket: Socket?

    var posX: Vec3
    var posY: Vec3
    var posZ: Vec3

    fun connect(other: Socket)

    fun disconnnect()

    var polarityPos: Boolean

    var isDC: Boolean
}