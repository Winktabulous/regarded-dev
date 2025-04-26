package com.zenyte.game.content.theatreofblood.room

import com.zenyte.game.content.boons.impl.BrawnOfJustice
import com.zenyte.game.content.theatreofblood.insideTob
import com.zenyte.game.content.theatreofblood.room.verzikvitur.VerzikVitur
import com.zenyte.game.content.theatreofblood.room.verzikvitur.VerzikViturPhase
import com.zenyte.game.content.tombsofamascut.AbstractTheatreNPC
import com.zenyte.game.util.Direction
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.Hit
import com.zenyte.game.world.entity.npc.combatdefs.ImmunityType
import com.zenyte.game.world.entity.npc.combatdefs.NPCCDLoader
import com.zenyte.game.world.entity.npc.combatdefs.NPCCombatDefinitions
import com.zenyte.game.world.entity.npc.combatdefs.StatType
import com.zenyte.game.world.entity.player.Player
import java.util.*
import kotlin.math.floor

/**
 * @author Jire
 * @author Tommeh
 */
internal abstract class TheatreNPC<T : TheatreRoom>(
    val room: T,
    id: Int,
    tile: Location?,
    facing: Direction = Direction.DEFAULT,
) : AbstractTheatreNPC(id, tile, facing, 0) {

    var maxHpScaled: Int = 0

    init {
        spawned = true
        supplyCache = false
        isForceMultiArea = true
    }

    override fun processNPC() {
        if (room.players.isEmpty()) return
        super.processNPC()
    }

    override fun updateCombatDefinitions() {
        super.updateCombatDefinitions()

        maxHpScaled = super.getMaxHitpoints()
        @Suppress("USELESS_ELVIS")
        room ?: return // required because the Java NPC code calls this on constructor (bad practice)

        setStats()
        combatDefinitions.immunityTypes = EnumSet.allOf(ImmunityType::class.java)
    }

    protected open fun setStats() {
        val cachedDefs = NPCCombatDefinitions.clone(getId(), NPCCDLoader.get(getId()))
        val difficultyMultiplier = 0.75
        val statDefinitions = cachedDefs.statDefinitions
        for (aggressiveStat in stats) {
            val stat = statDefinitions[aggressiveStat]
            if (stat > 0) {
                statDefinitions[aggressiveStat] = 1.coerceAtLeast(floor(stat * difficultyMultiplier).toInt())
            }
        }

        setCombatDefinitions(cachedDefs)

        var maxHitpoints = maxHpScaled
        if(room.raid.bypassMode && this is VerzikVitur) {
            maxHpScaled = when(this.phase) {
                VerzikViturPhase.FIRST -> 200
                VerzikViturPhase.SECOND, VerzikViturPhase.THIRD -> {1800}
                else -> {super.getMaxHitpoints()}
            }
            setHitpoints(maxHpScaled)
            return
        }
        val partySize = room.raid.party.size
        if (partySize <= 3)
            maxHitpoints = floor(maxHitpoints * 0.75).toInt()
        else if (partySize == 4)
            maxHitpoints = floor(maxHitpoints * 0.875).toInt()
        maxHpScaled = maxHitpoints
        setHitpoints(maxHpScaled)
    }

    override fun getMaxHitpoints(): Int {
        return maxHpScaled
    }

    override fun isTolerable() = false

    override fun isEntityClipped() = false

    override fun setRespawnTask() {}

    override fun processHit(hit: Hit) {
        if(hit.source is Player && hit.damage > 0) {
            val t = hit.source as Player
            if (t.hasBoon(BrawnOfJustice::class.java) && BrawnOfJustice.applies(t) && t.insideTob) hit.damage = (hit.damage * 1.2).toInt()
        }

        super.processHit(hit)

        val damage = hit.damage
        if (damage < 1) return
        val source = hit.source
        if (source is Player)
            room.playerDealtDamage(source, damage)
    }

    companion object {
        val stats = arrayOf(StatType.ATTACK_STAB, StatType.ATTACK_SLASH, StatType.ATTACK_CRUSH, StatType.ATTACK_MAGIC, StatType.ATTACK_RANGED,
            StatType.DEFENCE_STAB, StatType.DEFENCE_SLASH, StatType.DEFENCE_CRUSH, StatType.DEFENCE_MAGIC, StatType.DEFENCE_RANGED)
    }

}