package com.zenyte.game.world.region.area;

import com.near_reality.game.content.commands.DeveloperCommands;
import com.zenyte.game.content.skills.afk.AfkSkilling;
import com.zenyte.game.content.skills.afk.AfkSkillingConstants;
import com.zenyte.game.model.item.SkillcapePerk;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.RandomLocation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.privilege.MemberRank;
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.CannonRestrictionPlugin;
import com.zenyte.game.world.region.area.plugins.CycleProcessPlugin;
import com.zenyte.game.world.region.area.plugins.LoginPlugin;
import com.zenyte.game.world.region.area.plugins.LogoutPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public final class AfkArea extends EdgevilleArea implements CannonRestrictionPlugin, CycleProcessPlugin, LoginPlugin {

	public static final ArrayList<String> IP_ADDRESSES_IN_AREA = new ArrayList<>();
	private static final Location OUTSIDE = new Location(3105, 3484, 0);

	private static final int MAX_IPS_ALLOWED = 3;

	@Override
	public RSPolygon[] polygons() {
		return new RSPolygon[] {new RSPolygon(new int[][] {
				{ 3096, 3484 },
				{ 3102, 3484 },
				{ 3103, 3483 },
				{ 3106, 3483 },
				{ 3107, 3484 },
				{ 3114, 3484 },
				{ 3114, 3467 },
				{ 3096, 3467 }})};
	}

	@Override
	public void enter(final Player player) {
		if(DeveloperCommands.INSTANCE.getIpBasedDetections()) {
			if (!canBypass(player) && IP_ADDRESSES_IN_AREA.stream().filter(it -> it.equalsIgnoreCase(player.getIP())).count() > MAX_IPS_ALLOWED) {
				player.sendMessage("You are only allowed to have three accounts in this area at a time.");
				player.setLocation(OUTSIDE);
				player.stopAll();
			}
			IP_ADDRESSES_IN_AREA.add(player.getIP());
		}
		player.getAttributes().put(AfkSkillingConstants.AFK_TICKS, 0);
	}

	@Override
	public void leave(final Player player, boolean logout) {
		if(DeveloperCommands.INSTANCE.getIpBasedDetections())
			IP_ADDRESSES_IN_AREA.remove(player.getIP());
		player.getAttributes().put(AfkSkillingConstants.AFK_TICKS, 0);
	}

	@Override
	public String name() {
		return "Afk skilling area";
	}

	private int cycle = 100;

	@Override
	public void process() {
		if (--cycle == 0) {
			cycle = 100;
			for (final Player player : players) {
				AfkSkilling.pushAfkAreaTicks(player);
				if(AfkSkilling.overTimeLimit(player)) {
					player.sendMessage("You've ran out of time in the AFK guild. You can re-enter whenever you'd like.");
					player.setLocation(OUTSIDE);
					player.stopAll();
				}
			}
		}
	}

	@Override
	public void login(Player player) {
		if(!AfkSkilling.hasAfkTime(player))
			player.setLocation(OUTSIDE);
		if(DeveloperCommands.INSTANCE.getIpBasedDetections()) {
			if (!canBypass(player) && IP_ADDRESSES_IN_AREA.stream().filter(it -> it.equalsIgnoreCase(player.getIP())).count() > MAX_IPS_ALLOWED) {
				player.sendMessage("You are only allowed to have three accounts in this area at a time.");
				player.setLocation(OUTSIDE);
				player.stopAll();
			}
		}
	}

	private boolean canBypass(Player player) {
		return player.getPrivilege().eligibleTo(PlayerPrivilege.SUPPORT);
	}

}
