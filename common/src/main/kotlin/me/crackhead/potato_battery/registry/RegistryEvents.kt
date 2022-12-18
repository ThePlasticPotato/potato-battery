package me.crackhead.potato_battery.registry

import net.minecraft.core.RegistryAccess
import java.util.function.Consumer


object RegistryEvents {
    private val onTagsLoaded: MutableList<Runnable> = ArrayList()
    private val onRegistriesComplete: MutableList<Runnable> = ArrayList()

    // this can be beter...
    fun onTagsLoaded(event: Runnable) {
        onTagsLoaded.add(event)
    }

    fun tagsAreLoaded(registries: RegistryAccess?, client: Boolean) {
        onTagsLoaded.forEach(Consumer { obj: Runnable -> obj.run() })
    }

    fun onRegistriesComplete(event: Runnable) {
        onRegistriesComplete.add(event)
    }

    fun registriesAreComplete() {
        onRegistriesComplete.forEach(Consumer { obj: Runnable -> obj.run() })
    }
}