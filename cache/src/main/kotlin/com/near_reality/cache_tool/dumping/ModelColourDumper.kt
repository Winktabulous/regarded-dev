package com.near_reality.cache_tool.dumping

import com.near_reality.cache.group
import com.near_reality.cache_tool.cacheTo
import com.zenyte.CacheManager
import mgi.tools.jagcached.ArchiveType
import mgi.types.config.ObjectDefinitions
import mgi.types.draw.model.ModelData
import net.runelite.cache.models.JagexColor
import java.awt.Color

fun main() {

    CacheManager.setCache(cacheTo)
    ObjectDefinitions().load()
    println(ObjectDefinitions.get(43765).replacementColours?.contentToString())
    val modelDataBytes = cacheTo.getArchive(ArchiveType.MODELS).group(44082).findFileByID(0).getData().buffer
    val modelData = ModelData(modelDataBytes)

    modelData.faceColors.toSet().associate {
        it to JagexColor.formatHSL(it)
    }.forEach { rsColour, hslColour ->
        val hue = JagexColor.unpackHue(rsColour)
        val sat = JagexColor.unpackSaturation(rsColour)
        val lum = JagexColor.unpackLuminance(rsColour)
        println("RS Colour: $rsColour, HSL Colour: $hslColour, hsla($hue, $sat%, $lum%, 1)")
    }

    val colours = shortArrayOf(
        -25768, -25664, 32576
    )
    colours.forEach {
        val colour = hsbToColor(it, null)
        println("Color(${colour.red}, ${colour.green}, ${colour.blue})")
    }
    val blueColours = arrayOf(
        Color(25, 83, 177),
        Color(0, 49, 129),
        Color(18, 129, 123)
    )
    val newColours = arrayOf(
        Color(160, 45, 67),
        Color(98, 14, 16),
        Color(255, 109, 109)
    )
    blueColours.map {
        colorToHsb(it).first.toShort()
    }.forEach {
        println("Encoded blue: ${hsbToColor(it, null)}")
    }
    newColours.map {
        colorToHsb(it).first.toShort()
    }.joinToString {
        it.toString()
    }.also {
        println("Encoded new: $it")
    }
}


fun Color.encode(): Int {
    val hsb = java.awt.Color.RGBtoHSB(
        (red*255.0).toInt(),
        (green*255.0).toInt(),
        (blue*255.0).toInt(),
        null
    )
    return hsb[0].times(63).toInt().shl(10) +
            hsb[1].times(7).toInt().shl(7) +
            hsb[2].times(127).toInt()
}

fun hsbToColor(hsb: Short, alpha: Byte?) = hsbToColor(hsb.toInt(), alpha)

fun hsbToColor(hsb: Int, alpha: Byte?): Color {

    var transparency = alpha?.toUByte()?.toDouble()
    if(transparency == null || transparency <= 0)
        transparency = 255.0

    val hue = (hsb shr 10) and 0x3f
    val sat = (hsb shr 7) and 0x07
    val bri = (hsb and 0x7f)
    val awtCol = java.awt.Color.getHSBColor(hue.toFloat() / 63, sat.toFloat() / 7, bri.toFloat() / 127)
    return awtCol
}

fun colorToHsb(awtCol: Color): Pair<Int, Byte?> {
    // Extract the alpha and normalize it to a byte
    val alpha: Byte? = awtCol.alpha.toByte()

    // Extract HSB values from the Color object
    val hsbVals = FloatArray(3)
    Color.RGBtoHSB(awtCol.red, awtCol.green, awtCol.blue, hsbVals)

    // Calculate hue, saturation, and brightness in the expected ranges
    val hue = (hsbVals[0] * 63).toInt() // Scale and truncate to get values from 0 to 63
    val sat = (hsbVals[1] * 7).toInt() // Scale and truncate to get values from 0 to 7
    val bri = (hsbVals[2] * 127).toInt() // Scale and truncate to get values from 0 to 127

    // Encode HSB into a single integer
    val hsb = (hue shl 10) or (sat shl 7) or bri

    return Pair(hsb, alpha)
}
