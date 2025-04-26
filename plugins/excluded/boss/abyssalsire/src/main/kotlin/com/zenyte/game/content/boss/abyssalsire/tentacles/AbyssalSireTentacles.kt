package com.zenyte.game.content.boss.abyssalsire.tentacles

import com.zenyte.game.content.boss.abyssalsire.AbyssalSire
import com.zenyte.game.world.entity.npc.NPC

/**
 * @author Jire
 */
internal class AbyssalSireTentacles(private val collection: Collection<AbyssalSireTentacle>) {

    constructor(sire: AbyssalSire) : this(AbyssalSireTentacleSpawns.values.map {
        AbyssalSireTentacle(
            it.npcID,
            sire.corner.translate(it.location),
            sire
        ).apply(NPC::spawn)
    })

    fun reset() = collection.forEach(AbyssalSireTentacle::resetTentacle)

    fun disorientate() = collection.forEach(AbyssalSireTentacle::disorientate)

    fun rise() = collection.forEach(AbyssalSireTentacle::rise)

    fun riseIfNotActive() = collection.forEach(AbyssalSireTentacle::riseIfNotActive)

}