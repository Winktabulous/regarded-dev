package com.zenyte.game.content.minigame.duelarena.interfaces

/**
 * Represents item menu options.
 */
sealed class ItemStakeOption {

    /**
     * Add/remove [amount] items.
     */
    class Amount(val amount: Int) : ItemStakeOption()

    /**
     * Prompt for amount of items to add/remove.
     */
    object X : ItemStakeOption()
}