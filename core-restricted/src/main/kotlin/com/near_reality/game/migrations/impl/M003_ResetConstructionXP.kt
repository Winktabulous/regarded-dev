package com.near_reality.game.migrations.impl

import com.near_reality.game.migrations.ActiveMigration
import com.near_reality.game.migrations.GameMigration
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.SkillConstants

@Suppress("unused", "ClassName")
@ActiveMigration
class M003_ResetConstructionXP : GameMigration {

    override fun run(player: Player) {
        if(player.skills.getExperience(SkillConstants.CONSTRUCTION) > 0.0) {
            player.skills.setSkill(SkillConstants.CONSTRUCTION, 1, 0.0)
            player.sendMessage("Your construction skill progress has been reset.")
        }
    }

    override fun id(): Int = 3

}