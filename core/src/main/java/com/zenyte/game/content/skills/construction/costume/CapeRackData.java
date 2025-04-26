package com.zenyte.game.content.skills.construction.costume;

import com.zenyte.game.content.magicstorageunit.StorableSetPiece;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.container.ItemContainer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kris | 3. march 2018 : 22:06.36
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public enum CapeRackData {

	CAPE_OF_LEGENDS(10635, new StorableSetPiece(1052)),
	OBSIDIAN_CAPE(10636, new StorableSetPiece(6568)),
	FIRE_CAPE(10637, new StorableSetPiece(6570)),
	WILDERNESS_CAPE(10638),
	SARADOMIN_CAPE(2412, new StorableSetPiece(2412)),
	GUTHIX_CAPE(2413, new StorableSetPiece(2413)),
	ZAMORAK_CAPE(2414, new StorableSetPiece(2414)),
	ATTACK_CAPE(10639, new StorableSetPiece(9747, 9748), new StorableSetPiece(9749)),
	DEFENCE_CAPE(10641, new StorableSetPiece(9753, 9754), new StorableSetPiece(9755)),
	STRENGTH_CAPE(10640, new StorableSetPiece(9750, 9751), new StorableSetPiece(9752)),
	HITPOINTS_CAPE(10647, new StorableSetPiece(9768, 9769), new StorableSetPiece(9770)),
	AGILITY_CAPE(10648, new StorableSetPiece(9771, 9772), new StorableSetPiece(9773)),
	COOKING_CAPE(10658, new StorableSetPiece(9801, 9802), new StorableSetPiece(9803)),
	CONSTRUCTION_CAPE(10654, new StorableSetPiece(9789, 9790), new StorableSetPiece(9791)),
	CRAFTING_CAPE(10651, new StorableSetPiece(9780, 9781), new StorableSetPiece(9782)),
	FARMING_CAPE(10661, new StorableSetPiece(9810, 9811), new StorableSetPiece(9812)),
	FIREMAKING_CAPE(10659, new StorableSetPiece(9804, 9805), new StorableSetPiece(9806)),
	FISHING_CAPE(10657, new StorableSetPiece(9798, 9799), new StorableSetPiece(9800)),
	FLETCHING_CAPE(10652, new StorableSetPiece(9783, 9784), new StorableSetPiece(9785)),
	HERBLORE_CAPE(10649, new StorableSetPiece(9774, 9775), new StorableSetPiece(9776)),
	MAGIC_CAPE(10644, new StorableSetPiece(9762, 9763), new StorableSetPiece(9764)),
	MINING_CAPE(10655, new StorableSetPiece(9792, 9793), new StorableSetPiece(9794)),
	PRAYER_CAPE(10643, new StorableSetPiece(9759, 9760), new StorableSetPiece(9761)),
	RANGING_CAPE(10642, new StorableSetPiece(9756, 9757), new StorableSetPiece(9758)),
	RUNECRAFT_CAPE(10645, new StorableSetPiece(9765, 9766), new StorableSetPiece(9767)),
	SLAYER_CAPE(10653, new StorableSetPiece(9786, 9787), new StorableSetPiece(9788)),
	SMITHING_CAPE(10656, new StorableSetPiece(9795, 9796), new StorableSetPiece(9797)),
	THIEVING_CAPE(10650, new StorableSetPiece(9777, 9778), new StorableSetPiece(9779)),
	WOODCUTTING_CAPE(10660, new StorableSetPiece(9807, 9808), new StorableSetPiece(9809)),
	HUNTER_CAPE(10646, new StorableSetPiece(9948, 9949), new StorableSetPiece(9950)),
	QUEST_POINT_CAPE(10662, new StorableSetPiece(9813, 13068), new StorableSetPiece(9814)),
	ACHIEVEMENT_DIARY_CAPE(19476, new StorableSetPiece(19476, 13069), new StorableSetPiece(13070)),
	MUSIC_CAPE(13224, new StorableSetPiece(13221, 13222), new StorableSetPiece(13223)),
	SPOTTED_HUNTING_CAPE(10663, new StorableSetPiece(10069)),
	SPOTTIER_HUNTING_CAPE(10664, new StorableSetPiece(10071)),
	MAX_CAPE(13282, new StorableSetPiece(13342), new StorableSetPiece(13281)),
	INFERNAL_CAPE(21295, new StorableSetPiece(21295)),
	CHAMPIONS_CAPE(21439, new StorableSetPiece(21439)),
	IMBUED_SARADOMIN_CAPE(21791, new StorableSetPiece(21791)),
	
	IMBUED_GUTHIX_CAPE(21793, new StorableSetPiece(21793)),
	IMBUED_ZAMORAK_CAPE(21795, new StorableSetPiece(21795)),
	MYTHICAL_CAPE(22114, new StorableSetPiece(22114)),
	MORE(10165),
	BACK(10166);
	
	CapeRackData(final int displayItem, final StorableSetPiece... pieces) {
		this.displayItem = displayItem;
		this.pieces = pieces;
	}
	
	public static final CapeRackData[] VALUES = values();
	public static final ItemContainer[] CONTAINERS = new ItemContainer[] {
			new ItemContainer(40, false),
			new ItemContainer(4, false)
	};

    public static final Map<Integer, CapeRackData> MAP = new HashMap<Integer, CapeRackData>(VALUES.length * 5);
    public static final Map<Integer, CapeRackData> DISPLAY_MAP = new HashMap<Integer, CapeRackData>(VALUES.length);

    static {
        CONTAINERS[1].add(new Item(BACK.getDisplayItem()));
        for (CapeRackData val : VALUES) {
            if (val == MORE)
                break;
            if (val.ordinal() <= IMBUED_SARADOMIN_CAPE.ordinal()) {
                CONTAINERS[0].add(new Item(val.displayItem));
            } else
                CONTAINERS[1].add(new Item(val.displayItem));
            for (StorableSetPiece p : val.pieces)
                for (int i : p.getIds())
                    MAP.put(i, val);
            DISPLAY_MAP.put(val.getDisplayItem(), val);
        }
        CONTAINERS[0].add(new Item(MORE.getDisplayItem()));
    }

    private final int displayItem;
    private final StorableSetPiece[] pieces;

    public int getDisplayItem() {
        return displayItem;
    }

    public StorableSetPiece[] getPieces() {
        return pieces;
    }

}

