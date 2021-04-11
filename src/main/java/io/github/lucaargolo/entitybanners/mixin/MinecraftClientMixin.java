package io.github.lucaargolo.entitybanners.mixin;

import io.github.lucaargolo.entitybanners.utils.RenderEntityCache;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Inject(at = @At("HEAD"), method = "joinWorld")
    public void onJoinWorld(ClientWorld world, CallbackInfo ci) {
        RenderEntityCache.INSTANCE.clear();
    }

}
