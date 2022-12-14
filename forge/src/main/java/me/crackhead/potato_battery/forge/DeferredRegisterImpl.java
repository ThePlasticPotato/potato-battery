package me.crackhead.potato_battery.forge;

import kotlin.jvm.functions.Function0;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import me.crackhead.potato_battery.registry.DeferredRegister;
import me.crackhead.potato_battery.registry.RegistrySupplier;

import java.util.Iterator;

public class DeferredRegisterImpl<T> implements DeferredRegister<T> {
    private final net.minecraftforge.registries.DeferredRegister<T> forge;

    public DeferredRegisterImpl(String modId, ResourceKey<Registry<T>> registry) {
        forge = net.minecraftforge.registries.DeferredRegister.create(registry.location(), modId);
    }

    @NotNull
    @Override
    public <I extends T> RegistrySupplier<I> register(@NotNull String name, @NotNull Function0<? extends I> builder) {
        RegistryObject<I> result = forge.register(name, builder::invoke);

        return new RegistrySupplier<I>() {
            @NotNull
            @Override
            public String getName() {
                return name;
            }

            @Override
            public I get() {
                return result.get();
            }
        };
    }

    @Override
    public void applyAll() {
        forge.register(PotatoBatteryModForge.MOD_BUS);
    }

    @NotNull
    @Override
    public Iterator<RegistrySupplier<T>> iterator() {
        Iterator<RegistryObject<T>> iterator = forge.getEntries().iterator();

        return new Iterator<RegistrySupplier<T>>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public RegistrySupplier<T> next() {
                RegistryObject<T> result = iterator.next();

                return new RegistrySupplier<T>() {
                    @NotNull
                    @Override
                    public String getName() {
                        return result.getId().getPath();
                    }

                    @Override
                    public T get() {
                        return result.get();
                    }
                };
            }
        };
    }
}
