package com.zenyte.game.world.region.area;

import com.zenyte.game.model.item.SkillcapePerk;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.RandomLocation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.privilege.MemberRank;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.CannonRestrictionPlugin;
import com.zenyte.game.world.region.area.plugins.CycleProcessPlugin;

/**
 * @author Kris | 27. mai 2018 : 18:26:48
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class WarriorsGuildCyclopsArea extends DeathPlateau implements CannonRestrictionPlugin, CycleProcessPlugin {
	private static final Location OUTSIDE = new Location(2843, 3539, 2);
	private int cycle = 100;

	@Override
	public RSPolygon[] polygons() {
		return new RSPolygon[] {new RSPolygon(new int[][] {{2838, 3556}, {2838, 3543}, {2847, 3543}, {2847, 3538}, {2848, 3538}, {2848, 3537}, {2849, 3537}, {2849, 3534}, {2860, 3534}, {2860, 3537}, {2865, 3537}, {2866, 3536}, {2867, 3536}, {2868, 3537}, {2871, 3537}, {2872, 3536}, {2873, 3536}, {2874, 3537}, {2876, 3537}, {2876, 3539}, {2877, 3540}, {2877, 3541}, {2876, 3542}, {2876, 3545}, {2877, 3546}, {2877, 3547}, {2876, 3548}, {2876, 3551}, {2877, 3552}, {2877, 3553}, {2876, 3554}, {2876, 3556}, {2874, 3556}, {2873, 3557}, {2872, 3557}, {2871, 3556}, {2868, 3556}, {2867, 3557}, {2866, 3557}, {2865, 3556}, {2862, 3556}, {2861, 3557}, {2860, 3557}, {2859, 3556}, {2856, 3556}, {2855, 3557}, {2854, 3557}, {2853, 3556}}, 2)};
	}

	@Override
	public void enter(final Player player) {
	}

	@Override
	public void leave(final Player player, boolean logout) {
	}

	@Override
	public String name() {
		return "Warriors' guild top floor Cyclops' area";
	}

	@Override
	public void process() {
		if (--cycle == 0) {
			cycle = 100;
			for (final Player player : players) {
				if (SkillcapePerk.ATTACK.isEffective(player) || player.isMember() && Utils.random(getChance(player)) == 0) continue;
				//Above item deletion; Allows for one round after last tokens are removed.
				if (player.getInventory().getAmountOf(8851) < 10) {
					player.setLocation(RandomLocation.create(OUTSIDE,  1));
				}
				player.getInventory().deleteItem(8851, 10);
				player.sendMessage("10 of your tokens crumble away.");
			}
		}
	}

	public static int getChance(final Player player) {
		final MemberRank memberRank = player.getMemberRank();
		if (memberRank.equalToOrGreaterThan(MemberRank.MYTHICAL)) {
			return 25;
		} else if (memberRank.equalToOrGreaterThan(MemberRank.LEGENDARY)) {
			return 20;
		} else if (memberRank.equalToOrGreaterThan(MemberRank.EXTREME)) {
			return 15;
		}
		return 9;
	}
}
