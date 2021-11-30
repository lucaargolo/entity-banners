package io.github.lucaargolo.entitybanners.client

import io.github.lucaargolo.entitybanners.EntityBanners.ENTITY_BANNER_STATUS_EFFECT
import io.github.lucaargolo.entitybanners.network.PacketCompendium
import io.github.lucaargolo.entitybanners.utils.EntityBannerStatusEffectHolders
import net.fabricmc.api.ClientModInitializer
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRenderDispatcher
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.Formatting
import net.minecraft.util.math.Vec3f
import java.util.function.Consumer

object EntityBannersClient: ClientModInitializer {

    override fun onInitializeClient() {
        PacketCompendium.initClient()
    }

    fun drawEntityOnCanvas(renderDispatcher: EntityRenderDispatcher, entity: LivingEntity, matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, light: Int) {
        var width = entity.boundingBox.xLength
        var height = entity.boundingBox.yLength
        if(width > 0.6) {
            width *= 1f/(width.toFloat()/0.6f)
            height = entity.boundingBox.yLength*(width/entity.boundingBox.xLength)
        }
        if(height > 2.0) {
            width *= 1f/(height/2f)
            height = entity.boundingBox.yLength*(width/entity.boundingBox.xLength)
        }
        matrices.translate(0.0, 1.2+(((1.3*height)/2.0)/2.0), 0.0)
        matrices.scale(0.6f, 0.6f, 0.02f)
        matrices.scale((width/entity.boundingBox.xLength).toFloat(), (width/entity.boundingBox.xLength).toFloat(), (width/entity.boundingBox.xLength).toFloat())
        matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180.0f))
        matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(-30.0f))
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(225.0f))
        renderDispatcher.setRenderShadows(false)
        renderDispatcher.render<Entity>(entity, 0.0, 0.0, 0.0, 0f, 0f, matrices, vertexConsumers, light)
        renderDispatcher.setRenderShadows(true)
    }

    fun drawBannerEffectTooltip(screen: HandledScreen<*>, iterable: Iterable<StatusEffectInstance>, x: Int, y: Int, matrixStack: MatrixStack) {
        val client = MinecraftClient.getInstance()
        val mouseX = (client.mouse.x * client.window.scaledWidth.toDouble() / client.window.width.toDouble()).toInt()
        val mouseY = (client.mouse.y * client.window.scaledHeight.toDouble() / client.window.height.toDouble()).toInt()
        for ((index, instance) in iterable.withIndex()) {
            if (instance.effectType === ENTITY_BANNER_STATUS_EFFECT) {
                if (mouseX >= x && mouseX <= x + 120 && mouseY >= y + 32*index && mouseY <= y + 32*index + 32) {
                    screen.renderTooltip(matrixStack, ArrayList<Text>().also(::appendBannerTooltip), mouseX, mouseY)
                }
            }
        }
    }

    fun appendBannerTooltip(list: MutableList<Text>) {
        list.add(TranslatableText("tooltip.entitybanners.status_effect1").formatted(Formatting.DARK_PURPLE))
        list.add(TranslatableText("tooltip.entitybanners.status_effect2").formatted(Formatting.DARK_PURPLE))
        EntityBannerStatusEffectHolders.Client.entitySet.forEach(Consumer { entityType: EntityType<*> ->
            list.add(entityType.name.copy().formatted(Formatting.GRAY))
        })
    }

}