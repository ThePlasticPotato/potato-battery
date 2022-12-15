package me.crackhead.potato_battery.sim.solver

import me.crackhead.potato_battery.sim.PBVertex
import me.crackhead.potato_battery.sim.components.BiComponent
import org.jacop.core.Store
import org.jacop.floats.core.FloatVar
import kotlin.reflect.KProperty

interface Model {
    fun constrain(builder: ConstrainBuilder.() -> Constraint): org.jacop.constraints.Constraint
    operator fun provideDelegate(any: Any?, property: KProperty<*>): SolverValue
    fun getUniqueValue(): SolverValue
    fun getUniqueValue(name: String?): SolverValue
    fun wrap(value: Double): SolverValue
    fun makeAndConstrain(builder: ConstrainBuilder.(FloatVar) -> Constraint): SolverValue =
        getUniqueValue().apply { constrain { builder(this@apply) } }
}

class ModelImpl: Store(), Model {
    val vertexToValue = HashMap<PBVertex, SolverValue>()
    val edgeToCurrent = HashMap<BiComponent, Pair<SolverValue, ConstraintSet>>()
    val changedVars = ArrayList<SolverValue>()

    private val MIN_FLOAT = -1e+150
    private val MAX_FLOAT = 1e+150

    override fun constrain(builder: ConstrainBuilder.() -> Constraint): org.jacop.constraints.Constraint {
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

        this.impose(builded)
        return builded
    }

    override operator fun provideDelegate(any: Any?, property: KProperty<*>): SolverValue =
        FloatVar(this, property.name, MIN_FLOAT, MAX_FLOAT).apply { changedVars.add(this) }
    override fun getUniqueValue(): SolverValue =
        FloatVar(this, MIN_FLOAT, MAX_FLOAT).apply { changedVars.add(this) }
    override fun getUniqueValue(name: String?): SolverValue =
        if (name == null) getUniqueValue() else FloatVar(this, name, MIN_FLOAT, MAX_FLOAT)
    override fun wrap(value: Double): SolverValue =
        FloatVar(this, value, value).apply { changedVars.add(this) }

    fun rebuildNeeded() {
        edgeToCurrent.forEach { entry ->
            val comp = entry.key
            val (current, constraints) = entry.value

            if (comp.needsReconstruction()) {
                constraints.forEach { it.removeConstraint() }
                val v1 = vertexToValue[comp.source]!!
                val v2 = vertexToValue[comp.target]!!
                comp.constrain(this, current, v1, v2)
                changedVars.add(current); changedVars.add(v1); changedVars.add(v2)
            }
        }
    }

    fun solve() {
        for (valu in this.vars) {
            if (valu is FloatVar) {
                valu.setDomain(MIN_FLOAT, MAX_FLOAT)
            }
        }

        changedVars.clear()
        this.consistency()
    }
}