package com.near_reality.game.content.easter_2024

import com.near_reality.game.item.CustomNpcId
import com.near_reality.scripts.npc.drops.NPCDropTableScript
import com.near_reality.scripts.npc.drops.table.DropTableType.Always
import com.near_reality.scripts.npc.drops.table.always
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId.BROKEN_EGG_SHELLS

class ColossalChoccoChickenDropTable : NPCDropTableScript() {
    init {
        npcs(
            CustomNpcId.CHICKEN_RED_EASTER_2024,
            CustomNpcId.CHICKEN_GREEN_EASTER_2024,
            CustomNpcId.CHICKEN_BLUE_EASTER_2024,
            CustomNpcId.CHICKEN_RAINBOW_EASTER_2024
        )
        
        val eggShellAmountRange = 25..40
        val eggShellMvpBonusAmount = 25
        onDeath {
            if (playerDamageContributions.isEmpty()) {
                killer.sendDeveloperMessage("Did not find any damage dealers, ignoring drops.")
                return@onDeath
            }
        
            val mvp = playerDamageContributions.maxBy { it.value }.key
            mvp?.sendDeveloperMessage("You are the MVP with a damage percentage of ${playerDamageContributions[mvp]}")
            mvp?.sendMessage("You were the MVP for this fight and have received $eggShellMvpBonusAmount extra shells.")
            val damageContributionByMvp = playerDamageContributions[mvp]?:0.0
            val damageDealtByMvp = (damageContributionByMvp * npc.maxHitpoints).toInt()
            for ((player, damageContribution) in playerDamageContributions) {
                player.sendMessage("The MVP for this fight was: ${mvp?.name} dealing $damageDealtByMvp damage!")
                if (damageContribution >= 0.001) {
                    val eggShellBaseAmount = eggShellAmountRange.random()
                    val eggShellExtraAmount = (150 * damageContribution).toInt() + (if(player == mvp) eggShellMvpBonusAmount else 0)
                    val eggShellTotalAmount = eggShellBaseAmount + eggShellExtraAmount
                    val eggShellItem = Item(BROKEN_EGG_SHELLS, eggShellTotalAmount)
                    npc.dropItem(player, eggShellItem, npc.middleLocation, true)
                    player.sendDeveloperMessage("You received $eggShellTotalAmount egg shells (basse=$eggShellBaseAmount, extra=${eggShellExtraAmount}).")
                } else
                    player.sendMessage("You did not contribute enough damage to receive any drops.")
            }
        }
        
        buildTable {
            Always {
                BROKEN_EGG_SHELLS quantity 10..15 rarity always info { "+ extra (150 / % damage dealt) broken egg shells is given.<br>+ 10 extra broken egg shells if mvp." }
            }
        }
    }
}