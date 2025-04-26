package com.near_reality.game.content.tormented_demon

import com.near_reality.scripts.npc.drops.table.noted
import com.zenyte.game.content.follower.Follower
import com.zenyte.game.content.follower.PetWrapper
import com.zenyte.game.content.follower.impl.BossPet
import com.zenyte.game.item.Item
import com.zenyte.game.util.Utils
import com.zenyte.game.world.broadcasts.BroadcastType
import com.zenyte.game.world.broadcasts.WorldBroadcasts
import java.util.Random

import com.near_reality.scripts.npc.drops.NPCDropTableScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.item.ItemId
import com.zenyte.game.item.ItemId.*
import com.near_reality.scripts.npc.drops.table.DropTableType.*
import com.zenyte.game.world.entity.npc.drop.matrix.*
import com.zenyte.game.world.entity.npc.drop.matrix.Drop.*
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor.*

class TormentedDemonDropTable : NPCDropTableScript() {
    init {
        npcs(TORRMENTED_DEMON, TORRMENTED_DEMON_13600, TORRMENTED_DEMON_13601, TORRMENTED_DEMON_13602)
        
        onDeath {
            if (Random().nextDouble() < 1/3000) {
                val smolderingDemon = BossPet.SMOLDERING_DEMON
                val item = Item(smolderingDemon.itemId)
                killer.collectionLog.add(item)
                if ((PetWrapper.checkFollower(killer) && killer.follower.pet == smolderingDemon) || killer.containsItem(smolderingDemon.itemId))
                    killer.sendMessage("<col=ff0000>You have a funny feeling like you would have been followed...</col>")
        
                else if (killer.follower != null) {
                    if (killer.inventory.addItem(item).isFailure) {
                        if (killer.bank.add(item).isFailure)
                            killer.sendMessage("There was not enough space in your bank, and therefore the pet was lost.")
                        else killer.sendMessage(
                            "<col=ff0000>You have a funny feeling like you're being followed - The pet has " +
                                    "been added to your bank.</col>")
                    }
                    killer.sendMessage("<col=ff0000>You feel something weird sneaking into your backpack.</col>")
                    WorldBroadcasts.broadcast(killer, BroadcastType.PET, smolderingDemon)
                }
                else {
                    killer.sendMessage("<col=ff0000>You have a funny feeling like you're being followed.</col>")
                    killer.follower = Follower(smolderingDemon.petId, killer)
                    WorldBroadcasts.broadcast(killer, BroadcastType.PET, smolderingDemon)
                }
            }
        
            rollStaticTableAndDrop(killer, Tertiary)
            if (Utils.random(500) == 0)
                rollStaticTableAndDrop(killer, Unique)
            else
                rollStaticTableAndDrop(killer, Main)
            npc.dropItem(killer, Item(INFERNAL_ASHES))
        }
        
        buildTable(100) {
            Unique {
                TORMENTED_SYNAPSE quantity 1 oneIn 500
                BURNING_CLAW quantity 1 oneIn 501
            }
        
            Main {
                // Weapons and Armor
                RUNE_PLATEBODY quantity 1 oneIn 13
                DRAGON_DAGGER quantity 1 oneIn 17
                BATTLESTAFF quantity 1.noted oneIn 17
                RUNE_KITESHIELD quantity 1 oneIn 26
                // Runes and Ammo
                CHAOS_RUNE quantity IntRange(25, 100) oneIn 13
                RUNE_ARROW quantity IntRange(65, 125) oneIn 13
                SOUL_RUNE quantity IntRange(50, 75) oneIn 26
                // Herbs
                GRIMY_KWUARM quantity 1 oneIn 40
                GRIMY_DWARF_WEED quantity 1 oneIn 51
                GRIMY_CADANTINE quantity 1 oneIn 51
                GRIMY_LANTADYME quantity 1 oneIn 68
                GRIMY_AVANTOE quantity 1 oneIn 82
                GRIMY_RANARR_WEED quantity 1 oneIn 102
                GRIMY_SNAPDRAGON quantity 1 oneIn 102
                GRIMY_TORSTOL quantity 1 oneIn 136
                // Seeds
                RANARR_SEED quantity 1 oneIn 425
                SNAPDRAGON_SEED quantity 1 oneIn 455
                TORSTOL_SEED quantity 1 oneIn 580
                WATERMELON_SEED quantity 15 oneIn 607
                WILLOW_SEED quantity 1 oneIn 637
                MAHOGANY_SEED quantity 1 oneIn 708
                MAPLE_SEED quantity 1 oneIn 708
                TEAK_SEED quantity 1 oneIn 708
                YEW_SEED quantity 1 oneIn 708
                PAPAYA_TREE_SEED quantity 1 oneIn 911
                MAGIC_SEED quantity 1 oneIn 1159
                PALM_TREE_SEED quantity 1 oneIn 1275
                SPIRIT_SEED quantity 1 oneIn 1594
                DRAGONFRUIT_TREE_SEED quantity 1 oneIn 2125
                CELASTRUS_SEED quantity 1 oneIn 3188
                REDWOOD_TREE_SEED quantity 1 oneIn 3188
                // Consumables
                MANTA_RAY quantity IntRange(1, 2) oneIn 13
                SMOULDERING_GLAND quantity 1 oneIn 25
                SMOULDERING_PILE_OF_FLESH quantity 1 oneIn 25
                PRAYER_POTION4 quantity 1 oneIn 51
                PRAYER_POTION2 quantity 1 oneIn 51
                SMOULDERING_HEART quantity 1 oneIn 125
                // Other
                MAGIC_SHORTBOW_U quantity 1.noted oneIn 9
                GUTHIXIAN_TEMPLE_TELEPORT quantity 2 oneIn 12
                MALICIOUS_ASHES quantity IntRange(2, 3) oneIn 26
                FIRE_ORB quantity IntRange(5, 7).noted oneIn 26
                DRAGON_ARROWTIPS quantity IntRange(30, 40) oneIn 51
                MAGIC_LONGBOW_U quantity 1 oneIn 255
        
            }
        
            Tertiary {
                SCROLL_BOX_ELITE quantity 1 oneIn 160
            }
        }
        
    }
}