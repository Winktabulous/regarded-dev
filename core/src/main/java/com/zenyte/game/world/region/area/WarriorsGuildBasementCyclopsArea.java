package com.zenyte.game.world.region.area;

import com.zenyte.game.model.item.SkillcapePerk;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.RandomLocation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.privilege.MemberRank;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.CannonRestrictionPlugin;
import com.zenyte.game.world.region.area.plugins.CycleProcessPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 27. mai 2018 : 18:30:20
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class WarriorsGuildBasementCyclopsArea extends PolygonRegionArea implements CannonRestrictionPlugin, CycleProcessPlugin {
	private static final Location OUTSIDE = new Location(2908, 9970, 0);

	@Override
	public RSPolygon[] polygons() {
		return new RSPolygon[] {new RSPolygon(new int[][] {{2905, 9966}, {2905, 9957}, {2941, 9957}, {2941, 9974}, {2912, 9974}, {2912, 9966}})};
	}

	@Override
	public void enter(final Player player) {
	}

	@Override
	public void leave(final Player player, boolean logout) {
	}

	@Override
	public String name() {
		return "Warriors' guild basement";
	}

	private int cycle = 100;

	@Override
	public void process() {
		if (--cycle == 0) {
			cycle = 100;
			for (final Player player : players) {
				if (shouldKeepToken(player)) continue;
				//Above item deletion; Allows for one round after last tokens are removed.
				if (player.getInventory().getAmountOf(8851) < 10) {
					player.setLocation(RandomLocation.create(OUTSIDE,  1));
				}
				player.getInventory().deleteItem(8851, 10);
				player.sendMessage("10 of your tokens crumble away.");
			}
		}
	}

	public static boolean shouldKeepToken(@NotNull final Player player) {
		return SkillcapePerk.ATTACK.isEffective(player) || (player.getMemberRank().equalToOrGreaterThan(MemberRank.EXPANSION) && Utils.random(9) == 0);
	}
}
