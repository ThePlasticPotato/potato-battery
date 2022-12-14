/*
Inspired by the Immersive Engineering team- credit goes to them!
*/

package me.crackhead.potato_battery.api.components

import me.crackhead.potato_battery.api.TargetingContext
import me.crackhead.potato_battery.api.wire.ConnectionPoint
import net.minecraft.core.BlockPos
import net.minecraft.core.Vec3i
import net.minecraft.world.phys.Vec3


interface IPBConnectable {

    val canConnect: Boolean

    fun getConnectionMaster(target: TargetingContext): BlockPos

    fun canConnectCable(target: ConnectionPoint, other: IPBConnectable, otherTarget: ConnectionPoint): Boolean

    fun connectCable(target: ConnectionPoint, other: IPBConnectable, otherTarget: ConnectionPoint): Void
    fun removeCable(connectionPoint: ConnectionPoint?, attachedPoint: ConnectionPoint): Void

    fun getTargetedPoint(target: TargetingContext, offset: Vec3i): ConnectionPoint?

    fun getConnectionOffset(here: ConnectionPoint, other: ConnectionPoint): Vec3

    val getPosition: BlockPos
}