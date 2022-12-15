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
import org.jgrapht.graph.builder.GraphBuilder
import java.util.function.Supplier

fun main() {

    var voltage = 4000.0
    val mySource = object : BiComponent() {
        override fun constrain(model: Model, current: SolverValue, voltage1: SolverValue, voltage2: SolverValue) {
            VoltageSource(voltage).constrain(model, current, voltage1, voltage2)
        }

        override fun needsReconstruction(): Boolean = true
    }

    val graph = DirectedMultigraph<PBVertex, BiComponent>(BiComponent::class.java)
    var counter = 0
    graph.vertexSupplier = Supplier { PBVertex(counter++, null) }

    var point = graph.addVertex()
    run {
        val s1 = graph.addVertex()
        val s2 = graph.addVertex()
        graph.addEdge(s1, s2, mySource)

        var prev = s1
        repeat(10000) {
            val new = if (it == 5000) point else graph.addVertex()
            graph.addEdge(prev, new, Diode())
            prev = new
        }

        graph.addEdge(prev, s2, Resistor(1000.0))
    }

    val model = Graph2Model.buildModel(graph) as ModelImpl


    var startTime = System.nanoTime()
    val label1 = DepthFirstSearch<FloatVar>()
    model.rebuildNeeded()
    val s = SplitSelectFloat(model, arrayOf(model.vertexToValue[point]), SmallestDomainFloat())
    model.solve()
    label1.labeling(model, s)
    println("First Run: Took ${System.nanoTime() - startTime}ns")

    voltage *= 2.0

    startTime = System.nanoTime()
    val label2 = DepthFirstSearch<FloatVar>()
    model.rebuildNeeded()
    val s2 = SplitSelectFloat(model, arrayOf(model.vertexToValue[point]), SmallestDomainFloat())
    model.solve()
    label2.labeling(model, s2)
    println("Second Run: Took ${System.nanoTime() - startTime}ns")
}