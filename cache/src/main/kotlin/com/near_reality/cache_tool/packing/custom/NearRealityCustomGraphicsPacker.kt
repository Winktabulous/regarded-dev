package com.near_reality.cache_tool.packing.custom

import com.near_reality.cache_tool.packing.assetsBase
import mgi.tools.parser.TypeParser
import mgi.types.config.SpotAnimationDefinition

object NearRealityCustomGraphicsPacker {
    @JvmStatic
    fun pack() {
        assetsBase("assets/osnr/custom_graphics/") {
            "models" {
                TypeParser.packModel(id, bytes)
            }
            // scaled down version of General Graardor smash gfx
            SpotAnimationDefinition.get(1203).copy(6000).apply {
                widthScale = 64
                heightScale = 64
                pack()
            }
            // scaled down and rotated version of KreeArra's ranged projectile gfx
            SpotAnimationDefinition.get(1199).copy(6001).apply {
                widthScale = 56
                heightScale = 56
                modelId = 63000
                animationId = 25000
                pack()
            }
            // Snow ball rolling with long snow patch removed
            packGfx(1222,6003, modelId = 63001)

            packGfx(16,6004, modelId = 63002, scale = 0.75) // armadyl arrow projectile
            packGfx(25,6005, modelId = 63002, scale = 0.75) // armadyl arrow drawback
            packGfx(16,6006, modelId = 63003, scale = 0.75) // bandos arrow projectile
            packGfx(25,6007, modelId = 63003, scale = 0.75) // bandos arrow drawback
            packGfx(16,6008, modelId = 63004, scale = 0.75) // saradomin arrow projectile
            packGfx(25,6009, modelId = 63004, scale = 0.75) // saradomin arrow drawback
            packGfx(16,6010, modelId = 63005, scale = 0.75) // zamorak arrow projectile
            packGfx(25,6011, modelId = 63005, scale = 0.75) // zamorak arrow drawback

            // Recoloured version of vengeance gfx for death cape effect
            SpotAnimationDefinition.get(726).copy(6012).apply {
                recolorTo = shortArrayOf(0)
                pack()
            }
        }
    }

    private fun packGfx(inherit: Int, newId: Int, modelId: Int, scale: Double = 1.0) =
        SpotAnimationDefinition
            .get(inherit)
            .copy(newId)
            .apply {
                this.modelId = modelId
                widthScale = (widthScale * scale).toInt()
                heightScale = (heightScale * scale).toInt()
                pack()
            }
}


