package me.crackhead.potato_battery.sim.solver

import org.jacop.core.Store
import org.jacop.floats.core.FloatVar
import kotlin.reflect.KProperty

typealias SolverValue = FloatVar

operator fun FloatVar.getValue(nothing: Nothing?, property: KProperty<*>): FloatVar = this
operator fun FloatVar.setValue(nothing: Nothing?, property: KProperty<*>, value: Double) = this.setDomain(value, value)

class Model {
    val store = Store()
    private val MIN_FLOAT = -1e+150
    private val MAX_FLOAT = 1e+150

    fun constrain(builder: ConstrainBuilder.() -> Constraint) {
        val builder = ConstrainBuilder(this)
        val constraint = builder.builder()
        val builded = try {
            constraint.build(builder)
        } catch (e: UnsupportedOperationException) {
            if (constraint !is BiConstraint) throw UnsupportedOperationException("Constraint $constraint is not supported")

            try {
                constraint.swap().build(builder)
            } catch (e: UnsupportedOperationException) {
                throw UnsupportedOperationException("Constraint $constraint is not supported")
            }
        }

        store.impose(builded)
    }

    operator fun provideDelegate(any: Any?, property: KProperty<*>): SolverValue = FloatVar(store, property.name, MIN_FLOAT, MAX_FLOAT)
    fun getUniqueValue(): SolverValue = FloatVar(store, MIN_FLOAT, MAX_FLOAT)
    fun getUniqueValue(name: String?): SolverValue = if (name == null) getUniqueValue() else FloatVar(store, name, MIN_FLOAT, MAX_FLOAT)
    fun wrap(value: Double): SolverValue = FloatVar(store, value, value)
    fun makeAndConstrain(builder: ConstrainBuilder.(FloatVar) -> Constraint): SolverValue =
        getUniqueValue().apply { constrain { builder(this@apply) } }
}