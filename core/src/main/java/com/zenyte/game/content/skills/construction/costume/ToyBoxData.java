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
public enum ToyBoxData {

	BUNNY_EARS(10734, new StorableSetPiece(1037)),
	SCYTHE(10735, new StorableSetPiece(1419)),
	WAR_SHIP(795, new StorableSetPiece(795)),
	YO_YO(10733, new StorableSetPiece(4079)),
	RUBBER_CHICKEN(10732, new StorableSetPiece(4566)),
	ZOMBIE_HEAD(10731, new StorableSetPiece(6722)),
	BLUE_MARIONETTE(10730, new StorableSetPiece(6865)),
	GREEN_MARIONETTE(6866, new StorableSetPiece(6866)),
	RED_MARIONETTE(6867, new StorableSetPiece(6867)),
	BOBBLE_HAT(9815, new StorableSetPiece(6856)),
	JESTER_HAT(6858, new StorableSetPiece(6858)),
	TRI_JESTER_HAT(6860, new StorableSetPiece(6860)),
	WOOLLY_HAT(6862, new StorableSetPiece(6862)),
	BOBBLE_SCARF(9816, new StorableSetPiece(6857)),
	JESTER_SCARF(6859, new StorableSetPiece(6859)),
	TRI_JESTER_SCARF(6861, new StorableSetPiece(6861)),
	WOOLLY_SCARF(6863, new StorableSetPiece(6863)),
	EASTER_RING(10729, new StorableSetPiece(7927)),
	JACK_LANTERN_MASK(10723, new StorableSetPiece(9920)),
	SKELETON_OUTFIT(10728, new StorableSetPiece(9925), new StorableSetPiece(9924), new StorableSetPiece(9923), new StorableSetPiece(10725), new StorableSetPiece(10724)),
	REINDEER_HAT(10722, new StorableSetPiece(10507)),
	CHICKEN_OUTFIT(11015, new StorableSetPiece(11021), new StorableSetPiece(11020), new StorableSetPiece(11022), new StorableSetPiece(11019)),
	BLACK_HWEEN_MASK(11847, new StorableSetPiece(11847)),
	BLACK_PARTYHAT(11862, new StorableSetPiece(11862)),
	RAINBOW_PARTYHAT(11863, new StorableSetPiece(11863)),
	COW_OUTFIT(11919, new StorableSetPiece(11919), new StorableSetPiece(12956), new StorableSetPiece(11957), new StorableSetPiece(11958), new StorableSetPiece(11959)),
	EASTER_BASKET(4565, new StorableSetPiece(4565)),
	DRUIDIC_WREATH(12600, new StorableSetPiece(12600)),
	GRIM_REAPER_HOOD(12845, new StorableSetPiece(12845)),
	SANTA_OUTFIT(12887, new StorableSetPiece(12887), new StorableSetPiece(12888), new StorableSetPiece(12889), new StorableSetPiece(12890), new StorableSetPiece(12891)),
	ANTISANTA_OUTFIT(12892, new StorableSetPiece(12892), new StorableSetPiece(12893), new StorableSetPiece(12894), new StorableSetPiece(12895), new StorableSetPiece(12896)),
	BUNNY_FEET(13182, new StorableSetPiece(13182)),
	MASK_OF_BALANCE(13203, new StorableSetPiece(13203)),
	TIGER_TOY(13215, new StorableSetPiece(13215)),
	LION_TOY(13216, new StorableSetPiece(13216)),
	SNOW_LEOPARD_TOY(13217, new StorableSetPiece(13217)),
	AMUR_LEOPARD_TOY(13218, new StorableSetPiece(13218)),
	ANTI_PANTIES(13288, new StorableSetPiece(13288)),
	GRAVEDIGGER_OUTFIT(13283, new StorableSetPiece(13283), new StorableSetPiece(13284), new StorableSetPiece(13285), new StorableSetPiece(13286), new StorableSetPiece(13287)),
	
	BLACK_SANTA_HAT(13343, new StorableSetPiece(13343)),
	INVERTED_SANTA_HAT(13344, new StorableSetPiece(13344)),
	GNOME_CHILD_HAT(13655, new StorableSetPiece(13655)),
	BUNNY_TOP(13663, new StorableSetPiece(13663)),
	BUNNY_LEGS(13664, new StorableSetPiece(13664)),
	BUNNY_PAWS(13665, new StorableSetPiece(13665)),
	HORNWOOD_HELM(19699, new StorableSetPiece(19699)),
	BANSHEE_OUTFIT(20773, new StorableSetPiece(20773), new StorableSetPiece(20775), new StorableSetPiece(20777)),
	HUNTING_KNIFE(20779, new StorableSetPiece(20779)),
	SNOW_GLOBE(20832, new StorableSetPiece(20832)),
	SACK_OF_PRESENTS(20834, new StorableSetPiece(20834)),
	GIANT_PRESENT(20836, new StorableSetPiece(20836)),
	FOURTH_BIRTHDAY_HAT(21211, new StorableSetPiece(21211)),
	BIRTHDAY_BALLOONS(21209, new StorableSetPiece(21209)),
	EASTER_EGG_HELM(21214, new StorableSetPiece(21214)),
	RAINBOW_SCARF(21314, new StorableSetPiece(21314)),
	HAND_FAN(21354, new StorableSetPiece(21354)),
	RUNEFEST_SHIELD(21695, new StorableSetPiece(21695)),
	JONAS_MASK(21720, new StorableSetPiece(21720)),
	SNOW_IMP_COSTUME(21847, new StorableSetPiece(21847), new StorableSetPiece(21749), new StorableSetPiece(21851), new StorableSetPiece(21855), new StorableSetPiece(21857), new StorableSetPiece(21853)),
	WISE_OLD_MANS_SANTA_HAT(21859, new StorableSetPiece(21859)),
	MORE(10165),
	BACK(10166);
	
	ToyBoxData(final int displayItem, final StorableSetPiece... pieces) {
		this.displayItem = displayItem;
		this.pieces = pieces;
	}
	
	public static final ToyBoxData[] VALUES = values();
	public static final ItemContainer[] CONTAINERS = new ItemContainer[] {
			new ItemContainer(40, false),
			new ItemContainer(23, false),
	};
	
	public static final Map<Integer, ToyBoxData> MAP = new HashMap<Integer, ToyBoxData>(VALUES.length * 5);
	public static final Map<Integer, ToyBoxData> DISPLAY_MAP = new HashMap<Integer, ToyBoxData>(VALUES.length);

	static {
		for (int i = 0; i <= GRAVEDIGGER_OUTFIT.ordinal(); i++)
			CONTAINERS[0].add(new Item(VALUES[i].displayItem));
		CONTAINERS[0].add(new Item(MORE.displayItem));
		CONTAINERS[1].add(new Item(BACK.displayItem));
        for (int i = BLACK_SANTA_HAT.ordinal(); i <= WISE_OLD_MANS_SANTA_HAT.ordinal(); i++)
            CONTAINERS[1].add(new Item(VALUES[i].displayItem));
        for (ToyBoxData val : VALUES) {
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
