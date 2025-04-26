package com.near_reality.api.model

import kotlinx.serialization.Serializable

typealias SlotItemMap = Map<Int, Item>

@Serializable
data class Item(val id: Int, val amount: Int)

@Serializable
data class ItemConfig(
    val id: Int,
    val name: String,
    val tradeable: Boolean,
    val ecoValue: Int?,
    val generalStore: Int?,
    val grandExchangeAverage: Int?,
    val grandExchangeBuyMin: Int?,
    val grandExchangeSellMin: Int?,
    val grandExchangeBuyMax: Int?,
    val grandExchangeSellMax: Int?,
    val amountInEco: Int?,
    val protectionValue: Int?,
    val averageTradeValue: Int?,
)

@Serializable
data class Location(val hash: Int) {
    constructor(x: Int, y: Int, z: Int) : this(
        y + (x * 16384) + (z * 268435456) // avoids bit shifting entirely
    )

    val x get() = (hash shr 14) and 16383
    val y get() = hash and 16383
    val z get() = (hash shr 28) and 3

    override fun toString(): String {
        return "${x}, ${y}, ${z}"
    }
}

@Serializable
data class ItemContainer(
    val policy: Policy = Policy.NORMAL,
    val items: SlotItemMap = emptyMap()
) {
    @Serializable
    enum class Policy {
        ALWAYS_STACK,
        NEVER_STACK,
        NORMAL
    }
}

@Serializable
data class ItemContainerWrapper(
    val container: ItemContainer,
) {
    constructor(policy: ItemContainer.Policy) : this(ItemContainer(policy))
}
@Serializable
enum class GrandExchangeOfferType {
    BUY,
    SELL
}

@Serializable
data class GrandExchangeOffer(
    val item: Item,
    val slot: Int,
    val price: Int,
    val type: GrandExchangeOfferType,
)


@Serializable
data class DeathResult(
    val inventory: SlotItemMap,
    val equipment: SlotItemMap,
    val lootingBag: SlotItemMap,
    val runePouch1: SlotItemMap,
    val runePouch2: SlotItemMap,
    val kept: SlotItemMap,
    val lost: SlotItemMap,
    val lostToKiller: List<Item>,
    val grave:  List<Item>,
)

@Serializable
enum class WorldLocation(val id: Int) {
    UNITED_STATES_OF_AMERICA(0),
    UNITED_KINGDOM(1),
    CANADA(2),
    AUSTRALIA(3),
    NETHERLANDS(4),
    SWEDEN(5),
    FINLAND(6),
    GERMANY(7);
}

@Serializable
enum class WorldType(val mask: Int) {
    MEMBERS(1),
    PVP(4),
    BOUNTY(32),
    PVP_ARENA(64),
    SKILL_TOTAL(128),
    QUEST_SPEEDRUNNING(256),
    HIGH_RISK(1024),
    LAST_MAN_STANDING(16384),
    NOSAVE_MODE(33554432),
    TOURNAMENT(67108864),
    FRESH_START_WORLD(134217728),
    DEADMAN(536870912),
    SEASONAL(1073741824),
    ;
}

@Serializable
enum class WorldVisibility {

    /**
     * The world is only visible to developers.
     */
    DEVELOPER,

    /**
     * The world is only visible to beta testers.
     */
    BETA,

    /**
     * The world is visible to everyone.
     */
    PUBLIC
}
