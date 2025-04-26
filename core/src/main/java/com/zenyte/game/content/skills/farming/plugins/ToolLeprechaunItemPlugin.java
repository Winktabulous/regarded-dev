package com.zenyte.game.content.skills.farming.plugins;

import com.zenyte.game.content.skills.farming.FarmingProduct;
import com.zenyte.game.content.zahur.Herb;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnNPCAction;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

import java.util.Optional;

/**
 * @author Kris | 11. mai 2018 : 01:05:15
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>
 */
public final class ToolLeprechaunItemPlugin implements ItemOnNPCAction {
	@Override
	public void handleItemOnNPCAction(final Player player, final Item item, final int slot, final NPC npc) {
		player.getDialogueManager().start(new Dialogue(player, npc) {
			@Override
			public void buildDialogue() {
				final Optional<Item> noted = getNoted(player, item);
				if (!noted.isPresent()) {
					npc("Ay matey, I'm sorry, I cannot note that for ya!");
					return;
				}
				item(item, "The leprechaun exchanges your items for banknotes.");
				player.getInventory().deleteItem(item.getId(), 28);
				player.getInventory().addItem(noted.get()).onFailure(remainder -> World.spawnFloorItem(remainder, player));
			}
		});
	}

	/**
	 * Gets the noted variant of the farming product, as long as it is a farming product.
	 * @param player the player.
	 * @param unnoted the unnoted variant of the item.
	 * @return an optional of the noted version of the unnoted item, if exists.
	 */
	private Optional<Item> getNoted(final Player player, final Item unnoted) {
		if (unnoted.getDefinitions().isNoted() || unnoted.getDefinitions().getNotedOrDefault() == unnoted.getId()) {
			return Optional.empty();
		}
		final int id = unnoted.getId();
		final Herb herb = Herb.CLEAN_HERBS.get(id);
		if (herb != null) {
			return Optional.of(new Item(unnoted.getDefinitions().getNotedOrDefault(), player.getInventory().getAmountOf(unnoted.getId())));
		}
		for (final FarmingProduct product : FarmingProduct.values) {
			if (product == null) continue;
			final Item prod = product.getProduct();
			if (prod == null) continue;
			if (prod.getId() == id) {
				return Optional.of(new Item(unnoted.getDefinitions().getNotedOrDefault(), player.getInventory().getAmountOf(unnoted.getId())));
			}
		}
		return Optional.empty();
	}

	@Override
	public Object[] getItems() {
		return null;
	}

	@Override
	public Object[] getObjects() {
		return new Object[] {"Tool leprechaun"};
	}
}
