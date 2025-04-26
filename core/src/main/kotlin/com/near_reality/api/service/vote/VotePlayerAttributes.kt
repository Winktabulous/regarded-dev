package com.near_reality.api.service.vote

import com.zenyte.game.world.entity.persistentAttribute
import com.zenyte.game.world.entity.player.Player

var Player.lastVoteClaimTime: Long by persistentAttribute("vote_claim_time", 0L)
var Player.totalVoteCredits: Int by persistentAttribute("vote_points", 0)
