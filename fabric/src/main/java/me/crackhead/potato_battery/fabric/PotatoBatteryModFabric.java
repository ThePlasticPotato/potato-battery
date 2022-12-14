package me.crackhead.potato_battery.fabric;

import me.crackhead.potato_battery.PotatoBatteryMod;
import net.fabricmc.api.ModInitializer;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;

public class PotatoBatteryModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        PotatoBatteryMod.init();
    }

    @Environment(EnvType.CLIENT)
    public static class Client implements ClientModInitializer {

        @Override
        public void onInitializeClient() {
            PotatoBatteryMod.initClient();

        }
    }

    public static class ModMenu implements ModMenuApi {
    }
}