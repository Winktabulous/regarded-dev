package com.zenyte.game.content.skills.magic.spells.teleports

import com.zenyte.game.world.entity.player.teleportsystem.TeleportCategory

/**
 * Used to make invocation of Spell book teleport spell casts open the teleport interface
 * at the specified [category].
 */
enum class SpellbookOpenTeleportInterface(
    val category: TeleportCategory,
    val spellName: String
) {
    TRAINING_TELEPORT(
        TeleportCategory.TRAINING,
        "training teleports"
    ),
    SKILLING_TELEPORT(
        TeleportCategory.SKILLING,
        "skilling teleports"
    ),
    MINIGAMES_TELEPORT(
        TeleportCategory.MINIGAMES,
        "minigames teleports"
    ),
    WILDERNESS_TELEPORT(
        TeleportCategory.WILDERNESS,
        "wilderness teleports"
    ),
    BOSSES_TELEPORT(
        TeleportCategory.BOSSES,
        "bosses teleports"
    ),
    DUNGEONS_TELEPORT(
        TeleportCategory.DUNGEONS,
        "dungeons teleports"
    ),
    CITIES_TELEPORT(
        TeleportCategory.CITIES,
        "cities teleports"
    ),
    MISC_TELEPORT(
        TeleportCategory.MISC,
        "misc teleports"
    );

    companion object {
        fun findCategory(spellName: String) = values()
            .find { it.spellName == spellName }
            ?.category
    }
}