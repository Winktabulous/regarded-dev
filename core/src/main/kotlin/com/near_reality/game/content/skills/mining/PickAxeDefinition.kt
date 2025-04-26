package com.near_reality.game.content.skills.mining

import com.zenyte.game.world.entity.masks.Animation

interface PickAxeDefinition {

    val mineTime: Int

    val id: Int

    val level: Int

    val anim: Animation?

    val alternateAnimation: Animation?
}
