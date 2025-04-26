package com.near_reality.game.migrations

import com.zenyte.game.world.entity.player.Player

interface GameMigration {
    fun run(player: Player)
    fun id() : Int
}
