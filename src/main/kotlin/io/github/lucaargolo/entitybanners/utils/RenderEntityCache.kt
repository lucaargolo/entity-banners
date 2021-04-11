package io.github.lucaargolo.entitybanners.utils

import net.minecraft.client.world.ClientWorld
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity

object RenderEntityCache {

    private val map = linkedMapOf<EntityType<*>, LivingEntity>()

    fun createOrGetEntity(entityType: EntityType<*>, world: ClientWorld): LivingEntity? {
        return map.getOrPut(entityType) {
            val entity = entityType.create(world) as? LivingEntity ?: return null
            entity.calculateDimensions()
            entity
        }
    }

    fun clear() {
        map.clear()
    }

}