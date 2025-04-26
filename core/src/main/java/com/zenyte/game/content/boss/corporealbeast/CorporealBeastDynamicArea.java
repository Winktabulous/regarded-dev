package com.zenyte.game.content.boss.corporealbeast;

import com.google.common.eventbus.Subscribe;
import com.zenyte.game.content.clans.ClanChannel;
import com.zenyte.game.content.multicannon.DwarfMultiCannon;
import com.zenyte.game.content.multicannon.Multicannon;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.world.Position;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.DynamicArea;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.LogoutPlugin;
import com.zenyte.game.world.region.area.plugins.LootBroadcastPlugin;
import com.zenyte.game.world.region.area.plugins.PartialMovementPlugin;
import com.zenyte.game.world.region.area.plugins.RandomEventRestrictionPlugin;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;
import com.zenyte.plugins.events.CannonRemoveEvent;
import com.zenyte.plugins.object.CorporealBeastPrivatePortalOA;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Kris | 19. juuni 2018 : 17:29:06
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class CorporealBeastDynamicArea extends DynamicArea implements PartialMovementPlugin, RandomEventRestrictionPlugin, LootBroadcastPlugin {
	private static final Map<String, CorporealBeastDynamicArea> instances = new HashMap<>();

	public static CorporealBeastDynamicArea getArea(final Player player) {
		if (player == null) {
			return null;
		}
		final ClanChannel channel = player.getSettings().getChannel();
		if (channel == null) {
			return null;
		}
		return instances.get(channel.getOwner());
	}
	public static CorporealBeastDynamicArea getArea(String owner) {
		return instances.get(owner);
	}

	public CorporealBeastDynamicArea(final ClanChannel channel, final AllocatedArea area) {
		super(area, 369, 545);
		this.channel = channel;
		innerArea = getPolygon(new int[][] {{2974, 4390}, {2974, 4381}, {2975, 4380}, {2975, 4379}, {2977, 4379}, {2978, 4378}, {2979, 4378}, {2980, 4377}, {2980, 4375}, {2981, 4374}, {2981, 4373}, {2982, 4372}, {2983, 4372}, {2984, 4371}, {2985, 4372}, {2987, 4372}, {2988, 4371}, {2990, 4371}, {2991, 4372}, {2992, 4372}, {2993, 4373}, {2994, 4373}, {2999, 4378}, {2999, 4388}, {2997, 4390}, {2997, 4391}, {2996, 4391}, {2995, 4392}, {2993, 4392}, {2992, 4393}, {2992, 4394}, {2991, 4394}, {2990, 4395}, {2990, 4396}, {2988, 4396}, {2987, 4397}, {2986, 4397}, {2985, 4396}, {2984, 4396}, {2983, 4395}, {2983, 4394}, {2982, 4393}, {2981, 4393}, {2980, 4392}, {2979, 4392}, {2978, 4391}, {2977, 4391}, {2976, 4390}}, 2);
	}

	private final ClanChannel channel;
	private CorporealBeastNPC beast;
	private final RSPolygon innerArea;
	private final Set<Player> inside = new HashSet<>();

	@Override
	protected void cleared() {
		if (!isEmpty()) {
			return;
		}
		instances.remove(channel.getOwner());
		destroyRegion();
	}

	@Override
	public Location onLoginLocation() {
		return new Location(2965, 4382, 2);
	}

	@Subscribe
	public static void onCannonRemoveEvent(final CannonRemoveEvent event) {
		final Multicannon cannon = event.getCannon();
		final boolean inDynamicRegion = cannon.getX() >= 6400;
		if (inDynamicRegion) {
			for (final Map.Entry<String, CorporealBeastDynamicArea> entry : instances.entrySet()) {
				final CorporealBeastDynamicArea instance = entry.getValue();
				if (instance.innerArea.contains(cannon)) {
					instance.cleared();
				}
			}
		}
	}

	@Override
	public void enter(final Player player) {
		player.setForceMultiArea(true);
		player.getVarManager().sendBit(2185, true);
		player.setViewDistance(Player.SCENE_DIAMETER);
	}

	private void clearIfEmpty() {
		if (inside.isEmpty()) {
			if (beast == null || beast.isDead() || beast.isFinished()) return;
			beast.setHitpoints(beast.getMaxHitpoints());
			beast.getReceivedHits().clear();
		}
	}

	@Override
	public void leave(final Player player, boolean logout) {
		player.resetViewDistance();
		player.setForceMultiArea(false);
		player.getVarManager().sendBit(2185, false);
		inside.remove(player);
		clearIfEmpty();
		if (logout) {
			player.forceLocation(CorporealBeastPrivatePortalOA.ENTRANCE);
		}
		if (inside.isEmpty()) {
			cleared();
		}
	}

	@Override
	public String name() {
		return "Clan " + channel.getOwner() + "'s Corporeal beast instance";
	}

	@Override
	public void constructed() {
		instances.put(channel.getOwner(), this);
		beast = (CorporealBeastNPC) new CorporealBeastNPC(getLocation(new Location(2991, 4381, 2)), this).spawn();
	}

	private boolean isEmpty() {
		for (final Object2ObjectMap.Entry<String, Multicannon> cannonEntry : DwarfMultiCannon.placedCannons.object2ObjectEntrySet()) {
			final Multicannon cannon = cannonEntry.getValue();
			if (innerArea.contains(cannon)) {
				return false;
			}
		}
		return inside.size() > 0;
	}

	@Override
	public boolean processMovement(final Player player, final int x, final int y) {
		final Location location = player.getLocation();
		if (inside.contains(player)) {
			if (!innerArea.contains(location)) {
				player.getInterfaceHandler().closeInterface(InterfacePosition.OVERLAY);
				if (inside.remove(player)) {
					clearIfEmpty();
				}
			}
		} else {
			if (innerArea.contains(location)) {
				player.getInterfaceHandler().sendInterface(InterfacePosition.OVERLAY, 13);
				if (inside.add(player) && beast != null) {
					beast.refreshDamageDealt(player, 0);
				} else {
					player.getVarManager().sendBit(999, 0);
				}
			}
		}
		return true;
	}

	@Override
	public boolean isMultiwayArea(Position position) {
		return true;
	}

}
