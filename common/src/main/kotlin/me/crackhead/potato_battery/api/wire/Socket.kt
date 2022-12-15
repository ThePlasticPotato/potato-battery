package me.crackhead.potato_battery.api.wire

import com.mojang.math.Vector3d
import net.minecraft.world.phys.Vec3
import kotlin.math.roundToInt


class Socket() : Comparable<Socket>, ISocket {

    override var connectedSocket: Socket? = null
    override var pos: Vec3
        get() = pos
        set(position: Vec3) {
            pos = position
        }

    fun Socket() {
        if (connectedSocket != null) {
            //insert energy transfer code here :clueless:
        } else {
            //insert connection code here :troll:
        }
    }

    override fun compareTo(other: Socket): Int {
        val loc = this.pos
        val otherloc = other.pos
        val distance: Double = loc.distanceToSqr(otherloc)
        return distance.roundToInt()
    }

    override fun connect(other: Socket) {
        this.connectedSocket = other
    }

    override fun disconnnect() {
        this.connectedSocket = null
    }

    override var polarity: Boolean
        get() = polarity
        set(positive: Boolean) {
            polarity = positive
        }
    
}