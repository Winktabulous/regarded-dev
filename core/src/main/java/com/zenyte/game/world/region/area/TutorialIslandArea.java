package com.zenyte.game.world.region.area;

import com.zenyte.game.content.skills.magic.spells.teleports.Teleport;
import com.zenyte.game.model.HintArrow;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.RandomEventRestrictionPlugin;
import com.zenyte.game.world.region.area.plugins.TeleportPlugin;

/**
 * @author Tommeh | 28 aug. 2018 | 16:33:27
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class TutorialIslandArea extends PolygonRegionArea implements TeleportPlugin, RandomEventRestrictionPlugin {
	public static final RSPolygon polygon = new RSPolygon(new int[][] {{3103, 3140}, {3104, 3142}, {3086, 3154}, {3055, 3135}, {3040, 3101}, {3052, 3074}, {3042, 3055}, {3049, 3048}, {3073, 3040}, {3110, 3040}, {3132, 3044}, {3137, 3051}, {3136, 3068}, {3166, 3070}, {3171, 3102}, {3164, 3117}, {3164, 3129}, {3136, 3138}, {3132, 3144}});

	@Override
	public RSPolygon[] polygons() {
		return new RSPolygon[] {polygon};
	}

	@Override
	public void enter(final Player player) {
		World.findNPC(9005, player.getLocation(), 30)
				.ifPresent(npc -> player.getPacketDispatcher().sendHintArrow(new HintArrow(npc)));
	}

	@Override
	public void leave(final Player player, boolean logout) {
	}

	@Override
	public String name() {
		return "Tutorial Island";
	}

	@Override
	public boolean canTeleport(final Player player, final Teleport teleport) {
		return false;
	}
}
