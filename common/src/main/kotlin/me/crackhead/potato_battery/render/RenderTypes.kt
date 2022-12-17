package me.crackhead.potato_battery.render

import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.VertexFormat
import me.crackhead.potato_battery.PotatoBatteryMod
import me.crackhead.potato_battery.render.RenderTypes.name1
import me.crackhead.potato_battery.render.RenderTypes.runnable1
import me.crackhead.potato_battery.render.RenderTypes.runnable2
import net.minecraft.client.renderer.RenderStateShard
import net.minecraft.client.renderer.RenderType
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.inventory.InventoryMenu


object RenderTypes : RenderStateShard(name1, runnable1, runnable2) {

    val name1: String
        get() {
            return name1
        }
    val runnable1: Runnable
        get() {
            return runnable1
        }
    val runnable2: Runnable
        get() {
            return runnable2
        }
//    val name1: String = "RenderTypes"
//    private val setupState: Runnable? = null
//    private val clearState: Runnable? = null
//
//    override fun setupRenderState() {
//        setupState?.run()
//    }
//
//    override fun clearRenderState() {
//        clearState?.run()
//    }

    val outlineSolid: RenderType = RenderType.create(
        createLayerName("outline_solid"), DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false,
        false, RenderType.CompositeState.builder()
            .setShaderState(RENDERTYPE_ENTITY_SOLID_SHADER)
            .setTextureState(TextureStateShard(SpecialTextures.BLANK.location, false, false))
            .setCullState(CULL)
            .setLightmapState(LIGHTMAP)
            .setOverlayState(OVERLAY)
            .createCompositeState(false)
    )

    fun getOutlineTranslucent(texture: ResourceLocation?, cull: Boolean): RenderType {
        return RenderType.create(
            createLayerName("outline_translucent" + if (cull) "_cull" else ""),
            DefaultVertexFormat.NEW_ENTITY,
            VertexFormat.Mode.QUADS,
            256,
            false,
            true,
            RenderType.CompositeState.builder()
                .setShaderState(if (cull) RENDERTYPE_ENTITY_TRANSLUCENT_CULL_SHADER else RENDERTYPE_ENTITY_TRANSLUCENT_SHADER)
                .setTextureState(TextureStateShard(texture, false, false))
                .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                .setCullState(if (cull) CULL else NO_CULL)
                .setLightmapState(LIGHTMAP)
                .setOverlayState(OVERLAY)
                .setWriteMaskState(COLOR_WRITE)
                .createCompositeState(false)
        )
    }

    fun getGlowingSolid(texture: ResourceLocation?): RenderType {
        return RenderType.create(
            createLayerName("glowing_solid"), DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256,
            true, false, RenderType.CompositeState.builder()
                .setShaderState(NO_SHADER)
                .setTextureState(TextureStateShard(texture, false, false))
                .setCullState(CULL)
                .setLightmapState(LIGHTMAP)
                .setOverlayState(OVERLAY)
                .createCompositeState(true)
        )
    }

    val glowingSolid = getGlowingSolid(InventoryMenu.BLOCK_ATLAS)

    fun getGlowingTranslucent(texture: ResourceLocation?): RenderType {
        return RenderType.create(
            createLayerName("glowing_translucent"), DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS,
            256, true, true, RenderType.CompositeState.builder()
                .setShaderState(NO_SHADER)
                .setTextureState(TextureStateShard(texture, false, false))
                .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                .setLightmapState(LIGHTMAP)
                .setOverlayState(OVERLAY)
                .createCompositeState(true)
        )
    }

    val additive: RenderType = RenderType.create(
        createLayerName("additive"), DefaultVertexFormat.BLOCK,
        VertexFormat.Mode.QUADS, 256, true, true, RenderType.CompositeState.builder()
            .setShaderState(BLOCK_SHADER)
            .setTextureState(TextureStateShard(InventoryMenu.BLOCK_ATLAS, false, false))
            .setTransparencyState(ADDITIVE_TRANSPARENCY)
            .setCullState(NO_CULL)
            .setLightmapState(LIGHTMAP)
            .setOverlayState(OVERLAY)
            .createCompositeState(true)
    )
    val glowingTranslucent = getGlowingTranslucent(InventoryMenu.BLOCK_ATLAS)
    val itemPartialSolid: RenderType = RenderType.create(
        createLayerName("item_partial_solid"), DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true,
        false, RenderType.CompositeState.builder()
            .setShaderState(RENDERTYPE_ENTITY_SOLID_SHADER)
            .setTextureState(BLOCK_SHEET)
            .setCullState(CULL)
            .setLightmapState(LIGHTMAP)
            .setOverlayState(OVERLAY)
            .createCompositeState(true)
    )
    val itemPartialTranslucent: RenderType = RenderType.create(
        createLayerName("item_partial_translucent"),
        DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, RenderType.CompositeState.builder()
            .setShaderState(RENDERTYPE_ENTITY_TRANSLUCENT_CULL_SHADER)
            .setTextureState(BLOCK_SHEET)
            .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
            .setLightmapState(LIGHTMAP)
            .setOverlayState(OVERLAY)
            .createCompositeState(true)
    )
    val fluid: RenderType = RenderType.create(
        createLayerName("fluid"),
        DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder()
            .setShaderState(RENDERTYPE_ENTITY_TRANSLUCENT_CULL_SHADER)
            .setTextureState(BLOCK_SHEET_MIPPED)
            .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
            .setLightmapState(LIGHTMAP)
            .setOverlayState(OVERLAY)
            .createCompositeState(true)
    )

    private fun createLayerName(name: String): String {
        return PotatoBatteryMod.MOD_ID + ":" + name
    }


}