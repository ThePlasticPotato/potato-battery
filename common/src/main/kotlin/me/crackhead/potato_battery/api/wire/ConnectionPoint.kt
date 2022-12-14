/*
Inspired by the Immersive Engineering team- credit goes to them!
*/

package me.crackhead.potato_battery.api.wire

import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtUtils

data class ConnectionPoint(val position: BlockPos, val index: Int) : Comparable<ConnectionPoint> {

    fun ConnectionPoint(nbt: CompoundTag) {
        NbtUtils.readBlockPos(nbt); nbt.getInt("index")
    }

    fun createTag(): CompoundTag {
        var ret: CompoundTag = NbtUtils.writeBlockPos(position)
        ret.putInt("index", index)
        return ret
    }

    override fun compareTo(other: ConnectionPoint): Int {
        var blockCmp: Int = position.compareTo(other.position)
        if(blockCmp != 0) {
            return blockCmp
        }
        return Integer.compare(index, other.index)
    }

    fun getX(): Int {
        return position.x
    }
    fun getY(): Int {
        return position.y
    }
    fun getZ(): Int {
        return position.z
    }
}