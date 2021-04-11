package io.github.lucaargolo.entitybanners.network

import io.github.lucaargolo.entitybanners.utils.EntityBannerStatusEffectHolders
import io.github.lucaargolo.entitybanners.utils.ModIdentifier
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.util.registry.Registry

object PacketCompendium {

    val ADDED_ENTITY_BANNER_STATUS_EFFECT = ModIdentifier("added_entity_banner_status_effect")
    val REMOVED_ENTITY_BANNER_STATUS_EFFECT = ModIdentifier("removed_entity_banner_status_effect")

    fun initClient() {
        ClientPlayNetworking.registerGlobalReceiver(ADDED_ENTITY_BANNER_STATUS_EFFECT) { client, _, data, _ ->
            val entityRawId = data.readVarInt()
            val entityType = Registry.ENTITY_TYPE.get(entityRawId)
            client.execute {
                EntityBannerStatusEffectHolders.Client.entitySet.add(entityType)
            }
        }
        ClientPlayNetworking.registerGlobalReceiver(REMOVED_ENTITY_BANNER_STATUS_EFFECT) { client, _, data, _ ->
            val entityRawId = data.readVarInt()
            val entityType = Registry.ENTITY_TYPE.get(entityRawId)
            client.execute {
                EntityBannerStatusEffectHolders.Client.entitySet.remove(entityType)
            }
        }
    }


}