package me.crackhead.potato_battery.sim.solver

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import me.crackhead.potato_battery.sim.PBVertex
import me.crackhead.potato_battery.sim.components.BiComponent
import org.jgrapht.graph.DefaultDirectedGraph
import org.jgrapht.graph.DirectedMultigraph
import org.jgrapht.graph.SimpleGraph

object Graph2Model {

    fun buildModel(graph: DirectedMultigraph<PBVertex, BiComponent>): Pair<Model, Array<SolverValue>> {
        val model = Model()

        val vertexToValue = HashMap<PBVertex, SolverValue>()
        val edgeToCurrent = HashMap<BiComponent, SolverValue>()

        graph.edgeSet().forEach {
            val v1 = graph.getEdgeSource(it)
            val v2 = graph.getEdgeTarget(it)

            val v1Value = vertexToValue.getOrPut(v1) { model.getUniqueValue(v1.name) }
            val v2Value = vertexToValue.getOrPut(v2) { model.getUniqueValue(v2.name) }

            val current = model.getUniqueValue()
            edgeToCurrent[it] = current

            it.constrain(model, current, v1Value, v2Value)
        }

        graph.vertexSet().forEach {
            val value = vertexToValue[it] ?: return@forEach

            model.constrain { sum(graph.edgesOf(it).mapNotNull {  edge ->
                val current = edgeToCurrent[edge] ?: return@mapNotNull null

                if (graph.getEdgeSource(edge) == it)
                    current
                else
                    model.makeAndConstrain { -current eq it }
            }) eq 0.0 }
        }



        val vars = vertexToValue.values.asSequence().plus(edgeToCurrent.values.asSequence()).toList().toTypedArray()

        return model to vars
    }

}