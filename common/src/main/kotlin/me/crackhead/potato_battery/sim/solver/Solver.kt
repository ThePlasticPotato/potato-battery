package me.crackhead.potato_battery.sim.solver

import org.jacop.constraints.Constraint
import org.jacop.floats.core.FloatVar
import kotlin.reflect.KProperty

typealias SolverValue = FloatVar

operator fun FloatVar.getValue(nothing: Nothing?, property: KProperty<*>): FloatVar = this
operator fun FloatVar.setValue(nothing: Nothing?, property: KProperty<*>, value: Double) = this.setDomain(value, value)

typealias ConstraintSet = MutableSet<Constraint>
class ConstraintSetBuilder(val model: Model) : Model by model {
    val constraints: ConstraintSet = mutableSetOf()

    override fun constrain(builder: ConstrainBuilder.() -> me.crackhead.potato_battery.sim.solver.Constraint): org.jacop.constraints.Constraint =
        model.constrain(builder).apply { constraints.add(this) }

    fun remove() = constraints.forEach { it.removeConstraint() }
}