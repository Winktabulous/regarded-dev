package com.near_reality.osrsbox_db

import com.fasterxml.jackson.annotation.JsonProperty
import com.zenyte.game.parser.impl.ItemRequirements
import com.zenyte.game.world.entity.player.SkillConstants
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot
import mgi.types.config.items.ItemDefinitions
import org.apache.commons.lang3.ArrayUtils

/**
 * @author Jire
 */
data class ItemDefinition(
    val id: Int,
    val name: String,
    val incomplete: Boolean,
    val members: Boolean,
    val tradeable: Boolean = false,
    @JsonProperty("tradeable_on_ge") val tradeableOnGE: Boolean,
    val stackable: Boolean,
    val stacked: Int = -1,
    val noted: Boolean,
    val noteable: Boolean,
    @JsonProperty("linked_id_item") val linkedIDItem: Int? = -1,
    @JsonProperty("linked_id_noted") val linkedIDNoted: Int? = -1,
    @JsonProperty("linked_id_placeholder") val linkedIDPlaceholder: Int? = -1,
    val placeholder: Boolean,
    val equipable: Boolean,
    @JsonProperty("equipable_by_player") val equipableByPlayer: Boolean,
    @JsonProperty("equipable_weapon") val equipableWeapon: Boolean,
    val cost: Int,
    val lowalch: Int,
    val highalch: Int,
    val weight: Float? = 0.0F,
    @JsonProperty("buy_limit") val buyLimit: Int? = -1,
    @JsonProperty("quest_item") val questItem: Boolean,
    @JsonProperty("release_date") val releaseDate: String? = null,
    val duplicate: Boolean,
    val examine: String? = null,
    val icon: String? = null,
    @JsonProperty("wiki_name") val wikiName: String? = null,
    @JsonProperty("wiki_url") val wikiURL: String? = null,
    @JsonProperty("wiki_exchange") val wikiExchange: String? = null,
    val equipment: ItemEquipment? = null,
    val weapon: ItemWeapon? = null,
    @JsonProperty("last_updated") val lastUpdated: String? = null,
) {

    fun apply(id: Int) {
        val realID = id

        val def = ItemDefinitions.get(realID)!!
        
        if (examine != null) def.examine = examine
        if (weight != null) def.weight = weight.toFloat()
        if (equipment != null) {
            val slotString = equipment.slot.lowercase()

            val equipmentSlot = when (slotString) {
                "weapon", "2h" -> EquipmentSlot.WEAPON
                "shield", "shields", "block", "blocks" -> EquipmentSlot.SHIELD
                "neck", "amulet", "amulets" -> EquipmentSlot.AMULET
                "feet", "boot", "boots" -> EquipmentSlot.BOOTS
                "cape", "capes", "back" -> EquipmentSlot.CAPE
                "ammo", "arrow", "arrows", "quiver" -> EquipmentSlot.AMMUNITION
                "legs", "leg" -> EquipmentSlot.LEGS
                "hands", "hand" -> EquipmentSlot.HANDS
                "head", "helm", "helmet" -> EquipmentSlot.HELMET
                "ring", "rings", "finger", "fingers" -> EquipmentSlot.RING
                "body", "plate", "platebody", "chest" -> EquipmentSlot.PLATE
                else -> null
            }
            if (equipmentSlot != null) def.slot = equipmentSlot.slot

            if ("2h".equals(slotString, true) || ("two-handed_swords" == weapon?.weaponType))
                def.isTwoHanded = true

            def.bonuses = equipment.run {
                if(def.bonuses != null) {
                    intArrayOf(
                        if (attackStab > def.bonuses[0]) attackStab else def.bonuses[0],
                        if (attackSlash > def.bonuses[1]) attackSlash else def.bonuses[1],
                        if (attackCrush > def.bonuses[2]) attackCrush else def.bonuses[2],
                        if (attackMagic > def.bonuses[3]) attackCrush else def.bonuses[3],
                        if (attackRanged > def.bonuses[4]) attackRanged else def.bonuses[4],
                        if (defenceStab > def.bonuses[5]) defenceStab else def.bonuses[5],
                        if (defenceSlash > def.bonuses[6]) defenceSlash else def.bonuses[6],
                        if (defenceCrush > def.bonuses[7]) defenceCrush else def.bonuses[7],
                        if (defenceMagic > def.bonuses[8]) defenceMagic else def.bonuses[8],
                        if (defenceRanged > def.bonuses[9]) defenceRanged else def.bonuses[9],
                        if (meleeStrength > def.bonuses[10]) meleeStrength else def.bonuses[10],
                        if (rangedStrength > def.bonuses[11]) rangedStrength else def.bonuses[11],
                        if (magicDamage > def.bonuses[12]) magicDamage else def.bonuses[12],
                        if (prayer > def.bonuses[13]) prayer else def.bonuses[13]
                    )
                } else {
                    intArrayOf(
                        attackStab,
                        attackSlash,
                        attackCrush,
                        attackMagic,
                        attackRanged,
                        defenceStab,
                        defenceSlash,
                        defenceCrush,
                        defenceMagic,
                        defenceRanged,
                        meleeStrength,
                        rangedStrength,
                        magicDamage,
                        prayer
                    )
                }
            }

            if (weapon != null) def.attackSpeed = 10 - weapon.attackSpeed

            for ((name, level) in equipment.requirements ?: return) {
                val skillID = ArrayUtils.indexOf(SkillConstants.SKILLS, name)
                if (skillID >= 0)
                    ItemRequirements.add(realID, skillID, level.toInt())
            }
        }
    }

}