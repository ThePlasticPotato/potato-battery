package me.crackhead.potato_battery.sim.solver

import org.jacop.floats.constraints.*
import org.jacop.floats.core.FloatVar

sealed interface Constraint {
    fun build(builder: ConstrainBuilder): org.jacop.constraints.Constraint
}
private interface InvalidConstraint: Constraint {
    override fun build(builder: ConstrainBuilder): org.jacop.constraints.Constraint = throw UnsupportedOperationException()
}

interface BiConstraint: Constraint {
    fun swap(): BiConstraint
}

data class ConstantConstraint(val constant: Double): InvalidConstraint
data class DynamicConstraint(val value: SolverValue): InvalidConstraint

data class PlusConstraint(val left: Constraint, val right: Constraint): BiConstraint, InvalidConstraint {
    override fun swap(): BiConstraint = PlusConstraint(right, left)
}

data class MinusConstraint(val left: Constraint, val right: Constraint): BiConstraint, InvalidConstraint {
    override fun swap(): BiConstraint = MinusConstraint(right, left)
}

data class MulConstraint(val left: Constraint, val right: Constraint): BiConstraint, InvalidConstraint {
    override fun swap(): BiConstraint = MulConstraint(right, left)
}

data class SumConstraint(val list: List<SolverValue>): InvalidConstraint

data class NegativeConstraint(val value: SolverValue): InvalidConstraint

data class BiggerThenConstraint(val left: Constraint, val right: Constraint): BiConstraint {
    override fun swap(): BiConstraint = LesserThenEqualConstraint(right, left)

    override fun build(builder: ConstrainBuilder): org.jacop.constraints.Constraint = swap().build(builder)
}

data class BiggerThenEqualConstraint(val left: Constraint, val right: Constraint): BiConstraint {
    override fun swap(): BiConstraint = LesserThenConstraint(right, left)

    override fun build(builder: ConstrainBuilder): org.jacop.constraints.Constraint = swap().build(builder)
}

data class LesserThenConstraint(val left: Constraint, val right: Constraint): BiConstraint {
    override fun swap(): BiConstraint = BiggerThenEqualConstraint(right, left)

    override fun build(builder: ConstrainBuilder): org.jacop.constraints.Constraint = when(left) {
        is ConstantConstraint -> when (right) {
            is ConstantConstraint -> throw UnsupportedOperationException()
            is DynamicConstraint -> PgtC(right.value, left.constant)
            else -> throw UnsupportedOperationException()
        }
        is DynamicConstraint -> when (right) {
            is ConstantConstraint -> PltC(left.value, right.constant)
            is DynamicConstraint -> PltQ(left.value, right.value)
            else -> throw UnsupportedOperationException()
        }
        else -> throw UnsupportedOperationException()
    }
}

data class LesserThenEqualConstraint(val left: Constraint, val right: Constraint): BiConstraint {
    override fun swap(): BiConstraint = BiggerThenConstraint(right, left)

    override fun build(builder: ConstrainBuilder): org.jacop.constraints.Constraint = when(left) {
        is ConstantConstraint -> when (right) {
            is ConstantConstraint -> throw UnsupportedOperationException()
            is DynamicConstraint -> PgteqC(right.value, left.constant)
            else -> throw UnsupportedOperationException()
        }
        is DynamicConstraint -> when (right) {
            is ConstantConstraint -> PlteqC(left.value, right.constant)
            is DynamicConstraint -> PlteqQ(left.value, right.value)
            else -> throw UnsupportedOperationException()
        }
        else -> throw UnsupportedOperationException()
    }
}

data class EqualsConstraint(val left: Constraint, val right: Constraint): BiConstraint {

    override fun swap(): BiConstraint = EqualsConstraint(right, left)
    override fun build(builder: ConstrainBuilder): org.jacop.constraints.Constraint = when(left) {
        is ConstantConstraint -> when (right) {
            is ConstantConstraint -> throw UnsupportedOperationException()
            is DynamicConstraint -> PeqC(right.value, left.constant)
            else -> throw UnsupportedOperationException()
        }
        is DynamicConstraint -> when (right) {
            is ConstantConstraint -> PeqC(left.value, right.constant)
            is DynamicConstraint -> PeqQ(left.value, right.value)
            else -> throw UnsupportedOperationException()
        }

        is PlusConstraint -> when (right) {
            is ConstantConstraint ->  when (left.left) {
                is DynamicConstraint -> when (left.right) {
                    is DynamicConstraint ->
                        PplusQeqR(left.left.value, left.right.value, builder.wrap(right.constant))
                    else -> throw UnsupportedOperationException()
                }
                else -> throw UnsupportedOperationException()
            }
            is DynamicConstraint ->  when (left.left) {
                is DynamicConstraint -> when (left.right) {
                    is DynamicConstraint -> PplusQeqR(left.left.value, left.right.value, right.value)
                    is ConstantConstraint -> PplusCeqR(left.left.value, left.right.constant, right.value)
                    else -> throw UnsupportedOperationException()
                }
                is ConstantConstraint ->  when (left.right) {
                    is DynamicConstraint -> PplusCeqR(left.right.value, left.left.constant, right.value)
                    else -> throw UnsupportedOperationException()
                }
                else -> throw UnsupportedOperationException()
            }
            else -> throw UnsupportedOperationException()
        }

        is MinusConstraint -> when (right) {
            is ConstantConstraint ->  when (left.left) {
                is DynamicConstraint -> when (left.right) {
                    is DynamicConstraint ->
                        PminusQeqR(left.left.value, left.right.value, builder.wrap(right.constant))
                    else -> throw UnsupportedOperationException()
                }
                else -> throw UnsupportedOperationException()
            }
            is DynamicConstraint ->  when (left.left) {
                is DynamicConstraint -> when (left.right) {
                    is DynamicConstraint -> PminusQeqR(left.left.value, left.right.value, right.value)
                    is ConstantConstraint -> PminusCeqR(left.left.value, left.right.constant, right.value)
                    else -> throw UnsupportedOperationException()
                }
                is ConstantConstraint ->  when (left.right) {
                    is DynamicConstraint -> PminusCeqR(left.right.value, left.left.constant, right.value)
                    else -> throw UnsupportedOperationException()
                }
                else -> throw UnsupportedOperationException()
            }
            else -> throw UnsupportedOperationException()
        }

        is MulConstraint -> when (right) {
            is ConstantConstraint ->  when (left.left) {
                is DynamicConstraint -> when (left.right) {
                    is DynamicConstraint ->
                        PmulQeqR(left.left.value, left.right.value, builder.wrap(right.constant))
                    else -> throw UnsupportedOperationException()
                }
                else -> throw UnsupportedOperationException()
            }
            is DynamicConstraint ->  when (left.left) {
                is DynamicConstraint -> when (left.right) {
                    is DynamicConstraint -> PmulQeqR(left.left.value, left.right.value, right.value)
                    is ConstantConstraint -> PmulCeqR(left.left.value, left.right.constant, right.value)
                    else -> throw UnsupportedOperationException()
                }
                is ConstantConstraint ->  when (left.right) {
                    is DynamicConstraint -> PmulCeqR(left.right.value, left.left.constant, right.value)
                    else -> throw UnsupportedOperationException()
                }
                else -> throw UnsupportedOperationException()
            }
            else -> throw UnsupportedOperationException()
        }

        is SumConstraint -> when (right) {
            is ConstantConstraint -> SumFloat(left.list.toTypedArray(), "==", builder.wrap(right.constant))
            is DynamicConstraint -> SumFloat(left.list.toTypedArray(), "==", right.value)
            else -> throw UnsupportedOperationException()
        }

        is NegativeConstraint -> when (right) {
            is ConstantConstraint -> PeqC(left.value, -right.constant)
            is DynamicConstraint -> PmulCeqR(left.value, -1.0, right.value)
            else -> throw UnsupportedOperationException()
        }

        else -> throw UnsupportedOperationException()
    }
}

class ConstrainBuilder(val model: Model) {
    operator fun Double.plus(other: SolverValue): Constraint = PlusConstraint(ConstantConstraint(this), DynamicConstraint(other))
    operator fun SolverValue.plus(other: Double): Constraint = PlusConstraint(DynamicConstraint(this), ConstantConstraint(other))
    operator fun SolverValue.plus(other: SolverValue): Constraint = PlusConstraint(DynamicConstraint(this), DynamicConstraint(other))

    operator fun Double.minus(other: SolverValue): Constraint = MinusConstraint(ConstantConstraint(this), DynamicConstraint(other))
    operator fun SolverValue.minus(other: Double): Constraint = MinusConstraint(DynamicConstraint(this), ConstantConstraint(other))
    operator fun SolverValue.minus(other: SolverValue): Constraint = MinusConstraint(DynamicConstraint(this), DynamicConstraint(other))

    operator fun Double.times(other: SolverValue): Constraint = MulConstraint(ConstantConstraint(this), DynamicConstraint(other))
    operator fun SolverValue.times(other: Double): Constraint = MulConstraint(DynamicConstraint(this), ConstantConstraint(other))
    operator fun SolverValue.times(other: SolverValue): Constraint = MulConstraint(DynamicConstraint(this), DynamicConstraint(other))

    infix fun Constraint.eq(other: Double): Constraint = EqualsConstraint(this, ConstantConstraint(other))
    infix fun Constraint.eq(other: SolverValue): Constraint = EqualsConstraint(this, DynamicConstraint(other))

    infix fun SolverValue.eq(other: SolverValue): Constraint = EqualsConstraint(DynamicConstraint(this), DynamicConstraint(other))
    infix fun SolverValue.eq(other: Double): Constraint = EqualsConstraint(DynamicConstraint(this), ConstantConstraint(other))

    infix fun Double.eq(other: SolverValue): Constraint = EqualsConstraint(ConstantConstraint(this), DynamicConstraint(other))

    fun sum(values: List<SolverValue>): Constraint = SumConstraint(values)

    operator fun SolverValue.unaryMinus(): Constraint = NegativeConstraint(this)

    infix fun SolverValue.gt(other: SolverValue): Constraint =
        BiggerThenConstraint(DynamicConstraint(this), DynamicConstraint(other))

    infix fun SolverValue.lt(other: SolverValue): Constraint =
        LesserThenConstraint(DynamicConstraint(this), DynamicConstraint(other))

    infix fun SolverValue.gt(other: Double): Constraint =
        BiggerThenConstraint(DynamicConstraint(this), ConstantConstraint(other))

    infix fun SolverValue.lt(other: Double): Constraint =
        LesserThenConstraint(DynamicConstraint(this), ConstantConstraint(other))

    infix fun SolverValue.gte(other: SolverValue): Constraint =
        BiggerThenEqualConstraint(DynamicConstraint(this), DynamicConstraint(other))

    infix fun SolverValue.lte(other: SolverValue): Constraint =
        LesserThenEqualConstraint(DynamicConstraint(this), DynamicConstraint(other))

    infix fun SolverValue.gte(other: Double): Constraint =
        BiggerThenEqualConstraint(DynamicConstraint(this), ConstantConstraint(other))

    infix fun SolverValue.lte(other: Double): Constraint =
        LesserThenEqualConstraint(DynamicConstraint(this), ConstantConstraint(other))


    internal fun makeTMP(function: (SolverValue) -> org.jacop.constraints.Constraint): SolverValue {
        val value = model.getUniqueValue()
        model.store.impose(function(value))
        return value
    }

    internal fun wrap(value: Double): SolverValue =  model.wrap(value)
}