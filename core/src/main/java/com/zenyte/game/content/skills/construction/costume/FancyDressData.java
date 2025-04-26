package com.zenyte.game.content.skills.construction.costume;

import com.zenyte.game.content.magicstorageunit.StorableSetPiece;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.container.ItemContainer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kris | 3. march 2018 : 17:58.10
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public enum FancyDressData {

	MIME_COSTUME(10629, new StorableSetPiece(3057), new StorableSetPiece(3058), new StorableSetPiece(3059), new StorableSetPiece(3060), new StorableSetPiece(3061)),
	ROYAL_FROG_COSTUME(10630, new StorableSetPiece(6184, 6186), new StorableSetPiece(6185, 6187)),
	FROG_MASK(10721, new StorableSetPiece(6188)),
	ZOMBIE_OUTFIT(10631, new StorableSetPiece(7594), new StorableSetPiece(7592), new StorableSetPiece(7593), new StorableSetPiece(7595), new StorableSetPiece(7596)),
	CAMO_OUTFIT(10632, new StorableSetPiece(6656), new StorableSetPiece(6654), new StorableSetPiece(6655)),
	LEDERHOSEN_OUTFIT(10633, new StorableSetPiece(6182), new StorableSetPiece(6180), new StorableSetPiece(6181)),
	SHADE_ROBES(10634, new StorableSetPiece(546), new StorableSetPiece(548));
	
    public static final FancyDressData[] VALUES = values();
    public static final ItemContainer CONTAINER = new ItemContainer(7, false);
    public static final Map<Integer, FancyDressData> MAP = new HashMap<Integer, FancyDressData>(VALUES.length * 5);
    public static final Map<Integer, FancyDressData> DISPLAY_MAP = new HashMap<Integer, FancyDressData>(VALUES.length);

    static {
        for (FancyDressData val : VALUES) {
            CONTAINER.add(new Item(val.displayItem));
            for (StorableSetPiece p : val.pieces)
                for (int i : p.getIds())
                    MAP.put(i, val);
            DISPLAY_MAP.put(val.getDisplayItem(), val);
        }
    }

    private final int displayItem;
    private final StorableSetPiece[] pieces;
	FancyDressData(final int displayItem, final StorableSetPiece... pieces) {
        this.displayItem = displayItem;
        this.pieces = pieces;
    }

    public int getDisplayItem() {
        return displayItem;
    }

    public StorableSetPiece[] getPieces() {
        return pieces;
    }

}
