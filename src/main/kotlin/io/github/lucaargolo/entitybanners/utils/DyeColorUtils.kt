package io.github.lucaargolo.entitybanners.utils

import io.github.lucaargolo.entitybanners.mixed.DyeColorMixed
import net.minecraft.util.DyeColor
import java.awt.Color
import kotlin.math.abs


object DyeColorUtils {

    fun getDyeColorByColor(rgb: Int): DyeColor {
        val color = Color(rgb)
        val dyeColors = DyeColor.values().associateWith { Color((it as DyeColorMixed).entitybanners_getColor()) }
        val distances = linkedMapOf<Int, DyeColor>()
        for ((key, value) in dyeColors) {
            val distance = abs(color.red - value.red) + abs(color.green - value.green) + abs(color.blue - value.blue)
            distances[distance] = key
        }
        val dis = distances.keys.toTypedArray().sortedArray()
        return distances[dis[0]] ?: DyeColor.WHITE
    }

    fun getColorPair(dyeColor: DyeColor): Pair<DyeColor, DyeColor> {
        return when(dyeColor) {
            DyeColor.WHITE -> Pair(DyeColor.LIGHT_GRAY, DyeColor.WHITE)
            DyeColor.ORANGE -> Pair(DyeColor.BROWN, DyeColor.ORANGE)
            DyeColor.MAGENTA -> Pair(DyeColor.PURPLE, DyeColor.MAGENTA)
            DyeColor.LIGHT_BLUE -> Pair(DyeColor.LIGHT_BLUE, DyeColor.BLUE)
            DyeColor.YELLOW -> Pair(DyeColor.ORANGE, DyeColor.YELLOW)
            DyeColor.LIME -> Pair(DyeColor.LIME, DyeColor.GREEN)
            DyeColor.PINK -> Pair(DyeColor.PINK, DyeColor.RED)
            DyeColor.GRAY -> Pair(DyeColor.GRAY, DyeColor.BLACK)
            DyeColor.LIGHT_GRAY -> Pair(DyeColor.LIGHT_GRAY, DyeColor.WHITE)
            DyeColor.CYAN -> Pair(DyeColor.LIGHT_BLUE, DyeColor.CYAN)
            DyeColor.PURPLE -> Pair(DyeColor.PURPLE, DyeColor.MAGENTA)
            DyeColor.BLUE -> Pair(DyeColor.LIGHT_BLUE, DyeColor.BLUE)
            DyeColor.BROWN -> Pair(DyeColor.BROWN, DyeColor.ORANGE)
            DyeColor.GREEN -> Pair(DyeColor.LIME, DyeColor.GREEN)
            DyeColor.RED -> Pair(DyeColor.PINK, DyeColor.RED)
            DyeColor.BLACK -> Pair(DyeColor.GRAY, DyeColor.BLACK)
        }
    }
}