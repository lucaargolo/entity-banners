package io.github.lucaargolo.entitybanners.mixed;

import net.minecraft.entity.EntityType;

public interface BannerBlockEntityMixed {

    EntityType<?> entitybanners_getEntity();

    int entitybanners_getTickDelay();

    void entitybanners_setTickDelay(int tickDelay);

}
