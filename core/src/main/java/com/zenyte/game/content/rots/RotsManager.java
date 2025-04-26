package com.zenyte.game.content.rots;

import com.zenyte.game.content.skills.magic.spells.teleports.Teleport;
import com.zenyte.game.content.skills.magic.spells.teleports.TeleportType;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;
import com.zenyte.game.world.region.dynamicregion.MapBuilder;
import com.zenyte.game.world.region.dynamicregion.OutOfSpaceException;

public class RotsManager {

	public static void createInstance(Player player) {
		try {
			final AllocatedArea area = MapBuilder.findEmptyChunk(64, 64);
			final RotsInstance instance = new RotsInstance(area, player);
			instance.constructRegion();
			final Teleport teleport = new Teleport() {
				@Override
				public TeleportType getType() {
					return TeleportType.NEAR_REALITY_PORTAL_TELEPORT;
				}

				@Override
				public Location getDestination() {
					return instance.getBaseLocation(31, 4);
				}

				@Override
				public int getLevel() {
					return 0;
				}

				@Override
				public double getExperience() {
					return 0;
				}

				@Override
				public int getRandomizationDistance() {
					return 3;
				}

				@Override
				public Item[] getRunes() {
					return null;
				}

				@Override
				public int getWildernessLevel() {
					return 20;
				}

				@Override
				public boolean isCombatRestricted() {
					return false;
				}
			};
			teleport.teleport(player);
		} catch (OutOfSpaceException e) {
			e.printStackTrace();
		}
	}

}
