package com.near_reality.game.content.wilderness.event.hot_zone

import com.google.common.eventbus.Subscribe
import com.zenyte.plugins.events.ServerLaunchEvent

/**
 * The module that initializes the wilderness hot zone areas.
 *
 * @author Stan van der Bend
 */
@Suppress("unused")
object WildernessHotZoneModule {

    @JvmStatic
    @Subscribe
    fun onServerLaunchEvent(event: ServerLaunchEvent) {

//        WildernessEventManager.registerEvent(WildernessHotZoneEvent)
//
//        event.worldThread.apply {
//            hook<PlayerEvent.Died> {
//                val killer = killer as? Player ?: return@hook
//                if (player != killer && WildernessHotZoneEvent.inHotZone(player)) {
//                    if (killer.getIP().equals(player.getIP(), ignoreCase = true)) return@hook
//                    if (killer.killstreakLog.entryExists(player)) return@hook
//                    WildernessHotZoneEvent.rewardHandler.registerKill(killer)
//                }
//            }
//            hook<PlayerEvent.ExperienceGained> {
//                if (WildernessHotZoneEvent.inHotZone(player) && baseExperience > 0 && !Skills.isCombatSkill(skill))
//                    WildernessHotZoneEvent.rewardHandler.registerExperienceGained(player, baseExperience)
//            }
//            hook<PlayerEvent.DamageDealt> {
//                if (target is NPC && WildernessHotZoneEvent.inHotZone(player) && damage > 0)
//                    WildernessHotZoneEvent.rewardHandler.registerMonsterDamageDealt(player, damage)
//            }
//        }
    }
}
