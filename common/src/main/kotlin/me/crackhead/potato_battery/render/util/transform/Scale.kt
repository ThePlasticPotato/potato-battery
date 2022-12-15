package me.crackhead.potato_battery.render.util.transform

interface Scale<Self> {
    fun scale(factorX: Float, factorY: Float, factorZ: Float): Self
    fun scale(factor: Float): Self {
        return scale(factor, factor, factor)
    }
}