package com.zenyte.plugins.dialogue;

import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.area.wilderness.WildernessArea;

import java.util.Arrays;
import java.util.OptionalInt;

/**
 * @author Tommeh | 15 jan. 2018 : 16:41:11
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public final class SetObeliskDestinationD extends OptionsMenuD {
	public SetObeliskDestinationD(Player player, WorldObject object) {
		super(player, "Which Obelisk would you like to teleport to.", getSortedTeleportOptions(object));
	}

	public void handleClick(int slotId) {
		final int index = getIndex(getOptions()[slotId].substring(6, 8));
		player.getTemporaryAttributes().put("ObeliskDestinationIndex", index);
		player.getInterfaceHandler().closeInterface(InterfacePosition.CENTRAL);
	}

	private int getIndex(String string) {
		switch (string) {
		case "13": 
			return 2;
		case "19": 
			return 1;
		case "27": 
			return 4;
		case "35": 
			return 3;
		case "44": 
			return 5;
		case "50": 
			return 0;
		}
		return -1;
	}

	private static final String[] getSortedTeleportOptions(WorldObject object) {
		final String[] options = getTeleportOptions(object);
		if (options == null) {
			return null;
		}
		Arrays.sort(options);
		return options;
	}

	private static String[] getTeleportOptions(WorldObject object) {
		final OptionalInt level = WildernessArea.getWildernessLevel(object);
		if (!level.isPresent()) {
			return null;
		}
		switch (level.getAsInt()) {
		case 13: 
			return new String[] {"Level 44 Wilderness", "Level 27 Wilderness", "Level 35 Wilderness", "Level 19 Wilderness", "Level 50 Wilderness"};
		case 19: 
			return new String[] {"Level 44 Wilderness", "Level 27 Wilderness", "Level 35 Wilderness", "Level 13 Wilderness", "Level 50 Wilderness"};
		case 27: 
			return new String[] {"Level 44 Wilderness", "Level 35 Wilderness", "Level 13 Wilderness", "Level 19 Wilderness", "Level 50 Wilderness"};
		case 35: 
			return new String[] {"Level 44 Wilderness", "Level 27 Wilderness", "Level 13 Wilderness", "Level 19 Wilderness", "Level 50 Wilderness"};
		case 44: 
			return new String[] {"Level 27 Wilderness", "Level 35 Wilderness", "Level 13 Wilderness", "Level 19 Wilderness", "Level 50 Wilderness"};
		case 50: 
			return new String[] {"Level 44 Wilderness", "Level 27 Wilderness", "Level 35 Wilderness", "Level 13 Wilderness", "Level 19 Wilderness"};
		default: 
			return null;
		}
	}

	@Override
	public boolean cancelOption() {
		return false;
	}
}
