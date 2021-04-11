package io.github.lucaargolo.entitybanners.mixin;

import com.mojang.datafixers.util.Pair;
import io.github.fablabsmc.fablabs.api.bannerpattern.v1.LoomPattern;
import io.github.fablabsmc.fablabs.impl.bannerpattern.LoomPatternData;
import io.github.lucaargolo.entitybanners.client.EntityBannersClient;
import io.github.lucaargolo.entitybanners.utils.RenderEntityCache;
import io.github.lucaargolo.entitybanners.utils.EntityLoomPattern;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BannerBlockEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = BannerBlockEntityRenderer.class, priority = 0)
public class BannerBlockEntityRendererMixin {

    @SuppressWarnings({"JavaReflectionMemberAccess", "unchecked"})
    @Inject(at = @At("RETURN"), method = "renderCanvas")
    private static void renderCanvas(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, ModelPart canvas, SpriteIdentifier baseSprite, boolean isBanner, List<Pair<BannerPattern, DyeColor>> patterns, boolean bl, CallbackInfo ci) {
        try {
            Object object = BannerBlockEntityRenderer.class.getDeclaredField("loomPatterns").get(null);
            ((List<LoomPatternData>) object).forEach(loomPatternData -> {
                LoomPattern customPattern = loomPatternData.pattern;
                if(customPattern instanceof EntityLoomPattern) {
                    MinecraftClient client = MinecraftClient.getInstance();
                    EntityType<?> type = ((EntityLoomPattern) customPattern).getEntityType();
                    if(client.world != null) {
                        LivingEntity livingEntity = RenderEntityCache.INSTANCE.createOrGetEntity(type, client.world);
                        if (livingEntity != null) {
                            EntityRenderDispatcher renderDispatcher = client.getEntityRenderDispatcher();
                            matrices.push();
                            canvas.rotate(matrices);
                            matrices.translate(0.0, 0.0, -0.15);
                            EntityBannersClient.INSTANCE.drawEntityOnCanvas(renderDispatcher, livingEntity, matrices, vertexConsumers, light);
                            matrices.pop();
                        }
                    }
                }
            });
        } catch (ClassCastException | IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

}
