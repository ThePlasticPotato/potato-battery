package me.crackhead.potato_battery.fabric.services;

import kotlin.jvm.functions.Function0;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import me.crackhead.potato_battery.services.PotatoBatteryPlatformHelper;
import org.jetbrains.annotations.NotNull;

public class PotatoBatteryPlatformHelperFabric implements PotatoBatteryPlatformHelper {
    @NotNull
    @Override
    public CreativeModeTab createCreativeTab(@NotNull ResourceLocation id, @NotNull Function0<ItemStack> stack) {
        return FabricItemGroupBuilder.build(id, stack::invoke);
    }
}
