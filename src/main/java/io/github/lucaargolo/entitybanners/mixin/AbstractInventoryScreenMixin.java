package io.github.lucaargolo.entitybanners.mixin;

import io.github.lucaargolo.entitybanners.EntityBanners;
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
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Mixin(value = AbstractInventoryScreen.class, priority = -1000)
public abstract class AbstractInventoryScreenMixin<T extends ScreenHandler> extends HandledScreen<T> {

    public AbstractInventoryScreenMixin(T handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Inject(at = @At("TAIL"), method = "drawStatusEffects", locals = LocalCapture.CAPTURE_FAILSOFT)
    public void onFinishRender(MatrixStack matrices, int mouseX, int mouseY, CallbackInfo ci, int i, int j, Collection<StatusEffectInstance> collection, boolean bl, int k, Iterable<StatusEffectInstance> iterable) {
        if(bl) {
            EntityBannersClient.INSTANCE.drawBannerEffectTooltip(this, iterable, i, this.y, matrices);
        }
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/AbstractInventoryScreen;renderTooltip(Lnet/minecraft/client/util/math/MatrixStack;Ljava/util/List;Ljava/util/Optional;II)V"), method = "drawStatusEffects", locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
    public void onRenderTooltip(MatrixStack matrices, int mouseX, int mouseY, CallbackInfo ci, int i, int j, Collection<StatusEffectInstance> collection, boolean bl, int k, Iterable<StatusEffectInstance> iterable, int l, StatusEffectInstance statusEffectInstance, List<Text> list) {
        if(statusEffectInstance.getEffectType() == EntityBanners.INSTANCE.getENTITY_BANNER_STATUS_EFFECT()) {
            List<Text> newList = new ArrayList<>(list);
            EntityBannersClient.INSTANCE.appendBannerTooltip(newList);
            this.renderTooltip(matrices, newList, Optional.empty(), mouseX, mouseY);
            ci.cancel();
        }
    }

}
