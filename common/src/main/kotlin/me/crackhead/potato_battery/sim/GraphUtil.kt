package me.crackhead.potato_battery.sim

import org.jgrapht.Graph
import org.jgrapht.graph.DefaultEdge

fun <P, E> Graph<P, E>.collapse(point1: P, point2: P): P {
    if (!this.containsVertex(point1)) return point2

    this.edgesOf(point1).forEach { edge ->
        val other1 = this.getEdgeSource(edge)
        val other2 = this.getEdgeTarget(edge)
        if (other1 != point1) {
            this.addEdge(other1, point2)
        } else if (other2 != point1) {
            this.addEdge(other2, point2)
        }
    }

    this.removeVertex(point1)

    return point2
}

fun <P, E> Graph<P, E>.addOrGet(point: P): P {
    return if (this.addVertex(point)) {
        point
    } else {
        this.vertexSet().find { it == point }!!
    }
}

abstract class MyDefaultEdge<P> : DefaultEdge() {
    override fun getSource(): P = super.getSource() as P
    override fun getTarget(): P = super.getTarget() as P

    operator fun component1(): P = getSource()
    operator fun component2(): P = getTarget()
}