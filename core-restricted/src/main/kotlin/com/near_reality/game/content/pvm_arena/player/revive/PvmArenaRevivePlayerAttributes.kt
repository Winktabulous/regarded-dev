package com.near_reality.game.content.pvm_arena.player.revive

import com.zenyte.game.world.WorldThread
import com.zenyte.game.world.entity.attribute
import com.zenyte.game.world.entity.player.Player

/**
 * Represents the [PvmArenaReviveState] of a player being revived or a player reviving someone.
 */
var Player.pvmArenaReviveState: PvmArenaReviveState by attribute("pvm_arena_revive_state", PvmArenaReviveState.None)

/**
 * Represents the [world tick][WorldThread.getCurrentCycle] when the player last received a reward for reviving another player.
 */
internal var Player.pvmArenaLastReviveRewardTick: Long by attribute("pvm_arena_last_revive_reward_tick", 0)

/**
 * Represents the amount of times a player has successfully revived another player within the reward cooldown.
 */
internal var Player.pvmArenaReviveCountDuringCooldown: Int by attribute("pvm_arena_successful_revive_count_since_last_reward", 0)
