package me.crackhead.potato_battery.sim.solver

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import me.crackhead.potato_battery.sim.PBVertex
import me.crackhead.potato_battery.sim.components.BiComponent
import org.jgrapht.graph.DefaultDirectedGraph
import org.jgrapht.graph.DirectedMultigraph
import org.jgrapht.graph.SimpleGraph

object Graph2Model {

    fun buildModel(graph: DirectedMultigraph<PBVertex, BiComponent>): Model {
        val model = ModelImpl()

        graph.edgeSet().forEach {
            val v1 = graph.getEdgeSource(it)
            val v2 = graph.getEdgeTarget(it)

            val v1Value = model.vertexToValue.getOrPut(v1) { model.getUniqueValue(v1.name) }
            val v2Value = model.vertexToValue.getOrPut(v2) { model.getUniqueValue(v2.name) }

            val current = model.getUniqueValue()

            val tmpModel = ConstraintSetBuilder(model)
            it.constrain(tmpModel, current, v1Value, v2Value)

            model.edgeToCurrent[it] = current to tmpModel.constraints
        }

        graph.vertexSet().forEach {
            model.constrain { sum(graph.edgesOf(it).mapNotNull {  edge ->
                val (current, _) = model.edgeToCurrent[edge] ?: return@mapNotNull null

                if (graph.getEdgeSource(edge) == it)
                    current
                else
                    model.makeAndConstrain { -current eq it }
            }) eq 0.0 }
        }

        return model
    }

}