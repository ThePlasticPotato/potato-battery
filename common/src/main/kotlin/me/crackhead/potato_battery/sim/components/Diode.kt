package me.crackhead.potato_battery.sim.components

import me.crackhead.potato_battery.sim.solver.Model
import me.crackhead.potato_battery.sim.solver.SolverValue

class Diode : BiComponent() {
    override fun constrain(model: Model, current: SolverValue, voltage1: SolverValue, voltage2: SolverValue) {
        model.constrain { voltage1 - 0.3 eq voltage2 }
        model.constrain { current gte 0.0 }
    }
}