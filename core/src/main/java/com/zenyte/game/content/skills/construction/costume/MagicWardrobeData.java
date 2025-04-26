package com.zenyte.game.content.skills.construction.costume;

import com.zenyte.game.content.magicstorageunit.StorableSetPiece;
import com.zenyte.game.content.minigame.wintertodt.RewardCrate;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.container.ItemContainer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kris | 3. march 2018 : 19:46.47
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public enum MagicWardrobeData {

	BLUE_MYSTIC_ROBES(10601, new StorableSetPiece(4089), new StorableSetPiece(4091), new StorableSetPiece(4093), new StorableSetPiece(4095), new StorableSetPiece(4097)),
	DARK_MYSTIC_ROBES(10602, new StorableSetPiece(4099), new StorableSetPiece(4101), new StorableSetPiece(4103), new StorableSetPiece(4105), new StorableSetPiece(4107)),
	LIGHT_MYSTIC_ROBES(10603, new StorableSetPiece(4109), new StorableSetPiece(4111), new StorableSetPiece(4113), new StorableSetPiece(4115), new StorableSetPiece(4117)),
	SKELETAL_ROBES(10604, new StorableSetPiece(6137), new StorableSetPiece(6139), new StorableSetPiece(6141), new StorableSetPiece(6147), new StorableSetPiece(6153)),
	INFINITY_ROBES(10605, new StorableSetPiece(6918), new StorableSetPiece(6916), new StorableSetPiece(6924), new StorableSetPiece(6922), new StorableSetPiece(6920)),
	SPLITBARK_ROBES(10606, new StorableSetPiece(3385), new StorableSetPiece(3387), new StorableSetPiece(3389), new StorableSetPiece(3391), new StorableSetPiece(3393)),
	GHOSTLY_ROBES(10607, new StorableSetPiece(6109), new StorableSetPiece(6107), new StorableSetPiece(6108), new StorableSetPiece(6110), new StorableSetPiece(6106), new StorableSetPiece(6111)),
	MOONCLAN_ROBES(10608, new StorableSetPiece(9068), new StorableSetPiece(9069), new StorableSetPiece(9070), new StorableSetPiece(9071), new StorableSetPiece(9072), new StorableSetPiece(9073), new StorableSetPiece(9074)),
	LUNAR_ROBES(10609, new StorableSetPiece(9096), new StorableSetPiece(9097), new StorableSetPiece(9098), new StorableSetPiece(9099), new StorableSetPiece(9100), new StorableSetPiece(9101), new StorableSetPiece(9102), new StorableSetPiece(9104)),
	GRACEFUL_OUTFIT(11850, new StorableSetPiece(11850), new StorableSetPiece(11854), new StorableSetPiece(11856), new StorableSetPiece(11858), new StorableSetPiece(11860), new StorableSetPiece(11852)),
	GRACEFUL_OUTFIT_ARCEUUS(13579, new StorableSetPiece(13579), new StorableSetPiece(13583), new StorableSetPiece(13585), new StorableSetPiece(13587), new StorableSetPiece(13589), new StorableSetPiece(13581)),
	GRACEFUL_OUTFIT_HOSIDIUS(13627, new StorableSetPiece(13627), new StorableSetPiece(13631), new StorableSetPiece(13633), new StorableSetPiece(13635), new StorableSetPiece(13637), new StorableSetPiece(13629)),
	GRACEFUL_OUTFIT_LOVAKENGJI(13603, new StorableSetPiece(13603), new StorableSetPiece(13607), new StorableSetPiece(13609), new StorableSetPiece(13611), new StorableSetPiece(13613), new StorableSetPiece(13605)),
	GRACEFUL_OUTFIT_PISCARILIUS(13591, new StorableSetPiece(13591), new StorableSetPiece(13595), new StorableSetPiece(13597), new StorableSetPiece(13599), new StorableSetPiece(13601), new StorableSetPiece(13593)),
	GRACEFUL_OUTFIT_SHAYZIEN(13615, new StorableSetPiece(13615), new StorableSetPiece(13619), new StorableSetPiece(13621), new StorableSetPiece(13623), new StorableSetPiece(13625), new StorableSetPiece(13617)),
	GRACEFUL_OUTFIT_KOUREND(13667, new StorableSetPiece(13667), new StorableSetPiece(13671), new StorableSetPiece(13673), new StorableSetPiece(13675), new StorableSetPiece(13677), new StorableSetPiece(13669)),
	GRACEFUL_OUTFIT_AGILITY_ARENA(21061, new StorableSetPiece(21061), new StorableSetPiece(21067), new StorableSetPiece(21070), new StorableSetPiece(21073), new StorableSetPiece(21076), new StorableSetPiece(21064)),
	BLACK_NAVAL_ATTIRE(8963, new StorableSetPiece(8963), new StorableSetPiece(8956), new StorableSetPiece(8995)),
	BLUE_NAVAL_ATTIRE(8959, new StorableSetPiece(8959), new StorableSetPiece(8952), new StorableSetPiece(8991)),
	BROWN_NAVAL_ATTIRE(8962, new StorableSetPiece(8962), new StorableSetPiece(8955), new StorableSetPiece(8994)),
	GREEN_NAVAL_ATTIRE(8960, new StorableSetPiece(8960), new StorableSetPiece(8953), new StorableSetPiece(8992)),
	GREY_NAVAL_ATTIRE(8965, new StorableSetPiece(8965), new StorableSetPiece(8958), new StorableSetPiece(8997)),
	PURPLE_NAVAL_ATTIRE(8964, new StorableSetPiece(8964), new StorableSetPiece(8957), new StorableSetPiece(8996)),
	RED_NAVAL_ATTIRE(8961, new StorableSetPiece(8961), new StorableSetPiece(8954), new StorableSetPiece(8993)),
	ELDER_CHAOS_DRUID_ROBES(20595, new StorableSetPiece(20595), new StorableSetPiece(20517), new StorableSetPiece(20520)),
	EVIL_CHICKEN_COSTUME(20439, new StorableSetPiece(20439), new StorableSetPiece(20436), new StorableSetPiece(20442), new StorableSetPiece(20433)),
    PYROMANCER_OUTFIT(RewardCrate.PYROMANCER_HOOD, new StorableSetPiece(RewardCrate.PYROMANCER_HOOD), new StorableSetPiece(RewardCrate.PYROMANCER_GARB), new StorableSetPiece(RewardCrate.PYROMANCER_ROBE), new StorableSetPiece(RewardCrate.PYROMANCER_BOOTS));
    
    MagicWardrobeData(final int displayItem, final StorableSetPiece... pieces) {
		this.displayItem = displayItem;
		this.pieces = pieces;
	}
	
	public static final MagicWardrobeData[] VALUES = values();
	public static final ItemContainer CONTAINER = new ItemContainer(7, false);
	
	public static final Map<Integer, MagicWardrobeData> MAP = new HashMap<Integer, MagicWardrobeData>(VALUES.length * 5);
	public static final Map<Integer, MagicWardrobeData> DISPLAY_MAP = new HashMap<Integer, MagicWardrobeData>(VALUES.length);

    static {
        for (MagicWardrobeData val : VALUES) {
            CONTAINER.add(new Item(val.displayItem));
            for (StorableSetPiece p : val.pieces)
                for (int i : p.getIds())
                    MAP.put(i, val);
            DISPLAY_MAP.put(val.getDisplayItem(), val);
        }
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
