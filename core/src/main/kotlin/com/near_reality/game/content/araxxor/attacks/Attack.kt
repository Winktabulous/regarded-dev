package com.near_reality.game.content.araxxor.attacks

import com.near_reality.game.content.araxxor.Araxxor
import com.zenyte.game.world.entity.Entity

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-20
 */
interface Attack {
    operator fun invoke(araxxor: com.near_reality.game.content.araxxor.Araxxor, target: Entity?)
}