package com.near_reality.game.world.entity.player.action.combat

import com.zenyte.game.world.Projectile
import com.zenyte.game.world.entity.SoundEffect
import com.zenyte.game.world.entity.masks.Graphics

/**
 * Represents a type of ammunition that can be used by ranged weapons.
 *
 * @author Stan van der Bend
 */
interface AmmunitionDefinition {

    /**
     * The item ids of the ammunition.
     *
     * TODO: find out why for ammunition that doesnt have an item id, e.g. crystal bow ammunition,
     *       the ammunition item ids are set as the bow item ids.
     */
    val itemIds: IntArray

    /**
     * The [Graphics] to play at firing ammunition of this type.
     */
    val drawbackGfx: Graphics?

    /**
     * The [SoundEffect] to play at firing ammunition of this type.
     */
    val soundEffect: SoundEffect?

    /**
     * The [Projectile] to send at the target when firing ammunition of this type.
     */
    val projectile: Projectile?

    /**
     * The item ids of bows that can use this type of ammunition.
     *
     * If should be applicable to all bows, init as empty array.
     */
    val bows: IntArray

    /**
     * Whether this type of ammunition have a chance of being retrieved by the player.
     */
    val isRetrievable: Boolean

    /**
     * `true` when the ammunition is in the weapon slot.
     */
    val isWeapon: Boolean

    /**
     * Can the argued [bowId] use this type of ammunition.
     */
    fun isCompatible(bowId: Int): Boolean
}
