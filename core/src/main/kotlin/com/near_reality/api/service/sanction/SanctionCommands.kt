package com.near_reality.api.service.sanction

import com.near_reality.api.model.SanctionLevel
import com.near_reality.api.model.SanctionType
import com.zenyte.game.world.entity.player.GameCommands.Command
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege

object SanctionCommands {

    fun register() {
        submitSanctionCommand(command = "mute", level = SanctionLevel.ACCOUNT, type = SanctionType.MUTE)
        submitSanctionCommand(command = "yellmute", level = SanctionLevel.ACCOUNT, type = SanctionType.YELL_MUTE)
        submitSanctionCommand(command = "ban", level = SanctionLevel.ACCOUNT, type = SanctionType.BAN)
        submitSanctionCommand(command = "ipmute", level = SanctionLevel.IP, type = SanctionType.MUTE)
        submitSanctionCommand(command = "ipban",  privilege = PlayerPrivilege.ADMINISTRATOR, level = SanctionLevel.IP, type = SanctionType.BAN)
        submitSanctionCommand(command = "uuidban", privilege = PlayerPrivilege.ADMINISTRATOR, level = SanctionLevel.UUID, type = SanctionType.BAN)
        revokeSanctionCommand(command = "revoke")
    }

    private fun submitSanctionCommand(
        privilege: PlayerPrivilege = PlayerPrivilege.MODERATOR,
        command: String,
        level: SanctionLevel,
        type: SanctionType
    ): Command {
        return Command(privilege, command, "Submit $type at $level level") { player, _ ->
            SanctionPlayerHandler.startSanctionSubmission(player, type, level)
        }
    }
    private fun revokeSanctionCommand(command: String): Command {
        return Command(PlayerPrivilege.MODERATOR, command, "Revoke a sanction") { player, _ ->
            SanctionPlayerHandler.startSanctionRevoke(player)
        }
    }
}
