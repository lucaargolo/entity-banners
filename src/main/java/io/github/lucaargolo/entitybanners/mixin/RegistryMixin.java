package io.github.lucaargolo.entitybanners.mixin;

import io.github.lucaargolo.entitybanners.EntityBanners;
import net.minecraft.entity.EntityType;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Registry.class)
public class RegistryMixin {

    @Inject(at = @At("HEAD"), method = "register(Lnet/minecraft/util/registry/Registry;Lnet/minecraft/util/Identifier;Ljava/lang/Object;)Ljava/lang/Object;")
    private static <V, T extends V> void entitybanners_registerBannersOnEntityEggRegistry(Registry<V> registry, Identifier id, T entry, CallbackInfoReturnable<T> cir) {
        if(entry instanceof SpawnEggItem item) {
            EntityBanners.INSTANCE.onEggRegistered(item);
        }else if(entry instanceof EntityType<?> entityType) {
            EntityBanners.INSTANCE.onEntityRegistered(entityType, id);
        }
    }


}
