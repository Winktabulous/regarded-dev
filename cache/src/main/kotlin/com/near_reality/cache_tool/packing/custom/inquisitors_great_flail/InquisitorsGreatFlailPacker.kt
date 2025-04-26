package com.near_reality.cache_tool.packing.custom.inquisitors_great_flail

import com.near_reality.cache_tool.packing.AnimationDefinitionsPacking.packAnim

/**
 * @author Jire
 */
object InquisitorsGreatFlailPacker {

    private const val ASSETS_PATH = "assets/osnr/inquisitors_great_flail/"

    private const val ANIM_DEFS_PATH = "${ASSETS_PATH}animations/definitions/"

    @JvmStatic
    fun pack() {
        packAnim(20000, "${ANIM_DEFS_PATH}FlailStandAnim.dat")
        packAnim(20001, "${ANIM_DEFS_PATH}FlailWalkAnim.dat")
        packAnim(20002, "${ANIM_DEFS_PATH}FlailRunAnim.dat")
    }

}