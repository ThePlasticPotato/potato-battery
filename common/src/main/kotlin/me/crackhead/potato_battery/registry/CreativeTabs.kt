package me.crackhead.potato_battery.registry

import me.crackhead.potato_battery.services.PotatoBatteryPlatformHelper
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack
import java.util.ServiceLoader

class CreativeTabs {
    companion object {
        fun create(id: ResourceLocation, stack: () -> ItemStack): CreativeModeTab {
            return ServiceLoader.load(PotatoBatteryPlatformHelper::class.java)
                .findFirst()
                .get()
                .createCreativeTab(id, stack)
        }
    }
}