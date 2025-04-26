package com.near_reality.game.migrations.impl

import com.near_reality.game.migrations.ActiveMigration
import com.near_reality.game.migrations.GameMigration
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.world.entity.player.Player

@Suppress("unused", "ClassName")
@ActiveMigration
class M001_RemoveVyreChargesAndRefund : GameMigration {
    override fun run(player: Player) {
        val charges = (player.attributes["vyre well charges"] as Number?)?.toInt() ?: 0
        if (charges == 0) return
        val chargesIssued = minOf(charges, 20)
        player.sendMessage("Due to updates to the Vyre Well, ${chargesIssued * 270} blood runes have been returned to you.")
        player.tryAddInventoryThenBank(Item(ItemId.BLOOD_RUNE, chargesIssued * 270))
        player.attributes["vyre well charges"] = 0
    }

    override fun id(): Int = 1

}