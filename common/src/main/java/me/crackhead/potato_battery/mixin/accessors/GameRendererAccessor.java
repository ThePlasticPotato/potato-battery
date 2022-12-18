package me.crackhead.potato_battery.mixin.accessors;

import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GameRenderer.class)
public interface GameRendererAccessor {
    @Invoker("getFov")
    double pb$callGetFov(Camera camera, float partialTicks, boolean useFOVSetting);
}