package com.zenyte.game.content.theatreofblood.room.nylocas.model

import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.masks.HitType
import com.zenyte.game.world.entity.npc.NpcId

/**
 * @author Tommeh
 * @author Jire
 */
internal enum class NylocasType(
    val acceptableHitType: HitType,
    val explosionAnimation: Animation,
    val id: Int
) {

    MELEE(
        HitType.MELEE,
        Animation(8006),
        NpcId.NYLOCAS_VASILIAS_8355
    ),
    RANGED(
        HitType.RANGED,
        Animation(8000),
        NpcId.NYLOCAS_VASILIAS_8357
    ),
    MAGIC(
        HitType.MAGIC,
        Animation(7992),
        NpcId.NYLOCAS_VASILIAS_8356
    );


    companion object {
        val values = entries.toTypedArray()

        operator fun get(npcId: Int): NylocasType? {
            for (type in values)
                if (npcId == type.id)
                    return type
            return null
        }

    }

}