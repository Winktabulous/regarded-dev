package com.zenyte.game.content.skills.construction.costume;

import com.zenyte.game.content.magicstorageunit.StorableSetPiece;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.container.ItemContainer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kris | 27. veebr 2018 : 22:26.41
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public enum ArmourData {

	CASTLE_WARS_ARMOUR(10610, new StorableSetPiece(4071, 4506, 4511), new StorableSetPiece(4069, 4504, 4509), new StorableSetPiece(4070, 4505, 4510), new StorableSetPiece(4072, 4507, 4512)),
	VOID_KNIGHT_ARMOUR(10611, new StorableSetPiece(8839), new StorableSetPiece(8840), new StorableSetPiece(8842)),
	ELITE_VOID_KNIGHT_ARMOUR(13072, new StorableSetPiece(13072), new StorableSetPiece(13073), new StorableSetPiece(8842)),
	VOID_MELEE_HELM(11676, new StorableSetPiece(11665)),
	VOID_RANGER_HELM(11675, new StorableSetPiece(11664)),
	VOID_MAGE_HELM(11674, new StorableSetPiece(11663)),
	ROGUE_ARMOUR(10612, new StorableSetPiece(5554), new StorableSetPiece(5553), new StorableSetPiece(5555), new StorableSetPiece(5556), new StorableSetPiece(5557)),
	SPINED_ARMOUR(10614, new StorableSetPiece(6131), new StorableSetPiece(6133), new StorableSetPiece(6135), new StorableSetPiece(6149), new StorableSetPiece(6143)),
	ROCKSHELL_ARMOUR(10613, new StorableSetPiece(6128), new StorableSetPiece(6129), new StorableSetPiece(6130), new StorableSetPiece(6151), new StorableSetPiece(6145)),
	POISON_TRIBAL_MASK(10615, new StorableSetPiece(6335)),
	DISEASE_TRIBAL_MASK(10616, new StorableSetPiece(6337)),
	COMBAT_TRIBAL_MASK(10617, new StorableSetPiece(6339)),
	WHITE_KNIGHT_ARMOUR(10618, new StorableSetPiece(6623), new StorableSetPiece(6617), new StorableSetPiece(6625, 6627), new StorableSetPiece(6629), new StorableSetPiece(6619), new StorableSetPiece(6633)),
	INITIATE_ARMOUR(10619, new StorableSetPiece(5574), new StorableSetPiece(5575), new StorableSetPiece(5576)),
	PROSELYTE_ARMOUR(10620, new StorableSetPiece(9672), new StorableSetPiece(9674), new StorableSetPiece(9676, 9678)),
	MOURNER_GEAR(10621, new StorableSetPiece(1506), new StorableSetPiece(6065), new StorableSetPiece(6067), new StorableSetPiece(6068), new StorableSetPiece(6069), new StorableSetPiece(6070)),
	GRAAHK_HUNTER_GEAR(10624, new StorableSetPiece(10051), new StorableSetPiece(10049), new StorableSetPiece(10047)),
	LARUPIA_HUNTER_GEAR(10623, new StorableSetPiece(10045), new StorableSetPiece(10043), new StorableSetPiece(10041)),
	KYATT_HUNTER_GEAR(10622, new StorableSetPiece(10039), new StorableSetPiece(10037), new StorableSetPiece(10035)),
	POLAR_CAMOUFLAGE_GEAR(10628, new StorableSetPiece(10065), new StorableSetPiece(10067)),
	JUNGLE_CAMOUFLAGE_GEAR(10626, new StorableSetPiece(10057), new StorableSetPiece(10059)),
	WOODLAND_CAMOUFLAGE_GEAR(10625, new StorableSetPiece(10053), new StorableSetPiece(10055)),
	DESERT_CAMOUFLAGE_GEAR(10627, new StorableSetPiece(10061), new StorableSetPiece(10063)),
	BUILDERS_COSTUME(10883, new StorableSetPiece(10862), new StorableSetPiece(10863), new StorableSetPiece(10864), new StorableSetPiece(10865)),
	LUMBERJACK_COSTUME(10945, new StorableSetPiece(10941), new StorableSetPiece(10939), new StorableSetPiece(10940), new StorableSetPiece(10933)),
	BOMBER_JACKER_COSTUME(11135, new StorableSetPiece(9945, 9946), new StorableSetPiece(9944)),
	HAM_ROBES(11274, new StorableSetPiece(4302), new StorableSetPiece(4298), new StorableSetPiece(4300), new StorableSetPiece(4308), new StorableSetPiece(4310), new StorableSetPiece(4304, 4306)),
	PROSPECTOR_KIT(12013, new StorableSetPiece(12013), new StorableSetPiece(12014), new StorableSetPiece(12015), new StorableSetPiece(12016)),
	ANGLERS_OUTFIT(13258, new StorableSetPiece(13258), new StorableSetPiece(13259), new StorableSetPiece(13260), new StorableSetPiece(13261)),
	SHAYZIEN_ARMOUR_T1(13359, new StorableSetPiece(13359), new StorableSetPiece(13361), new StorableSetPiece(13360), new StorableSetPiece(13357), new StorableSetPiece(13358)),
	SHAYZIEN_ARMOUR_T2(13364, new StorableSetPiece(13364), new StorableSetPiece(13366), new StorableSetPiece(13365), new StorableSetPiece(13362), new StorableSetPiece(13363)),
	SHAYZIEN_ARMOUR_T3(13369, new StorableSetPiece(13369), new StorableSetPiece(13371), new StorableSetPiece(13370), new StorableSetPiece(13367), new StorableSetPiece(13368)),
	SHAYZIEN_ARMOUR_T4(13374, new StorableSetPiece(13374), new StorableSetPiece(13376), new StorableSetPiece(13375), new StorableSetPiece(13372), new StorableSetPiece(13373)),
	SHAYZIEN_ARMOUR_T5(13379, new StorableSetPiece(13379), new StorableSetPiece(13381), new StorableSetPiece(13380), new StorableSetPiece(13377), new StorableSetPiece(13378)),
	XERICIAN_ROBES(13385, new StorableSetPiece(13385), new StorableSetPiece(13387), new StorableSetPiece(13389)),
	FARMERS_OUTFIT(13646, new StorableSetPiece(13646, 13647), new StorableSetPiece(13642, 13643), new StorableSetPiece(13640, 13641), new StorableSetPiece(13644, 13645)),
	CLUE_HUNTER_OUTFIT(19689, new StorableSetPiece(19689), new StorableSetPiece(19693), new StorableSetPiece(19691), new StorableSetPiece(19695), new StorableSetPiece(19697)),
	CORRUPTED_ARMOUR(20838, new StorableSetPiece(20838), new StorableSetPiece(20840), new StorableSetPiece(20842, 20844), new StorableSetPiece(20846)),
	ANCESTRAL_ROBES(21018, new StorableSetPiece(21018), new StorableSetPiece(21021), new StorableSetPiece(21024)),
	OBSIDIAN_ARMOUR(21298, new StorableSetPiece(21298), new StorableSetPiece(21301), new StorableSetPiece(21304)),
	HELM_OF_RAEDWALD(19687, new StorableSetPiece(19687)),
	MORE(10165),
	BACK(10166);
	
	ArmourData(final int displayItem, final StorableSetPiece... pieces) {
		this.displayItem = displayItem;
		this.pieces = pieces;
	}
	
	public static final ArmourData[] VALUES = values();
	public static final ItemContainer[] CONTAINERS = new ItemContainer[] {
			new ItemContainer(40, false),
			new ItemContainer(3, false)
	};
	
	public static final Map<Integer, ArmourData> MAP = new HashMap<Integer, ArmourData>(VALUES.length * 5);
	public static final Map<Integer, ArmourData> DISPLAY_MAP = new HashMap<Integer, ArmourData>(VALUES.length);

	static {
		for (int i = 0; i <= ANCESTRAL_ROBES.ordinal(); i++)
			CONTAINERS[0].add(new Item(VALUES[i].displayItem));
		CONTAINERS[0].add(new Item(MORE.displayItem));
		CONTAINERS[1].add(new Item(BACK.displayItem));
		for (int i = OBSIDIAN_ARMOUR.ordinal(); i <= HELM_OF_RAEDWALD.ordinal(); i++)
			CONTAINERS[1].add(new Item(VALUES[i].displayItem));
		
		for (ArmourData val : VALUES) {
			for (StorableSetPiece p : val.pieces) {
				for (int i : p.getIds()) {
					MAP.put(i, val);
				}
			}
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
