package com.near_reality.osrsbox_db

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.zenyte.game.parser.impl.NPCExamineLoader
import com.zenyte.game.util.Examine
import com.zenyte.game.world.entity.npc.combatdefs.*
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessorLoader
import com.zenyte.game.world.entity.npc.drop.matrix.NPCDrops
import com.zenyte.plugins.PluginManager
import com.zenyte.plugins.events.PluginsLoadedEvent
import mgi.types.config.AnimationDefinitions
import mgi.types.config.npcs.NPCDefinitions
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

/**
 * @author Jire
 */
data class MonsterDefinition
@JsonCreator constructor(
    val id: Int,
    val name: String,
    @JsonProperty("last_updated") val lastUpdated: String? = null,
    val incomplete: Boolean,
    val members: Boolean,
    @JsonProperty("release_date") val releaseDate: String? = null,
    @JsonProperty("combat_level") val combatLevel: Int,
    val size: Int,
    val hitpoints: Int,
    @JsonProperty("max_hit") val maxHit: Int = 0,
    @JsonProperty("attack_type") val attackType: Array<String>? = null,
    @JsonProperty("attack_speed") val attackSpeed: Int = 4,
    val aggressive: Boolean,
    val poisonous: Boolean,
    val venomous: Boolean,
    @JsonProperty("immune_poison") val immuneToPoison: Boolean,
    @JsonProperty("immune_venom") val immuneToVenom: Boolean,
    val attributes: Array<String>? = null,
    val weakness: Array<String>? = null,
    val category: Array<String>? = null,
    @JsonProperty("slayer_monster") val slayerMonster: Boolean,
    @JsonProperty("slayer_level") val slayerLevel: Int? = null,
    @JsonProperty("slayer_xp") val slayerXP: Int? = null,
    @JsonProperty("slayer_masters") val slayerMasters: Array<String>? = null,
    val duplicate: Boolean,
    val examine: String,
    val icon: String? = null,
    @JsonProperty("wiki_name") val wikiName: String,
    @JsonProperty("wiki_url") val wikiURL: String,
    @JsonProperty("attack_level") val attackLevel: Int,
    @JsonProperty("strength_level") val strengthLevel: Int,
    @JsonProperty("defence_level") val defenceLevel: Int,
    @JsonProperty("magic_level") val magicLevel: Int,
    @JsonProperty("ranged_level") val rangedLevel: Int,
    @JsonProperty("attack_magic") val attackMagic: Int,
    @JsonProperty("attack_ranged") val attackRanged: Int,
    @JsonProperty("defence_stab") val defenceStab: Int,
    @JsonProperty("defence_slash") val defenceSlash: Int,
    @JsonProperty("defence_crush") val defenceCrush: Int,
    @JsonProperty("defence_magic") val defenceMagic: Int,
    @JsonProperty("defence_ranged") val defenceRanged: Int,
    @JsonProperty("attack_accuracy") val attackAccuracy: Int,
    @JsonProperty("melee_strength") val meleeStrength: Int,
    @JsonProperty("ranged_strength") val rangedStrength: Int,
    @JsonProperty("magic_damage") val magicDamage: Int,

    @JsonProperty("attack_bonus") val attackBonus: Int,
    @JsonProperty("strength_bonus") val strengthBonus: Int,
    @JsonProperty("magic_bonus") val magicBonus: Int,
    @JsonProperty("ranged_bonus") val rangedBonus: Int,

    val drops: MutableList<MonsterDrop>? = null,
    @JsonProperty("rare_drop_table") val rareDropTable: Boolean
) {

    val logger: Logger = LoggerFactory.getLogger(MonsterDefinition::class.java)

    fun apply(id: Int) {
        val realID = id
        val nDef = NPCDefinitions.get(realID)!!
        NPCExamineLoader.insert(realID, Examine(realID, examine))

        val existingDef = NPCCDLoader.get(realID)
        val merge = existingDef != null
        val def = existingDef ?: NPCCombatDefinitions()
        if (!merge) def.id = realID
        if (hitpoints > 0)
            def.hitpoints = hitpoints
        if (attackSpeed > 0)
            def.attackSpeed = attackSpeed

        def.immunityTypes = EnumSet.noneOf(ImmunityType::class.java).apply {
            if (immuneToPoison) add(ImmunityType.POISON)
            if (immuneToVenom) add(ImmunityType.VENOM)
        }

        if (weakness != null) {
            def.weaknesses = EnumSet.noneOf(WeaknessType::class.java).apply {
                for (name in weakness) {
                    val weaknessType = WeaknessType.valueOf(name.lowercase())
                    add(weaknessType)
                }
            }
        }

        if (attributes != null && attributes.contains("undead")) {
            def.monsterType = MonsterType.UNDEAD
        }

        if (slayerLevel != null) def.slayerLevel = slayerLevel

        val statDefinitions = def.statDefinitions ?: StatDefinitions()
        def.statDefinitions = statDefinitions.apply {
            set(StatType.ATTACK, attackLevel)
            set(StatType.STRENGTH, strengthLevel)
            set(StatType.DEFENCE, defenceLevel)
            set(StatType.MAGIC, magicLevel)
            set(StatType.RANGED, rangedLevel)

            set(StatType.ATTACK_STAB, attackBonus)
            set(StatType.ATTACK_SLASH, attackBonus)
            set(StatType.ATTACK_CRUSH, attackBonus)
            set(StatType.ATTACK_MAGIC, attackMagic)
            set(StatType.ATTACK_RANGED, attackRanged)

            set(StatType.DEFENCE_STAB, defenceStab)
            set(StatType.DEFENCE_SLASH, defenceSlash)
            set(StatType.DEFENCE_CRUSH, defenceCrush)
            set(StatType.DEFENCE_MAGIC, defenceMagic)
            set(StatType.DEFENCE_RANGED, defenceRanged)

            set(StatType.MELEE_STRENGTH_BONUS, meleeStrength)
            set(StatType.RANGED_STRENGTH_BONUS, rangedStrength)
            set(StatType.MAGIC_STRENGTH_BONUS, magicDamage)
        }

        val skeleton = AnimationDefinitions.getSkeletonId(nDef.walkAnimation)
        if (def.attackDefinitions == null) {
            val skeletonDef = NPCCDLoader.skeletonToAttackDefs[skeleton]
            if (skeletonDef != null) {
                def.attackDefinitions = AttackDefinitions.construct(skeletonDef).apply {
                    this.maxHit = this@MonsterDefinition.maxHit
                }
            }
        }
        if (def.blockDefinitions == null) {
            val skeletonDef = NPCCDLoader.skeletonToBlockDefs[skeleton]
            if (skeletonDef != null) {
                def.blockDefinitions = BlockDefinitions.construct(skeletonDef)
            }
        }
        if (def.spawnDefinitions == null) {
            val skeletonDef = NPCCDLoader.skeletonToSpawnDefs[skeleton]
            if (skeletonDef != null) {
                def.spawnDefinitions = SpawnDefinitions.construct(skeletonDef)
            }
        }

        if (!drops.isNullOrEmpty()) {
            PluginManager.register(PluginsLoadedEvent::class.java) {

                if (NPCDrops.getTable(def.id) != null)
                    return@register

                val dropProcessor = DropProcessorLoader.get(def.id)
                if (dropProcessor != null && dropProcessor.any { it.disregardTableFromDefinitions() })
                    return@register

                buildFromTable(drops)
            }
        }

        if (!merge)
            NPCCDLoader.insert(realID, def)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MonsterDefinition

        if (id != other.id) return false
        if (name != other.name) return false
        if (incomplete != other.incomplete) return false
        if (members != other.members) return false
        if (releaseDate != other.releaseDate) return false
        if (combatLevel != other.combatLevel) return false
        if (size != other.size) return false
        if (hitpoints != other.hitpoints) return false
        if (maxHit != other.maxHit) return false
        if (attackType != null) {
            if (other.attackType == null) return false
            if (!attackType.contentEquals(other.attackType)) return false
        } else if (other.attackType != null) return false
        if (attackSpeed != other.attackSpeed) return false
        if (aggressive != other.aggressive) return false
        if (poisonous != other.poisonous) return false
        if (immuneToPoison != other.immuneToPoison) return false
        if (immuneToVenom != other.immuneToVenom) return false
        if (weakness != null) {
            if (other.weakness == null) return false
            if (!weakness.contentEquals(other.weakness)) return false
        } else if (other.weakness != null) return false
        if (attributes != null) {
            if (other.attributes == null) return false
            if (!attributes.contentEquals(other.attributes)) return false
        } else if (other.attributes != null) return false
        if (category != null) {
            if (other.category == null) return false
            if (!category.contentEquals(other.category)) return false
        } else if (other.category != null) return false
        if (slayerMonster != other.slayerMonster) return false
        if (slayerLevel != other.slayerLevel) return false
        if (slayerXP != other.slayerXP) return false
        if (slayerMasters != null) {
            if (other.slayerMasters == null) return false
            if (!slayerMasters.contentEquals(other.slayerMasters)) return false
        } else if (other.slayerMasters != null) return false
        if (duplicate != other.duplicate) return false
        if (examine != other.examine) return false
        if (icon != other.icon) return false
        if (wikiName != other.wikiName) return false
        if (wikiURL != other.wikiURL) return false
        if (attackLevel != other.attackLevel) return false
        if (strengthLevel != other.strengthLevel) return false
        if (defenceLevel != other.defenceLevel) return false
        if (magicLevel != other.magicLevel) return false
        if (rangedLevel != other.rangedLevel) return false
        if (attackMagic != other.attackMagic) return false
        if (attackRanged != other.attackRanged) return false
        if (defenceStab != other.defenceStab) return false
        if (defenceSlash != other.defenceSlash) return false
        if (defenceCrush != other.defenceCrush) return false
        if (defenceMagic != other.defenceMagic) return false
        if (defenceRanged != other.defenceRanged) return false
        if (attackAccuracy != other.attackAccuracy) return false
        if (meleeStrength != other.meleeStrength) return false
        if (rangedStrength != other.rangedStrength) return false
        if (magicDamage != other.magicDamage) return false
        if (drops != other.drops) return false
        if (rareDropTable != other.rareDropTable) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + incomplete.hashCode()
        result = 31 * result + members.hashCode()
        result = 31 * result + (releaseDate?.hashCode() ?: 0)
        result = 31 * result + combatLevel
        result = 31 * result + size
        result = 31 * result + hitpoints
        result = 31 * result + maxHit
        result = 31 * result + (attackType?.contentHashCode() ?: 0)
        result = 31 * result + attackSpeed
        result = 31 * result + aggressive.hashCode()
        result = 31 * result + poisonous.hashCode()
        result = 31 * result + immuneToPoison.hashCode()
        result = 31 * result + immuneToVenom.hashCode()
        result = 31 * result + (weakness?.contentHashCode() ?: 0)
        result = 31 * result + (attributes?.contentHashCode() ?: 0)
        result = 31 * result + (category?.contentHashCode() ?: 0)
        result = 31 * result + slayerMonster.hashCode()
        result = 31 * result + (slayerLevel ?: 0)
        result = 31 * result + (slayerXP ?: 0)
        result = 31 * result + (slayerMasters?.contentHashCode() ?: 0)
        result = 31 * result + duplicate.hashCode()
        result = 31 * result + examine.hashCode()
        result = 31 * result + (icon?.hashCode() ?: 0)
        result = 31 * result + wikiName.hashCode()
        result = 31 * result + wikiURL.hashCode()
        result = 31 * result + attackLevel
        result = 31 * result + strengthLevel
        result = 31 * result + defenceLevel
        result = 31 * result + magicLevel
        result = 31 * result + rangedLevel
        result = 31 * result + attackMagic
        result = 31 * result + attackRanged
        result = 31 * result + defenceStab
        result = 31 * result + defenceSlash
        result = 31 * result + defenceCrush
        result = 31 * result + defenceMagic
        result = 31 * result + defenceRanged
        result = 31 * result + attackAccuracy
        result = 31 * result + meleeStrength
        result = 31 * result + rangedStrength
        result = 31 * result + magicDamage
        result = 31 * result + (drops?.hashCode() ?: 0)
        result = 31 * result + rareDropTable.hashCode()
        return result
    }

}
