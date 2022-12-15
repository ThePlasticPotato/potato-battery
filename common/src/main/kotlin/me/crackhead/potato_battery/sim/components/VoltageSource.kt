package me.crackhead.potato_battery.sim.components

import me.crackhead.potato_battery.sim.solver.Model
import me.crackhead.potato_battery.sim.solver.SolverValue

open class VoltageSource(val voltage: Double) : BiComponent() {
    override fun constrain(model: Model, current: SolverValue, voltage1: SolverValue, voltage2: SolverValue) {
        model.constrain { voltage1 eq voltage }
        model.constrain { voltage2 eq 0.0 }
    }

}