package io.github.lucaargolo.entitybanners.mixin;

import io.github.lucaargolo.entitybanners.EntityBanners;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Box;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(BannerBlockEntity.class)
public class BannerBlockEntityMixin extends BlockEntity implements Tickable {

    int entitybanners_TickDelay = 0;
    EntityType<?> entitybanners_Entity = null;

    public BannerBlockEntityMixin(BlockEntityType<?> type) {
        super(type);
    }

    @Inject(at = @At("HEAD"), method = "fromTag")
    public void onFromTag(BlockState state, CompoundTag tag, CallbackInfo ci) {
        if(tag.contains("entitybanners_Entity")) {
            Identifier identifier = new Identifier(tag.getString("entitybanners_Entity"));
            entitybanners_Entity = Registry.ENTITY_TYPE.get(identifier);
        }
    }

    @Inject(at = @At("HEAD"), method = "toTag")
    public void onToTag(CompoundTag tag, CallbackInfoReturnable<CompoundTag> cir) {
        if(entitybanners_Entity != null) {
            Identifier identifier = Registry.ENTITY_TYPE.getId(entitybanners_Entity);
            tag.putString("entitybanners_Entity", identifier.toString());
        }
    }

    @Override
    public void tick() {
        if(this.world != null && !this.world.isClient && entitybanners_Entity != null) {
            if(entitybanners_TickDelay >= 20) {
                entitybanners_TickDelay = 0;
                int r = EntityBanners.INSTANCE.getCONFIG().getBannerRadius();
                Box effectArea = new Box(pos.getX()-r, pos.getY()-r, pos.getZ()-r, pos.getX()+r, pos.getY()+r, pos.getZ()+r);
                List<ServerPlayerEntity> players = this.world.getNonSpectatingEntities(ServerPlayerEntity.class, effectArea);
                players.forEach(player -> EntityBanners.INSTANCE.onPlayerNearBanner(player, entitybanners_Entity));
            }else{
                entitybanners_TickDelay++;
            }
        }
    }

}
