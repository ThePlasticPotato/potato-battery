package me.crackhead.potato_battery.sim.solver

import me.crackhead.potato_battery.sim.PBVertex
import me.crackhead.potato_battery.sim.components.BiComponent
import me.crackhead.potato_battery.sim.components.Diode
import me.crackhead.potato_battery.sim.components.Resistor
import me.crackhead.potato_battery.sim.components.VoltageSource
import org.jacop.floats.core.FloatVar
import org.jacop.floats.search.SmallestDomainFloat
import org.jacop.floats.search.SplitSelectFloat
import org.jacop.search.DepthFirstSearch
import org.jacop.search.Search
import org.jacop.search.SelectChoicePoint
import org.jacop.search.SimpleSolutionListener
import org.jgrapht.graph.DefaultDirectedGraph
import org.jgrapht.graph.DirectedMultigraph
import java.util.function.Supplier

fun main() {
    var start: SolverValue? = null
    var end: SolverValue? = null

    val mySource = object : VoltageSource(12.0) {
        override fun constrain(model: Model, current: SolverValue, voltage1: SolverValue, voltage2: SolverValue) {
            super.constrain(model, current, voltage1, voltage2)
            start = voltage1
            end = voltage2
        }
    }

    val graph = DirectedMultigraph<PBVertex, BiComponent>(BiComponent::class.java)
    var counter = 0
    graph.vertexSupplier = Supplier { PBVertex(counter++, null) }

    run {
        val s1 = graph.addVertex()
        val s2 = graph.addVertex()
        graph.addEdge(s1, s2, mySource)
        val s3 = PBVertex(counter++, "divide"); graph.addVertex(s3)
        graph.addEdge(s1, s3, Resistor(220.0))
        graph.addEdge(s3, s2, Resistor(220.0))
        graph.addEdge(s3, s2, Diode())
    }

    val (model, vars) = Graph2Model.buildModel(graph)

    val label = DepthFirstSearch<FloatVar>()
    val s = SplitSelectFloat(model.store, vars, SmallestDomainFloat())
    label.setSolutionListener(object : SimpleSolutionListener<FloatVar>() {
        override fun executeAfterSolution(search: Search<FloatVar>, select: SelectChoicePoint<FloatVar>): Boolean {
            val parent = super.executeAfterSolution(search, select)

            select.variablesMapping.forEach { (k, v) ->
                println("${k.id()} = ${k.value()}")
            }

            return parent
        }
    })
    label.getSolutionListener().recordSolutions(true)
    label.getSolutionListener().searchAll(true)
    label.setAssignSolution(true)
    label.labeling(model.store, s)
}