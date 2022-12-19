package me.crackhead.potato_battery.mixin;

import me.crackhead.potato_battery.api.mixin.SocketHitProvider;
import me.crackhead.potato_battery.api.wire.Socket;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Minecraft.class)
public class MixinMinecraft implements SocketHitProvider.Container {

    @Unique
    @Nullable private Socket socketHit;

    @Nullable
    @Override
    public Socket getHitResultSocket() {
        return socketHit;
    }

    @Override
    public void setHitResultSocket(@Nullable Socket socket) {
        this.socketHit = socket;
    }
}
