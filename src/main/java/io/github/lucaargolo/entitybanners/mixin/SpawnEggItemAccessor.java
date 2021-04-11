package io.github.lucaargolo.entitybanners.mixin;

import net.minecraft.item.SpawnEggItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SpawnEggItem.class)
public interface SpawnEggItemAccessor {

    @Accessor
    int getPrimaryColor();

    @Accessor
    int getSecondaryColor();

}
