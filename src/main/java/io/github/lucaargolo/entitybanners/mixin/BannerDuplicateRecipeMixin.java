package io.github.lucaargolo.entitybanners.mixin;

import io.github.lucaargolo.entitybanners.EntityBanners;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.BannerDuplicateRecipe;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BannerDuplicateRecipe.class)
public class BannerDuplicateRecipeMixin {

    @Inject(at = @At("RETURN"), method = "matches(Lnet/minecraft/inventory/CraftingInventory;Lnet/minecraft/world/World;)Z", cancellable = true)
    public void entitybanners_fixDuplicateRecipe(CraftingInventory craftingInventory, World world, CallbackInfoReturnable<Boolean> cir) {
        if(cir.getReturnValue()) {
            for(int i = 0; i < craftingInventory.size(); ++i) {
                ItemStack stack = craftingInventory.getStack(i);
                if(stack.getItem() == EntityBanners.INSTANCE.getENTITY_BANNER_ITEM()) {
                    cir.setReturnValue(false);
                }
            }
        }
    }

}
