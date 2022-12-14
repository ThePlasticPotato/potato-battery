package me.crackhead.potato_battery.services

import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import me.crackhead.potato_battery.registry.DeferredRegister

interface DeferredRegisterBackend {
    fun <T> makeDeferredRegister(id: String, registry: ResourceKey<Registry<T>>): DeferredRegister<T>
}