package me.crackhead.potato_battery.blockentity.machine

import me.crackhead.potato_battery.PotatoBatteryBlockEntities
import me.crackhead.potato_battery.api.SocketsBlockEntity
import me.crackhead.potato_battery.api.wire.Socket
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class BatteryBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(PotatoBatteryBlockEntities.BATTERY.get(), pos, state), SocketsBlockEntity {
    override val sockets: List<Socket> = mutableListOf()
}