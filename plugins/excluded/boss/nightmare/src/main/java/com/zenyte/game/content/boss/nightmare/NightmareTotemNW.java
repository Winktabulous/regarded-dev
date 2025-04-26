package com.zenyte.game.content.boss.nightmare;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NpcId;

public class NightmareTotemNW extends NightmareTotem {

	public static final int REGULAR = NpcId.TOTEM_9440;
	public static final int CHARGE = NpcId.TOTEM_9441;
	public static final int FULL_CHARGE = NpcId.TOTEM_9442;
	public static final Location SPAWN_LOCATION = new Location(3863, 9958, 3);

	public NightmareTotemNW(BaseNightmareNPC boss, Location location) {
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
		return "The north west totem is fully charged.";
	}

}
