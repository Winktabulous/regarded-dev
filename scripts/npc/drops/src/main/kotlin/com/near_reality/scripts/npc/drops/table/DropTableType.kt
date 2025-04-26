package com.near_reality.scripts.npc.drops.table

/**
 * Represents a [DropTable] type.
 *
 * @author Stan van der Bend
 */
enum class DropTableType {

    /**
     * These are the items that a monster is guaranteed to drop when it dies.
     */
    Always,

    /**
     * The main drops table, if a monster has one, is guaranteed to be rolled when a monster dies.
     * It contains a selection of items, with one randomly chosen.
     * For a small handful of monsters, this table can be rolled multiple times in one kill.
     */
    Main,

    /**
     * Secondary drops are drops alongside main drops.
     * A secondary drop is also known as a complementary drop.
     */
    Secondary,

    /**
     * Tertiary drops are a separate table that is rolled for alongside the main drop.
     * Unlike the main drop, multiple tertiary drops can be obtained in a single kill;
     * however, similarly classed items cannot be obtained at the same time.
     */
    Tertiary,

    /**
     * The rare drop table is an extra table of drops that can be rolled
     * if a drop slot indicating it is rolled on the main drop.
     * As such, the rare drop table drop will count as the main drop.
     * The items available on the rare drop table are the same across every monster.
     */
    Rare,

    /**
     * Universal drops are a class of items that can be obtained by nearly every monster.
     */
    Universal,

    /**
     * Unique drops are drops that are unique to a particular monster.
     * In other words, they are dropped exclusively from that monster.
     * For example, pets, Fremennik rings, and Wilderness rings are unique drops.
     * These drops are generally awarded from the main drop table.
     */
    Unique,

    Nested,
    Standalone
}
