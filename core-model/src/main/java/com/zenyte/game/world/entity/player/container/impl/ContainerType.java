package com.zenyte.game.world.entity.player.container.impl;

/**
 * @author Kris | 4. mai 2018 : 02:16:14
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public enum ContainerType {

    /** If1 containers */
    INVENTORY(93),
    FALADOR_PARTY_CHEST_DEPOSIT(92),
    FALADOR_PARTY_CHEST_PUBLIC_DEPOSIT(91),
    FALADOR_PARTY_CHEST_PRIVATE_DEPOSIT(92),
	FALADOR_PARTY_CHEST_INVENTORY_DEPOSIT(93),

	CUSTOM_FUR_CLOTHING(477),

	/** If3 containers */
	MYSTERY_BOX(133),
	TOURNAMENT(207),
	HERB_SACK(10), //custom
	GEM_BAG(12), //custom
	MAGIC_STORAGE(100),
	COLLECTION_LOG(620),
	STASH_UNIT_BUILD_STAGES(576),
	PRIVATE_STORAGE(583),
	SHARED_STORAGE(582, -2),
    MAXIMUM_SIZE_CONTAINER(582),
	RAID_REWARDS(581),
	EQUIPMENT(94),
	SPOILS_STAKE(541),
    TRADE(90),
    BANK(95),
    RUNE_POUCH(169),
    GE_COLLECTABLES_1(518),
    GE_COLLECTABLES_2(519),
    GE_COLLECTABLES_3(520),
    GE_COLLECTABLES_4(521),
    GE_COLLECTABLES_5(522),
    GE_COLLECTABLES_6(523),
    GE_COLLECTABLES_7(539),
    GE_COLLECTABLES_8(540),
    PRICE_CHECKER(90),
    SHOP(510),
	BARROWS_CHEST(141),
	ITEMS_KEPT_ON_DEATH(584),
	ITEMS_LOST_ON_DEATH(468),
    DEATHS_OFFICE_RETRIEVAL(636),
	PUZZLE_BOX(140),
	THEATRE_OF_BLOOD(612),
	FALADOR_PARTY_CHEST(91),
	ITEM_RETRIEVAL_SERVICE(525),
	DUEL_STAKE(134, -1),
	OPPONENT_STAKE(134, -2),
	SEED_BOX(150),
	SEED_VAULT(626),
	LOOTING_BAG(516),

	WILDERNESS_LOOT_KEY(797),

    STORAGE_ROOM(637),
    TOA_SUPPLY_LIFE(807),
    TOA_SUPPLY_CHAOS(808),
    TOA_SUPPLY_POWER(809),
    TOA_SUPPLY_BAG(810),
    TOA_REWARD(811),

	GIM_BANK(659),
	GIM_INVENTORY(660),

    MX_STORE_ITEMS(1000),
    MX_STORE_CART(1001),
    MX_STORE_PRICES(1002),
    MX_STORE_STOCK(1003),
    MX_STORE_DISCOUNTED(1004),
    MX_STORE_CART_AMOUNT(1005),
    MX_STORE_CART_PRICES(1006),
	;

    public static final ContainerType[] VALUES = values();

    public static final ContainerType[] GE_COLLECTABLES_CONTAINERS = new ContainerType[8];

    static {
        for (int i = 0; i < 8; i++) {
            GE_COLLECTABLES_CONTAINERS[i] = ContainerType.valueOf("GE_COLLECTABLES_" + (i + 1));
        }
    }

    private final int id, interfaceId, componentId;
    private final String name;

    ContainerType(final int id) {
        this(id, -1, 0);
    }

    ContainerType(final int id, final int interfaceId) {
        this(id, interfaceId, 0);
    }

    ContainerType(final int id, final int interfaceId, final int componentId) {
		this.id = id;
        this.interfaceId = interfaceId;
        this.componentId = componentId;
        name = toString().toLowerCase().replaceAll("_", " ");
    }

    public int getId() {
        return id;
    }

    public int getInterfaceId() {
        return interfaceId;
    }

    public int getComponentId() {
        return componentId;
    }

    public String getName() {
        return name;
    }

}
