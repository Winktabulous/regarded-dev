package com.zenyte.game.content;

import com.google.common.eventbus.Subscribe;
import com.zenyte.game.content.skills.magic.spells.teleports.Teleport;
import com.zenyte.game.content.skills.magic.spells.teleports.TeleportType;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.GameCommands;
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege;
import com.zenyte.plugins.events.ServerLaunchEvent;

public enum WildernessTeleportCommand {
	CORP("corp", create(new Location(2967, 4383, 2))),
	FORTY_FOUR("44s", create(new Location(2976, 3867))),
	FIFTY("50s", create(new Location(3285, 3919))),
	WESTS("wests", create(new Location(2979, 3595))),
	EASTS("easts", create(new Location(3346, 3666))),
	MB("mb", create(new Location(2539, 4716))),
	REVS("revs", create(new Location(3079, 3653))),
	FEROX("ferox", create(new Location(3150, 3636))),
	GDZ("gdz", create(new Location(3288, 3886))),
	GRAVES("graves", create(new Location(3149, 3670))),
    MAGE_BANK("mb", create(new Location(2537, 4715))),
	SEVENTEEN("17s", create(new Location(3188, 3653))),
	ELDER("elders", create(new Location(3236, 3640))),
	//SLAYER_CAVE("sc", create(new Location(3291, 3759))),
	CHINS("chins", create(new Location(3144, 3770)));


	WildernessTeleportCommand(String command, Teleport teleport) {
		new GameCommands.Command(PlayerPrivilege.PLAYER, command,  (p, args) -> {
			if (p.isLocked()) {
				return;
			}
			teleport.teleport(p);
		});
	}

	@Subscribe
	public static void on(ServerLaunchEvent event) {
	}

	private static Teleport create(Location location) {
		return new Teleport() {
			@Override
			public TeleportType getType() {
				return TeleportType.NEAR_REALITY_PORTAL_TELEPORT;
			}

			@Override
			public Location getDestination() {
				return location;
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
				return true;
			}
		};
	}
}
