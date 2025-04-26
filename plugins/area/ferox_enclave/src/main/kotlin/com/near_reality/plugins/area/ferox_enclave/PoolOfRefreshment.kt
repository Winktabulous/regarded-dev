package com.near_reality.plugins.area.ferox_enclave

import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.world.`object`.ObjectId
import com.zenyte.game.world.`object`.WorldObject

/**
 * Two pools of refreshment are found in the chapel in the Ferox Enclave in the Wilderness.
 *
 * The pool fully restores the player's Hitpoints, Prayer points, run energy,
 * cures poison and disease, and resets all boosted or lowered skills. However,
 * they do not restore special attack energy. Drinking from it will also disable all currently active Prayers.
 *
 * @author Stan van der Bend
 */
@Suppress("UNUSED")
class PoolOfRefreshment : ObjectAction {

    override fun handleObjectAction(
        player: Player,
        `object`: WorldObject,
        name: String,
        optionId: Int,
        option: String,
    ) {
        player.apply {
            animation = Animation.GRAB
            prayerManager.deactivateActivePrayers()
            hitpoints = maxHitpoints
            for (i in 0..22)
                skills.setLevel(i, skills.getLevelForXp(i))
            toxins.reset()
            variables.runEnergy = 100.0
            sendMessage("You feel reinvigorated after drinking from the pool.")
        }
    }

    override fun getObjects() = arrayOf(ObjectId.POOL_OF_REFRESHMENT)
}
