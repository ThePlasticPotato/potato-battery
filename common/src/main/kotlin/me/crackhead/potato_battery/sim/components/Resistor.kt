package me.crackhead.potato_battery.sim.components

import me.crackhead.potato_battery.sim.solver.Model
import me.crackhead.potato_battery.sim.solver.SolverValue


class Resistor(val resistance: Double) : BiComponent() {
    override fun constrain(model: Model, current: SolverValue, voltage1: SolverValue, voltage2: SolverValue) {
        val diff = model.makeAndConstrain { voltage1 - voltage2 eq it }
        model.constrain { current * resistance eq diff }
    }
}