package io.github.lucaargolo.entitybanners.mixin;

import io.github.lucaargolo.entitybanners.EntityBanners;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BannerPattern.class)
public class BannerPatternMixin {

    @Inject(at = @At("HEAD"), method = "getSpriteId", cancellable = true)
    private static void entitybanners_fixSpriteId(RegistryKey<BannerPattern> pattern, boolean banner, CallbackInfoReturnable<Identifier> cir) {
        if(pattern.getValue().getNamespace().equals(EntityBanners.MOD_ID)) {
            cir.setReturnValue(new Identifier("minecraft:entity/banner/base"));
        }
    }


}
