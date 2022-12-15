package me.crackhead.potato_battery.sim

import com.mxgraph.layout.mxCircleLayout
import me.crackhead.potato_battery.PotatoBatteryMod
import org.jgrapht.ext.JGraphXAdapter
import java.awt.Dimension
import javax.swing.JFrame


object Visualizer {
    private val defaultWindowSize: Dimension = Dimension(530, 320)
    private val adapter = JGraphXAdapter(PotatoBatteryMod.graph)
    private val graphComponent = com.mxgraph.swing.mxGraphComponent(adapter)
    private val frame = JFrame()

    init {
        graphComponent.graph.isAllowDanglingEdges = false
        graphComponent.isConnectable = false
        graphComponent.preferredSize = defaultWindowSize

        val layout = mxCircleLayout(adapter)
        val radius = 100
        layout.x0 = defaultWindowSize.width / 2.0 - radius
        layout.y0 = defaultWindowSize.height / 2.0 - radius
        layout.radius = radius.toDouble()
        layout.isMoveCircle = true
        layout.execute(adapter.defaultParent)

        frame.defaultCloseOperation = JFrame.HIDE_ON_CLOSE
        frame.contentPane.add(graphComponent)
        frame.pack()
    }

    fun visualize() {
        frame.isVisible = true
    }

}