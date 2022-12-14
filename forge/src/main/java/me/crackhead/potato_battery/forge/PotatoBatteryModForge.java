package me.crackhead.potato_battery.forge;


import me.crackhead.potato_battery.PotatoBatteryMod;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(PotatoBatteryMod.MOD_ID)
public class PotatoBatteryModForge {
    boolean happendClientSetup = false;
    static IEventBus MOD_BUS;

    public PotatoBatteryModForge() {
        // Submit our event bus to let architectury register our content on the right time
        MOD_BUS = FMLJavaModLoadingContext.get().getModEventBus();
        MOD_BUS.addListener(this::clientSetup);

//        MOD_BUS.addListener(this::onModelRegistry);
        MOD_BUS.addListener(this::clientSetup);
//        MOD_BUS.addListener(this::entityRenderers);

        PotatoBatteryMod.init();
    }

    void clientSetup(final FMLClientSetupEvent event) {
        if (happendClientSetup) return;
        happendClientSetup = true;

        PotatoBatteryMod.initClient();
    }
}