package com.zenyte.game.world.region.area;

import com.zenyte.game.content.skills.mining.DaeyaltEssence;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.CycleProcessPlugin;

public final class DaeyaltMineArea extends PolygonRegionArea implements CycleProcessPlugin {

	@Override
	public RSPolygon[] polygons() {
		return new RSPolygon[]{new RSPolygon(14744)};
	}


	@Override
	public void enter(final Player player) {
	}

	@Override
	public void leave(final Player player, boolean logout) {
	}

	@Override
	public String name() {
		return "Daeyalt Mine";
	}

	private int cycle = 100;
	private DaeyaltEssence essence = DaeyaltEssence.ONE;

	@Override
	public void process() {
		if (--cycle == 0) {
			cycle = 100;

			for (DaeyaltEssence value : DaeyaltEssence.VALUES) {
				WorldObject obj = World.getObjectWithId(value.getLocation(), DaeyaltEssence.ESSENSE_INACTIVE);
				if(obj != null)
					World.removeObject(obj);

				obj = World.getObjectWithId(value.getLocation(), DaeyaltEssence.ESSENSE);

				if(obj != null)
					World.removeObject(obj);
			}

			int rock = essence.ordinal() + 1;
			if(rock >= DaeyaltEssence.VALUES.length)
				rock = 0;
			for (int i = 0; i < DaeyaltEssence.VALUES.length; i++) {
				DaeyaltEssence value = DaeyaltEssence.VALUES[i];
				WorldObject obj;
				if(i == rock) {
					essence = value;
					obj = new WorldObject(DaeyaltEssence.ESSENSE, 10, value.getFace(), value.getLocation());
				} else
					obj = new WorldObject(DaeyaltEssence.ESSENSE_INACTIVE, 10, value.getFace(), value.getLocation());
				World.spawnObject(obj);

			}

		}
	}

}
