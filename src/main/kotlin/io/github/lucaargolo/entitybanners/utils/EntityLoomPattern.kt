package io.github.lucaargolo.entitybanners.utils

import io.github.fablabsmc.fablabs.api.bannerpattern.v1.LoomPattern
import io.github.lucaargolo.entitybanners.EntityBanners
import io.github.lucaargolo.entitybanners.mixin.SpawnEggItemAccessor
import net.minecraft.entity.EntityType
import net.minecraft.util.Identifier

class EntityLoomPattern(val entityType: EntityType<*>): LoomPattern(true) {

    override fun getSpriteId(type: String?) = Identifier("entity/banner/base")

    val primaryColor: Int
        get() = (EntityBanners.REGISTERED_EGGS[entityType] as? SpawnEggItemAccessor)?.primaryColor ?: 0

    val secondaryColor: Int
        get() = (EntityBanners.REGISTERED_EGGS[entityType] as? SpawnEggItemAccessor)?.secondaryColor ?: 0

}