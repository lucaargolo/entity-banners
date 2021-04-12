package io.github.lucaargolo.entitybanners

import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import io.github.fablabsmc.fablabs.api.bannerpattern.v1.LoomPatterns
import io.github.lucaargolo.entitybanners.common.effects.EntityBannerStatusEffect
import io.github.lucaargolo.entitybanners.common.items.EntityBannerItem
import io.github.lucaargolo.entitybanners.network.PacketCompendium
import io.github.lucaargolo.entitybanners.utils.*
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.SpawnEggItem
import net.minecraft.network.MessageType
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.stat.Stats
import net.minecraft.text.TranslatableText
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import net.minecraft.util.Util
import net.minecraft.util.registry.Registry
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.File
import java.io.PrintWriter
import java.nio.file.Files
import java.util.*

object EntityBanners: ModInitializer {

    const val MOD_ID = "entitybanners"
    private val LOGGER: Logger = LogManager.getLogger("Entity Banners")

    val CONFIG: ModConfig by lazy {
        val parser = JsonParser()
        val gson = GsonBuilder().setPrettyPrinting().create()
        val configFile = File("${FabricLoader.getInstance().configDir}${File.separator}${MOD_ID}.json")
        var finalConfig: ModConfig
        LOGGER.info("Trying to read config file...")
        try {
            if (configFile.createNewFile()) {
                LOGGER.info("No config file found, creating a new one...")
                val json: String = gson.toJson(parser.parse(gson.toJson(ModConfig())))
                PrintWriter(configFile).use { out -> out.println(json) }
                finalConfig = ModConfig()
                LOGGER.info("Successfully created default config file.")
            } else {
                LOGGER.info("A config file was found, loading it..")
                finalConfig = gson.fromJson(String(Files.readAllBytes(configFile.toPath())), ModConfig::class.java)
                if (finalConfig == null) {
                    throw NullPointerException("The config file was empty.")
                } else {
                    LOGGER.info("Successfully loaded config file.")
                }
            }
        } catch (exception: Exception) {
            LOGGER.error("There was an error creating/loading the config file!", exception)
            finalConfig = ModConfig()
            LOGGER.warn("Defaulting to original config.")
        }
        finalConfig
    }

    val CREATIVE_TAB: ItemGroup by lazy {
        FabricItemGroupBuilder.create(ModIdentifier("creative_tab")).icon{ ENTITY_BANNER_ITEM.defaultStack }.build()
    }

    val REGISTERED_EGGS = linkedMapOf<EntityType<*>, SpawnEggItem>()
    val REGISTERED_PATTERNS = linkedMapOf<EntityType<*>, EntityLoomPattern>()

    val ENTITY_BANNER_ITEM: EntityBannerItem by lazy {
        Registry.register(Registry.ITEM, ModIdentifier("entity_banner"), EntityBannerItem(Item.Settings().group(CREATIVE_TAB).maxCount(16)))
    }
    val ENTITY_BANNER_STATUS_EFFECT: EntityBannerStatusEffect by lazy {
        Registry.register(Registry.STATUS_EFFECT, ModIdentifier("entity_banner_status_effect"), EntityBannerStatusEffect())
    }

    val DAMAGE_INCREASE = EntityAttributeModifier(UUID.fromString("7a0af36c-0f98-4627-8476-42c352bf3047"), "Entity banners damage increase", CONFIG.damageMultiplier, EntityAttributeModifier.Operation.MULTIPLY_TOTAL)

    private var tickDelay = 0

    override fun onInitialize() {
        ENTITY_BANNER_ITEM.toString()
        ENTITY_BANNER_STATUS_EFFECT.toString()
        ServerTickEvents.END_SERVER_TICK.register {
            if(tickDelay >= 20) {
                tickDelay = 0
                val firstMapIterator = EntityBannerStatusEffectHolders.Server.map.iterator()
                while(firstMapIterator.hasNext()) {
                    val firstMapEntry = firstMapIterator.next()
                    val player = firstMapEntry.key
                    val entityMap = firstMapEntry.value
                    val secondMapIterator = entityMap.iterator()
                    while(secondMapIterator.hasNext()) {
                        val secondMapEntry = secondMapIterator.next()
                        secondMapEntry.setValue(secondMapEntry.value - 20)
                        if(secondMapEntry.value <= 0) {
                            val entityType = secondMapEntry.key
                            val entityTypeRaw = Registry.ENTITY_TYPE.getRawId(entityType)
                            val buf = PacketByteBufs.create()
                            buf.writeVarInt(entityTypeRaw)
                            ServerPlayNetworking.send(player, PacketCompendium.REMOVED_ENTITY_BANNER_STATUS_EFFECT, buf)
                            secondMapIterator.remove()
                        }
                    }
                    if(entityMap.isEmpty()) {
                        firstMapIterator.remove()
                        player.removeStatusEffect(ENTITY_BANNER_STATUS_EFFECT)
                    }
                }
            }else{
                tickDelay++
            }
            BannerAttackerHolder.set.forEach { serverPlayerEntity ->
                serverPlayerEntity.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE)?.removeModifier(DAMAGE_INCREASE)
            }
            BannerAttackerHolder.set.clear()
        }
        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register { serverWorld, entity, livingEntity ->
            (entity as? ServerPlayerEntity)?.let { player ->
                REGISTERED_PATTERNS[livingEntity.type]?.let { loomPattern ->
                    val killed = player.statHandler.getStat(Stats.KILLED.getOrCreateStat(livingEntity.type))
                    if (killed % CONFIG.necessaryKills == 0) {
                        if(CONFIG.shouldBroadcastWhenGivenBanner) {
                            if(CONFIG.shouldBroadcastToEveryone) {
                                serverWorld.server.playerManager.broadcastChatMessage(TranslatableText("chat.entitybanners.killed_n_entities", player.name, killed, livingEntity.type.name).formatted(Formatting.GOLD), MessageType.CHAT, Util.NIL_UUID)
                            }else{
                                player.sendMessage(TranslatableText("chat.entitybanners.killed_n_entities", player.name, killed, livingEntity.type.name).formatted(Formatting.GOLD), false)
                            }
                        }
                        player.inventory.offerOrDrop(serverWorld, ENTITY_BANNER_ITEM.getPatternStack(loomPattern))
                    }
                }
            }
        }
    }

    fun onEggRegistered(item: SpawnEggItem) {
        REGISTERED_EGGS[item.getEntityType(null)] = item
    }

    fun onEntityRegistered(entityType: EntityType<*>, id: Identifier) {
        if(entityType.spawnGroup != SpawnGroup.MISC && !CONFIG.blacklistedEntities.contains(id.toString())) {
            REGISTERED_PATTERNS[entityType] = Registry.register(LoomPatterns.REGISTRY, Identifier("${id}_banner"), EntityLoomPattern(entityType))
        }
    }

    fun onPlayerNearBanner(player: ServerPlayerEntity, entityType: EntityType<*>) {
        player.addStatusEffect(StatusEffectInstance(ENTITY_BANNER_STATUS_EFFECT, 200))
        EntityBannerStatusEffectHolders.Server.map.getOrPut(player) { linkedMapOf() }[entityType] = 40
        val entityTypeRaw = Registry.ENTITY_TYPE.getRawId(entityType)
        val buf = PacketByteBufs.create()
        buf.writeVarInt(entityTypeRaw)
        ServerPlayNetworking.send(player, PacketCompendium.ADDED_ENTITY_BANNER_STATUS_EFFECT, buf)
    }

}