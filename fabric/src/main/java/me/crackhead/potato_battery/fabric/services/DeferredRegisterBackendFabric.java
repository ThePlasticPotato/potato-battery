package me.crackhead.potato_battery.fabric.services;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.NotNull;
import me.crackhead.potato_battery.fabric.DeferredRegisterImpl;
import me.crackhead.potato_battery.registry.DeferredRegister;
import me.crackhead.potato_battery.services.DeferredRegisterBackend;

public class DeferredRegisterBackendFabric implements DeferredRegisterBackend {

    @NotNull
    @Override
    public <T> DeferredRegister<T> makeDeferredRegister(@NotNull String id, @NotNull ResourceKey<Registry<T>> registry) {
        return new DeferredRegisterImpl<>(id, registry);
    }
}
