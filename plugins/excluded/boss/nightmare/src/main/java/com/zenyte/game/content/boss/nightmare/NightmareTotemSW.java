package com.zenyte.game.content.boss.nightmare;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NpcId;

public class NightmareTotemSW extends NightmareTotem {

	public static final int REGULAR = NpcId.TOTEM;
	public static final int CHARGE = NpcId.TOTEM_9435;
	public static final int FULL_CHARGE = NpcId.TOTEM_9436;
	public static final Location SPAWN_LOCATION = new Location(3863, 9942, 3);

	public NightmareTotemSW(BaseNightmareNPC boss, Location location) {
		super(REGULAR, location, boss);
	}

	@Override
	public int chargedId() {
		return FULL_CHARGE;
	}

	@Override
	public int activeId() {
		return CHARGE;
	}

	@Override
	public String chargedMessage() {
		return "The south west totem is fully charged.";
	}

}
