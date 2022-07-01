package io.github.lucaargolo.entitybanners.utils

import io.github.lucaargolo.entitybanners.EntityBanners
import io.github.lucaargolo.entitybanners.mixin.SpawnEggItemAccessor
import net.minecraft.block.entity.BannerPattern
import net.minecraft.entity.EntityType

class EntityLoomPattern(val entityType: EntityType<*>, id: String): BannerPattern(id) {

    val primaryColor: Int
        get() = (EntityBanners.REGISTERED_EGGS[entityType] as? SpawnEggItemAccessor)?.primaryColor ?: 0

    val secondaryColor: Int
        get() = (EntityBanners.REGISTERED_EGGS[entityType] as? SpawnEggItemAccessor)?.secondaryColor ?: 0

}