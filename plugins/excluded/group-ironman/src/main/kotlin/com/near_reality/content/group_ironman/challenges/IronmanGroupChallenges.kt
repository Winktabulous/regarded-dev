package com.near_reality.content.group_ironman.challenges

import com.near_reality.content.group_ironman.IronmanGroup
import com.near_reality.content.group_ironman.IronmanGroupMember
import com.near_reality.game.content.challenges.ChallengeRegistry
import com.zenyte.game.content.achievementdiary.AchievementDiaries
import com.zenyte.game.content.achievementdiary.Diary
import com.zenyte.game.content.achievementdiary.DiaryComplexity
import com.zenyte.game.content.follower.impl.BossPet
import com.zenyte.game.content.follower.impl.SkillingPet
import com.zenyte.game.content.larranskey.LarransKey
import com.zenyte.game.item.ItemId
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.collectionlog.CollectionLogInterface
import mgi.types.config.StructDefinitions
import mgi.types.config.enums.EnumDefinitions
import java.util.*
import kotlin.reflect.KClass

@Suppress("unused")
object IronmanGroupChallenges : ChallengeRegistry<IronmanGroup>() {

    override fun type(): KClass<IronmanGroup>  =
        IronmanGroup::class

    override fun resolveType(uuid: UUID) =
        IronmanGroup.find(uuid)

    override fun registerChallenges() {
        register(10305, "All 5 maxed") {
            sumOf { skills.totalLevel }
        }
        register(10306, "8 skilling pets") {
            val pets = SkillingPet.SKILLING_PETS_SKILLS.map(SkillingPet::getBySkill)
            countUniqueInstancesAmongMembers(pets, SkillingPet::hasPet)
        }
        register(10308, "15 boss pets") {
            countUniqueInstancesAmongMembers(BossPet.VALUES.asIterable(), BossPet::hasPet)
        }
        /*register(10307, "All achievement diaries") {
            sumOf {
                var completedAmount = 0
                var taskAmount = 0
                for (diary in AchievementDiaries.ALL_DIARIES) {
                    val map: Map<DiaryComplexity, List<Diary>> = diary[0].map()
                    for (list in map.values) {
                        for (entry in list) {
                            taskAmount++
                            if (achievementDiaries.getProgress(entry) == entry.objectiveLength()) {
                                completedAmount++
                            }
                        }
                    }
                }
                completedAmount
            }
        } */

        register(10309, "Nex collection log") {
            countCollectionLogEntries(nexCollectionLog)
        }
        register(10310, "Slayer collection log") {
            countCollectionLogEntries(slayerCollectionLog)
        }
        register(10311, "Lil zik") {
            anyMember(BossPet.LIL_ZIK::hasPet)
        }
        register(10312, "Twisted bow") {
            anyMemberHasItem(ItemId.TWISTED_BOW)
        }
        register(10313, "Scythe") {
            anyMemberHasItem(ItemId.SCYTHE_OF_VITUR, ItemId.SCYTHE_OF_VITUR_UNCHARGED)
        }
        register(10314, "Godswords") {
            countUniqueInstancesAmongMembers(listOf(
                ItemId.ARMADYL_GODSWORD,
                ItemId.SARADOMIN_GODSWORD,
                ItemId.ZAMORAK_GODSWORD,
                ItemId.ANCIENT_GODSWORD,
                ItemId.BANDOS_GODSWORD,
            )) { player -> player.containsItem(this) }
        }
        /*register(10315, "VLS") {
            anyMemberHasItem(ItemId.VESTAS_LONGSWORD)
        }
        register(10316, "Larrans chest") {
            sumOf { getNumericAttribute(LarransKey.LARGE_CHEST_ATTRIBUTE).toInt() }
        }
        register(10317, "Ward set") {
            countUniqueInstancesAmongMembers(listOf(
                ItemId.ODIUM_WARD,
                ItemId.MALEDICTION_WARD,
            )) { player -> player.containsItem(this) }
        }
        register(10318, "Angelic artefact") {
            anyMemberHasItem(ItemId.ANGELIC_ARTIFACT)
        } */
        register(10319, "Corrupted gauntlets") {
            sumOf { getNumericAttribute("corrupted_gauntlet_completions").toInt() }
        }
        register(10320, "Barrows") {
            countCollectionLogEntries(barrowsCollectionLog)
        }
    }

    private fun IronmanGroup.anyMemberHasItem(vararg itemIds: Int) =
        anyMember { itemIds.any(::containsItem) }

    private fun IronmanGroup.countCollectionLogEntries(log: List<Int>) =
        countUniqueInstancesAmongMembers(log) {
            it.collectionLog.container.contains(this, 1)
        }

    private fun <T> IronmanGroupMember.mapIfOnline(default: T, mapper: Player.() -> T) =
        findPlayer().map(mapper).orElse(default)

    private fun <T> IronmanGroup.countUniqueInstancesAmongMembers(collection: Iterable<T>, has: T.(Player) -> Boolean) =
        activeMembers.flatMap { member ->
            member.mapIfOnline(emptyList()) {
                collection.filter { it.has(this) }
            }
        }.toSet().size


    private fun IronmanGroup.anyMember(predicate: Player.() -> Boolean) =
        if (activeMembers.any { it.mapIfOnline(false, predicate) }) 1 else 0

    private fun IronmanGroup.sumOf(extractor: Player.() -> Int) =
        activeMembers.sumOf { it.mapIfOnline(0, extractor) }

    private fun IronmanGroup.countBoolean(predicate: Player.() -> Boolean) =
        activeMembers.count { it.mapIfOnline(false, predicate) }

    private fun IronmanGroupMember.mapIfOnline(default: Boolean = false, mapper: Player.() -> Boolean) =
        mapIfOnline<Boolean>(default, mapper)

    private val nexCollectionLog = collectCollectionLogItems(3769)
    private val slayerCollectionLog = collectCollectionLogItems(527)
    private val barrowsCollectionLog = collectCollectionLogItems(477)

    private fun collectCollectionLogItems(structId: Int): List<Int> =
        StructDefinitions.get(structId)
            ?.getValue(CollectionLogInterface.STRUCT_POINTER_SUB_ENUM_CAT)
            ?.orElseThrow { RuntimeException() }.toString().toInt()
            .let(EnumDefinitions::getIntEnum)
            .values
            .int2IntEntrySet()
            .map { it.intValue }
}
