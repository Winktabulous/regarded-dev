package com.near_reality.api.dao

import com.near_reality.api.model.Skill
import com.near_reality.api.util.toLevel
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object UserSkillStats : IntIdTable("user_skill_stats") {
    val user = reference("account", Users).uniqueIndex()
    val totalExperience = integer("total_experience").default(Skill.defaultTotalExperience)
    val totalLevel = integer("total_level").default(Skill.defaultTotalLevel)
    val attackXp = integer("attack_xp").default(Skill.ATTACK.defaultExperience)
    val defenceXp = integer("defence_xp").default(Skill.DEFENCE.defaultExperience)
    val strengthXp = integer("strength_xp").default(Skill.STRENGTH.defaultExperience)
    val hitpointsXp = integer("hitpoints_xp").default(Skill.HITPOINTS.defaultExperience)
    val rangedXp = integer("ranged_xp").default(Skill.RANGED.defaultExperience)
    val prayerXp = integer("prayer_xp").default(Skill.PRAYER.defaultExperience)
    val magicXp = integer("magic_xp").default(Skill.MAGIC.defaultExperience)
    val cookingXp = integer("cooking_xp").default(Skill.COOKING.defaultExperience)
    val woodcuttingXp = integer("woodcutting_xp").default(Skill.WOODCUTTING.defaultExperience)
    val fletchingXp = integer("fletching_xp").default(Skill.FLETCHING.defaultExperience)
    val fishingXp = integer("fishing_xp").default(Skill.FISHING.defaultExperience)
    val firemakingXp = integer("firemaking_xp").default(Skill.FIREMAKING.defaultExperience)
    val craftingXp = integer("crafting_xp").default(Skill.CRAFTING.defaultExperience)
    val smithingXp = integer("smithing_xp").default(Skill.SMITHING.defaultExperience)
    val miningXp = integer("mining_xp").default(Skill.MINING.defaultExperience)
    val herbloreXp = integer("herblore_xp").default(Skill.HERBLORE.defaultExperience)
    val agilityXp = integer("agility_xp").default(Skill.AGILITY.defaultExperience)
    val thievingXp = integer("thieving_xp").default(Skill.THIEVING.defaultExperience)
    val slayerXp = integer("slayer_xp").default(Skill.SLAYER.defaultExperience)
    val farmingXp = integer("farming_xp").default(Skill.FARMING.defaultExperience)
    val runecraftingXp = integer("runecrafting_xp").default(Skill.RUNECRAFTING.defaultExperience)
    val hunterXp = integer("hunter_xp").default(Skill.HUNTER.defaultExperience)
    val constructionXp = integer("construction_xp").default(Skill.CONSTRUCTION.defaultExperience)
}

class UserSkillStatEntity(id: EntityID<Int>) : IntEntity(id) {

    companion object : IntEntityClass<UserSkillStatEntity>(UserSkillStats)

    var user by UserEntity referencedOn UserSkillStats.user
    var totalExperience by UserSkillStats.totalExperience
    var totalLevel by UserSkillStats.totalLevel
    var attackXp by UserSkillStats.attackXp
    var defenceXp by UserSkillStats.defenceXp
    var strengthXp by UserSkillStats.strengthXp
    var hitpointsXp by UserSkillStats.hitpointsXp
    var rangedXp by UserSkillStats.rangedXp
    var prayerXp by UserSkillStats.prayerXp
    var magicXp by UserSkillStats.magicXp
    var cookingXp by UserSkillStats.cookingXp
    var woodcuttingXp by UserSkillStats.woodcuttingXp
    var fletchingXp by UserSkillStats.fletchingXp
    var fishingXp by UserSkillStats.fishingXp
    var firemakingXp by UserSkillStats.firemakingXp
    var craftingXp by UserSkillStats.craftingXp
    var smithingXp by UserSkillStats.smithingXp
    var miningXp by UserSkillStats.miningXp
    var herbloreXp by UserSkillStats.herbloreXp
    var agilityXp by UserSkillStats.agilityXp
    var thievingXp by UserSkillStats.thievingXp
    var slayerXp by UserSkillStats.slayerXp
    var farmingXp by UserSkillStats.farmingXp
    var runecraftingXp by UserSkillStats.runecraftingXp
    var hunterXp by UserSkillStats.hunterXp
    var constructionXp by UserSkillStats.constructionXp

    fun getExperience(skill: Skill): Int = when(skill){
        Skill.ATTACK -> attackXp
        Skill.DEFENCE -> defenceXp
        Skill.STRENGTH -> strengthXp
        Skill.HITPOINTS -> hitpointsXp
        Skill.RANGED -> rangedXp
        Skill.PRAYER -> prayerXp
        Skill.MAGIC -> magicXp
        Skill.COOKING -> cookingXp
        Skill.WOODCUTTING -> woodcuttingXp
        Skill.FLETCHING -> fletchingXp
        Skill.FISHING -> fishingXp
        Skill.FIREMAKING -> firemakingXp
        Skill.CRAFTING -> craftingXp
        Skill.SMITHING -> smithingXp
        Skill.MINING -> miningXp
        Skill.HERBLORE -> herbloreXp
        Skill.AGILITY -> agilityXp
        Skill.THIEVING -> thievingXp
        Skill.SLAYER -> slayerXp
        Skill.FARMING -> farmingXp
        Skill.RUNECRAFTING -> runecraftingXp
        Skill.HUNTER -> hunterXp
        Skill.CONSTRUCTION -> constructionXp
    }

    fun setExperience(skill: Skill, experience: Int) {
        val previousExperience = getExperience(skill)
        if (previousExperience == experience) return
        val previousLevel = getLevel(skill)
        when(skill){
            Skill.ATTACK -> attackXp = experience
            Skill.DEFENCE -> defenceXp = experience
            Skill.STRENGTH -> strengthXp = experience
            Skill.HITPOINTS -> hitpointsXp = experience
            Skill.RANGED -> rangedXp = experience
            Skill.PRAYER -> prayerXp = experience
            Skill.MAGIC -> magicXp = experience
            Skill.COOKING -> cookingXp = experience
            Skill.WOODCUTTING -> woodcuttingXp = experience
            Skill.FLETCHING -> fletchingXp = experience
            Skill.FISHING -> fishingXp = experience
            Skill.FIREMAKING -> firemakingXp = experience
            Skill.CRAFTING -> craftingXp = experience
            Skill.SMITHING -> smithingXp = experience
            Skill.MINING -> miningXp = experience
            Skill.HERBLORE -> herbloreXp = experience
            Skill.AGILITY -> agilityXp = experience
            Skill.THIEVING -> thievingXp = experience
            Skill.SLAYER -> slayerXp = experience
            Skill.FARMING -> farmingXp = experience
            Skill.RUNECRAFTING -> runecraftingXp = experience
            Skill.HUNTER -> hunterXp = experience
            Skill.CONSTRUCTION -> constructionXp = experience
        }
        totalExperience += experience - previousExperience
        val newLevel = experience.toLevel()
        if(previousLevel != newLevel)
            totalLevel += newLevel - previousLevel
    }

    fun getLevel(skill: Skill): Int = getExperience(skill).toLevel()
}

fun Skill.getColumn() = when(this){
    Skill.ATTACK -> UserSkillStats.attackXp
    Skill.DEFENCE -> UserSkillStats.defenceXp
    Skill.STRENGTH -> UserSkillStats.strengthXp
    Skill.HITPOINTS -> UserSkillStats.hitpointsXp
    Skill.RANGED -> UserSkillStats.rangedXp
    Skill.PRAYER -> UserSkillStats.prayerXp
    Skill.MAGIC -> UserSkillStats.magicXp
    Skill.COOKING -> UserSkillStats.cookingXp
    Skill.WOODCUTTING -> UserSkillStats.woodcuttingXp
    Skill.FLETCHING -> UserSkillStats.fletchingXp
    Skill.FISHING -> UserSkillStats.fishingXp
    Skill.FIREMAKING -> UserSkillStats.firemakingXp
    Skill.CRAFTING -> UserSkillStats.craftingXp
    Skill.SMITHING -> UserSkillStats.smithingXp
    Skill.MINING -> UserSkillStats.miningXp
    Skill.HERBLORE -> UserSkillStats.herbloreXp
    Skill.AGILITY -> UserSkillStats.agilityXp
    Skill.THIEVING -> UserSkillStats.thievingXp
    Skill.SLAYER -> UserSkillStats.slayerXp
    Skill.FARMING -> UserSkillStats.farmingXp
    Skill.RUNECRAFTING -> UserSkillStats.runecraftingXp
    Skill.HUNTER -> UserSkillStats.hunterXp
    Skill.CONSTRUCTION -> UserSkillStats.constructionXp
}
