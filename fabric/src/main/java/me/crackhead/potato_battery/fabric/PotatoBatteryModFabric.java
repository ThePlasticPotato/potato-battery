package me.crackhead.potato_battery.fabric;

import com.mojang.brigadier.CommandDispatcher;
import me.crackhead.potato_battery.PotatoBatteryMod;
import net.fabricmc.api.ModInitializer;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
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
            PotatoBatteryMod.registerClientCommands((CommandDispatcher<ClientSuggestionProvider>) (Object) ClientCommandManager.DISPATCHER);
        }
    }

    public static class ModMenu implements ModMenuApi {
    }
}