package com.zenyte.game.content.boss.nightmare;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NpcId;

public class NightmareTotemNE extends NightmareTotem {

	public static final int REGULAR = NpcId.TOTEM_9443;
	public static final int CHARGE = NpcId.TOTEM_9444;
	public static final int FULL_CHARGE = NpcId.TOTEM_9445;
	public static final Location SPAWN_LOCATION = new Location(3879, 9958, 3);

	public NightmareTotemNE(BaseNightmareNPC boss, Location location) {
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
		return "The north east totem is fully charged.";
	}

}
