package com.zenyte.plugins.interfaces;

import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.model.ui.UserInterface;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.FairyRing;

import java.util.List;

public final class FairyRingLog implements UserInterface {
	
	public static void open(final Player player) {
		player.getInterfaceHandler().closeInterface(InterfacePosition.SINGLE_TAB);
		player.getPacketDispatcher().sendComponentText(381, 6, "Travel log");
		final List<String> favourites = FairyRing.getFavourites(player);
		for (final FairyRing entry : FairyRing.values) {
			if (favourites.contains(entry.getCode())) {
				continue;
			}
			player.getPacketDispatcher().sendComponentText(381, entry.getComponent(), "<br>"+entry.getTag());
		}
		for (int i = 0; i < 4; i++) {
			if (i >= favourites.size()) {
				continue;
			}
			final FairyRing fav = FairyRing.getRing(favourites.get(i));
			if (fav == null) {
				continue;
			}
			player.getPacketDispatcher().sendComponentText(381, 140 + i, "<br>" + fav.getTag());
			player.getPacketDispatcher().sendComponentText(381, 144 + i, fav.getCode());
		}
		player.getInterfaceHandler().sendInterface(InterfacePosition.SINGLE_TAB, 381);
	}
	
	@Override
	public void handleComponentClick(final Player player, final int interfaceId, final int componentId, final int slotId, final int itemId, final int optionId, final String option) {
		if (componentId >= 140 && optionId == 2 || componentId >= 148) {
			FairyRing.removeFavourite(player, componentId, optionId);
			return;
		}
		if (componentId >= 140) {
			final List<String> favourites = FairyRing.getFavourites(player);
			final int index = componentId - 140;
			final FairyRing ring = FairyRing.getRing(favourites.get(index));
			if (ring == null) {
				return;
			}
			FairyRingCombination.refresh(player, ring);
			return;
		}
		FairyRing ring = FairyRing.getRing(componentId);
		if (ring == null || optionId == 2) {
			if (ring == null) {
				ring = FairyRing.getRing(componentId - 1);
			}
			if (ring == null) {
				return;
			}
			FairyRing.addFavourite(player, ring);
			return;
		}
		FairyRingCombination.refresh(player, ring);
	}

	@Override
	public int[] getInterfaceIds() {
		return new int[] { 381 };
	}

}
