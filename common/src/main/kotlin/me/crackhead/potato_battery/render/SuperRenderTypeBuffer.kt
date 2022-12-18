package me.crackhead.potato_battery.render

import com.mojang.blaze3d.vertex.BufferBuilder
import com.mojang.blaze3d.vertex.VertexConsumer
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap
import net.minecraft.Util
import net.minecraft.client.renderer.ChunkBufferBuilderPack
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.Sheets
import net.minecraft.client.resources.model.ModelBakery
import java.util.*
import java.util.function.Consumer


class SuperRenderTypeBuffer : MultiBufferSource {
    private val earlyBuffer: SuperRenderTypeBufferPhase
    private val defaultBuffer: SuperRenderTypeBufferPhase
    private val lateBuffer: SuperRenderTypeBufferPhase

    init {
        earlyBuffer = SuperRenderTypeBufferPhase()
        defaultBuffer = SuperRenderTypeBufferPhase()
        lateBuffer = SuperRenderTypeBufferPhase()
    }

    fun getEarlyBuffer(type: RenderType): VertexConsumer {
        return earlyBuffer.bufferSource.getBuffer(type)
    }

    override fun getBuffer(type: RenderType): VertexConsumer {
        return defaultBuffer.bufferSource.getBuffer(type)
    }

    fun getLateBuffer(type: RenderType): VertexConsumer {
        return lateBuffer.bufferSource.getBuffer(type)
    }

    fun draw() {
        earlyBuffer.bufferSource.endBatch()
        defaultBuffer.bufferSource.endBatch()
        lateBuffer.bufferSource.endBatch()
    }

    fun draw(type: RenderType) {
        earlyBuffer.bufferSource.endBatch(type)
        defaultBuffer.bufferSource.endBatch(type)
        lateBuffer.bufferSource.endBatch(type)
    }

    private class SuperRenderTypeBufferPhase {
        // Visible clones from RenderBuffers
        private val fixedBufferPack = ChunkBufferBuilderPack()
        private val fixedBuffers: SortedMap<RenderType, BufferBuilder> = Util.make(
            Object2ObjectLinkedOpenHashMap()
        ) { map ->
            map[Sheets.solidBlockSheet()] = fixedBufferPack.builder(RenderType.solid())
            map[Sheets.cutoutBlockSheet()] = fixedBufferPack.builder(RenderType.cutout())
            map[Sheets.bannerSheet()] = fixedBufferPack.builder(RenderType.cutoutMipped())
            map[Sheets.translucentCullBlockSheet()] = fixedBufferPack.builder(RenderType.translucent())
            put(
                map,
                Sheets.shieldSheet()
            )
            put(
                map,
                Sheets.bedSheet()
            )
            put(
                map,
                Sheets.shulkerBoxSheet()
            )
            put(
                map,
                Sheets.signSheet()
            )
            put(
                map,
                Sheets.chestSheet()
            )
            put(
                map,
                RenderType.translucentNoCrumbling()
            )
            put(
                map,
                RenderType.armorGlint()
            )
            put(
                map,
                RenderType.armorEntityGlint()
            )
            put(
                map,
                RenderType.glint()
            )
            put(
                map,
                RenderType.glintDirect()
            )
            put(
                map,
                RenderType.glintTranslucent()
            )
            put(
                map,
                RenderType.entityGlint()
            )
            put(
                map,
                RenderType.entityGlintDirect()
            )
            put(
                map,
                RenderType.waterMask()
            )
            ModelBakery.DESTROY_TYPES.forEach(Consumer { p_173062_: RenderType ->
                put(
                    map,
                    p_173062_
                )
            })
        }
        val bufferSource = MultiBufferSource.immediateWithBuffers(fixedBuffers, BufferBuilder(256))

        companion object {
            private fun put(map: Object2ObjectLinkedOpenHashMap<RenderType?, BufferBuilder?>, type: RenderType) {
                map[type] = BufferBuilder(type.bufferSize())
            }
        }
    }

    companion object {
        val instance = SuperRenderTypeBuffer()
    }
}