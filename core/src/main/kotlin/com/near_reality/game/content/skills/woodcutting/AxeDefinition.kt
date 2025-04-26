package com.near_reality.game.content.skills.woodcutting

import com.zenyte.game.world.entity.masks.Animation

interface AxeDefinition {

    val itemId: Int

    val levelRequired: Int

    val cutTime: Int

    val treeCutAnimation: Animation?
    val trunkCutAnimation: Animation?
    val canoeCutAnimation: Animation?

}
