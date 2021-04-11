package io.github.lucaargolo.entitybanners.utils

import net.minecraft.entity.EntityType
import net.minecraft.server.network.ServerPlayerEntity

class EntityBannerStatusEffectHolders {

    open class Client {
        val entitySet = linkedSetOf<EntityType<*>>()
        companion object INSTANCE: Client()
    }

    open class Server {
        val map = linkedMapOf<ServerPlayerEntity, LinkedHashMap<EntityType<*>, Int>>()
        companion object INSTANCE: Server()
    }

}