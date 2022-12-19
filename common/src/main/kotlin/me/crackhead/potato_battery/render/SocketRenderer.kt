package me.crackhead.potato_battery.render

import com.google.common.cache.CacheBuilder
import com.mojang.blaze3d.vertex.PoseStack
import me.crackhead.potato_battery.api.SocketsBlockEntity
import me.crackhead.potato_battery.api.wire.Socket
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.core.BlockPos

object SocketRenderer {
    private val sockets = CacheBuilder.newBuilder()
        .maximumSize(16)
        .build<Socket, AABBSocketRender>()

    fun render(
        be: SocketsBlockEntity,
        poseStack: PoseStack,
        blockPos: BlockPos,
        camX: Double,
        camY: Double,
        camZ: Double,
        bufferSource: MultiBufferSource.BufferSource,
        partialTick: Float
    ) {
        poseStack.translate(-camX + blockPos.x, -camY + blockPos.y, -camZ + blockPos.z)
        be.sockets.forEach {
            render(it, poseStack, bufferSource, partialTick)
        }
    }

    fun render(
        it: Socket,
        poseStack: PoseStack,
        bufferSource: MultiBufferSource.BufferSource,
        partialTick: Float
    ) {
        sockets.get(it) { AABBSocketRender(it.aabb) }.render(poseStack, bufferSource, partialTick)
    }
}