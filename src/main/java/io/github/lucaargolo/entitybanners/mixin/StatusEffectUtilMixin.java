package io.github.lucaargolo.entitybanners.mixin;

import io.github.lucaargolo.entitybanners.EntityBanners;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StatusEffectUtil.class)
public class StatusEffectUtilMixin {

    @Inject(at = @At("HEAD"), method = "durationToString", cancellable = true)
    private static void onDurationToString(StatusEffectInstance effect, float multiplier, CallbackInfoReturnable<String> cir) {
        if(effect.getEffectType() == EntityBanners.INSTANCE.getENTITY_BANNER_STATUS_EFFECT()) {
            cir.setReturnValue(new TranslatableText("tooltip.entitybanners.nearby_banner").getString());
        }
    }

}
