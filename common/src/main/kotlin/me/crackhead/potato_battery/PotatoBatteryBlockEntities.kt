package me.crackhead.potato_battery

import me.crackhead.potato_battery.blockentity.machine.BatteryBlockEntity
import me.crackhead.potato_battery.registry.DeferredRegister
import me.crackhead.potato_battery.registry.RegistrySupplier
import net.minecraft.Util
import net.minecraft.core.Registry
import net.minecraft.util.datafix.fixes.References
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType

@Suppress("unused")
object PotatoBatteryBlockEntities {

    private val BLOCKENTITIES = DeferredRegister.create(PotatoBatteryMod.MOD_ID, Registry.BLOCK_ENTITY_TYPE_REGISTRY)


    val ENGINE = PotatoBatteryBlocks.BATTERY withBE BatteryBlockEntity.supplier byName "battery"

    fun register() {
        BLOCKENTITIES.register()
    }

    private infix fun <T : BlockEntity> Set<RegistrySupplier<out Block>>.withBE(blockEntity: () -> T) =
        Pair(this, blockEntity)

    private infix fun <T : BlockEntity> RegistrySupplier<out Block>.withBE(blockEntity: () -> T) =
        Pair(setOf(this), blockEntity)

    private infix fun <T : BlockEntity> Block.withBE(blockEntity: () -> T) = Pair(this, blockEntity)
    private infix fun <T : BlockEntity> Pair<Set<RegistrySupplier<out Block>>, () -> T>.byName(name: String): RegistrySupplier<BlockEntityType<T>> =
        BLOCKENTITIES.register(name) {
            val type = Util.fetchChoiceType(References.BLOCK_ENTITY, name)
            BlockEntityType.Builder.of(this.second, *this.first.map { it.get() }.toTypedArray()).build(type)
        }
}
}