package me.crackhead.potato_battery.util

import net.minecraft.world.phys.AABB

fun makeVoxelAABB(x1: Double, y1: Double, z1: Double, x2: Double, y2: Double, z2: Double): AABB {
    return AABB(x1 / 16, y1 / 16, z1 / 16, x2 / 16, y2 / 16, z2 / 16)
}