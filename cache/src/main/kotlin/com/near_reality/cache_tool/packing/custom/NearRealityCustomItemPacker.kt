package com.near_reality.cache_tool.packing.custom

import com.near_reality.cache_tool.packing.assetsBase
import com.near_reality.game.item.CustomItemId
import com.zenyte.ContentConstants
import com.zenyte.game.item.ItemId.ABYSSAL_DAGGER
import com.zenyte.game.item.ItemId.ABYSSAL_DAGGER_BH
import com.zenyte.game.item.ItemId.ABYSSAL_DAGGER_BHP
import com.zenyte.game.item.ItemId.ABYSSAL_DAGGER_BHP_27865
import com.zenyte.game.item.ItemId.ABYSSAL_DAGGER_BHP_27867
import com.zenyte.game.item.ItemId.ABYSSAL_DAGGER_P
import com.zenyte.game.item.ItemId.ABYSSAL_DAGGER_P_13269
import com.zenyte.game.item.ItemId.ABYSSAL_DAGGER_P_13271
import com.zenyte.game.item.ItemId.AHRIMS_ARMOUR_SET
import com.zenyte.game.item.ItemId.AHRIMS_HOOD
import com.zenyte.game.item.ItemId.AHRIMS_ROBESKIRT
import com.zenyte.game.item.ItemId.AHRIMS_ROBETOP
import com.zenyte.game.item.ItemId.AHRIMS_STAFF
import com.zenyte.game.item.ItemId.ATTACKER_ICON
import com.zenyte.game.item.ItemId.BANDOS_CHESTPLATE
import com.zenyte.game.item.ItemId.BANDOS_TASSETS
import com.zenyte.game.item.ItemId.BARRELCHEST_ANCHOR_BH
import com.zenyte.game.item.ItemId.BURNING_CLAWS
import com.zenyte.game.item.ItemId.COLLECTOR_ICON
import com.zenyte.game.item.ItemId.DARK_BOW_BH
import com.zenyte.game.item.ItemId.DEFENDER_ICON
import com.zenyte.game.item.ItemId.DHAROKS_ARMOUR_SET
import com.zenyte.game.item.ItemId.DHAROKS_GREATAXE
import com.zenyte.game.item.ItemId.DHAROKS_HELM
import com.zenyte.game.item.ItemId.DHAROKS_PLATEBODY
import com.zenyte.game.item.ItemId.DHAROKS_PLATELEGS
import com.zenyte.game.item.ItemId.DIVINE_RUNE_POUCH
import com.zenyte.game.item.ItemId.DRAGON_2H_SWORD_CR
import com.zenyte.game.item.ItemId.DRAGON_BATTLEAXE_CR
import com.zenyte.game.item.ItemId.DRAGON_CLAWS_CR
import com.zenyte.game.item.ItemId.DRAGON_CROSSBOW_CR
import com.zenyte.game.item.ItemId.DRAGON_DAGGER_CR
import com.zenyte.game.item.ItemId.DRAGON_DAGGER_PCR
import com.zenyte.game.item.ItemId.DRAGON_DAGGER_PCR_28023
import com.zenyte.game.item.ItemId.DRAGON_DAGGER_PCR_28025
import com.zenyte.game.item.ItemId.DRAGON_HALBERD_CR
import com.zenyte.game.item.ItemId.DRAGON_LONGSWORD_BH
import com.zenyte.game.item.ItemId.DRAGON_LONGSWORD_CR
import com.zenyte.game.item.ItemId.DRAGON_MACE_BH
import com.zenyte.game.item.ItemId.DRAGON_MACE_CR
import com.zenyte.game.item.ItemId.DRAGON_SCIMITAR_CR
import com.zenyte.game.item.ItemId.DRAGON_SPEAR_CR
import com.zenyte.game.item.ItemId.DRAGON_SPEAR_PCR
import com.zenyte.game.item.ItemId.DRAGON_SPEAR_PCR_28045
import com.zenyte.game.item.ItemId.DRAGON_SPEAR_PCR_28047
import com.zenyte.game.item.ItemId.DRAGON_SWORD_CR
import com.zenyte.game.item.ItemId.DRAGON_WARHAMMER_CR
import com.zenyte.game.item.ItemId.EMBERLIGHT
import com.zenyte.game.item.ItemId.GUTHANS_ARMOUR_SET
import com.zenyte.game.item.ItemId.GUTHANS_CHAINSKIRT
import com.zenyte.game.item.ItemId.GUTHANS_HELM
import com.zenyte.game.item.ItemId.GUTHANS_PLATEBODY
import com.zenyte.game.item.ItemId.GUTHANS_WARSPEAR
import com.zenyte.game.item.ItemId.HEALER_ICON
import com.zenyte.game.item.ItemId.KARILS_ARMOUR_SET
import com.zenyte.game.item.ItemId.KARILS_COIF
import com.zenyte.game.item.ItemId.KARILS_CROSSBOW
import com.zenyte.game.item.ItemId.KARILS_LEATHERSKIRT
import com.zenyte.game.item.ItemId.KARILS_LEATHERTOP
import com.zenyte.game.item.ItemId.LARRANS_KEY
import com.zenyte.game.item.ItemId.MORRIGANS_JAVELIN_BH
import com.zenyte.game.item.ItemId.MORRIGANS_THROWING_AXE_BH
import com.zenyte.game.item.ItemId.PURGING_STAFF
import com.zenyte.game.item.ItemId.RING_OF_LEVITATION
import com.zenyte.game.item.ItemId.SCORCHING_BOW
import com.zenyte.game.item.ItemId.SKELETON_BOOTS
import com.zenyte.game.item.ItemId.SKELETON_GLOVES
import com.zenyte.game.item.ItemId.SKELETON_LEGGINGS
import com.zenyte.game.item.ItemId.SKELETON_MASK
import com.zenyte.game.item.ItemId.SKELETON_SHIRT
import com.zenyte.game.item.ItemId.SLED_25282
import com.zenyte.game.item.ItemId.SLED_4084
import com.zenyte.game.item.ItemId.STATIUSS_WARHAMMER_BH
import com.zenyte.game.item.ItemId.TORAGS_ARMOUR_SET
import com.zenyte.game.item.ItemId.TORAGS_HAMMERS
import com.zenyte.game.item.ItemId.TORAGS_HELM
import com.zenyte.game.item.ItemId.TORAGS_PLATEBODY
import com.zenyte.game.item.ItemId.TORAGS_PLATELEGS
import com.zenyte.game.item.ItemId.VERACS_ARMOUR_SET
import com.zenyte.game.item.ItemId.VERACS_BRASSARD
import com.zenyte.game.item.ItemId.VERACS_FLAIL
import com.zenyte.game.item.ItemId.VERACS_HELM
import com.zenyte.game.item.ItemId.VERACS_PLATESKIRT
import com.zenyte.game.item.ItemId.VESTAS_LONGSWORD_BH
import com.zenyte.game.item.ItemId.VESTAS_SPEAR_BH
import com.zenyte.game.world.entity.player.container.impl.ContainerType
import mgi.tools.parser.TypeParser
import mgi.tools.parser.TypeParser.packModel
import mgi.types.config.InventoryDefinitions
import mgi.types.config.enums.EnumDefinitions
import mgi.types.config.items.ItemDefinitions
import mgi.types.config.npcs.NPCDefinitions
import java.io.File


/**
 * Handles the packing of custom Near Reality items into the cache.
 *
 * @author Stan van der Bend
 */
internal object NearRealityCustomItemPacker {

    @JvmStatic
    fun pack() = assetsBase("assets/osnr/custom_items/") {
        "models" {
            val custom = fileNameCustomMap[name] ?: error("no id mapping found for $name")
            if (custom is CustomDefinition.Model)
                packModel(custom.modelId, bytes)
        }
        TypeParser.parse(File(folder("item_config")))
        // special attack descriptions
        EnumDefinitions.get(1739).apply {
            values[CustomItemId.HOLY_GREAT_WARHAMMER] = "Smash: Deal an attack that inflicts 50% more damage and lowers your target's Defence level by 30%."
            values[32161] = "Enhanced Sanctuary: In addition to a better boost in defence (15% + 2 boost), heals the player 10 hp every 2 seconds for 10 seconds for a total of 50 hp."
            values[CustomItemId.ARMADYL_BOW] = "The Judgement: Equivalent to that of the Armadyl god sword."
            values[CustomItemId.BANDOS_BOW] = "Favour Of The War God: Equivalent to that of the Ancient mace."
            values[CustomItemId.ZAMORAK_BOW] = "Ice Cleave: Equivalent to that of the Zamorak god sword."
            values[CustomItemId.SARADOMIN_BOW] = "Saradomin's Lightning: Equivalent to that of the Saradomin sword."
            values[CustomItemId.LIME_WHIP] = "Binding Tentacle: Equivalent to that of the Abyssal tentacle."
            values[CustomItemId.LAVA_WHIP] = "Binding Tentacle: Equivalent to that of the Abyssal tentacle."
            values[CustomItemId.POLYPORE_STAFF] = "Defrost: Lifts any freeze effect."
            values[CustomItemId.POLYPORE_STAFF_DEG] = values[CustomItemId.POLYPORE_STAFF]

            values[BARRELCHEST_ANCHOR_BH] = "Drains the target's combat levels equivalent to 10% of the damage dealt. Drains in the following order: Defence, Attack, Ranged, then Magic.";
            values[DRAGON_MACE_BH] = "Increases damage and accuracy for one hit."
            values[DRAGON_LONGSWORD_BH] = "Deals 25% more damage."
            values[DARK_BOW_BH] = "Launches a double attack."
            values[ABYSSAL_DAGGER_BH] = "Hits the target twice, either hitting or missing both attacks."
            values[ABYSSAL_DAGGER_BHP] = values[ABYSSAL_DAGGER_BH]
            values[ABYSSAL_DAGGER_BHP_27865] = values[ABYSSAL_DAGGER_BH]
            values[ABYSSAL_DAGGER_BHP_27867] = values[ABYSSAL_DAGGER_BH]

            values[BURNING_CLAWS] = "Attacks 3 times in quick succession, with a chance to burn the enemy on each hit."
            values[PURGING_STAFF] = "Requires being on the Arceuus spellbook." +
                    "Uses the best demonbane spell the player can cast; if the demonic creature targeted dies from Scatter ashes, the special attack energy used is refunded, and the player's next attack can be used three ticks earlier."
            values[SCORCHING_BOW] = "Binds demonic creatures for 12 seconds (20 ticks), dealing 1 burn damage in the process." +
                    "It deals an additional 1 burn damage every 4 ticks (2.4 seconds), for a total of 5 burn damage. Cannot be used against non-demonic monsters."
            values[EMBERLIGHT] = "Drains the target's Attack, Strength, and Defence by 5% of their level + 1." +
                    "Three times as effective on demons (reduces each stat by 15%)." +
                    "Special is only activated on successful hits."
            pack()
        }
        // special attack energy cost
        EnumDefinitions.get(906).apply {
            values[CustomItemId.HOLY_GREAT_WARHAMMER] = 500
            values[32161] = 500
            values[CustomItemId.ARMADYL_BOW] = 650
            values[CustomItemId.BANDOS_BOW] = 650
            values[CustomItemId.ZAMORAK_BOW] = 650
            values[CustomItemId.SARADOMIN_BOW] = 650
            values[CustomItemId.LIME_WHIP] = 500
            values[CustomItemId.LAVA_WHIP] = 500
            values[CustomItemId.POLYPORE_STAFF] = 550
            values[CustomItemId.POLYPORE_STAFF_DEG] = 550
            values[11235] = 500
            values[12765] = 500
            values[12766] = 500
            values[12767] = 500
            values[12768] = 500
            values[ABYSSAL_DAGGER] = 250
            values[ABYSSAL_DAGGER_P] = 250
            values[ABYSSAL_DAGGER_P_13269] = 250
            values[ABYSSAL_DAGGER_P_13271] = 250

            values[BARRELCHEST_ANCHOR_BH] = 500
            values[DRAGON_MACE_BH] = 250
            values[DRAGON_LONGSWORD_BH] = 250
            values[DARK_BOW_BH] = 450
            values[ABYSSAL_DAGGER_BH] = 350
            values[ABYSSAL_DAGGER_BHP] = 350
            values[ABYSSAL_DAGGER_BHP_27865] = 350
            values[ABYSSAL_DAGGER_BHP_27867] = 350
            values[STATIUSS_WARHAMMER_BH] = 350
            values[VESTAS_LONGSWORD_BH] = 250
            values[VESTAS_SPEAR_BH] = 500
            values[MORRIGANS_JAVELIN_BH] = 500
            values[MORRIGANS_THROWING_AXE_BH] = 500

            values[DRAGON_DAGGER_CR] = 250
            values[DRAGON_DAGGER_PCR] = 250
            values[DRAGON_DAGGER_PCR_28023] = 250
            values[DRAGON_DAGGER_PCR_28025] = 250
            values[DRAGON_MACE_CR] = 250
            values[DRAGON_SWORD_CR] = 400
            values[DRAGON_SCIMITAR_CR] = 550
            values[DRAGON_LONGSWORD_CR] = 250
            values[DRAGON_WARHAMMER_CR] = 500
            values[DRAGON_BATTLEAXE_CR] = 1000
            values[DRAGON_CLAWS_CR] = 500
            values[DRAGON_SPEAR_CR] = 250
            values[DRAGON_SPEAR_PCR] = 250
            values[DRAGON_SPEAR_PCR_28045] = 250
            values[DRAGON_SPEAR_PCR_28047] = 250
            values[DRAGON_HALBERD_CR] = 300
            values[DRAGON_2H_SWORD_CR] = 600
            values[DRAGON_CROSSBOW_CR] = 600

            values[BURNING_CLAWS] = 300
            values[PURGING_STAFF] = 250
            values[SCORCHING_BOW] = 250
            values[EMBERLIGHT] = 500
            pack()
        }
        ItemDefinitions.get(RING_OF_LEVITATION).apply {
            name = "Ring of levitation"
            lowercaseName = name.lowercase()
            isGrandExchange = true
            pack()
        }
        ItemDefinitions.get(DIVINE_RUNE_POUCH).apply {
            inventoryOptions[4] = "Extra Slot"
            pack()
        }
        NPCDefinitions.get(12166).apply {
            options[1] = "Attack"
            pack()
        }
        val barrowsSets = listOf(TORAGS_ARMOUR_SET, DHAROKS_ARMOUR_SET, KARILS_ARMOUR_SET, AHRIMS_ARMOUR_SET, GUTHANS_ARMOUR_SET, VERACS_ARMOUR_SET)
        for(id in barrowsSets) {
            ItemDefinitions.get(id).apply{
                inventoryOptions[1] = "Unpack"
                pack()
            }
        }
        InventoryDefinitions.get(ContainerType.COLLECTION_LOG.id)?.apply {
            size = 2500
            pack()
        }
        makeTradeable(LARRANS_KEY)
        makeTradeable(26649) // skis
        makeTradeable(SLED_4084, SLED_25282)
        makeTradeable(SKELETON_MASK, SKELETON_BOOTS, SKELETON_GLOVES, SKELETON_SHIRT, SKELETON_LEGGINGS)
        makeTradeable(ATTACKER_ICON, DEFENDER_ICON, COLLECTOR_ICON, HEALER_ICON)
        makeTradeable(DHAROKS_HELM, DHAROKS_PLATEBODY, DHAROKS_PLATELEGS, DHAROKS_GREATAXE)
        makeTradeable(AHRIMS_HOOD, AHRIMS_ROBETOP, AHRIMS_ROBESKIRT, AHRIMS_STAFF)
        makeTradeable(GUTHANS_HELM, GUTHANS_PLATEBODY, GUTHANS_CHAINSKIRT, GUTHANS_WARSPEAR)
        makeTradeable(KARILS_COIF, KARILS_LEATHERTOP, KARILS_LEATHERSKIRT, KARILS_CROSSBOW)
        makeTradeable(VERACS_HELM, VERACS_PLATESKIRT, VERACS_BRASSARD, VERACS_FLAIL)
        makeTradeable(TORAGS_HELM, TORAGS_PLATEBODY, TORAGS_PLATELEGS, TORAGS_HAMMERS)
    }

    private fun makeTradeable(vararg itemIds: Int) = itemIds.forEach { itemId ->
        ItemDefinitions.get(itemId).apply {
            isGrandExchange = true
            pack()
        }
    }

    private val fileNameCustomMap: Map<String, CustomDefinition> = mapOf(
        "ancient_eye" to CustomDefinition.Model(60000),
        "ancient_book_equip" to CustomDefinition.Model(60001),
        "ancient_book_drop" to CustomDefinition.Model(60002),
        "armadyl_soul_crystal" to CustomDefinition.Model(60003),
        "armadyl_bow_equip" to GodBow(60004),
        "armadyl_bow_drop" to CustomDefinition.Model(60005),
        "bandos_soul_crystal" to CustomDefinition.Model(60006),
        "bandos_bow_equip" to GodBow(60007),
        "bandos_bow_drop" to CustomDefinition.Model(60008),
        "saradomin_soul_crystal" to CustomDefinition.Model(60009),
        "saradomin_bow_equip" to GodBow(60010),
        "saradomin_bow_drop" to CustomDefinition.Model(60011),
        "zamorak_soul_crystal" to CustomDefinition.Model(60012),
        "zamorak_bow_equip" to GodBow(60013),
        "zamorak_bow_drop" to CustomDefinition.Model(60014),
        "training_bow_equip" to CustomDefinition.Model(60015),
        "training_bow_drop" to CustomDefinition.Model(60016),
        "training_sword_equip" to CustomDefinition.Model(60017),
        "training_sword_drop" to CustomDefinition.Model(60018),
        "training_staff_equip" to CustomDefinition.Model(60019),
        "training_staff_drop" to CustomDefinition.Model(60020),
        "dragon_kite_equip" to CustomDefinition.Model(60021),
        "dragon_kite_drop" to CustomDefinition.Model(60022),
        "ancient_medallion_equip" to CustomDefinition.Model(60023),
        "ancient_medallion_drop" to CustomDefinition.Model(60024),
        "imbued_ancient_cape_equip" to CustomDefinition.Model(60025),
        "imbued_ancient_cape_drop" to CustomDefinition.Model(60026),
        "imbued_armadyl_cape_equip" to CustomDefinition.Model(60027),
        "imbued_armadyl_cape_drop" to CustomDefinition.Model(60028),
        "imbued_bandos_cape_equip" to CustomDefinition.Model(60029),
        "imbued_bandos_cape_drop" to CustomDefinition.Model(60030),
        "imbued_seren_cape_equip" to CustomDefinition.Model(60032),
        "imbued_seren_cape_drop" to CustomDefinition.Model(60033),
        "skip1" to CustomDefinition.Model(-1),
        "skip2" to CustomDefinition.Model(-1),
        "skip3" to CustomDefinition.Model(-1),
        "skip4" to CustomDefinition.Model(-1),
        "polypore_spores" to CustomDefinition.Model(60039),
        "skip5" to CustomDefinition.Model(-1),
        "polypore_staff_degraded_equip" to CustomDefinition.Model(60040),
        "polypore_staff_degraded_drop" to CustomDefinition.Model(60041),
        "polypore_staff_equip" to CustomDefinition.Model(60042),
        "polypore_staff_drop" to CustomDefinition.Model(modelId = 60043),
        "bronze_key" to CustomDefinition.Model(modelId = 60044),
        "silver_key" to CustomDefinition.Model(modelId = 60045),
        "gold_key" to CustomDefinition.Model(modelId = 60046),
        "platinum_key" to CustomDefinition.Model(60047),
        "diamond_key" to CustomDefinition.Model(60048),
        "n_tablet" to CustomDefinition.Model(60049),
        "nr_tablet" to CustomDefinition.Model(60050),
        "death_cape_equip" to CustomDefinition.Model(60051),
        "death_cape_drop" to CustomDefinition.Model(modelId = 60052),
        "lime_whip_equip" to CustomDefinition.Model(modelId = 60053),
        "lime_whip_drop" to CustomDefinition.Model(modelId = 60054),
        "lime_whip_special_equip" to CustomDefinition.Model(modelId = 60055),
        "lime_whip_special_drop" to CustomDefinition.Model(modelId = 60056),
        "lava_whip_equip" to CustomDefinition.Model(modelId = 60059),
        "lava_whip_drop" to CustomDefinition.Model(modelId = 60060),
        "Pink partyhat" to CustomDefinition.Recolor(recolorMap = mapOf(926 to 57300)),
        "orange_partyhat_equip" to CustomDefinition.Model(modelId = 60057),
        "orange_partyhat_drop" to CustomDefinition.Model(modelId = 60058),
        "donator_pin_10" to CustomDefinition.Model(60061),
        "donator_pin_25" to CustomDefinition.Model(modelId = 60062),
        "donator_pin_50" to CustomDefinition.Model(modelId = 60063),
        "donator_pin_100" to CustomDefinition.Model(modelId = 60064),
        "gauntlet_slayer_helm_equip" to CustomDefinition.Model(60035),
        "gauntlet_slayer_helm_drop" to CustomDefinition.Model(60036),
        "corrupted_gauntlet_slayer_helm_equip" to CustomDefinition.Model(60037),
        "corrupted_gauntlet_slayer_helm_drop" to CustomDefinition.Model(60038),
        "near_reality_party_hat_equip" to CustomDefinition.Model(modelId = 60065),
        "near_reality_party_hat_drop" to CustomDefinition.Model(modelId = 60066),
        "nr_launch_box" to CustomDefinition.Model(modelId = 60067),

        "ankou_blue_boots_drop" to CustomDefinition.Model(modelId = 60068),
        "ankou_blue_boots_female_equip" to CustomDefinition.Model(modelId = 60069),
        "ankou_blue_boots_male_equip" to CustomDefinition.Model(modelId = 60070),
        "ankou_blue_gloves_drop" to CustomDefinition.Model(modelId = 60071),
        "ankou_blue_gloves_female_equip" to CustomDefinition.Model(modelId = 60072),
        "ankou_blue_gloves_male_equip" to CustomDefinition.Model(modelId = 60073),
        "ankou_blue_legs_drop" to CustomDefinition.Model(modelId = 60074),
        "ankou_blue_legs_female_equip" to CustomDefinition.Model(modelId = 60075),
        "ankou_blue_legs_male_equip" to CustomDefinition.Model(modelId = 60076),
        "ankou_blue_mask_drop" to CustomDefinition.Model(modelId = 60077),
        "ankou_blue_mask_female_equip" to CustomDefinition.Model(modelId = 60078),
        "ankou_blue_mask_male_equip" to CustomDefinition.Model(modelId = 60079),
        "ankou_blue_top_drop" to CustomDefinition.Model(modelId = 60080),
        "ankou_blue_top_female_equip" to CustomDefinition.Model(modelId = 60081),
        "ankou_blue_top_male_equip" to CustomDefinition.Model(modelId = 60082),

        "ankou_green_boots_drop" to CustomDefinition.Model(modelId = 60083),
        "ankou_green_boots_female_equip" to CustomDefinition.Model(modelId = 60084),
        "ankou_green_boots_male_equip" to CustomDefinition.Model(modelId = 60085),
        "ankou_green_gloves_drop" to CustomDefinition.Model(modelId = 60086),
        "ankou_green_gloves_female_equip" to CustomDefinition.Model(modelId = 60087),
        "ankou_green_gloves_male_equip" to CustomDefinition.Model(modelId = 60088),
        "ankou_green_legs_drop" to CustomDefinition.Model(modelId = 60089),
        "ankou_green_legs_female_equip" to CustomDefinition.Model(modelId = 60090),
        "ankou_green_legs_male_equip" to CustomDefinition.Model(modelId = 60091),
        "ankou_green_mask_drop" to CustomDefinition.Model(modelId = 60092),
        "ankou_green_mask_female_equip" to CustomDefinition.Model(modelId = 60093),
        "ankou_green_mask_male_equip" to CustomDefinition.Model(modelId = 60094),
        "ankou_green_top_drop" to CustomDefinition.Model(modelId = 60095),
        "ankou_green_top_female_equip" to CustomDefinition.Model(modelId = 60096),
        "ankou_green_top_male_equip" to CustomDefinition.Model(modelId = 60097),

        "ankou_orange_boots_drop" to CustomDefinition.Model(modelId = 60098),
        "ankou_orange_boots_female_equip" to CustomDefinition.Model(modelId = 60099),
        "ankou_orange_boots_male_equip" to CustomDefinition.Model(modelId = 60100),
        "ankou_orange_gloves_drop" to CustomDefinition.Model(modelId = 60101),
        "ankou_orange_gloves_female_equip" to CustomDefinition.Model(modelId = 60102),
        "ankou_orange_gloves_male_equip" to CustomDefinition.Model(modelId = 60103),
        "ankou_orange_legs_drop" to CustomDefinition.Model(modelId = 60104),
        "ankou_orange_legs_female_equip" to CustomDefinition.Model(modelId = 60105),
        "ankou_orange_legs_male_equip" to CustomDefinition.Model(modelId = 60106),
        "ankou_orange_mask_drop" to CustomDefinition.Model(modelId = 60107),
        "ankou_orange_mask_female_equip" to CustomDefinition.Model(modelId = 60108),
        "ankou_orange_mask_male_equip" to CustomDefinition.Model(modelId = 60109),
        "ankou_orange_top_drop" to CustomDefinition.Model(modelId = 60110),
        "ankou_orange_top_female_equip" to CustomDefinition.Model(modelId = 60111),
        "ankou_orange_top_male_equip" to CustomDefinition.Model(modelId = 60112),

        "ankou_white_boots_drop" to CustomDefinition.Model(modelId = 60113),
        "ankou_white_boots_female_equip" to CustomDefinition.Model(modelId = 60114),
        "ankou_white_boots_male_equip" to CustomDefinition.Model(modelId = 60115),
        "ankou_white_gloves_drop" to CustomDefinition.Model(modelId = 60116),
        "ankou_white_gloves_female_equip" to CustomDefinition.Model(modelId = 60117),
        "ankou_white_gloves_male_equip" to CustomDefinition.Model(modelId = 60118),
        "ankou_white_legs_drop" to CustomDefinition.Model(modelId = 60119),
        "ankou_white_legs_female_equip" to CustomDefinition.Model(modelId = 60120),
        "ankou_white_legs_male_equip" to CustomDefinition.Model(modelId = 60121),
        "ankou_white_mask_drop" to CustomDefinition.Model(modelId = 60122),
        "ankou_white_mask_female_equip" to CustomDefinition.Model(modelId = 60123),
        "ankou_white_mask_male_equip" to CustomDefinition.Model(modelId = 60124),
        "ankou_white_top_drop" to CustomDefinition.Model(modelId = 60125),
        "ankou_white_top_female_equip" to CustomDefinition.Model(modelId = 60126),
        "ankou_white_top_male_equip" to CustomDefinition.Model(modelId = 60127),

        "bandos_platebody_drop" to CustomDefinition.Model(modelId = 60128),
        "bandos_platebody_male_equip" to CustomDefinition.Model(modelId = 60129),
        "bandos_platebody_female_equip" to CustomDefinition.Model(modelId = 60130),

        "bandos_platelegs_drop" to CustomDefinition.Model(modelId = 60131),
        "bandos_platelegs_male_equip" to CustomDefinition.Model(modelId = 60132),
        "bandos_platelegs_female_equip" to CustomDefinition.Model(modelId = 60133),

        "bandos_ornament_kit" to CustomDefinition.Model(modelId = 60134),
        "vote_token" to CustomDefinition.Model(modelId = 60135),
        "booster_larrans_key" to CustomDefinition.Model(modelId = 60136),
        "booster_ganodermic" to CustomDefinition.Model(modelId = 60137),
        "booster_slayer" to CustomDefinition.Model(modelId = 60138),
        "booster_pet" to CustomDefinition.Model(modelId = 60139),
        "booster_gauntlet" to CustomDefinition.Model(modelId = 60140),
        "booster_bm" to CustomDefinition.Model(modelId = 60141),
        "booster_clue" to CustomDefinition.Model(modelId = 60142),
        "booster_tob" to CustomDefinition.Model(modelId = 60143),
        "slayer_task_picker" to CustomDefinition.Model(modelId = 60144),
        "slayer_task_reset" to CustomDefinition.Model(modelId = 60145),
        "enhanced_stew" to CustomDefinition.Model(modelId = 60146),
        "inferno_skip_scroll" to CustomDefinition.Model(modelId = 60147),
        "enhanced_excalibur_wear" to CustomDefinition.Model(modelId = 60148),
        "enhanced_excalibur_drop" to CustomDefinition.Model(modelId = 60149),

        "mystery_box_super" to CustomDefinition.Model(modelId = 60150),
        "mystery_box_ultra" to CustomDefinition.Model(modelId = 60151),
        "mystery_box_vote" to CustomDefinition.Model(modelId = 60152),

        "bandos_platebody_male_equip_arms" to CustomDefinition.Model(modelId = 60153),
        "bandos_platebody_female_equip_arms" to CustomDefinition.Model(modelId = 60154),

        "booster_revenant" to CustomDefinition.Model(modelId = 60155),
        "booster_nex" to CustomDefinition.Model(modelId = 60156),

        "mystery_box_cosmetic" to CustomDefinition.Model(modelId = 60157),
        "vesta_helm_drop" to CustomDefinition.Model(modelId = 60158),
        "vesta_helm_equip" to CustomDefinition.Model(modelId = 60159),

        "wilderness_vault_entrance" to CustomDefinition.Model(modelId = 60160),

        "barrows_totem" to CustomDefinition.Model(modelId = 60161),

        "vesta_helm_equipf" to CustomDefinition.Model(modelId = 60162),

        "queen_reaver" to CustomDefinition.Model(modelId = 60164),
        "angel_of_death" to CustomDefinition.Model(modelId = 60165),
        "wilderness_vault_entrance_closed" to CustomDefinition.Model(modelId = 60166),

        "Wildy_Vault_Chestopen_rareb" to CustomDefinition.Model(modelId = 60167),
        "Wildy_Vault_Chestrareb" to CustomDefinition.Model(modelId = 60168),

        "Wildy_Vault_Chestopenb" to CustomDefinition.Model(modelId = 60169),
        "Wildy_Vault_Chestb" to CustomDefinition.Model(modelId = 60170),

        "Soulgobletb" to CustomDefinition.Model(modelId = 60171),
        "Degraded_Essenceb" to CustomDefinition.Model(modelId = 60172),
        "Holy_GreatWarhammerb" to CustomDefinition.Model(modelId = 60173),
        "Holy_GreatWarhammerdropb" to CustomDefinition.Model(modelId = 60174),
        "Holy_GreatWarhammerfemaleb" to CustomDefinition.Model(modelId = 60175),
        "Holy_Greatlance_dropb" to CustomDefinition.Model(modelId = 60176),
        "Holy_Greatlancefemaleb" to CustomDefinition.Model(modelId = 60177),
        "Holy_Greatlance" to CustomDefinition.Model(modelId = 60178),
        "Blood_Tentaclesb" to CustomDefinition.Model(modelId = 60179),
        "Angelic_Artifactb" to CustomDefinition.Model(modelId = 60180),

        "Lil_Ahrimsb" to CustomDefinition.Model(modelId = 60181),
        "Lil_Dharokb" to CustomDefinition.Model(modelId = 60182),
        "Lil_Guthanb" to CustomDefinition.Model(modelId = 60183),
        "Lil_Karilb" to CustomDefinition.Model(modelId = 60184),
        "Lil_Toragb" to CustomDefinition.Model(modelId = 60185),
        "Lil_Veracb" to CustomDefinition.Model(modelId = 60186),
        "Malevolent_Energyb" to CustomDefinition.Model(modelId = 60187),

        "Malovelent_Kiteshield(drop)b" to CustomDefinition.Model(modelId = 60188),
        "Malovelent_Kiteshieldb" to CustomDefinition.Model(modelId = 60189),
        "Malovelent_Kiteshield(female)b" to CustomDefinition.Model(modelId = 60190),

        "Mercicless_Kiteshield(drop)b" to CustomDefinition.Model(modelId = 60191),
        "Mercicless_Kiteshieldb" to CustomDefinition.Model(modelId = 60192),
        "Mercicless_Kiteshield(female)b" to CustomDefinition.Model(modelId = 60193),

        "Vengeful_Kiteshield(drop)b" to CustomDefinition.Model(modelId = 60194),
        "Vengeful_Kiteshieldb" to CustomDefinition.Model(modelId = 60195),
        "Vengeful_Kiteshield(female)b" to CustomDefinition.Model(modelId = 60196),

        "Wood_Score_Boardb" to CustomDefinition.Model(modelId = 60197),
        "goodwillb" to CustomDefinition.Model(modelId = 60198),
        "star_emblem" to CustomDefinition.Model(modelId = 60199),

        "gold_mbox" to CustomDefinition.Model(modelId = 60203),
        "pk_box" to CustomDefinition.Model(modelId = 60204),
        "divine_pool" to CustomDefinition.Model(modelId = 60205),
        "overload_pool" to CustomDefinition.Model(modelId = 60206),

        "Droprate_Boost_Scrollb" to CustomDefinition.Model(modelId = 60207),
        "BlackwhiteBoxb" to CustomDefinition.Model(modelId = 60208),
        "SecondaryCasketb" to CustomDefinition.Model(modelId = 60209),

        "DragonHide_Pouchb" to CustomDefinition.Model(modelId = 60210),
        "Dragon_Hunter_Gloves_Femaleb" to CustomDefinition.Model(modelId = 60211),
        "Dragon_Hunter_Gloves_dropb" to CustomDefinition.Model(modelId = 60212),
        "Dragon_Hunter_Gloves_Maleb" to CustomDefinition.Model(modelId = 60213),
        "DragonHide_Pouch_openb" to CustomDefinition.Model(modelId = 60214),
        "Bone_Pouchb" to CustomDefinition.Model(modelId = 60215),
        "Bone_Pouch_openb" to CustomDefinition.Model(modelId = 60216),

        "Anubis body dropb" to CustomDefinition.Model(modelId = 60217),
        "Anubis gloves dropb" to CustomDefinition.Model(modelId = 60218),
        "Anubis Khopesh dropb" to CustomDefinition.Model(modelId = 60219),
        "Anubis Legs dropb" to CustomDefinition.Model(modelId = 60220),
        "Anubis mask dropb" to CustomDefinition.Model(modelId = 60221),
        "Anubis round shield dropb" to CustomDefinition.Model(modelId = 60222),
        "Anubis Sq Shield dropb" to CustomDefinition.Model(modelId = 60223),

        "Anubis bodyb" to CustomDefinition.Model(modelId = 60224),
        "Anubis glovesb" to CustomDefinition.Model(modelId = 60225),
        "Anubis Khopeshb" to CustomDefinition.Model(modelId = 60226),
        "Anubis Legsb" to CustomDefinition.Model(modelId = 60227),
        "Anubis maskb" to CustomDefinition.Model(modelId = 60228),
        "Anubis round shieldb" to CustomDefinition.Model(modelId = 60229),
        "Anubis Sq Shieldb" to CustomDefinition.Model(modelId = 60230),

        "Anubis body femb" to CustomDefinition.Model(modelId = 60231),
        "Anubis gloves femb" to CustomDefinition.Model(modelId = 60232),
        "Anubis khopesh femb" to CustomDefinition.Model(modelId = 60233),
        "Anubis Legs femb" to CustomDefinition.Model(modelId = 60234),
        "Anubis mask femb" to CustomDefinition.Model(modelId = 60235),
        "Anubis round shield femb" to CustomDefinition.Model(modelId = 60236),
        "Anubis Sq Shield femb" to CustomDefinition.Model(modelId = 60237),

        "Anubis sandalsb" to CustomDefinition.Model(modelId = 60238),

        "Ice_phat_femb" to CustomDefinition.Model(modelId = 60239),
        "Ice_phatb" to CustomDefinition.Model(modelId = 60240),
        "Ice_phatdropb" to CustomDefinition.Model(modelId = 60241),
        "Ice_santa_dropb" to CustomDefinition.Model(modelId = 60242),
        "Ice_santa_femb" to CustomDefinition.Model(modelId = 60243),
        "Ice_santab" to CustomDefinition.Model(modelId = 60244),
        "Ice_hween_femb" to CustomDefinition.Model(modelId = 60245),
        "Ice_hweendropb" to CustomDefinition.Model(modelId = 60246),
        "Ice_hweenb" to CustomDefinition.Model(modelId = 60247),

        "Abyssal_Statueb" to CustomDefinition.Model(modelId = 60248),
        "Hydra_Statueb" to CustomDefinition.Model(modelId = 60249),
        "Jad_Statueb" to CustomDefinition.Model(modelId = 60250),
        "Kalaphite_Statueb" to CustomDefinition.Model(modelId = 60251),
        "KBD_Statueb" to CustomDefinition.Model(modelId = 60252),
        "Olm_Statueb" to CustomDefinition.Model(modelId = 60253),
        "RegularSlayer_Statueb" to CustomDefinition.Model(modelId = 60254),
        "Skotizo_Statueb" to CustomDefinition.Model(modelId = 60255),
        "VorkathStatueb" to CustomDefinition.Model(modelId = 60256),
        "SlayerStatueEmptyb" to CustomDefinition.Model(modelId = 60257),
        "Verzik_Statueb" to CustomDefinition.Model(modelId = 60258),
        "Zuk_Statueb" to CustomDefinition.Model(modelId = 60259),

        "Slayer's Greate Axeb" to CustomDefinition.Model(modelId = 60260),
        "Slayer's Hauberkb" to CustomDefinition.Model(modelId = 60261),
        "Slayer's Plateskirtb" to CustomDefinition.Model(modelId = 60262),
        "Slayer's Platformb" to CustomDefinition.Model(modelId = 60263),

        "Regal_Mystery_Boxb" to CustomDefinition.Model(modelId = 60264),
        "light_pink_hweenbdropb" to CustomDefinition.Model(modelId = 60265),
        "pinkhween" to CustomDefinition.Model(modelId = 60266),
        "pink_santabdropb" to CustomDefinition.Model(modelId = 60267),
        "pink_santab" to CustomDefinition.Model(modelId = 60268),
        "SleepCapDropb" to CustomDefinition.Model(modelId = 60269),
        "MaleCapV2b" to CustomDefinition.Model(modelId = 60270),
        "FemaleCapV2b" to CustomDefinition.Model(modelId = 60271),

        "OS Completionist cape (DROP & INV)b" to CustomDefinition.Model(modelId = 60272),
        "OS Completionist cape (MALE)b" to CustomDefinition.Model(modelId = 60273),
        "OS Completionist cape (FEMALE)b" to CustomDefinition.Model(modelId = 60274),
        "OS Completionist hood (DROP & INV)b" to CustomDefinition.Model(modelId = 60275),
        "OS Completionist hood (MALE)b" to CustomDefinition.Model(modelId = 60276),
        "OS Completionist hood (FEMALE)b" to CustomDefinition.Model(modelId = 60277),
        "OS Completionist hood chathead (MALE)b" to CustomDefinition.Model(modelId = 60278),
        "OS Completionist hood chathead (FEMALE)b" to CustomDefinition.Model(modelId = 60279),
        "Max exp cape (DROP & INV)" to CustomDefinition.Model(modelId = 60280),
        "Max exp cape (MALE)" to CustomDefinition.Model(modelId = 60281),
        "Max exp cape (FEMALE)" to CustomDefinition.Model(modelId = 60282),
        "Carrot spear dropb" to CustomDefinition.Model(modelId = 60283),
        "Carrot_spearb" to CustomDefinition.Model(modelId = 60284),
        "Carrot_spear_femb" to CustomDefinition.Model(modelId = 60285),
        "Carrot crown dropb" to CustomDefinition.Model(modelId = 60286),
        "Carrot crownb" to CustomDefinition.Model(modelId = 60287),
        "Carrot crown femb" to CustomDefinition.Model(modelId = 60288),
        "Broken egg shellsb" to CustomDefinition.Model(modelId = 60289),
        "Easter M boxb" to CustomDefinition.Model(modelId = 60290),
        "Easter cardsb" to CustomDefinition.Model(modelId = 60291),
        "BlueTBowDropb" to CustomDefinition.Model(modelId = 60292),
        "BlueTBowFemaleb" to CustomDefinition.Model(modelId = 60293),
        "BlueTBowKitb" to CustomDefinition.Model(modelId = 60294),
        "BlueTBowMaleb" to CustomDefinition.Model(modelId = 60295),
        "DivineKitb" to CustomDefinition.Model(modelId = 60296),
        "DivineSSDropb" to CustomDefinition.Model(modelId = 60297),
        "DivineSSFemaleb" to CustomDefinition.Model(modelId = 60298),
        "DivineSSMaleb" to CustomDefinition.Model(modelId = 60299),
        "DvineSigilb" to CustomDefinition.Model(modelId = 60300),
        "PurpleTBowDropb" to CustomDefinition.Model(modelId = 60301),
        "PurpleTBowFemaleb" to CustomDefinition.Model(modelId = 60302),
        "PurpleTBowKitb" to CustomDefinition.Model(modelId = 60303),
        "PurpleTBowMaleb" to CustomDefinition.Model(modelId = 60304),
        "RedTBowDropb" to CustomDefinition.Model(modelId = 60305),
        "RedTBowFemaleb" to CustomDefinition.Model(modelId = 60306),
        "RedTBowKitb" to CustomDefinition.Model(modelId = 60307),
        "RedTBowMaleb" to CustomDefinition.Model(modelId = 60308),
        "WhiteTBowDropb" to CustomDefinition.Model(modelId = 60309),
        "WhiteTBowFemaleb" to CustomDefinition.Model(modelId = 60310),
        "WhiteTbowKitb" to CustomDefinition.Model(modelId = 60311),
        "WhiteTBowMaleb" to CustomDefinition.Model(modelId = 60312),
    )

    @JvmStatic
    fun main(args: Array<String>) {
        val customItemIds = mutableListOf<String>()
        val placeHolders = mutableListOf<String>()

        var id = 32142
        val parts = listOf("platebody", "platelegs")
        val inheritMap = mapOf(
            "platebody" to BANDOS_CHESTPLATE,
            "platelegs" to BANDOS_TASSETS,
        )
        val renameMap = mapOf(
            "platebody" to "Bandos chestplate (or)",
            "platelegs" to "Bandos tassets (or)"
        )
        for (part in parts) {
            val name = renameMap[part]!!
            val drop = getModelId("bandos_${part}_drop")
            val maleEquip = getModelId("bandos_${part}_male_equip")
            val femaleEquip = getModelId("bandos_${part}_female_equip")
            println(buildString {
                appendLine("#${ContentConstants.SERVER_NAME} $name")
                appendLine("[[item]]")
                appendLine("inherit=${inheritMap[part]!!}")
                appendLine("id=$id")
                appendLine("name=\"$name\"")
                appendLine("invmodel=$drop")
                appendLine("primarymalemodel=$maleEquip")
                appendLine("primaryfemalemodel=$femaleEquip")
                appendLine("placeholderid=${id + 1000}")
                appendLine("stackable=0")
                appendLine("notedid=${id + 1}")
            })
            customItemIds += buildString {
                appendLine(
                    "\tpublic static final int ${
                        name.replace("'", "").uppercase().replace(" ", "_")
                    } = $id;"
                )
            }
            placeHolders += buildString {
                appendLine("#${ContentConstants.SERVER_NAME} $name (placeholder)")
                appendLine("[[item]]")
                appendLine("inherit=${inheritMap[part]!!}")
                appendLine("id=${id + 1000}")
                appendLine("name=\"$name\"")
                appendLine("invmodel=$drop")
                appendLine("placeholderid=${id}")
                appendLine("placeholdertemplate=14401")
            }
            id++
            println(buildString {
                appendLine("#${ContentConstants.SERVER_NAME} $name (noted)")
                appendLine("[[item]]")
                appendLine("inherit=${inheritMap[part]!! + 1}")
                appendLine("id=$id")
                appendLine("name=\"$name\"")
                appendLine("invmodel=$drop")
                appendLine("notedid=${id - 1}")
            })
            id++
        }
        for (placeHolder in placeHolders)
            println(placeHolder)
        for (customItemId in customItemIds)
            println(customItemId)
    }

    private fun getModelId(name: String) = (fileNameCustomMap[name] as CustomDefinition.Model).modelId

    sealed class CustomDefinition {

        open class Model(val modelId: Int) : CustomDefinition()

        class Recolor(val recolorMap: Map<Int, Int>) : CustomDefinition()
    }

    private class GodBow(modelId: Int) : CustomDefinition.Model(modelId)
}
