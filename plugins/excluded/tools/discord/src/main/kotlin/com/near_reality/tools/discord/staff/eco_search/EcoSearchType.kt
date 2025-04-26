package com.near_reality.tools.discord.staff.eco_search

/**
 * Represents a way to do an economy search (analysis).
 *
 * @author Stan van der Bend
 */
sealed class EcoSearchType {

    companion object {

        /**
         * Represents a list of all search types.
         */
        val all = listOf(Item, Value, Player)

        /**
         * Gets the [EcoSearchType] whose [EcoSearchType.discordId] matches the specified [string]
         */
        fun forId(string: String) = all.find { it.discordId == string }
    }

    /**
     * Discord backend unique id for this type of search,
     */
    val discordId get() = this::class.simpleName!!

    /**
     * Discord visual label to the user for his search type.
     */
    val discordLabel get() = "By $discordId"

    /**
     * The description of this type of search.
     */
    abstract val discordDescription: String

    /**
     * The text field placeholder prompting for user input, e.g. item id.
     */
    abstract val discordTextInputPlaceHolder: String

    /**
     * Searches for a specific item (or multiple items) in all player accounts.
     */
    object Item : EcoSearchType() {
        override val discordDescription: String
            get() = "Search the entire economy for a specific Item"
        override val discordTextInputPlaceHolder: String
            get() = "Enter the name or id of an item"
    }

    /**
     * Calculates total value of all player accounts.
     */
    object Value : EcoSearchType() {
        override val discordDescription: String
            get() = "Search the entire economy, filtered and sorted by value"
        override val discordTextInputPlaceHolder: String
            get() = "Enter the value in number or using letters (e.g. 100m)"
    }

    /**
     * Does a full player account analysis.
     */
    object Player : EcoSearchType() {
        override val discordDescription: String
            get() =  "Search a specific player and list their items"
        override val discordTextInputPlaceHolder: String
            get() = "Enter the name of a player"
    }
}
