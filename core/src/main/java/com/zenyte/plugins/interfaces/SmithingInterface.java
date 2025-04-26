package com.zenyte.plugins.interfaces;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.skills.smithing.Smithing;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.world.entity.player.Player;

import java.util.Optional;

/**
 * @author Kris | 10. nov 2017 : 19:29.13
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class SmithingInterface extends Interface {
	private static final int QUANTITY_VARP = 2224;

	public static int getTierForBar(final Item item) {
		final int id = item.getId();
		return id == 2349 ? 1 : id == 2351 ? 2 : id == 2353 ? 3 : id == 2359 ? 4 : id == 2361 ? 5 : id == 2363 ? 6 : id == 9467 ? 7 : 0;
	}

	public static void openInterface(final Player player, final int tier, final int objectId) {
		player.getVarManager().sendVarInstant(210, tier);
		if (player.getVarManager().getValue(QUANTITY_VARP) < 1) {
			player.getVarManager().sendVarInstant(QUANTITY_VARP, 1);
		}
		GameInterface.SMITHING_INTERFACE.open(player);
		player.getTemporaryAttributes().put("SmithingTier", tier);
		player.getTemporaryAttributes().put("AnvilObjectId", objectId);
	}

	public static void restoreInputFromSmithing(Player player) {
		player.getPacketDispatcher().sendClientScript(6123);
	}

	@Override
	public void close(final Player player, final Optional<GameInterface> replacement) {
		restoreInputFromSmithing(player);
	}

	@Override
	protected void attach() {
		put(3, "Quantity 1");
		put(4, "Quantity 5");
		put(5, "Quantity 10");
		put(6, "Quantity X");
		put(7, "Quantity All");
		for (int i = 9; i <= 35; i++) {
			put(i, "Smith " + i);
		}
	}

	@Override
	protected void build() {
		bind("Quantity 1", player -> player.getVarManager().sendVar(QUANTITY_VARP, 1));
		bind("Quantity 5", player -> player.getVarManager().sendVar(QUANTITY_VARP, 5));
		bind("Quantity 10", player -> player.getVarManager().sendVar(QUANTITY_VARP, 10));
		bind("Quantity X", player -> {
			restoreInputFromSmithing(player);
			player.sendInputInt("How many would you like to make?", value -> player.getVarManager().sendVar(QUANTITY_VARP, Math.max(0, Math.min(28, value))));
		});
		bind("Quantity All", player -> player.getVarManager().sendVar(QUANTITY_VARP, 28));

		for (int i = 9; i <= 35; i++) {
			final int finalI = i;
			bind("Smith " + i, (player, slotId, itemId, option) -> {
				final int tier = player.getNumericTemporaryAttribute("SmithingTier").intValue();
				final int objectId = player.getNumericTemporaryAttribute("AnvilObjectId").intValue();
				final int amount = player.getVarManager().getValue(QUANTITY_VARP);

				player.getActionManager().setAction(new Smithing(objectId, tier - 1, finalI - 9, amount));
			});
		}
	}

	@Override
	public GameInterface getInterface() {
		return GameInterface.SMITHING_INTERFACE;
	}
}
