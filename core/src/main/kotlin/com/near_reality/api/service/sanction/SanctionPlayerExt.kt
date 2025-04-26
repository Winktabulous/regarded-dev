package com.near_reality.api.service.sanction

import com.near_reality.api.model.Sanction
import com.near_reality.api.model.SanctionLevel
import com.near_reality.api.model.SanctionType
import com.near_reality.api.model.User
import com.zenyte.game.world.entity.player.Player

val Player.isBanned get() = user?.isBanned?:false
val Player.isMuted get() = user?.isMuted?:false
val Player.isYellMuted get() = user?.isYellMuted?:false

val User.isBanned get() = activeSanctions.any { it.type == SanctionType.BAN }
val User.isMuted get() = activeSanctions.any { it.type == SanctionType.MUTE }
val User.isYellMuted get() = activeSanctions.any { it.type == SanctionType.YELL_MUTE }

val Player.banSanction get() = user?.banSanction
val Player.muteSanction get() = user?.muteSanction
val Player.yellMuteSanction get() = user?.yellMuteSanction

val User.banSanction get() = activeSanctions.firstOrNull { it.type == SanctionType.BAN }
val User.muteSanction get() = activeSanctions.firstOrNull { it.type == SanctionType.MUTE }
val User.yellMuteSanction get() = activeSanctions.firstOrNull { it.type == SanctionType.YELL_MUTE }

val Player.activeSanctions get() = user?.activeSanctions?:emptyList()

val User.activeSanctions get() = sanctions.filterNot(Sanction::isExpired)

fun Player.submitInfiniteAccountBan(offender: Player, reason: String? = null) =
    SanctionPlayerHandler.submitSanction(SanctionLevel.ACCOUNT, SanctionType.BAN, offender, this, reason)
fun Player.submitInfiniteIPBan(offender: Player, reason: String? = null) =
    SanctionPlayerHandler.submitSanction(SanctionLevel.IP, SanctionType.BAN, offender, this, reason)
fun Player.submitInfiniteAccountMuteFor(offender: Player, reason: String? = null) =
    SanctionPlayerHandler.submitSanction(SanctionLevel.ACCOUNT, SanctionType.MUTE, offender, this, reason)
fun Player.submitInfiniteIPMuteFor(offender: Player, reason: String? = null) =
    SanctionPlayerHandler.submitSanction(SanctionLevel.IP, SanctionType.MUTE, offender, this, reason)
fun Player.submitInfiniteAccountYellMuteFor(offender: Player, reason: String? = null) =
    SanctionPlayerHandler.submitSanction(SanctionLevel.ACCOUNT, SanctionType.YELL_MUTE, offender, this, reason)
fun Player.submitInfiniteIPYellMuteFor(offender: Player, reason: String? = null) =
    SanctionPlayerHandler.submitSanction(SanctionLevel.IP, SanctionType.YELL_MUTE, offender, this, reason)
