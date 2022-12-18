package me.crackhead.potato_battery

import net.minecraft.core.Registry
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.FireBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.Material
import net.minecraft.world.level.material.MaterialColor
import me.crackhead.potato_battery.block.*
import me.crackhead.potato_battery.block.machine.BatteryBlock
import me.crackhead.potato_battery.registry.DeferredRegister
import me.crackhead.potato_battery.registry.RegistryEvents

@Suppress("unused")
object PotatoBatteryBlocks {
    private val BLOCKS = DeferredRegister.create(PotatoBatteryMod.MOD_ID, Registry.BLOCK_REGISTRY)

    val BATTERY = BLOCKS.register("battery", ::BatteryBlock)


    fun register() {
        BLOCKS.register()
    }
    fun registerItems(items: DeferredRegister<Item>) {
        BLOCKS.iterator().forEach {
            items.register(it.id) { BlockItem(it.get(), Item.Properties().tab(PotatoBatteryItems.TAB)) }
        }
    }

    private infix fun Block.byName(name: String) = BLOCKS.register(name) { this }
}