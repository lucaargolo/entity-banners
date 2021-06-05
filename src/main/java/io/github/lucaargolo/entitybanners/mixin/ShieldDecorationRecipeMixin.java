package io.github.lucaargolo.entitybanners.mixin;

import io.github.lucaargolo.entitybanners.EntityBanners;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.ShieldDecorationRecipe;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShieldDecorationRecipe.class)
public class ShieldDecorationRecipeMixin {

    @Inject(at = @At("RETURN"), method = "matches", cancellable = true)
    public void matches(CraftingInventory craftingInventory, World world, CallbackInfoReturnable<Boolean> cir) {
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
