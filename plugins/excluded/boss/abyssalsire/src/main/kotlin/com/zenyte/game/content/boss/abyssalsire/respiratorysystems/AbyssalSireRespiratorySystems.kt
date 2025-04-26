package com.zenyte.game.content.boss.abyssalsire.respiratorysystems

import com.zenyte.game.content.boss.abyssalsire.AbyssalSire
import com.zenyte.game.world.entity.ImmutableLocation
import com.zenyte.game.world.entity.npc.NPC

/**
 * @author Jire
 * @author Kris
 */
internal class AbyssalSireRespiratorySystems(
    private val collection: Collection<AbyssalSireRespiratorySystem>
) {

    constructor(sire: AbyssalSire) : this(locations.map {
        AbyssalSireRespiratorySystem(
            sire.corner.translate(it),
            sire
        )
    })

    fun allDead(): Boolean = collection.none { it.state == AbyssalSireRespiratorySystem.State.GASSY }

    fun respawnAll() {
        collection.forEach(NPC::finish)
        collection.forEach(NPC::spawn)
    }

    private companion object {
        private val locations = listOf(
            ImmutableLocation(2964, 4844, 0),
            ImmutableLocation(2992, 4843, 0),
            ImmutableLocation(2967, 4834, 0),
            ImmutableLocation(2995, 4833, 0)
        )
    }

}