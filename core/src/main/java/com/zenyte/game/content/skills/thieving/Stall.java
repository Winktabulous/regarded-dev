package com.zenyte.game.content.skills.thieving;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;

import java.util.HashMap;
import java.util.Map;

import static com.zenyte.game.content.skills.thieving.StallType.*;

/**
 * @author Tommeh | 19 jul. 2018 | 21:52:58
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public enum Stall {

	HOME_COINS_STALL_BEGINNER(COIN_STALL_BEGINNER, 50055, 4276),
	HOME_COINS_STALL_EASY(COIN_STALL_EASY, 50056, 4276),
	HOME_COINS_STALL_MEDIUM(COIN_STALL_MEDIUM, 50057, 4276),
	HOME_COINS_STALL_HARD(COIN_STALL_HARD, 50058, 4276),
	HOME_COINS_STALL_MASTER(COIN_STALL_MASTER, 50059, 4276),
	LDI_STALL(COIN_STALL_LDI, 36571, 4276),

	VARROCK_AND_KOUREND_TEA_STALL(TEA_STALL, 635, 634),
	RELLEKKA_FISH_STALL(FISH_STALL, 4277, 4276),
	RELLEKKA_FUR_STALL(FUR_STALL, 4278, 4276),
	MISCELLANIA_FISH_STALL(FISH_STALL, 4705, 634),
	MISCELLANIA_VEGETABLE_STALL(VEGETABLE_STALL, 4706, 634),
	ETCETERIA_FISH_STALL(FISH_STALL, 4707, 634),
	ETCETERIA_VEGETABLE_STALL(VEGETABLE_STALL, 4708, 634),
	APE_ATOLL_FOOD_STALL(FOOD_STALL, 4875, 4797),
	APE_ATOLL_SCIMITAR_STALL(SCIMITAR_STALL, 4878, 4797),
	APE_ATOLL_GENERAL_STALL(GENERAL_STALL, 4876, 4797),
	APE_ATOLL_MAGIC_STALL(MAGIC_STALL, 4877, 4797),
    APE_ATOLL_CRAFTING_STALL(CRAFTING_STALL, 4874, 4797),
	KELDAGRIM_BAKERY_STALL(BAKERS_STALL, 6163, 6984),
	KELDAGRIM_GEM_STALL(GEM_STALL, 6162, 6984),
	KELDAGRIM_CRAFTING_STALL(CRAFTING_STALL, 6166, 6984),
	KELDAGRIM_CROSSBOW_STALL(CROSSBOW_STALL, 17031, 6984),
	KELDAGRIM_SILVER_STALL(SILVER_STALL, 6164, 6984),
	KELDAGRIM_CLOTHES_STALL(SILK_STALL, 6165, 6984),
	DRAYNOR_SEED_STALL(SEED_STALL, 7053, 634),
	DRAYNOR_MARKET_STALL(WINE_STALL, 14011, 634),
	ARDOUGNE_AND_KOUREND_SILK_STALL(SILK_STALL, 11729, 634),
	ARDOUGNE_AND_KOUREND_BAKERS_STALL(BAKERS_STALL, 11730, 634),
	ARDOUGNE_AND_KOUREND_GEM_STALL(GEM_STALL, 11731, 634),
	ARDOUGNE_FUR_STALL(FUR_STALL, 11732, 634),
	ARDOUGNE_SPICE_STALL(SPICE_STALL, 11733, 634),
	ARDOUGNE_AND_KOUREND_SILVER_STALL(SILVER_STALL, 11734, 634),
	TZHAAR_ORE_SHOP_COUNTER(TZHAAR_ORE_STALL, 30279, 30278),
	TZHAAR_GEM_SHOP_COUNTER(TZHAAR_GEM_COUNTER, 30280, 30278),
	KOUREND_FRUIT_STALL(FRUIT_STALL, 28823, 27537);
	
	private final StallType type;
	private final int objectId;
    private final int emptyId;

	public static final Stall[] VALUES = values();
	private static final Map<Integer, Stall> STALLS = new HashMap<>();
	public static final ObjectList<Stall> APE_ATOLL_STALLS = ObjectArrayList.wrap(new Stall[]{
			APE_ATOLL_FOOD_STALL, APE_ATOLL_SCIMITAR_STALL, APE_ATOLL_GENERAL_STALL, APE_ATOLL_MAGIC_STALL, APE_ATOLL_CRAFTING_STALL
	});

	static {
		for (final Stall stall : VALUES) {
			STALLS.put(stall.objectId, stall);
		}
	}
	
	public static final Stall getStall(final int objectId) {
		return STALLS.get(objectId);
	}
	
	Stall(final StallType type, final int objectId, final int emptyId) {
		this.type = type;
		this.objectId = objectId;
		this.emptyId = emptyId;
	}
	
	public StallType getType() {
	    return type;
	}
	
	public int getObjectId() {
	    return objectId;
	}
	
	public int getEmptyId() {
	    return emptyId;
	}

}
