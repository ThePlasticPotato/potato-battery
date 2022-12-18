package me.crackhead.potato_battery

import me.crackhead.potato_battery.block.*
import me.crackhead.potato_battery.block.machine.BatteryBlock
import me.crackhead.potato_battery.registry.DeferredRegister
import net.minecraft.core.Registry
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

@Suppress("unused")
object PotatoBatteryBlocks {
    private val BLOCKS = DeferredRegister.create(PotatoBatteryMod.MOD_ID, Registry.BLOCK_REGISTRY)

    val BATTERY = BLOCKS.register("battery", ::BatteryBlock)


    fun register() {
        BLOCKS.applyAll()
    }

    fun registerItems(items: DeferredRegister<Item>) {
        BLOCKS.iterator().forEach {
            items.register(it.name) { BlockItem(it.get(), Item.Properties().tab(CreativeModeTab.TAB_MISC)) }
        }
    }

    private infix fun Block.byName(name: String) = BLOCKS.register(name) { this }
}