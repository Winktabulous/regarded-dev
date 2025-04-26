package com.near_reality.game.content.contests.launch

import com.near_reality.game.content.challenges.ChallengeRegistry
import com.zenyte.game.content.follower.impl.BossPet
import com.zenyte.game.content.follower.impl.SkillingPet
import com.zenyte.game.item.ItemId
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.collectionlog.CollectionLogInterface
import com.zenyte.game.world.entity.player.privilege.GameMode
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege
import mgi.types.config.StructDefinitions
import mgi.types.config.enums.EnumDefinitions
import java.util.*
import kotlin.jvm.optionals.getOrNull
import kotlin.reflect.KClass

@Suppress("unused")
class SoloLaunchContest : ChallengeRegistry<SoloContestant>() {
    override fun registerChallenges() {
        register(10410, "Maxed realist") {
            if(this.findPlayer().isEmpty)
                return@register 0
            if(this.findPlayer().get().privilege.inherits(PlayerPrivilege.ADMINISTRATOR))
                return@register 0
            if(this.findPlayer().getOrNull()?.combatXPRate == 5 && this.findPlayer().getOrNull()?.gameMode == GameMode.REGULAR)
                return@register sumOf { skills.totalLevel }
            else 0
        }
        register(10411, "Maxed non-group ironman") {
            if(this.findPlayer().isEmpty)
                return@register 0
            if(this.findPlayer().get().privilege.inherits(PlayerPrivilege.ADMINISTRATOR))
                return@register 0
            if(this.findPlayer().getOrNull()?.isIronman == true && this.findPlayer().getOrNull()?.gameMode?.isGroupIronman == false)
                return@register sumOf { skills.totalLevel }
            else 0
        }
        register(10412, "Maxed Ultimate ironman") {
            if(this.findPlayer().isEmpty)
                return@register 0
            if(this.findPlayer().get().privilege.inherits(PlayerPrivilege.ADMINISTRATOR))
                return@register 0
            if(this.findPlayer().getOrNull()?.gameMode == GameMode.ULTIMATE_IRON_MAN)
                return@register sumOf { skills.totalLevel }
            else 0
        }
        register(10413, "Maxed Realist ironman") {
            if(this.findPlayer().isEmpty)
                return@register 0
            if(this.findPlayer().get().privilege.inherits(PlayerPrivilege.ADMINISTRATOR))
                return@register 0
            if(this.findPlayer().get().isIronman && !this.findPlayer().get().gameMode.isGroupIronman && this.findPlayer().get().combatXPRate == 5) {
                return@register sumOf { skills.totalLevel }
            }
            0
        }
        register(10414, "Achievements Cape") {
            if(this.findPlayer().isEmpty)
                return@register 0
            if(this.findPlayer().get().privilege.inherits(PlayerPrivilege.ADMINISTRATOR))
                return@register 0
            if(this.findPlayer().get().combatAchievements.hasAllTiersCompleted() && this.findPlayer().get().achievementDiaries.isAllCompleted)
                return@register 1
            else 0
        }
        register(10415, "Twisted Bow") {
            if(this.findPlayer().isEmpty)
                return@register 0
            if(this.findPlayer().get().privilege.inherits(PlayerPrivilege.ADMINISTRATOR))
                return@register 0
            anyMemberHasItem(ItemId.TWISTED_BOW)
        }

        register(10416, "Olmlet") {
            if(this.findPlayer().isEmpty)
                return@register 0
            if(this.findPlayer().get().privilege.inherits(PlayerPrivilege.ADMINISTRATOR))
                return@register 0
            anyMemberHasItem(ItemId.OLMLET)
        }

        register(10417, "Metamorphic Dust") {
            if(this.findPlayer().isEmpty)
                return@register 0
            if(this.findPlayer().get().privilege.inherits(PlayerPrivilege.ADMINISTRATOR))
                return@register 0
            anyMemberHasItem(ItemId.METAMORPHIC_DUST)
        }

        register(10418, "Most CoX Completions") {
            if(this.findPlayer().isEmpty)
                return@register 0
            if(this.findPlayer().get().privilege.inherits(PlayerPrivilege.ADMINISTRATOR))
                return@register 0
            val player = this.findPlayer().get()
            val kc = player.getNumericAttribute("chambersofxeric").toInt() + player.getNumericAttribute("challengechambersofxeric").toInt()
            kc
        }

        register(10419, "Fastest Solo CoX CM") {
            if(this.findPlayer().isEmpty)
                return@register 0
            if(this.findPlayer().get().privilege.inherits(PlayerPrivilege.ADMINISTRATOR))
                return@register 0
            val player = this.findPlayer().get()
            val kc = player.getNumericAttribute("cox_cm_solo_ticks").toInt()
            kc
        }
    }

    private fun SoloContestant.mapIfOnline(default: Boolean = false, mapper: Player.() -> Boolean) =
        mapIfOnline<Boolean>(default, mapper)

    private fun <T> SoloContestant.mapIfOnline(default: T, mapper: Player.() -> T) =
        findPlayer().map(mapper).orElse(default)

    private fun SoloContestant.anyMember(predicate: Player.() -> Boolean) =
        if (activeMembers.any { it.mapIfOnline(false, predicate) }) 1 else 0

    private fun SoloContestant.sumOf(extractor: Player.() -> Int) =
        activeMembers.sumOf { it.mapIfOnline(0, extractor) }

    private fun SoloContestant.countBoolean(predicate: Player.() -> Boolean) =
        activeMembers.count { it.mapIfOnline(false, predicate) }

    private fun SoloContestant.anyMemberHasItem(vararg itemIds: Int) =
        anyMember { itemIds.any(::containsItem) }

    private fun SoloContestant.countCollectionLogEntries(log: List<Int>) =
        countUniqueInstancesAmongMembers(log) {
            it.collectionLog.container.contains(this, 1)
        }

    private fun <T> SoloContestant.countUniqueInstancesAmongMembers(collection: Iterable<T>, has: T.(Player) -> Boolean) =
        activeMembers.flatMap { member ->
            member.mapIfOnline(emptyList()) {
                collection.filter { it.has(this) }
            }
        }.toSet().size

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

    override fun resolveType(uuid: UUID) =
        SoloContestant.find(uuid)


    override fun type(): KClass<SoloContestant> =
        SoloContestant::class
}