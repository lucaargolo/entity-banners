package io.github.lucaargolo.entitybanners.common.items

import io.github.lucaargolo.entitybanners.EntityBanners
import io.github.lucaargolo.entitybanners.utils.DyeColorUtils
import io.github.lucaargolo.entitybanners.utils.EntityLoomPattern
import net.minecraft.block.Blocks
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.EntityType
import net.minecraft.item.BannerItem
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import net.minecraft.world.World

class EntityBannerItem(settings: Settings): BannerItem(Blocks.WHITE_BANNER, Blocks.WHITE_WALL_BANNER, settings) {

    override fun appendTooltip(stack: ItemStack, world: World?, tooltip: MutableList<Text>, context: TooltipContext) {
        if(stack.hasTag()) {
            val entityString = stack.orCreateTag.getCompound("BlockEntityTag").getString("entitybanners_Entity")
            val entityIdentifier = Identifier(entityString)
            val entityType = Registry.ENTITY_TYPE.get(entityIdentifier)
            tooltip.add(TranslatableText("tooltip.entitybanners.nearby_players1").formatted(Formatting.ITALIC, Formatting.DARK_PURPLE))
            tooltip.add(TranslatableText("tooltip.entitybanners.nearby_players2", entityType.name.copy().formatted(Formatting.GRAY)).formatted(Formatting.ITALIC, Formatting.DARK_PURPLE))
        }
    }

    override fun getName(): Text = TranslatableText("item.entitybanners.entity_banner", "Entity")

    override fun getName(stack: ItemStack): Text {
        if(stack.hasTag()) {
            val entityString = stack.orCreateTag.getCompound("BlockEntityTag").getString("entitybanners_Entity")
            val entityIdentifier = Identifier(entityString)
            val entityType = Registry.ENTITY_TYPE.get(entityIdentifier)
            return TranslatableText("item.entitybanners.entity_banner", entityType.name)
        }
        return super.getName(stack)
    }

    override fun getDefaultStack(): ItemStack {
        return getPatternStack(EntityBanners.REGISTERED_PATTERNS[EntityType.ZOMBIE]!!)
    }

    fun getPatternStack(entityPattern: EntityLoomPattern): ItemStack {
        var primaryColor = DyeColorUtils.getDyeColorByColor(entityPattern.primaryColor)
        var secondaryColor = DyeColorUtils.getDyeColorByColor(entityPattern.secondaryColor)
        if(primaryColor == secondaryColor) {
            val pair = DyeColorUtils.getColorPair(primaryColor)
            primaryColor = pair.first
            secondaryColor = pair.second
        }
        val id = Registry.ENTITY_TYPE.getId(entityPattern.entityType)
        val bannerStack = ItemStack(EntityBanners.ENTITY_BANNER_ITEM)
        val bannerTag = bannerStack.orCreateTag
        val blockEntityTag = CompoundTag()
        val loomPatternsTag = ListTag()
        val entityPatternTag = CompoundTag()
        entityPatternTag.putString("Pattern", "${id}_banner")
        entityPatternTag.putInt("Index", 0)
        entityPatternTag.putInt("Color", secondaryColor.id)
        loomPatternsTag.add(entityPatternTag)
        blockEntityTag.put("Bannerpp_LoomPatterns", loomPatternsTag)
        val patternsTag = ListTag()
        val ttsPattern = CompoundTag()
        ttsPattern.putString("Pattern", "tts")
        ttsPattern.putInt("Color", primaryColor.id)
        patternsTag.add(ttsPattern)
        val btsPattern = CompoundTag()
        btsPattern.putString("Pattern", "bts")
        btsPattern.putInt("Color", primaryColor.id)
        patternsTag.add(btsPattern)
        val boPattern = CompoundTag()
        boPattern.putString("Pattern", "bo")
        boPattern.putInt("Color", primaryColor.id)
        patternsTag.add(boPattern)
        val csPattern = CompoundTag()
        csPattern.putString("Pattern", "cs")
        csPattern.putInt("Color", secondaryColor.id)
        patternsTag.add(csPattern)
        blockEntityTag.put("Patterns", patternsTag)
        blockEntityTag.putString("entitybanners_Entity", id.toString())
        bannerTag.put("BlockEntityTag", blockEntityTag)
        return bannerStack
    }


}