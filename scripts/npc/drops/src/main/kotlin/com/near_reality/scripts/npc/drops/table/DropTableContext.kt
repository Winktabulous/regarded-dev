package com.near_reality.scripts.npc.drops.table

import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.player.Player

sealed class DropTableContext(
    val type: DropTableType,
    val table: DropTable,
) {

    abstract fun nested(table: DropTable): DropTableContext

    class Standalone(type: DropTableType, table: DropTable) : DropTableContext(type, table) {
        override fun nested(table: DropTable): DropTableContext =
            Standalone(type, table)
    }

    sealed class ForPlayer(val player: Player, type: DropTableType, table: DropTable) : DropTableContext(type, table) {

        class NpcKill(val npc: NPC, player: Player, type: DropTableType, table: DropTable) :
            ForPlayer(player, type, table) {

            override fun nested(table: DropTable) =
                NpcKill(npc, player, type, table)
        }

        class Misc(player: Player, type: DropTableType, table: DropTable) :
            ForPlayer(player, type, table) {

            override fun nested(table: DropTable) =
                Misc(player, type, table)
        }
    }
}
