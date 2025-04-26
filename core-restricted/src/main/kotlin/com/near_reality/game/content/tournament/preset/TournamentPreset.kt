package com.near_reality.game.content.tournament.preset

import com.near_reality.game.content.tournament.preset.component.EquipmentComponent
import com.near_reality.game.content.tournament.preset.component.InventoryComponent
import com.near_reality.game.content.tournament.preset.component.RunePouchComponent
import com.near_reality.game.content.tournament.preset.component.SkillsComponent
import com.near_reality.game.content.tournament.previousBoxingWinner
import com.near_reality.game.item.CustomItemId
import com.zenyte.game.content.skills.magic.Spellbook
import com.zenyte.game.content.skills.prayer.Prayer
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.util.Utils
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.SkillConstants
import com.zenyte.game.world.entity.player.Skills
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot
import mgi.utilities.StringFormatUtil
import java.util.*

/**
 * @author Tommeh | 25/05/2019 | 15:59
 * @see [Rune-Server profile](https://www.rune-server.ee/members/tommeh/)
 */
enum class TournamentPreset(
    checkBaseItem: Int,
    announcementIcon: Int,
    val includeInAutoSchedulePool: Boolean = true,
    val presetEquipment: EquipmentComponent,
    val presetInventory: InventoryComponent,
    val presetStats: SkillsComponent,
    val presetSpellbook: Spellbook,
    val presetRunePouch: RunePouchComponent? = null,
    val maximumBrews: Int,
    val disabledPrayers: Array<Prayer>,
) {
    DHAROKS(
        checkBaseItem = ItemId.DHAROKS_GREATAXE,
        announcementIcon = 57,
        presetEquipment = EquipmentComponent.Builder()
            .put(EquipmentSlot.HELMET, 4716, true)
            .put(EquipmentSlot.CAPE, 21295, true)
            .put(EquipmentSlot.AMULET, 20366, true)
            .put(EquipmentSlot.WEAPON, 12006, true)
            .put(EquipmentSlot.PLATE, 4720, true)
            .put(EquipmentSlot.SHIELD, 22322, true)
            .put(EquipmentSlot.LEGS, 4722, true)
            .put(EquipmentSlot.HANDS, 22981, true)
            .put(EquipmentSlot.BOOTS, 13239, true)
            .put(EquipmentSlot.RING, 11773, true)
            .build(),
        presetInventory = InventoryComponent.Builder()
            .add(12695, true).add(6685, true)
            .add(10925, true).add(13441, true)
            .add(6685, true).add(10925, true)
            .add(13441, 2, true).add(3144, 4, true)
            .add(13441, 9, true).add(4718, true)
            .add(11802, true).add(13441, 4, true)
            .add(30006, false)
            .build(),
        presetStats = SkillsComponent.Builder()
            .set(SkillConstants.STRENGTH, 99)
            .set(SkillConstants.ATTACK, 99)
            .set(SkillConstants.DEFENCE, 99)
            .set(SkillConstants.PRAYER, 99)
            .set(SkillConstants.HITPOINTS, 99)
            .set(SkillConstants.MAGIC, 99)
            .set(SkillConstants.RANGED, 99)
            .build(),
        presetSpellbook = Spellbook.LUNAR,
        presetRunePouch = RunePouchComponent.VENGEANCE,
        maximumBrews = 2,
        disabledPrayers = arrayOf(
            Prayer.PROTECT_FROM_MELEE,
            Prayer.PROTECT_FROM_MISSILES,
            Prayer.PROTECT_FROM_MAGIC
        )
    ) {
        override fun toString(): String = "Dharok's"
    },

    DDS(
        checkBaseItem = ItemId.DRAGON_DAGGERP_5698,
        announcementIcon = 57,
        presetEquipment = EquipmentComponent.Builder()
            .put(EquipmentSlot.WEAPON, 5698, false)
            .build(),
        presetInventory = InventoryComponent.Builder()
            .build(),
        presetStats = SkillsComponent.Builder()
            .set(SkillConstants.STRENGTH, 99)
            .set(SkillConstants.ATTACK, 99)
            .set(SkillConstants.DEFENCE, 99)
            .set(SkillConstants.PRAYER, 1)
            .set(SkillConstants.HITPOINTS, 99)
            .build(),
        presetSpellbook = Spellbook.LUNAR,
        presetRunePouch = null,
        maximumBrews = 2,
        disabledPrayers = arrayOf(
            Prayer.PROTECT_FROM_MELEE,
            Prayer.PROTECT_FROM_MISSILES,
            Prayer.PROTECT_FROM_MAGIC
        )
    ) {
        override fun toString(): String = "DDS"
    },


    BOXING(
        checkBaseItem = ItemId.BOXING_GLOVES,
        announcementIcon = 57,
        presetEquipment = EquipmentComponent.Builder()
            .put(EquipmentSlot.WEAPON, if(Utils.randomBoolean()) ItemId.BOXING_GLOVES else ItemId.BOXING_GLOVES_7673, false)
            .build(),
        presetInventory = InventoryComponent.Builder()
            .build(),
        presetStats = SkillsComponent.Builder()
            .set(SkillConstants.STRENGTH, 99)
            .set(SkillConstants.ATTACK, 99)
            .set(SkillConstants.DEFENCE, 1)
            .set(SkillConstants.PRAYER, 1)
            .set(SkillConstants.HITPOINTS, 99)
            .build(),
        presetSpellbook = Spellbook.LUNAR,
        presetRunePouch = null,
        maximumBrews = 2,
        disabledPrayers = arrayOf(
            Prayer.PROTECT_FROM_MELEE,
            Prayer.PROTECT_FROM_MISSILES,
            Prayer.PROTECT_FROM_MAGIC
        )
    ) {
        override fun toString(): String = "Boxing"
    },

    F2P_PURE(
        checkBaseItem = ItemId.RUNE_SCIMITAR,
        announcementIcon = 57,
        presetEquipment = EquipmentComponent.Builder()
            .put(EquipmentSlot.HELMET, ItemId.COIF, true)
            .put(EquipmentSlot.AMULET, ItemId.AMULET_OF_POWER, true)
            .put(EquipmentSlot.WEAPON, ItemId.MAPLE_SHORTBOW, true)
            .put(EquipmentSlot.PLATE, ItemId.LEATHER_BODY, true)
            .put(EquipmentSlot.LEGS, ItemId.GREEN_DHIDE_CHAPS, true)
            .put(EquipmentSlot.HANDS, ItemId.GREEN_DHIDE_VAMB, true)
            .put(EquipmentSlot.BOOTS, ItemId.LEATHER_BOOTS, true)
            .put(EquipmentSlot.CAPE, ItemId.STRENGTH_CAPE, true)
            .put(EquipmentSlot.AMMUNITION, ItemId.ADAMANT_ARROW, 1000, false)
            .build(),
        presetInventory = InventoryComponent.Builder()
            .add(1333, true).add(1319, true)
            .add(113, true).add(ItemId.ADAMANT_ARROW, 1000, true)
            .add(2297, 4, true).add(373, 17, true)
            .build(),
        presetStats = SkillsComponent.Builder()
            .set(SkillConstants.STRENGTH, 99)
            .set(SkillConstants.ATTACK, 40)
            .set(SkillConstants.DEFENCE, 1)
            .set(SkillConstants.PRAYER, 44)
            .set(SkillConstants.HITPOINTS, 99)
            .set(SkillConstants.RANGED, 99)
            .build(),
        presetSpellbook = Spellbook.LUNAR,
        presetRunePouch = null,
        maximumBrews = 2,
        disabledPrayers = arrayOf(
            Prayer.PROTECT_FROM_MELEE,
            Prayer.PROTECT_FROM_MISSILES,
            Prayer.PROTECT_FROM_MAGIC
        )
    ) {
        override fun toString(): String = "F2P Pure"
    },

    MYSTERY_BOX(
        checkBaseItem = ItemId.PVP_MYSTERY_BOX,
        announcementIcon = 57,
        presetEquipment = EquipmentComponent.Builder()
            .build(),
        presetInventory = InventoryComponent.Builder()
            .add(ItemId.SHARK, 12, true).add(CustomItemId.PVP_TOURNEY_MYSTERY_BOX, 10,true)
            .build(),
        presetStats = SkillsComponent.Builder()
            .set(SkillConstants.STRENGTH, 99)
            .set(SkillConstants.ATTACK, 99)
            .set(SkillConstants.DEFENCE, 99)
            .set(SkillConstants.PRAYER, 99)
            .set(SkillConstants.AGILITY, 70)
            .set(SkillConstants.HITPOINTS, 99)
            .set(SkillConstants.RANGED, 99)
            .set(SkillConstants.MAGIC, 99)
            .build(),
        presetSpellbook = Spellbook.ANCIENT,
        presetRunePouch = null,
        maximumBrews = 2,
        disabledPrayers = arrayOf(
            Prayer.PROTECT_FROM_MELEE,
            Prayer.PROTECT_FROM_MISSILES,
            Prayer.PROTECT_FROM_MAGIC
        )
    ) {
        override fun toString(): String = "Mystery Box"
    },

    KARILS_DHAROKS(
        checkBaseItem = ItemId.KARILS_CROSSBOW,
        announcementIcon = 54,
        presetEquipment = EquipmentComponent.Builder()
            .put(EquipmentSlot.HELMET, 4732, true)
            .put(EquipmentSlot.CAPE, 21295, true)
            .put(EquipmentSlot.AMULET, 12853, true)
            .put(EquipmentSlot.WEAPON, 4734, true)
            .put(EquipmentSlot.PLATE, 4736, true)
            .put(EquipmentSlot.LEGS, 4738, true)
            .put(EquipmentSlot.HANDS, 7462, true)
            .put(EquipmentSlot.BOOTS, 13239, true)
            .put(EquipmentSlot.RING, 11773, true)
            .put(EquipmentSlot.AMMUNITION, 4740, 10000, true)
            .build(),
        presetInventory = InventoryComponent.Builder()
            .add(4716, true).add(4720, true)
            .add(12695, true).add(2444, true)
            .add(4718, true).add(4722, true)
            .add(6685, true).add(24225, true)
            .add(3144, 2, true).add(10925, 2, true)
            .add(13441, 15, true).add(30006, false)
            .build(),
        presetStats = SkillsComponent.Builder()
            .set(SkillConstants.STRENGTH, 99)
            .set(SkillConstants.ATTACK, 99)
            .set(SkillConstants.DEFENCE, 85)
            .set(SkillConstants.PRAYER, 99)
            .set(SkillConstants.HITPOINTS, 99)
            .set(SkillConstants.MAGIC, 99)
            .set(SkillConstants.RANGED, 99)
            .build(),
        presetSpellbook = Spellbook.LUNAR,
        presetRunePouch = RunePouchComponent.VENGEANCE,
        maximumBrews = 2,
        disabledPrayers = arrayOf(
            Prayer.PROTECT_FROM_MELEE,
            Prayer.PROTECT_FROM_MISSILES,
            Prayer.PROTECT_FROM_MAGIC
        )
    ) {
        override fun toString(): String = "Karil's/Dharok's"
    },

    MELEE_PURE(
        checkBaseItem = ItemId.ZAMORAK_HALO,
        announcementIcon = 67,
        presetEquipment = EquipmentComponent.Builder()
            .put(EquipmentSlot.HELMET, 12638, true)
            .put(EquipmentSlot.CAPE, 21295, true)
            .put(EquipmentSlot.AMULET, 19553, true)
            .put(EquipmentSlot.WEAPON, 12006, true)
            .put(EquipmentSlot.PLATE, 11899, true)
            .put(EquipmentSlot.SHIELD, 12608, true)
            .put(EquipmentSlot.LEGS, 23246, true)
            .put(EquipmentSlot.HANDS, 11133, true)
            .put(EquipmentSlot.BOOTS, 23389, true)
            .put(EquipmentSlot.RING, 11773, true)
            .put(EquipmentSlot.AMMUNITION, 20235, true)
            .build(),
        presetInventory = InventoryComponent.Builder()
            .add(12695, true).add(6685, true)
            .add(10925, true).add(13441, true)
            .add(6685, true).add(10925, true)
            .add(13441, 2, true).add(3144, 4, true)
            .add(13441, 9, true).add(13652, true)
            .add(21003, true).add(13441, 5, true)
            .build(),
        presetStats = SkillsComponent.Builder()
            .set(SkillConstants.STRENGTH, 99)
            .set(SkillConstants.ATTACK, 75)
            .set(SkillConstants.DEFENCE, 1)
            .set(SkillConstants.PRAYER, 52)
            .set(SkillConstants.HITPOINTS, 99)
            .set(SkillConstants.MAGIC, 99)
            .set(SkillConstants.RANGED, 99)
            .build(),
        presetSpellbook = Spellbook.NORMAL,
        presetRunePouch = null,
        maximumBrews = 2,
        disabledPrayers = arrayOf(
            Prayer.PROTECT_FROM_MELEE,
            Prayer.PROTECT_FROM_MISSILES,
            Prayer.PROTECT_FROM_MAGIC,
            Prayer.SMITE
        )
    ),

    MAX_MELEE(
        checkBaseItem = ItemId.TORVA_FULLHELM,
        announcementIcon = 62,
        presetEquipment = EquipmentComponent.Builder()
            .put(EquipmentSlot.HELMET, 26382, true)
            .put(EquipmentSlot.CAPE, 21295, true)
            .put(EquipmentSlot.AMULET, 19553, true)
            .put(EquipmentSlot.WEAPON, 12006, true)
            .put(EquipmentSlot.PLATE, 26384, true)
            .put(EquipmentSlot.SHIELD, 22322, true)
            .put(EquipmentSlot.LEGS, 26386, true)
            .put(EquipmentSlot.HANDS, 22981, true)
            .put(EquipmentSlot.BOOTS, 13239, true)
            .put(EquipmentSlot.RING, 11773, true)
            .put(EquipmentSlot.AMMUNITION, 20232, true)
            .build(),
        presetInventory = InventoryComponent.Builder()
            .add(12695, true).add(10925, 2, true)
            .add(6685, true).add(12695, true)
            .add(11802, true).add(24225, true)
            .add(3144, 4, true).add(13441, 16, true)
            .add(30006, false)
            .build(),
        presetStats = SkillsComponent.Builder()
            .set(SkillConstants.STRENGTH, 99)
            .set(SkillConstants.ATTACK, 99)
            .set(SkillConstants.DEFENCE, 99)
            .set(SkillConstants.PRAYER, 99)
            .set(SkillConstants.HITPOINTS, 99)
            .set(SkillConstants.MAGIC, 99)
            .set(SkillConstants.RANGED, 99)
            .build(),
        presetSpellbook = Spellbook.LUNAR,
        presetRunePouch = RunePouchComponent.VENGEANCE,
        maximumBrews = 1,
        disabledPrayers = arrayOf(
            Prayer.PROTECT_FROM_MELEE,
            Prayer.PROTECT_FROM_MISSILES,
            Prayer.PROTECT_FROM_MAGIC
        )
    ),

    ZERKER_MID_LEVEL(
        checkBaseItem = ItemId.BERSERKER_HELM,
        announcementIcon = 66,
        presetEquipment = EquipmentComponent.Builder()
            .put(EquipmentSlot.HELMET, 3751, true)
            .put(EquipmentSlot.CAPE, 21295, true)
            .put(EquipmentSlot.AMULET, 19553, true)
            .put(EquipmentSlot.WEAPON, 4587, true)
            .put(EquipmentSlot.PLATE, 10551, true)
            .put(EquipmentSlot.SHIELD, 8850, true)
            .put(EquipmentSlot.LEGS, 23246, true)
            .put(EquipmentSlot.HANDS, 7462, true)
            .put(EquipmentSlot.BOOTS, 23389, true)
            .put(EquipmentSlot.RING, 11773, true)
            .put(EquipmentSlot.AMMUNITION, 20235, true)
            .build(),
        presetInventory = InventoryComponent.Builder()
            .add(13652, true).add(24225, true)
            .add(12695, true).add(6685, true)
            .add(10925, 2, true).add(3144, 4, true)
            .add(13441, 17, true).add(30006, false)
            .build(),
        presetStats = SkillsComponent.Builder()
            .set(SkillConstants.STRENGTH, 99)
            .set(SkillConstants.ATTACK, 60)
            .set(SkillConstants.DEFENCE, 45)
            .set(SkillConstants.PRAYER, 52)
            .set(SkillConstants.HITPOINTS, 99)
            .set(SkillConstants.MAGIC, 99)
            .set(SkillConstants.RANGED, 99)
            .build(),
        presetSpellbook = Spellbook.LUNAR,
        presetRunePouch = RunePouchComponent.VENGEANCE,
        maximumBrews = 2,
        disabledPrayers = arrayOf(
            Prayer.PROTECT_FROM_MELEE,
            Prayer.PROTECT_FROM_MISSILES,
            Prayer.PROTECT_FROM_MAGIC,
            Prayer.SMITE
        )
    ) {
        override fun toString(): String = "Zerker Mid-Lvl"
    },
    ZERKER_HIGH_LVL(
        checkBaseItem = ItemId.BERSERKER_HELM,
        announcementIcon = 66,
        presetEquipment = EquipmentComponent.Builder()
            .put(EquipmentSlot.HELMET, 3751, true)
            .put(EquipmentSlot.CAPE, 21295, true)
            .put(EquipmentSlot.AMULET, 6585, true)
            .put(EquipmentSlot.WEAPON, 12788, true)
            .put(EquipmentSlot.PLATE, 10551, true)
            .put(EquipmentSlot.LEGS, 23246, true)
            .put(EquipmentSlot.HANDS, 7462, true)
            .put(EquipmentSlot.BOOTS, 23389, true)
            .put(EquipmentSlot.RING, 11773, true)
            .put(EquipmentSlot.AMMUNITION, 21326, 10000, true)
            .build(),
        presetInventory = InventoryComponent.Builder()
            .add(11802, true).add(24225, true)
            .add(2444, true).add(12695, true)
            .add(6685, true).add(10925, 2, true)
            .add(3144, 4, true).add(6685, true)
            .add(13441, 15, true).add(30006, false)
            .build(),
        presetStats = SkillsComponent.Builder()
            .set(SkillConstants.STRENGTH, 99)
            .set(SkillConstants.ATTACK, 75)
            .set(SkillConstants.DEFENCE, 45)
            .set(SkillConstants.PRAYER, 52)
            .set(SkillConstants.HITPOINTS, 99)
            .set(SkillConstants.MAGIC, 99)
            .set(SkillConstants.RANGED, 99)
            .build(),
        presetSpellbook = Spellbook.LUNAR,
        presetRunePouch = RunePouchComponent.VENGEANCE,
        maximumBrews = 2,
        disabledPrayers = arrayOf(
            Prayer.PROTECT_FROM_MELEE,
            Prayer.PROTECT_FROM_MISSILES,
            Prayer.PROTECT_FROM_MAGIC,
            Prayer.SMITE
        )
    ) {
        override fun toString(): String = "Zerker High-Lvl"
    },

    WELFARE_BRID(
        checkBaseItem = ItemId.MYSTIC_ROBE_TOP,
        announcementIcon = 60,
        presetEquipment = EquipmentComponent.Builder()
            .put(EquipmentSlot.HELMET, 10828, true)
            .put(EquipmentSlot.CAPE, 21791, true)
            .put(EquipmentSlot.AMULET, 6585, true)
            .put(EquipmentSlot.WEAPON, 22296, true)
            .put(EquipmentSlot.PLATE, 4091, true)
            .put(EquipmentSlot.SHIELD, 12831, true)
            .put(EquipmentSlot.LEGS, 4093, true)
            .put(EquipmentSlot.HANDS, 7462, true)
            .put(EquipmentSlot.BOOTS, 11840, true)
            .put(EquipmentSlot.RING, 11773, true)
            .put(EquipmentSlot.AMMUNITION, 20235, true)
            .build(),
        presetInventory = InventoryComponent.Builder()
            .add(4720, true).add(12006, true)
            .add(21295, true).add(13441, true)
            .add(4759, true).add(22322, true)
            .add(13441, 2, true).add(4736, true)
            .add(11802, true).add(13441, 2, true)
            .add(6685, 3, true).add(13441, true)
            .add(12695, true).add(10925, 2, true)
            .add(13441, 8, true).add(30006, false)
            .build(),
        presetStats = SkillsComponent.Builder()
            .set(SkillConstants.STRENGTH, 99)
            .set(SkillConstants.ATTACK, 99)
            .set(SkillConstants.DEFENCE, 99)
            .set(SkillConstants.PRAYER, 99)
            .set(SkillConstants.HITPOINTS, 99)
            .set(SkillConstants.MAGIC, 99)
            .set(SkillConstants.RANGED, 99)
            .build(),
        presetSpellbook = Spellbook.ANCIENT,
        presetRunePouch = RunePouchComponent.ICE_BARRAGE,
        maximumBrews = 3,
        disabledPrayers = arrayOf(
            Prayer.PROTECT_FROM_MELEE,
            Prayer.PROTECT_FROM_MISSILES,
            Prayer.PROTECT_FROM_MAGIC
        )
    ),

    TRIBRID(
        checkBaseItem = ItemId.HELM_OF_NEITIZNOT,
        announcementIcon = 56,
        presetEquipment = EquipmentComponent.Builder()
            .put(EquipmentSlot.HELMET, 10828, false)
            .put(EquipmentSlot.CAPE, 21795, false)
            .put(EquipmentSlot.AMULET, 6585, false)
            .put(EquipmentSlot.WEAPON, 22296, false)
            .put(EquipmentSlot.PLATE, 4101, false)
            .put(EquipmentSlot.SHIELD, 12831, false)
            .put(EquipmentSlot.LEGS, 4103, false)
            .put(EquipmentSlot.HANDS, 7462, false)
            .put(EquipmentSlot.BOOTS, 11840, false)
            .put(EquipmentSlot.RING, 11773, false)
            .put(EquipmentSlot.AMMUNITION, 21932, 10000, false)
            .build(),
        presetInventory = InventoryComponent.Builder()
            .add(4736, false).add(21902, false)
            .add(12006, false).add(13441, true)
            .add(4759, false).add(22109, false)
            .add(22322, false).add(13441, true)
            .add(2444, true).add(11802, true)
            .add(13441, 2, true).add(10925, 2, true)
            .add(12695, true).add(13441, true)
            .add(6685, 3, true).add(13441, 8, true)
            .add(30006, false)
            .build(),
        presetStats = SkillsComponent.Builder()
            .set(SkillConstants.STRENGTH, 99)
            .set(SkillConstants.ATTACK, 99)
            .set(SkillConstants.DEFENCE, 99)
            .set(SkillConstants.PRAYER, 99)
            .set(SkillConstants.HITPOINTS, 99)
            .set(SkillConstants.MAGIC, 99)
            .set(SkillConstants.RANGED, 99)
            .build(),
        presetSpellbook = Spellbook.ANCIENT,
        presetRunePouch = RunePouchComponent.ICE_BARRAGE,
        maximumBrews = 3,
        disabledPrayers = emptyArray()
    ),

//    VOID_RANGED(
//        checkBaseItem = ItemId.VOID_RANGER_HELM,
//        announcementIcon = 65,
//        presetEquipment = EquipmentComponent.Builder()
//            .put(EquipmentSlot.HELMET, 11664, true)
//            .put(EquipmentSlot.CAPE, 22109, true)
//            .put(EquipmentSlot.AMULET, 22249, true)
//            .put(EquipmentSlot.WEAPON, 22810, 1000, true)
//            .put(EquipmentSlot.PLATE, 13072, true)
//            .put(EquipmentSlot.SHIELD, 21000, true)
//            .put(EquipmentSlot.LEGS, 13073, true)
//            .put(EquipmentSlot.HANDS, 8842, true)
//            .put(EquipmentSlot.BOOTS, 13237, true)
//            .put(EquipmentSlot.RING, 11771, true)
//            .put(EquipmentSlot.AMMUNITION, 21946, 10000, true)
//            .build(),
//        presetInventory = InventoryComponent.Builder()
//            .add(19481, true).add(19484, 200, true)
//            .add(20849, 200, true).add(26374, true)
//            .add(2444, true).add(10925, 2, true)
//            .add(6685, true).add(3144, 4, true)
//            .add(13441, 15, true).add(30006, false)
//            .build(),
//        presetStats = SkillsComponent.Builder()
//            .set(SkillConstants.STRENGTH, 42)
//            .set(SkillConstants.ATTACK, 42)
//            .set(SkillConstants.DEFENCE, 75)
//            .set(SkillConstants.PRAYER, 74)
//            .set(SkillConstants.HITPOINTS, 99)
//            .set(SkillConstants.MAGIC, 99)
//            .set(SkillConstants.RANGED, 99)
//            .build(),
//        presetSpellbook = Spellbook.LUNAR,
//        presetRunePouch = RunePouchComponent.VENGEANCE,
//        maximumBrews = 2,
//        disabledPrayers = arrayOf(
//            Prayer.PROTECT_FROM_MELEE,
//            Prayer.PROTECT_FROM_MISSILES,
//            Prayer.PROTECT_FROM_MAGIC
//        )
//    ),

    OBSIDIAN(
        checkBaseItem = ItemId.TOKTZXILAK,
        announcementIcon = 64,
        presetEquipment = EquipmentComponent.Builder()
            .put(EquipmentSlot.HELMET, 21298, true)
            .put(EquipmentSlot.CAPE, 21295, true)
            .put(EquipmentSlot.AMULET, 11128, true)
            .put(EquipmentSlot.WEAPON, 6523, true)
            .put(EquipmentSlot.PLATE, 21301, true)
            .put(EquipmentSlot.SHIELD, 19722, true)
            .put(EquipmentSlot.LEGS, 21304, true)
            .put(EquipmentSlot.HANDS, 7462, true)
            .put(EquipmentSlot.BOOTS, 13239, true)
            .put(EquipmentSlot.RING, 11773, true)
            .build(),
        presetInventory = InventoryComponent.Builder()
            .add(12695, true).add(6685, true)
            .add(10925, 2, true).add(6528, true)
            .add(24225, true).add(3144, 4, true)
            .add(13441, 17, true).add(30006, false)
            .build(),
        presetStats = SkillsComponent.Builder()
            .set(SkillConstants.STRENGTH, 99)
            .set(SkillConstants.ATTACK, 60)
            .set(SkillConstants.DEFENCE, 75)
            .set(SkillConstants.PRAYER, 85)
            .set(SkillConstants.HITPOINTS, 99)
            .set(SkillConstants.MAGIC, 99)
            .set(SkillConstants.RANGED, 99)
            .build(),
        presetSpellbook = Spellbook.LUNAR,
        presetRunePouch = RunePouchComponent.VENGEANCE,
        maximumBrews = 2,
        disabledPrayers = arrayOf(
            Prayer.PROTECT_FROM_MELEE,
            Prayer.PROTECT_FROM_MISSILES,
            Prayer.PROTECT_FROM_MAGIC
        )
    ),

    VESTA(
        checkBaseItem = ItemId.VESTAS_LONGSWORD,
        announcementIcon = 61,
        presetEquipment = EquipmentComponent.Builder()
            .put(EquipmentSlot.HELMET, 24271, true)
            .put(EquipmentSlot.CAPE, 21295, true)
            .put(EquipmentSlot.AMULET, 19553, true)
            .put(EquipmentSlot.WEAPON, 22324, true)
            .put(EquipmentSlot.PLATE, 22616, true)
            .put(EquipmentSlot.SHIELD, 22322, true)
            .put(EquipmentSlot.LEGS, 22619, true)
            .put(EquipmentSlot.HANDS, 22981, true)
            .put(EquipmentSlot.BOOTS, 13239, true)
            .put(EquipmentSlot.RING, 11773, true)
            .build(),
        presetInventory = InventoryComponent.Builder()
            .add(12695, true).add(6685, 2, true)
            .add(13441, true).add(10925, 2, true)
            .add(3144, true).add(13441, true)
            .add(3144, true).add(13441, true)
            .add(3144, true).add(13441, 2, true)
            .add(3144, true).add(13441, 7, true)
            .add(22613, true).add(5698, true)
            .add(13441, 4, true).add(30006, false)
            .build(),
        presetStats = SkillsComponent.Builder()
            .set(SkillConstants.STRENGTH, 99)
            .set(SkillConstants.ATTACK, 99)
            .set(SkillConstants.DEFENCE, 99)
            .set(SkillConstants.PRAYER, 99)
            .set(SkillConstants.HITPOINTS, 99)
            .set(SkillConstants.MAGIC, 99)
            .set(SkillConstants.RANGED, 99)
            .build(),
        presetSpellbook = Spellbook.LUNAR,
        presetRunePouch = RunePouchComponent.VENGEANCE,
        maximumBrews = 2,
        disabledPrayers = arrayOf(
            Prayer.PROTECT_FROM_MELEE,
            Prayer.PROTECT_FROM_MISSILES,
            Prayer.PROTECT_FROM_MAGIC
        )
    ),

    PureNHBrid(
        checkBaseItem = ItemId.ELDER_CHAOS_HOOD,
        announcementIcon = 58,
        presetEquipment = EquipmentComponent.Builder()
            .put(EquipmentSlot.HELMET, 20595, true)
            .put(EquipmentSlot.CAPE, 21795, true)
            .put(EquipmentSlot.AMULET, 12002, true)
            .put(EquipmentSlot.WEAPON, 12904, true)
            .put(EquipmentSlot.PLATE, 20517, true)
            .put(EquipmentSlot.SHIELD, 3842, true)
            .put(EquipmentSlot.LEGS, 20520, true)
            .put(EquipmentSlot.HANDS, 11133, true)
            .put(EquipmentSlot.BOOTS, 23389, true)
            .put(EquipmentSlot.AMMUNITION, 21950, 10000, true)
            .put(EquipmentSlot.RING, 11773, true)
            .build(),
        presetInventory = InventoryComponent.Builder()
            .add(6585, true).add(12383, true)
            .add(12695, true).add(6685, true)
            .add(11785, true).add(10499, true)
            .add(2444, true).add(6685, true)
            .add(21295, true).add(10925, 2, true)
            .add(4587, true).add(13652, true)
            .add(3144, 2, true).add(13441, 12, true)
            .add(30006, true)
            .build(),
        presetStats = SkillsComponent.Builder()
            .set(SkillConstants.STRENGTH, 99)
            .set(SkillConstants.ATTACK, 75)
            .set(SkillConstants.DEFENCE, 1)
            .set(SkillConstants.PRAYER, 45)
            .set(SkillConstants.HITPOINTS, 90)
            .set(SkillConstants.MAGIC, 99)
            .set(SkillConstants.RANGED, 99)
            .build(),
        presetSpellbook = Spellbook.ANCIENT,
        presetRunePouch = RunePouchComponent.ICE_BARRAGE,
        maximumBrews = 3,
        disabledPrayers = emptyArray()
    ) {
        override fun toString(): String = "Elder Pure Brid"
    },

    GraniteSLAB(
        checkBaseItem = -1,
        announcementIcon = -1,
        includeInAutoSchedulePool = false,
        presetEquipment = EquipmentComponent.Builder()
            .put(EquipmentSlot.HELMET, 10589, true)
            .put(EquipmentSlot.CAPE, 21295, true)
            .put(EquipmentSlot.AMULET, 6585, true)
            .put(EquipmentSlot.WEAPON, 21742, true)
            .put(EquipmentSlot.PLATE, 10564, true)
            .put(EquipmentSlot.SHIELD, 3122, true)
            .put(EquipmentSlot.LEGS, 6809, true)
            .put(EquipmentSlot.HANDS, 21736, true)
            .put(EquipmentSlot.BOOTS, 21643, true)
            .put(EquipmentSlot.RING, 12691, true)
            .build(),
        presetInventory = InventoryComponent.Builder()
            .add(12695, true).add(6685, true)
            .add(6685, true).add(6685, true)
            .add(10925, 2, true).add(6969, 4, true)
            .add(3144, 5, true).add(24225, true)
            .add(6969, 12, true).add(30006, false)
            .build(),
        presetStats = SkillsComponent.Builder()
            .set(SkillConstants.STRENGTH, 60)
            .set(SkillConstants.ATTACK, 50)
            .set(SkillConstants.DEFENCE, 50)
            .set(SkillConstants.PRAYER, 43)
            .set(SkillConstants.HITPOINTS, 65)
            .set(SkillConstants.MAGIC, 1)
            .set(SkillConstants.RANGED, 1)
            .build(),
        presetSpellbook = Spellbook.NORMAL,
        presetRunePouch = RunePouchComponent.VENGEANCE,
        maximumBrews = 1,
        disabledPrayers = arrayOf(
            Prayer.PROTECT_FROM_MELEE,
            Prayer.PROTECT_FROM_MISSILES,
            Prayer.PROTECT_FROM_MAGIC
        )
    ) {
        override fun toString(): String = "Granite Slab"
    },

    BISSTAB(
        checkBaseItem = ItemId.GHRAZI_RAPIER,
        announcementIcon = 55,
        presetEquipment = EquipmentComponent.Builder()
            .put(EquipmentSlot.HELMET, 10548, true)
            .put(EquipmentSlot.CAPE, 20760, true)
            .put(EquipmentSlot.AMULET, 19553, true)
            .put(EquipmentSlot.WEAPON, 22324, true)
            .put(EquipmentSlot.PLATE, 26384, true)
            .put(EquipmentSlot.SHIELD, 22322, true)
            .put(EquipmentSlot.LEGS, 26386, true)
            .put(EquipmentSlot.HANDS, 22981, true)
            .put(EquipmentSlot.BOOTS, 22951, true)
            .put(EquipmentSlot.RING, 12692, true)
            .put(EquipmentSlot.AMMUNITION, 20220, true)
            .build(),
        presetInventory = InventoryComponent.Builder()
            .add(12695, true).add(6685, true)
            .add(6685, true).add(6685, true)
            .add(10925, 2, true).add(13441, 4, true)
            .add(3144, 5, true).add(22613, true)
            .add(13441, 11, true).add(30006, true)
            .build(),
        presetStats = SkillsComponent.Builder()
            .set(SkillConstants.STRENGTH, 99)
            .set(SkillConstants.ATTACK, 99)
            .set(SkillConstants.DEFENCE, 99)
            .set(SkillConstants.PRAYER, 99)
            .set(SkillConstants.HITPOINTS, 99)
            .set(SkillConstants.MAGIC, 99)
            .set(SkillConstants.RANGED, 99)
            .build(),
        presetSpellbook = Spellbook.LUNAR,
        presetRunePouch = RunePouchComponent.VENGEANCE,
        maximumBrews = 2,
        disabledPrayers = arrayOf(
            Prayer.PROTECT_FROM_MELEE,
            Prayer.PROTECT_FROM_MISSILES,
            Prayer.PROTECT_FROM_MAGIC
        )
    ) {
        override fun toString(): String = "Best In Slot - Stab"
    },

    BISSLASH(
        checkBaseItem = ItemId.BLADE_OF_SAELDOR_C,
        announcementIcon = 59,
        presetEquipment = EquipmentComponent.Builder()
            .put(EquipmentSlot.HELMET, 10548, true)
            .put(EquipmentSlot.CAPE, 21295, true)
            .put(EquipmentSlot.AMULET, 19553, true)
            .put(EquipmentSlot.WEAPON, 24551, true)
            .put(EquipmentSlot.PLATE, 11832, true)
            .put(EquipmentSlot.SHIELD, 22322, true)
            .put(EquipmentSlot.LEGS, 11834, true)
            .put(EquipmentSlot.HANDS, 22981, true)
            .put(EquipmentSlot.BOOTS, 13239, true)
            .put(EquipmentSlot.RING, 11772, true)
            .put(EquipmentSlot.AMMUNITION, 20220, true)
            .build(),
        presetInventory = InventoryComponent.Builder()
            .add(12695, true).add(6685, true)
            .add(6685, true).add(6685, true)
            .add(10925, 2, true).add(13441, 4, true)
            .add(3144, 5, true).add(11802, true)
            .add(13441, 11, true).add(30006, true)
            .build(),
        presetStats = SkillsComponent.Builder()
            .set(SkillConstants.STRENGTH, 99)
            .set(SkillConstants.ATTACK, 99)
            .set(SkillConstants.DEFENCE, 99)
            .set(SkillConstants.PRAYER, 99)
            .set(SkillConstants.HITPOINTS, 99)
            .set(SkillConstants.MAGIC, 99)
            .set(SkillConstants.RANGED, 99)
            .build(),
        presetSpellbook = Spellbook.LUNAR,
        presetRunePouch = RunePouchComponent.VENGEANCE,
        maximumBrews = 2,
        disabledPrayers = arrayOf(
            Prayer.PROTECT_FROM_MELEE,
            Prayer.PROTECT_FROM_MISSILES,
            Prayer.PROTECT_FROM_MAGIC
        )
    ) {
        override fun toString(): String = "Best In Slot - Slash"
    },

    BISSMASH(
        checkBaseItem = ItemId.ELDER_MAUL,
        announcementIcon = 63,
        presetEquipment = EquipmentComponent.Builder()
            .put(EquipmentSlot.HELMET, 24419, true)
            .put(EquipmentSlot.CAPE, 24855, true)
            .put(EquipmentSlot.AMULET, 19553, true)
            .put(EquipmentSlot.WEAPON, 24417, true)
            .put(EquipmentSlot.PLATE, 24420, true)
            .put(EquipmentSlot.SHIELD, 22322, true)
            .put(EquipmentSlot.LEGS, 24421, true)
            .put(EquipmentSlot.HANDS, 22981, true)
            .put(EquipmentSlot.BOOTS, 13239, true)
            .put(EquipmentSlot.RING, 12691, true)
            .put(EquipmentSlot.AMMUNITION, 20220, true)
            .build(),
        presetInventory = InventoryComponent.Builder()
            .add(12695, true).add(6685, true)
            .add(6685, true).add(6685, true)
            .add(10925, 2, true).add(13441, 4, true)
            .add(3144, 5, true).add(21003, true)
            .add(13441, 11, true).add(30006, true)
            .build(),
        presetStats = SkillsComponent.Builder()
            .set(SkillConstants.STRENGTH, 99)
            .set(SkillConstants.ATTACK, 99)
            .set(SkillConstants.DEFENCE, 99)
            .set(SkillConstants.PRAYER, 99)
            .set(SkillConstants.HITPOINTS, 99)
            .set(SkillConstants.MAGIC, 99)
            .set(SkillConstants.RANGED, 99)
            .build(),
        presetSpellbook = Spellbook.LUNAR,
        presetRunePouch = RunePouchComponent.VENGEANCE,
        maximumBrews = 2,
        disabledPrayers = arrayOf(
            Prayer.PROTECT_FROM_MELEE,
            Prayer.PROTECT_FROM_MISSILES,
            Prayer.PROTECT_FROM_MAGIC
        )
    ) {
        override fun toString(): String = "Best In Slot - Crush"
    };

    val announcementIcon: String = "<img=$announcementIcon>"
    val checkBaseItem: Item = Item(checkBaseItem)

    fun apply(player: Player) {
        val tempInventory = player.inventoryTemp
        val tempEquipment = player.equipmentTemp
        tempInventory.container.clear()
        tempEquipment.container.clear()
        for (item in presetInventory.items)
            tempInventory.container.add(item.t)
        for ((key, value) in presetEquipment.items) {
            if(key == 3 && this == BOXING && player.previousBoxingWinner) tempEquipment.container[key] = Item(ItemId.GILDED_BOXING_GLOVES)
            else tempEquipment.container[key] = value?.t
        }
        val tempSkills = player.skillsTemp
        for ((key, level) in presetStats.skills)
            tempSkills.setSkill(key, level, Skills.getXPForLevel(level).toDouble())
        val tempSecondaryRunePouch = player.secondaryRunePouchTemp
        if (presetRunePouch != null) {
            tempSecondaryRunePouch.clear()
            for ((slot, entry) in presetRunePouch.entries.withIndex())
                tempSecondaryRunePouch.container[slot] = Item(entry.rune.id, entry.amount)
        }
        player.combatDefinitions.apply {
            tempSpellbook = presetSpellbook
        }
        player.bonuses.update()

    }

    override fun toString(): String =
        StringFormatUtil.formatString(name.replace("_".toRegex(), " ").lowercase(Locale.getDefault()))
}
