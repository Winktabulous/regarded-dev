package com.near_reality.game.content.boss.nex

import com.zenyte.game.content.godwars.objects.GodwarsBossDoorObject.BossDoor
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.Hit
import com.zenyte.game.world.entity.masks.HitType
import com.zenyte.game.world.entity.player.GameCommands
import com.zenyte.game.world.entity.player.dialogue.options
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege.ADMINISTRATOR

object NexCommands {

    fun register() {
        GameCommands.Command(ADMINISTRATOR, "nex") { p, _ ->
            p.options {
                "Teleport-to" { p.teleport(Location(2924, 5202, 0)) }
                "Force Spawn Nex" { NexModule.spawnNex() }
                "Set Nex Essence (KC)" {
                    p.sendInputInt("How many kills?") {
                        p.addAttribute(BossDoor.ANCIENT.formattedName + "Kills", it)
                    }
                }
                "Set spawn delay" {
                    p.sendInputInt("How many ticks?") {
                        NexModule.NEX_SPAWN_DELAY = it.coerceAtLeast(1)
                    }
                }
                "Kill nex" {
                    NexModule.getNexNPC()?.run {
                        removeHitpoints(Hit(p, hitpoints, HitType.MELEE))
                    }
                }
            }
        }
    }
}
