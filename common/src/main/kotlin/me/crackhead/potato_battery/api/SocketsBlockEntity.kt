package me.crackhead.potato_battery.api

import me.crackhead.potato_battery.api.wire.Socket

interface SocketsBlockEntity {
    val sockets: List<Socket>
}