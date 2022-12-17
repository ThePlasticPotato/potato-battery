package me.crackhead.potato_battery.blockentity.machine

import me.crackhead.potato_battery.render.SocketBoxTransform
import me.crackhead.potato_battery.render.util.VecHelper.voxelSpace
import net.minecraft.core.Direction
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.Vec3


class BatteryBlockEntity {

}

open class BatterySocketBox : SocketBoxTransform.Sided() {
    override val southLocation: Vec3
        protected get() = voxelSpace(8.0, 12.0, 15.75)

    override fun isSideActive(state: BlockState?, direction: Direction?): Boolean {
        return direction?.getAxis()?.isVertical == true
    }
}