package io.github.lucaargolo.entitybanners.mixin;

import io.github.lucaargolo.entitybanners.mixed.DyeColorMixed;
import net.minecraft.block.MapColor;
import net.minecraft.util.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DyeColor.class)
public class DyeColorMixin implements DyeColorMixed {

    private int entitybanners_color = 0;

    @Override
    public int entitybanners_getColor() {
        return entitybanners_color;
    }

    @Inject(at = @At("TAIL"), method = "<init>")
    public void entitybanners_assignBannerColor(String enumName, int ordinal, int woolId, String name, int color, MapColor mapColor, int fireworkColor, int signColor, CallbackInfo ci) {
        this.entitybanners_color = color;
    }

}
