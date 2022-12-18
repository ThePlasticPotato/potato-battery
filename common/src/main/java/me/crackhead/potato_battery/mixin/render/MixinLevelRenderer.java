package me.crackhead.potato_battery.mixin.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import me.crackhead.potato_battery.render.AABBSocketRender;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class MixinLevelRenderer {

    @Shadow @Final private RenderBuffers renderBuffers;

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;popPush(Ljava/lang/String;)V", ordinal = 11), method = "renderLevel")
    private void render(PoseStack poseStack,
                        float partialTick, long finishNanoTime, boolean renderBlockOutline,
                        Camera camera, GameRenderer gameRenderer, LightTexture lightTexture,
                        Matrix4f projectionMatrix, CallbackInfo ci) {

        poseStack.pushPose();

        poseStack.translate(-camera.getPosition().x, -camera.getPosition().y, -camera.getPosition().z);

        new AABBSocketRender(AABB.ofSize(Vec3.ZERO, 1, 1, 1), new BlockPos(0, 0, 0))
                .render(poseStack, this.renderBuffers.bufferSource(), partialTick);

        poseStack.popPose();
    }
}
