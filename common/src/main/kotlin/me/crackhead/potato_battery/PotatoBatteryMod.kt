package me.crackhead.potato_battery

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import me.crackhead.potato_battery.sim.PBVertex
import me.crackhead.potato_battery.sim.Visualizer
import net.minecraft.client.multiplayer.ClientSuggestionProvider
import net.minecraft.resources.ResourceLocation
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.DefaultListenableGraph
import org.jgrapht.graph.SimpleGraph

object PotatoBatteryMod {
    const val DEBUG = true
    const val MOD_ID = "pb"
    val graph = DefaultListenableGraph(SimpleGraph<PBVertex, DefaultEdge>(DefaultEdge::class.java))

    @JvmStatic
    fun init() {
        PotatoBatteryBlocks.register()
        //PotatoBatteryItems.register()
        PotatoBatteryBlockEntities.register()
    }

    @JvmStatic
    fun initClient() {

    }

    @JvmStatic
    fun registerClientCommands(dispatcher: CommandDispatcher<ClientSuggestionProvider>) {
        if (DEBUG) {
            dispatcher.register(
                literal<ClientSuggestionProvider>("pb").then(
                    literal<ClientSuggestionProvider>("visualize").executes {
                        try {
                            Visualizer.visualize()
                        } catch (e: Exception) {
                            e.printStackTrace()
                            throw e
                        }

                        1
                    })
            )
        }
    }

    val String.resource: ResourceLocation get() = ResourceLocation(MOD_ID, this)
}