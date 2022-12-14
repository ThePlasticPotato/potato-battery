package me.crackhead.potato_battery.services

import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack

interface PotatoBatteryPlatformHelper {
    fun createCreativeTab(id: ResourceLocation, stack: () -> ItemStack): CreativeModeTab
}