package io.github.lucaargolo.entitybanners.mixin;

import io.github.lucaargolo.entitybanners.EntityBanners;
import io.github.lucaargolo.entitybanners.utils.BannerAttackerHolder;
import io.github.lucaargolo.entitybanners.utils.EntityBannerStatusEffectHolders;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.LinkedHashMap;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {

    @SuppressWarnings("SuspiciousMethodCalls")
    @ModifyVariable(method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", at = @At("HEAD"), argsOnly = true)
    private float entitybanners_injectCustomDamage(float amount, DamageSource source) {
        Entity entity = source.getSource();
        if(entity instanceof LivingEntity) {
            LinkedHashMap<EntityType<?>, Integer> map = EntityBannerStatusEffectHolders.Server.INSTANCE.getMap().get(this);
            if(map != null && map.containsKey(entity.getType())) {
                return amount*EntityBanners.INSTANCE.getCONFIG().getDefenseMultiplier();
            }
        }
        return amount;
    }

    @SuppressWarnings("ConstantConditions")
    @Inject(at = @At("HEAD"), method = "attack")
    public void entitybanners_injectCustomAttack(Entity target, CallbackInfo ci) {
        ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) ((Object) this);
        LinkedHashMap<EntityType<?>, Integer> map = EntityBannerStatusEffectHolders.Server.INSTANCE.getMap().get(serverPlayerEntity);
        if(map != null && map.containsKey(target.getType())) {
            serverPlayerEntity.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).addTemporaryModifier(EntityBanners.INSTANCE.getDAMAGE_INCREASE());
            BannerAttackerHolder.INSTANCE.getSet().add(serverPlayerEntity);
        }
    }

}
