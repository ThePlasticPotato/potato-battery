package me.crackhead.potato_battery.block.machine

import me.crackhead.potato_battery.blockentity.machine.BatteryBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Material
import net.minecraft.world.level.material.MaterialColor
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape

class BatteryBlock : BaseEntityBlock(Properties.of(Material.BAMBOO, MaterialColor.COLOR_BLACK)) {
    val SHAPE = box(0.5, 0.0, 0.5, 15.5, 15.5, 15.5);

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = BatteryBlockEntity(pos, state)
    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape = SHAPE
    override fun getRenderShape(state: BlockState): RenderShape = RenderShape.MODEL
}