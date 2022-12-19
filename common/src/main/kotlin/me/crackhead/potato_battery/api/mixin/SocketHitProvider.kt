package me.crackhead.potato_battery.api.mixin

import me.crackhead.potato_battery.api.wire.Socket
import net.minecraft.client.Minecraft

interface SocketHitProvider {

    val hitResultSocket: Socket?

    interface Container: SocketHitProvider {
        override var hitResultSocket: Socket?
    }

    companion object {
        fun getHit(): Socket? = (Minecraft.getInstance() as SocketHitProvider).hitResultSocket
    }
}