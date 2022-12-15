package me.crackhead.potato_battery.sim

import kotlin.math.PI
import kotlin.math.sin

sealed interface Signal {
   fun voltage(time: Double): Double
}

data class ConstantACSignal(val amplitude: Double, val frequency: Double): Signal {
    override fun voltage(time: Double): Double = sin(time * PI * frequency) * amplitude
}

data class ConstantDCSignal(val voltage: Double): Signal {
    override fun voltage(time: Double): Double = voltage
}

data class DigitalSignal(val voltage: Double, val data: Any): Signal { // TODO implement data
    override fun voltage(time: Double): Double = TODO()
}

data class AnalogSignal(val amplitude: Double, val data: Any): Signal { // TODO implement data
    override fun voltage(time: Double): Double = TODO()
}