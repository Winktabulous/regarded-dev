package com.zenyte.game.content.universalshop

import com.zenyte.game.world.entity.player.GameCommands
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege

object UniversalShopCommands {
    fun register() {
        GameCommands.Command(PlayerPrivilege.MODERATOR, "usm") { player, _ ->
            player.dialogueManager.start(MainMenu(player))
        }
    }
}