package me.crackhead.potato_battery.mixin.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import me.crackhead.potato_battery.api.SocketsBlockEntity;
import me.crackhead.potato_battery.render.AABBSocketRender;
import me.crackhead.potato_battery.render.SocketRenderer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class MixinLevelRenderer {

    @Shadow @Final private RenderBuffers renderBuffers;

    @Shadow @Final private Minecraft minecraft;

    @Shadow private @Nullable ClientLevel level;

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;popPush(Ljava/lang/String;)V", ordinal = 11), method = "renderLevel")
    private void render(PoseStack poseStack,
                        float partialTick, long finishNanoTime, boolean renderBlockOutline,
                        Camera camera, GameRenderer gameRenderer, LightTexture lightTexture,
                        Matrix4f projectionMatrix, CallbackInfo ci) {
        double camX =  camera.getPosition().x, camY = camera.getPosition().y, camZ = camera.getPosition().z;

        var hitResult = this.minecraft.hitResult;
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            var bResult = ((BlockHitResult) hitResult);
            BlockEntity be = this.level.getBlockEntity(bResult.getBlockPos());

            if (be instanceof SocketsBlockEntity) {
                poseStack.pushPose();

                SocketRenderer.INSTANCE.render((SocketsBlockEntity) be, poseStack, bResult.getBlockPos(),
                        camX, camY, camZ, this.renderBuffers.bufferSource(), partialTick);

                poseStack.popPose();
            }
        }

        poseStack.pushPose();
        poseStack.translate(-camX, -camY, -camZ);

        new AABBSocketRender(AABB.ofSize(Vec3.ZERO, 1, 1, 1))
                .render(poseStack, this.renderBuffers.bufferSource(), 0);

        poseStack.popPose();
    }
}
