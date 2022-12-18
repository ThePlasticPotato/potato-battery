package me.crackhead.potato_battery.blockentity.machine

import me.crackhead.potato_battery.PotatoBatteryBlockEntities
import me.crackhead.potato_battery.api.SocketsBlockEntity
import me.crackhead.potato_battery.api.wire.Socket
import me.crackhead.potato_battery.render.SocketBoxTransform
import me.crackhead.potato_battery.render.util.VecHelper.voxelSpace
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.Vec3

class BatteryBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(PotatoBatteryBlockEntities.BATTERY.get(), pos, state), SocketsBlockEntity {
    override val sockets: List<Socket> = mutableListOf()


}

open class BatterySocketBox : SocketBoxTransform.Sided() {
    override val southLocation: Vec3
        protected get() = voxelSpace(8.0, 12.0, 15.75)

    override fun isSideActive(state: BlockState?, direction: Direction?): Boolean {
        return direction?.getAxis()?.isVertical == true
    }
}