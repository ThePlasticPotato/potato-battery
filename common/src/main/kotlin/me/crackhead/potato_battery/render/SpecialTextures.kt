package me.crackhead.potato_battery.render

import com.mojang.blaze3d.systems.RenderSystem
import me.crackhead.potato_battery.PotatoBatteryMod
import net.minecraft.resources.ResourceLocation


enum class SpecialTextures(filename: String) {
    BLANK("blank.png"), CHECKERED("checkerboard.png"), THIN_CHECKERED("thin_checkerboard.png"), CUTOUT_CHECKERED("cutout_checkerboard.png"), HIGHLIGHT_CHECKERED(
        "highlighted_checkerboard.png"
    ),
    SELECTION("selection.png");

    val ASSET_PATH = "textures/special/"
    val location: ResourceLocation

    init {
        location = PotatoBatteryMod.asResource(ASSET_PATH + filename)!!
    }

    fun bind() {
        RenderSystem.setShaderTexture(0, location)
    }

}