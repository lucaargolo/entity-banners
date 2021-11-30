package io.github.lucaargolo.entitybanners.mixin;

import io.github.lucaargolo.entitybanners.mixed.BannerBlockEntityMixed;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BannerBlockEntity.class)
public class BannerBlockEntityMixin implements BannerBlockEntityMixed {

    private int entitybanners_TickDelay = 0;
    private EntityType<?> entitybanners_Entity = null;

    @Inject(at = @At("HEAD"), method = "readNbt")
    public void onReadNbt(NbtCompound nbt, CallbackInfo ci) {
        if(nbt.contains("entitybanners_Entity")) {
            Identifier identifier = new Identifier(nbt.getString("entitybanners_Entity"));
            entitybanners_Entity = Registry.ENTITY_TYPE.get(identifier);
        }
    }

    @Inject(at = @At("HEAD"), method = "writeNbt")
    public void onWriteNbt(NbtCompound nbt, CallbackInfo ci) {
        if(entitybanners_Entity != null) {
            Identifier identifier = Registry.ENTITY_TYPE.getId(entitybanners_Entity);
            nbt.putString("entitybanners_Entity", identifier.toString());
        }
    }

    @Override
    public EntityType<?> entitybanners_getEntity() {
        return entitybanners_Entity;
    }

    @Override
    public int entitybanners_getTickDelay() {
        return entitybanners_TickDelay;
    }

    @Override
    public void entitybanners_setTickDelay(int tickDelay) {
        entitybanners_TickDelay = tickDelay;
    }
}
