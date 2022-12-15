package me.crackhead.potato_battery.mixin;

import me.crackhead.potato_battery.PotatoBatteryMod;
import net.minecraft.client.main.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Main.class)
public class MixinMain {

    @Inject(at = @At("HEAD"), method = "main", remap = false)
    private static void main(String[] strings, CallbackInfo ci) {
        if (PotatoBatteryMod.DEBUG)
            System.setProperty("java.awt.headless", "false");
    }

}
