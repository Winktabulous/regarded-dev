package com.near_reality.game.content.consumables.drinks

import com.zenyte.game.content.consumables.Consumable
import com.zenyte.game.content.consumables.Drinkable
import com.zenyte.game.item.Item
import com.zenyte.game.world.entity.player.Player

/**
 * Adapter class for Potion typed [drinkable items][Drinkable].
 *
 * @author Stan van der Bend
 */
open class PotionAdapter(private val ids: IntArray) : Drinkable {

    override fun leftoverItem(id: Int): Item {
        val index = ids.indexOf(id)
        if (index == -1)
            throw RuntimeException("Invalid id: $id $this")
        else
            return  Item(if (index == 0) Drinkable.vial.id else ids[index - 1])
    }

    override fun boosts(): Array<Consumable.Boost>  = emptyArray()

    override fun startMessage() = "You drink some of your %s."

    override fun endMessage(player: Player?) = "You have %d dose%s of potion left."

    override fun emptyMessage(player: Player?) = "You have finished your potion."

    override fun getDoses(id: Int): Int {
        val index  =ids.indexOf(id)
        if (index == -1)
            throw java.lang.RuntimeException("Invalid id: $id $this")
        return index + 1
    }

    override fun getItem(dose: Int): Item {
        if (dose < 0 || dose > ids.size)
            throw java.lang.RuntimeException("The potion $this doesn't support dose $dose.")
        else
            return Item(if (dose == 0) Drinkable.vial.id else ids[dose-1])
    }

    override fun getIds() = ids
}
