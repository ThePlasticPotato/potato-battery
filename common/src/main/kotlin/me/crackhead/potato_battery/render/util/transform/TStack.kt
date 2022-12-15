package me.crackhead.potato_battery.render.util.transform


interface TStack<Self> {
    fun pushPose(): Self
    fun popPose(): Self
}