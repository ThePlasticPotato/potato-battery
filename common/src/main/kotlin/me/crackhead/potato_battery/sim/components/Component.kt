package me.crackhead.potato_battery.sim.components

import me.crackhead.potato_battery.sim.solver.Model
import me.crackhead.potato_battery.sim.solver.SolverValue
import org.jgrapht.graph.DefaultEdge

abstract class BiComponent: DefaultEdge() {
    abstract fun constrain(model: Model, current: SolverValue, voltage1: SolverValue, voltage2: SolverValue)
}