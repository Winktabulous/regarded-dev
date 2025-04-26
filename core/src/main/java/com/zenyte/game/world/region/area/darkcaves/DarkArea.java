package com.zenyte.game.world.region.area.darkcaves;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.area.plugins.ContainerPlugin;
import com.zenyte.game.world.region.area.plugins.CycleProcessPlugin;
import com.zenyte.plugins.item.LightSourceItem;
import com.zenyte.plugins.item.LightSourceItem.LightSource;

import java.util.Map;

/**
 * @author Kris | 27. aug 2018 : 22:43:48
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public abstract class DarkArea extends PolygonRegionArea implements CycleProcessPlugin, ContainerPlugin {
	protected void refreshOverlay(final Player player) {
		final int brightness = Math.max(0, LightSource.getBrightness(player));
		if (brightness >= 2) {
			player.getInterfaceHandler().closeInterface(InterfacePosition.OVERLAY);
			return;
		}
		if (brightness == 0) {
			player.getTemporaryAttributes().put("full darkess timer", 0);
		}
		player.getInterfaceHandler().sendInterface(InterfacePosition.OVERLAY, brightness == 0 ? 96 : 97);
	}

	@Override
	public void enter(final Player player) {
		refreshOverlay(player);
	}

	@Override
	public void leave(final Player player, boolean logout) {
		player.getInterfaceHandler().closeInterface(InterfacePosition.OVERLAY);
	}

	@Override
	public void process() {
		if (players.isEmpty()) {
			return;
		}
		for (final Player player : players) {
			final Map<Object, Object> attributes = player.getTemporaryAttributes();
			if (player.getInterfaceHandler().isVisible(96)) {
				final int ticksPassed = player.getNumericTemporaryAttribute("full darkess timer").intValue();
				if (ticksPassed == 5) {
					player.sendMessage("You hear tiny insects skittering over the ground...");
				}
				if (ticksPassed >= 25) {
					if (ticksPassed == 25) {
						player.sendMessage("Tiny biting insects swarm all over you!");
					}
					player.applyHit(new Hit(1, HitType.REGULAR));
				}
				attributes.put("full darkess timer", ticksPassed + 1);
				continue;
			}
			if (Utils.random(100) == 0) {
				final LightSourceItem.LightSource[] lightsources = LightSource.getLitLightSources(player, LightSourceItem.NONE);
				if (lightsources.length > 0) {
					LightSource.extinguish(player, 1, lightsources[Utils.random(lightsources.length - 1)]);
					refreshOverlay(player);
				}
			}
		}
	}

	@Override
	public void onContainerModification(final Player player, final Container container, final Item previousItem, final Item currentItem) {
		final LightSourceItem.LightSource previousSource = LightSource.getSource(previousItem);
		final LightSourceItem.LightSource currentSource = LightSource.getSource(currentItem);
		if (previousSource != null || currentSource != null) {
			refreshOverlay(player);
		}
	}
}
