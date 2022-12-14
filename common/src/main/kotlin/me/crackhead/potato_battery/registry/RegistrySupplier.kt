package me.crackhead.potato_battery.registry

interface RegistrySupplier<T> {

    val name: String
    fun get(): T

}