package io.github.lucaargolo.entitybanners.mixin;

import io.github.lucaargolo.entitybanners.client.EntityBannersClient;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = AbstractInventoryScreen.class, priority = -1000)
public abstract class AbstractInventoryScreenMixin<T extends ScreenHandler> extends HandledScreen<T> {

    public AbstractInventoryScreenMixin(T handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Inject(at = @At("TAIL"), method = "drawStatusEffectDescriptions")
    public void onFinishDrawingStatusEffect(MatrixStack matrixStack, int i, int j, Iterable<StatusEffectInstance> iterable, CallbackInfo ci) {
        EntityBannersClient.INSTANCE.scheduleBannerEffectTooltipDraw(j, iterable);
    }

    @Inject(at = @At("TAIL"), method = "render")
    public void onFinishRender(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        EntityBannersClient.INSTANCE.drawBannerEffectTooltip(this, this.x, this.y, matrices);
    }

}
