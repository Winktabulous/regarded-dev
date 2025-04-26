package com.near_reality.game.content.crystal.recipes.chargeable

import com.near_reality.game.model.item.degrading.Degradeable

/**
 * Represents a type of [Degradeable] implemented in crystal items.
 *
 * @author Stan van der Bend
 */
interface CrystalDegradeable : Degradeable {

    /**
     * Item id of the inactive variant.
     */
    val inactiveId: Int

    /**
     * The initial number of charges of a created crystal item.
     */
    val startCharges: Int

    /**
     * The maximum number of total charges that the crystal item can hold.
     */
    override val maximumCharges: Int
}
