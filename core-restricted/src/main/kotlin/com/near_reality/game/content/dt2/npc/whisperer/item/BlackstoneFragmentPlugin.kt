package com.near_reality.game.content.dt2.npc.whisperer.item

import com.near_reality.game.content.dt2.area.WhispererInstance
import com.near_reality.game.content.dt2.npc.whisperer.WhispererCombat
import com.near_reality.game.content.dt2.npc.whisperer.attacks.special.impl.leech.ShadowLeachSpecialAttack
import com.near_reality.game.content.dt2.npc.whisperer.attacks.special.impl.screech.ScreechSpecialAttack
import com.near_reality.game.content.dt2.npc.whisperer.attacks.special.impl.siphon.SoulSiphonSpecialAttack
import com.near_reality.game.content.offset
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.pluginextensions.ItemPlugin
import com.zenyte.game.world.entity.Tinting
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.world.entity.player.Player
import java.util.*

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-09-25
 */
class BlackstoneFragmentPlugin : ItemPlugin() {
    override fun handle() {
        bind("Activate") { player, item, _, slot ->
            run {
                val playerInstance = player.mapInstance
                if (playerInstance == null) {
                    player.sendMessage("You don't seem to have a reason to use this right now...")
                    return@bind
                }
                if (playerInstance is WhispererInstance) {
                    val specActive = playerInstance.whisperer.state.usingSpecial
                    if (specActive) {
                        // if we're in the main world, and have the non-glowing Fragment
                        if (Objects.equals(item.id, ItemId.BLACKSTONE_FRAGMENT_28357))
                            transferIntoShadowRealm(player, playerInstance.whisperer)
                        // else if we're in the Shadow Realm, and have the Glowing Fragment
                        else if (Objects.equals(item.id, ItemId.BLACKSTONE_FRAGMENT))
                            transferIntoMainWorld(player, playerInstance.whisperer)

                        updateBlackstoneFragment(player, item, slot)
                    }
                }
            }
        }
    }

    private fun transferIntoShadowRealm(player: Player, whisperer: WhispererCombat) {
        val xOffset = Pair(-64, 0)
        player.teleport(player.location offset xOffset)
        when (val special = whisperer.getSpecialAttack()) {
            is ScreechSpecialAttack -> {
                special.pillars.forEach { pillar ->
                    run {
                        whisperer.setLocation((whisperer.spawnLocation offset Pair(0, -10)) offset xOffset)
                        pillar.setTransformation(FLOATING_COLUMN)
                        pillar.setLocation(pillar.location offset xOffset)
                    }
                }
            }

            is SoulSiphonSpecialAttack -> {
                whisperer.setLocation(whisperer.spawnLocation offset xOffset)
                special.lostSouls.forEach { lostSoul ->
                    run {
                        lostSoul.setTransformation(LOST_SOUL_12212)
                        lostSoul.setLocation(lostSoul.location offset xOffset)
                        lostSoul.tinting = lostSoul.soulType.soulTinting
                    }
                }
            }

            is ShadowLeachSpecialAttack -> {
                whisperer.setLocation(whisperer.spawnLocation offset xOffset)
            }
        }
    }

    private fun transferIntoMainWorld(player: Player, whisperer: WhispererCombat) {
        val xOffset = Pair(64, 0)
        player.teleport(player.location offset xOffset)
        when (val special = whisperer.getSpecialAttack()) {
            is ScreechSpecialAttack -> {
                whisperer.setLocation((whisperer.spawnLocation offset Pair(0, -10)))
                special.pillars.forEach { pillar ->
                    run {
                        pillar.setTransformation(FLOATING_COLUMN_12210)
                        pillar.setLocation(pillar.location offset xOffset)
                    }
                }
            }

            is SoulSiphonSpecialAttack -> {
                whisperer.setLocation(whisperer.spawnLocation)
                special.lostSouls.forEach { lostSoul ->
                    run {
                        lostSoul.setTransformation(LOST_SOUL)
                        lostSoul.setLocation(lostSoul.location offset xOffset)
                        lostSoul.tinting = Tinting(-1, -1, -1, 0, 0, 0)
                    }
                }
            }

            is ShadowLeachSpecialAttack -> {
                whisperer.setLocation(whisperer.spawnLocation)
            }
        }

    }

    private fun updateBlackstoneFragment(player: Player, blackstoneFragment: Item, slot: Int) {
        if (Objects.equals(blackstoneFragment.id, ItemId.BLACKSTONE_FRAGMENT_28357))
            blackstoneFragment.id = ItemId.BLACKSTONE_FRAGMENT
        else if (Objects.equals(blackstoneFragment.id, ItemId.BLACKSTONE_FRAGMENT))
            blackstoneFragment.id = ItemId.BLACKSTONE_FRAGMENT_28357
        player.getInventory().refresh(slot)
    }

    override fun getItems(): IntArray = intArrayOf(ItemId.BLACKSTONE_FRAGMENT, ItemId.BLACKSTONE_FRAGMENT_28357)
}