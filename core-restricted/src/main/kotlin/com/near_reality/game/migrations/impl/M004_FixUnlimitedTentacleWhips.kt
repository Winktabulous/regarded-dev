package com.near_reality.game.migrations.impl

import com.near_reality.game.migrations.ActiveMigration
import com.near_reality.game.migrations.GameMigration
import com.zenyte.game.item.ItemId
import com.zenyte.game.world.entity.player.Player

@Suppress("unused", "ClassName")
@ActiveMigration
class M004_FixUnlimitedTentacleWhips : GameMigration {
    override fun run(player: Player) {
        for (item in player.inventory.container.items.values) {
            if(item.id == ItemId.ABYSSAL_TENTACLE && item.charges == 0) {
                item.charges = 10_000
            }
        }

        for (item in player.equipment.container.items.values) {
            if(item.id == ItemId.ABYSSAL_TENTACLE && item.charges == 0) {
                item.charges = 10_000
            }
        }

        for (item in player.bank.container.items.values) {
            if(item.id == ItemId.ABYSSAL_TENTACLE && item.charges == 0) {
                item.charges = 10_000
            }
        }
    }

    override fun id(): Int = 4
}