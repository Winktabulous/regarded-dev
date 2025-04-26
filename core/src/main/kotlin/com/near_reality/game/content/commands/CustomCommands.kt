package com.near_reality.game.content.commands

import com.near_reality.game.item.CustomItemId
import com.zenyte.game.item.Item
import com.zenyte.game.world.entity.player.GameCommands
import com.zenyte.game.world.entity.player.dialogue.options
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege

/**
 * Developer commands for spawning all Near-Reality custom items in the game.
 *
 * @author Stan van der Bend.
 */
object CustomCommands {

    fun register() {
        GameCommands.Command(PlayerPrivilege.DEVELOPER, "customs") { p, args ->
            p.options() {

                "Spawn all in bank" {
                    p.bank.apply {
                        add(Item(CustomItemId.ANCIENT_EYE, 10))
                        add(Item(CustomItemId.ANCIENT_BOOK_32004, 10))
                        add(Item(CustomItemId.ARMADYL_SOUL_CRYSTAL, 10))
                        add(Item(CustomItemId.ARMADYL_BOW, 10))
                        add(Item(CustomItemId.BANDOS_SOUL_CRYSTAL, 10))
                        add(Item(CustomItemId.BANDOS_BOW, 10))
                        add(Item(CustomItemId.SARADOMIN_SOUL_CRYSTAL, 10))
                        add(Item(CustomItemId.SARADOMIN_BOW, 10))
                        add(Item(CustomItemId.ZAMORAK_SOUL_CRYSTAL, 10))
                        add(Item(CustomItemId.ZAMORAK_BOW, 10))
                        add(Item(CustomItemId.DRAGON_KITE, 10))
                        add(Item(CustomItemId.ANCIENT_MEDALLION_32024, 10))
                        add(Item(CustomItemId.IMBUED_ANCIENT_CAPE, 10))
                        add(Item(CustomItemId.IMBUED_ARMADYL_CAPE, 10))
                        add(Item(CustomItemId.IMBUED_BANDOS_CAPE, 10))
                        add(Item(CustomItemId.IMBUED_SEREN_CAPE, 10))
                        add(Item(CustomItemId.GAUNTLET_SLAYER_HELM, 10))
                        add(Item(CustomItemId.CORRUPTED_GAUNTLET_SLAYER_HELM, 10))
                        add(Item(CustomItemId.POLYPORE_SPORES, 10))
                        add(Item(CustomItemId.POLYPORE_STAFF_DEG, 10))
                        add(Item(CustomItemId.POLYPORE_STAFF, 10))
                        add(Item(CustomItemId.BRONZE_KEY, 10))
                        add(Item(CustomItemId.SILVER_KEY, 10))
                        add(Item(CustomItemId.GOLD_KEY, 10))
                        add(Item(CustomItemId.PLATINUM_KEY, 10))
                        add(Item(CustomItemId.DIAMOND_KEY, 10))
                        add(Item(CustomItemId.NR_TABLET, 10))
                        add(Item(CustomItemId.DEATH_CAPE, 10))
                        add(Item(CustomItemId.LIME_WHIP, 10))
                        add(Item(CustomItemId.LAVA_WHIP, 10))
                        add(Item(CustomItemId.PINK_PARTYHAT, 10))
                        add(Item(CustomItemId.ORANGE_PARTYHAT, 10))

                        add(Item(CustomItemId.DONATOR_PIN_10 , 10))
                        add(Item(CustomItemId.DONATOR_PIN_25 , 10))
                        add(Item(CustomItemId.DONATOR_PIN_50 , 10))
                        add(Item(CustomItemId.DONATOR_PIN_100, 10))

                        add(Item(CustomItemId.GANODERMIC_RUNT, 10))

                        add(Item(CustomItemId.BLUE_ANKOU_SOCKS, 10))
                        add(Item(CustomItemId.BLUE_ANKOU_GLOVES, 10))
                        add(Item(CustomItemId.BLUE_ANKOUS_LEGGINGS, 10))
                        add(Item(CustomItemId.BLUE_ANKOU_MASK, 10))
                        add(Item(CustomItemId.BLUE_ANKOU_TOP, 10))
                        add(Item(CustomItemId.GREEN_ANKOU_SOCKS, 10))
                        add(Item(CustomItemId.GREEN_ANKOU_GLOVES, 10))
                        add(Item(CustomItemId.GREEN_ANKOUS_LEGGINGS, 10))
                        add(Item(CustomItemId.GREEN_ANKOU_MASK, 10))
                        add(Item(CustomItemId.GREEN_ANKOU_TOP, 10))
                        add(Item(CustomItemId.GOLD_ANKOU_SOCKS, 10))
                        add(Item(CustomItemId.GOLD_ANKOU_GLOVES, 10))
                        add(Item(CustomItemId.GOLD_ANKOUS_LEGGINGS, 10))
                        add(Item(CustomItemId.GOLD_ANKOU_MASK, 10))
                        add(Item(CustomItemId.GOLD_ANKOU_TOP, 10))
                        add(Item(CustomItemId.WHITE_ANKOU_SOCKS, 10))
                        add(Item(CustomItemId.WHITE_ANKOU_GLOVES, 10))
                        add(Item(CustomItemId.WHITE_ANKOUS_LEGGINGS, 10))
                        add(Item(CustomItemId.WHITE_ANKOU_MASK, 10))
                        add(Item(CustomItemId.WHITE_ANKOU_TOP, 10))
                        add(Item(CustomItemId.BLACK_ANKOU_SOCKS, 10))
                        add(Item(CustomItemId.BLACK_ANKOU_GLOVES, 10))
                        add(Item(CustomItemId.BLACK_ANKOUS_LEGGINGS, 10))
                        add(Item(CustomItemId.BLACK_ANKOU_MASK, 10))
                        add(Item(CustomItemId.BLACK_ANKOU_TOP, 10))
                    }
                }
            }
        }
    }
}
