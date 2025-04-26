package com.near_reality.game.content.gauntlet.npc

import com.zenyte.game.world.entity.npc.NpcId

/**
 * @author Andys1814.
 * @since 1/21/2022.
 */
enum class GauntletMonsterType(val npcId: Int, val corruptedNpcId: Int) {

    RAT(NpcId.CRYSTALLINE_RAT, NpcId.CORRUPTED_RAT),
    SPIDER(NpcId.CRYSTALLINE_SPIDER, NpcId.CORRUPTED_SPIDER),
    BAT(NpcId.CRYSTALLINE_BAT, NpcId.CORRUPTED_BAT),
    UNICORN(NpcId.CRYSTALLINE_UNICORN, NpcId.CORRUPTED_UNICORN),
    SCORPION(NpcId.CRYSTALLINE_SCORPION, NpcId.CORRUPTED_SCORPION),
    WOLF(NpcId.CRYSTALLINE_WOLF, NpcId.CORRUPTED_WOLF),
    BEAR(NpcId.CRYSTALLINE_BEAR, NpcId.CORRUPTED_BEAR),
    DRAGON(NpcId.CRYSTALLINE_DRAGON, NpcId.CORRUPTED_DRAGON),
    DARK_BEAST(NpcId.CRYSTALLINE_DARK_BEAST, NpcId.CORRUPTED_DARK_BEAST);

    companion object {
        @JvmField val TIER_ONE = arrayOf(RAT, SPIDER, BAT)
        @JvmField val TIER_TWO = arrayOf(UNICORN, SCORPION, WOLF)
        @JvmField val DEMI_BOSS = arrayOf(BEAR, DRAGON, DARK_BEAST)
    }
}
