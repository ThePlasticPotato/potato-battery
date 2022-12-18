package me.crackhead.potato_battery

import com.mojang.datafixers.types.Type
import me.crackhead.potato_battery.blockentity.machine.BatteryBlockEntity
import me.crackhead.potato_battery.registry.DeferredRegister
import me.crackhead.potato_battery.registry.RegistrySupplier
import net.minecraft.Util
import net.minecraft.core.BlockPos
import net.minecraft.core.Registry
import net.minecraft.util.datafix.fixes.References
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

@Suppress("unused")
object PotatoBatteryBlockEntities {
    private val BLOCKENTITIES = DeferredRegister.create(PotatoBatteryMod.MOD_ID, Registry.BLOCK_ENTITY_TYPE_REGISTRY)

    val BATTERY = PotatoBatteryBlocks.BATTERY withBE ::BatteryBlockEntity byName "battery"

    fun register() {
        BLOCKENTITIES.applyAll()
    }

    private infix fun <T : BlockEntity> Set<RegistrySupplier<out Block>>.withBE(blockEntity:  (BlockPos, BlockState) -> T) =
        Pair(this, blockEntity)

    private infix fun <T : BlockEntity> RegistrySupplier<out Block>.withBE(blockEntity:  (BlockPos, BlockState) -> T) =
        Pair(setOf(this), blockEntity)

    private infix fun <T : BlockEntity> Block.withBE(blockEntity: (BlockPos, BlockState) -> T) = Pair(this, blockEntity)
    private infix fun <T : BlockEntity> Pair<Set<RegistrySupplier<out Block>>, (BlockPos, BlockState) -> T>.byName(name: String): RegistrySupplier<BlockEntityType<T>> =
        BLOCKENTITIES.register(name) {
            val type: Type<*>? = Util.fetchChoiceType(References.BLOCK_ENTITY, name)
            BlockEntityType.Builder.of(this.second, *this.first.map { it.get() }.toTypedArray()).build(type)
        }
}
