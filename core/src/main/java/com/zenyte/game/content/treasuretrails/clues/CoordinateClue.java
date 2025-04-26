package com.zenyte.game.content.treasuretrails.clues;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.treasuretrails.ClueLevel;
import com.zenyte.game.content.treasuretrails.TreasureGuardianNPC;
import com.zenyte.game.content.treasuretrails.TreasureTrailType;
import com.zenyte.game.content.treasuretrails.challenges.ClueChallenge;
import com.zenyte.game.content.treasuretrails.challenges.DigRequest;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import static com.zenyte.game.content.treasuretrails.ClueLevel.*;
import static com.zenyte.game.content.treasuretrails.TreasureGuardianNPC.*;

/**
 * @author Kris | 29. march 2018 : 21:12.37
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum CoordinateClue implements Clue {
	//C00_00N_07_13W(HARD, SARADOMIN_WIZARD),
	C00_05S_01_13E(MEDIUM), C00_13S_13_58E(MEDIUM), C00_18S_02_35W(ELITE, GUARDIAN), C00_18S_09_28E(MEDIUM), C00_20S_23_15E(MEDIUM), C00_30N_24_16E(MEDIUM), C00_31S_17_43E(MEDIUM), 
	//C00_35N_35_50E(ELITE, GUARDIAN),
	C01_18S_14_15E(MEDIUM), 
	//C01_24N_08_05W(HARD, SARADOMIN_WIZARD),
	//C01_26N_08_01E(MEDIUM),
	C01_30N_20_01E(HARD, SARADOMIN_WIZARD), C01_35S_07_28E(MEDIUM), C01_54S_08_54W(MASTER, BRASSICAN_MAGE), C02_35S_11_52E(ELITE, GUARDIAN), C02_43S_33_26E(MEDIUM), 
	//C02_46N_29_11E(HARD),//Available but inside duel zones so let's leave it out entirely for now.
	C02_48N_08_05E(HARD, SARADOMIN_WIZARD), C02_48N_22_30E(MEDIUM), C02_48N_34_33E(HARD, SARADOMIN_WIZARD), C02_50N_06_20E(MEDIUM), C02_58N_34_30E(HARD, SARADOMIN_WIZARD), C03_07S_03_41W(MEDIUM), C03_09S_42_50E(ELITE, GUARDIAN), C03_09S_43_26E(MASTER, BRASSICAN_MAGE), 
	//C03_26N_12_18E(MASTER, BRASSICAN_MAGE),
	C03_35S_13_35E(MEDIUM), C03_45S_22_45E(HARD, SARADOMIN_WIZARD), 
	//C03_46N_08_07W(ELITE, GUARDIAN),
	C03_50N_09_07E(MASTER, BRASSICAN_MAGE), C04_00S_12_46E(MEDIUM), 
	//C04_03S_03_11E(HARD, SARADOMIN_WIZARD),
	//C04_05S_04_24E(HARD, SARADOMIN_WIZARD),
	//C04_13N_12_45E(MEDIUM),
	C04_16S_16_16E(HARD, SARADOMIN_WIZARD), 
	//C04_41N_03_09W(HARD, SARADOMIN_WIZARD),
	//C04_58N_36_56E(MASTER, BRASSICAN_MAGE),
	C05_07S_13_26E(ELITE, GUARDIAN), 
	//C05_13N_04_16W(MASTER, BRASSICAN_MAGE),
	C05_20S_04_28E(MEDIUM), C05_24S_26_56E(ELITE, GUARDIAN), 
	//C05_37N_31_15E(HARD, SARADOMIN_WIZARD),
	C05_39S_02_13E(ELITE, GUARDIAN), C05_43N_23_05E(MEDIUM), C05_50S_10_05E(HARD, SARADOMIN_WIZARD), C06_00S_21_48E(HARD, SARADOMIN_WIZARD), C06_11S_15_07E(HARD, SARADOMIN_WIZARD), C06_31N_01_46W(MEDIUM), C06_35N_09_07E(ELITE, GUARDIAN), C06_41N_27_15E(MEDIUM), C06_58N_21_16E(MEDIUM), C07_05N_30_56E(MEDIUM), C07_33N_15_00E(MEDIUM), C07_37N_35_18E(MASTER, BRASSICAN_MAGE), 
	//C07_43S_12_26E(HARD, SARADOMIN_WIZARD),
	C08_03N_31_16E(HARD, SARADOMIN_WIZARD), 
	//C08_05S_15_56E(HARD, SARADOMIN_WIZARD),
	C08_11S_04_48E(MEDIUM), C08_11N_12_30E(MASTER, BRASSICAN_MAGE), C08_15N_35_24E(ELITE, GUARDIAN), 
	//C08_26S_10_28E(HARD, SARADOMIN_WIZARD),
	C08_33N_01_39W(MEDIUM), 
	//C09_33N_02_15E(MEDIUM),
	C09_35N_01_50W(MEDIUM), C09_46S_43_22E(ELITE, GUARDIAN), C09_48N_17_39E(MEDIUM), C10_05S_24_31E(ELITE, GUARDIAN), C10_45N_04_31E(MEDIUM), C10_54N_20_50W(ELITE, GUARDIAN), C11_03N_31_20E(MEDIUM), C11_05N_00_45W(MEDIUM), C11_18N_30_54E(MEDIUM), C11_33N_02_24W(MEDIUM), C11_41N_14_58E(MEDIUM), C12_28N_34_37E(MEDIUM), 
	//C12_31N_43_11E(ELITE, GUARDIAN),
	//C12_35N_36_20E(ELITE, GUARDIAN),
	//C12_35N_36_22E(MASTER, BRASSICAN_MAGE),
	C12_45N_20_09E(MASTER, BRASSICAN_MAGE), C13_28N_29_43E(MEDIUM), 
	//C13_33S_15_26E(MASTER, BRASSICAN_MAGE),
	//C13_45S_15_30E(ELITE),
	C13_46N_21_01E(HARD, ZAMORAK_WIZARD), C14_15S_08_01E(ELITE, GUARDIAN), C14_20N_30_45W(MEDIUM), C14_54N_09_13E(MEDIUM), C15_22N_07_31E(MEDIUM), C16_03N_14_07E(HARD, SARADOMIN_WIZARD), C16_07N_22_45E(HARD, ZAMORAK_WIZARD), C16_09N_10_33E(ELITE, GUARDIAN), C16_31N_12_54E(HARD, SARADOMIN_WIZARD), C16_35N_27_01E(HARD, ZAMORAK_WIZARD), C16_41N_30_54W(MASTER, ANCIENT_WIZARDS), C16_43N_19_13E(HARD, ZAMORAK_WIZARD), C16_43N_26_56E(HARD, ZAMORAK_WIZARD), C16_43N_30_01W(HARD, SARADOMIN_WIZARD), C17_50N_08_30E(HARD, SARADOMIN_WIZARD), 
	//C18_03N_03_03E(MASTER, BRASSICAN_MAGE),
	C18_05N_12_05E(ELITE, GUARDIAN), C18_22N_16_33E(HARD, ZAMORAK_WIZARD), C18_50N_20_26E(HARD, ZAMORAK_WIZARD), C19_00N_27_13E(HARD, ZAMORAK_WIZARD), C19_24N_30_37W(HARD, SARADOMIN_WIZARD), 
	//C19_43N_23_11W(MASTER, ANCIENT_WIZARDS),
	C19_43N_25_07E(HARD, ZAMORAK_WIZARD), C19_56N_02_31W(ELITE, GUARDIAN), C20_05N_21_52E(HARD, ZAMORAK_WIZARD), C20_11N_07_41W(ELITE, GUARDIAN), C20_13N_08_07E(ELITE, GUARDIAN), C20_33N_15_48E(HARD, ZAMORAK_WIZARD), C20_35N_15_58E(MASTER, ANCIENT_WIZARDS), C20_45N_07_26W(MASTER, ANCIENT_WIZARDS), C20_45N_41_35E(HARD, SARADOMIN_WIZARD), C21_03N_24_13E(ELITE), C21_24N_17_54E(HARD, ZAMORAK_WIZARD), C21_37N_21_13W(MASTER, BRASSICAN_MAGE), C21_56N_10_56W(MASTER, ANCIENT_WIZARDS), C22_24N_31_11W(MASTER, BRASSICAN_MAGE), 
	//C22_30N_03_01E(MEDIUM),
	C22_35N_19_18E(HARD, ZAMORAK_WIZARD), C22_45N_26_33E(HARD, ZAMORAK_WIZARD), C22_54N_29_01E(ELITE, GUARDIAN), 
	//C23_00N_41_33E(HARD, SARADOMIN_WIZARD),
	C23_03N_02_01E(HARD, SARADOMIN_WIZARD), C23_48N_11_43W(ELITE, GUARDIAN), C24_07N_23_22E(ELITE, GUARDIAN), C24_18N_23_22E(MASTER, BRASSICAN_MAGE), C24_22N_27_00E(MASTER, ANCIENT_WIZARDS), C24_24N_26_24E(HARD, ZAMORAK_WIZARD), C24_45N_17_24E(ELITE), C24_56N_22_28E(HARD, ZAMORAK_WIZARD), C24_58N_18_43E(HARD, ZAMORAK_WIZARD), C25_00N_17_18E(MASTER, BRASSICAN_MAGE), C25_03N_17_05E(HARD, ZAMORAK_WIZARD), C25_03N_23_24E(HARD, ZAMORAK_WIZARD), C25_03N_29_22E(ELITE, GUARDIAN);
	public static final int LONGITUDE_OFFSET = 2440;
	public static final int LATITUDE_OFFSET = 3161;
	public static final float MINUTE_TO_COORDINATE_MULTIPLIER = 1.875F;
	private final ClueLevel level;
	private final TreasureGuardianNPC target;
	private final Location tile;
	private final ClueChallenge challenge;
	private final String formattedText;

	CoordinateClue(final ClueLevel level) {
		this(level, null);
	}

	CoordinateClue(final ClueLevel level, final TreasureGuardianNPC target) {
		this.level = level;
		this.target = target;
		final String coord = toString();
		final char latitudeChar = coord.charAt(6);
		final int latitudeMultiplier = latitudeChar == 'S' ? -1 : 1;
		final int latitudeDegrees = Integer.parseInt(coord.substring(1, 3));
		final int latitudeMinutes = Integer.parseInt(coord.substring(4, 6)) + (latitudeDegrees * 60);
		final char longitudeChar = coord.charAt(13);
		final int longitudeMultiplier = longitudeChar == 'W' ? -1 : 1;
		final int longitudeDegrees = Integer.parseInt(coord.substring(8, 10));
		final int longitudeMinutes = Integer.parseInt(coord.substring(11, 13)) + (longitudeDegrees * 60);
		formattedText = String.format("%02d", latitudeDegrees) + " degrees " + String.format("%02d", Integer.parseInt(coord.substring(4, 6))) + " minutes " + (latitudeChar == 'S' ? "south" : "north") + "<br>" + String.format("%02d", longitudeDegrees) + " degrees " + String.format("%02d", Integer.parseInt(coord.substring(11, 13))) + " minutes " + (longitudeChar == 'W' ? "west" : "east");
		final int x = Math.round(LONGITUDE_OFFSET + (((float) longitudeMinutes / MINUTE_TO_COORDINATE_MULTIPLIER) * longitudeMultiplier));
		final int y = Math.round(LATITUDE_OFFSET + (((float) latitudeMinutes / MINUTE_TO_COORDINATE_MULTIPLIER) * latitudeMultiplier));
		this.tile = new Location(x, y, 0);
		challenge = new DigRequest(tile, target);
	}

	@Override
	public void view(@NotNull Player player, @NotNull Item item) {
		player.getTemporaryAttributes().put("Clue scroll item", item);
		GameInterface.CLUE_SCROLL.open(player);
	}

	@Override
	public TreasureTrailType getType() {
		return TreasureTrailType.COORDINATE;
	}

	@Override
	public String getEnumName() {
		return toString();
	}

	@Override
	public String getText() {
		return formattedText;
	}

	@Override
	public ClueChallenge getChallenge() {
		return challenge;
	}

	@NotNull
	@Override
	public ClueLevel level() {
		return level;
	}

	public ClueLevel getLevel() {
		return level;
	}

	public TreasureGuardianNPC getTarget() {
		return target;
	}

	public Location getTile() {
		return tile;
	}
}
