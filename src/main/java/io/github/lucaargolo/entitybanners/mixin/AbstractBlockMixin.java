package io.github.lucaargolo.entitybanners.mixin;

import io.github.lucaargolo.entitybanners.mixed.BannerBlockEntityMixed;
import io.github.lucaargolo.entitybanners.utils.ModIdentifier;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(AbstractBlock.class)
public class AbstractBlockMixin {

    @Inject(at = @At("HEAD"), method = "getDroppedStacks", cancellable = true)
    public void getDroppedStacks(BlockState state, LootContext.Builder builder, CallbackInfoReturnable<List<ItemStack>> cir) {
        BlockEntity blockEntity = builder.getNullable(LootContextParameters.BLOCK_ENTITY);
        if(blockEntity instanceof BannerBlockEntityMixed) {
            EntityType<?> entityType = ((BannerBlockEntityMixed) blockEntity).entitybanners_getEntity();
            if(entityType != null) {
                Identifier identifier = new ModIdentifier("blocks/entity_banner");
                LootContext lootContext = builder.parameter(LootContextParameters.BLOCK_STATE, state).build(LootContextTypes.BLOCK);
                ServerWorld serverWorld = lootContext.getWorld();
                LootTable lootTable = serverWorld.getServer().getLootManager().getTable(identifier);
                cir.setReturnValue(lootTable.generateLoot(lootContext));
            }
        }
    }

}
