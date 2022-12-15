package me.crackhead.potato_battery.api.wire

import com.mojang.math.Vector3d
import net.minecraft.world.phys.Vec3
import kotlin.math.roundToInt


class Socket() : Comparable<Socket>, ISocket {

    override var isOccupied: Boolean = false
    override var connectedSocket: Socket? = null
    override var posX: Vec3
        get() = posX
        set(x: Vec3) {
            posX = x
        }
    override var posY: Vec3
        get() = posY
        set(y: Vec3) {
            posY = y
        }
    override var posZ: Vec3
        get() = posZ
        set(z: Vec3) {
            posZ = z
        }

    fun Socket() {
        if (isOccupied && connectedSocket != null) {
            //insert energy transfer code here :clueless:
        } else {
            //insert connection code here :troll:
        }
    }

    override fun compareTo(other: Socket): Int {
        val loc = Vec3(this.posX.x, this.posY.y, this.posZ.z)
        val otherloc = Vec3(other.posX.x, other.posY.y, other.posZ.z)
        val distance: Double = loc.distanceToSqr(otherloc)
        return distance.roundToInt()
    }

    override fun connect(other: Socket) {
        this.connectedSocket = other
        this.isOccupied = true
    }

    override fun disconnnect() {
        this.connectedSocket = null
        this.isOccupied = false
    }

    override var polarityPos: Boolean
        get() = polarityPos
        set(positive: Boolean) {
            polarityPos = positive
        }
    override var isDC: Boolean
        get() = isDC
        set(dc: Boolean) {
            isDC = dc
        }
}