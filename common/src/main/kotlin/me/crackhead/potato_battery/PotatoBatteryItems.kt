package me.crackhead.potato_battery

import me.crackhead.potato_battery.PotatoBatteryMod.resource
import me.crackhead.potato_battery.registry.CreativeTabs
import me.crackhead.potato_battery.registry.DeferredRegister
import net.minecraft.core.Registry
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

object PotatoBatteryItems {
    private val ITEMS = DeferredRegister.create(PotatoBatteryMod.MOD_ID, Registry.ITEM_REGISTRY)
    val TAB: CreativeModeTab = CreativeTabs.create("potato_battery".resource,) { ItemStack(PotatoBatteryBlocks.BATTERY.get()) }

    fun register() {
        PotatoBatteryBlocks.registerItems(ITEMS)
        ITEMS.applyAll()
    }

    private infix fun Item.byName(name: String) = ITEMS.register(name) { this }
}