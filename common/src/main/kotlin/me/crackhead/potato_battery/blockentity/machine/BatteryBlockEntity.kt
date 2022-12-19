package me.crackhead.potato_battery.blockentity.machine

import me.crackhead.potato_battery.PotatoBatteryBlockEntities
import me.crackhead.potato_battery.api.SocketsBlockEntity
import me.crackhead.potato_battery.api.wire.DefaultSocket
import me.crackhead.potato_battery.api.wire.Socket
import me.crackhead.potato_battery.util.makeVoxelAABB
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class BatteryBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(PotatoBatteryBlockEntities.BATTERY.get(), pos, state), SocketsBlockEntity {
    override val sockets: List<Socket> = listOf(
        DefaultSocket(this.blockPos, makeVoxelAABB(10.75, 15.0, 7.0, 12.75, 17.0, 9.0).inflate(0.02)),
        DefaultSocket(this.blockPos, makeVoxelAABB(3.25, 15.0, 7.0, 5.25, 17.0, 9.0).inflate(0.02))
    )
}