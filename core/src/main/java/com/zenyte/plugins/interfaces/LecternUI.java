package com.zenyte.plugins.interfaces;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.model.ui.UserInterface;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;

import java.util.HashMap;
import java.util.Map;

import static com.zenyte.game.content.skills.magic.spells.MagicSpell.*;

/**
 * @author Kris | 25. veebr 2018 : 3:33.14
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class LecternUI implements UserInterface {

	private enum Tablet {
		
		VARROCK_TELEPORT(11, 25, new Item(8007), new Item(FIRE_RUNE), new Item(AIR_RUNE, 3), new Item(LAW_RUNE)),
		LUMBRIDGE_TELEPORT(12, 31, new Item(8008), new Item(EARTH_RUNE), new Item(AIR_RUNE, 3), new Item(LAW_RUNE)),
		FALADOR_TELEPORT(13, 37, new Item(8009), new Item(WATER_RUNE), new Item(AIR_RUNE, 3), new Item(LAW_RUNE)),
		CAMELOT_TELEPORT(14, 45, new Item(8010), new Item(AIR_RUNE, 5), new Item(LAW_RUNE)),
		ARDOUGNE_TELEPORT(15, 51, new Item(8011), new Item(WATER_RUNE, 2), new Item(LAW_RUNE, 2)),
		WATCHTOWER_TELEPORT(16, 58, new Item(8012), new Item(EARTH_RUNE, 2), new Item(LAW_RUNE, 2)),
		TELEPORT_TO_HOUSE(17, 40, new Item(8013), new Item(LAW_RUNE), new Item(AIR_RUNE), new Item(EARTH_RUNE)),
		SAPPHIRE_ENCHANTMENT(3, 7, new Item(8016), new Item(WATER_RUNE), new Item(COSMIC_RUNE)),
		EMERALD_ENCHANTMENT(4, 27, new Item(8017), new Item(AIR_RUNE, 3), new Item(COSMIC_RUNE)),
		RUBY_ENCHANTMENT(6, 49, new Item(8018), new Item(FIRE_RUNE, 5), new Item(COSMIC_RUNE)),
		DIAMOND_ENCHANTMENT(7, 57, new Item(8019), new Item(EARTH_RUNE, 10), new Item(COSMIC_RUNE)),
		DRAGONSTONE_ENCHANTMENT(8, 68, new Item(8020), new Item(WATER_RUNE, 15), new Item(EARTH_RUNE, 15), new Item(COSMIC_RUNE)),
		ONYX_ENCHANTMENT(9, 87, new Item(8021), new Item(EARTH_RUNE, 20), new Item(FIRE_RUNE, 20), new Item(COSMIC_RUNE)),
		BONES_TO_BANANAS(5, 15, new Item(8014), new Item(EARTH_RUNE, 2), new Item(WATER_RUNE, 2), new Item(NATURE_RUNE)),
		BONES_TO_PEACHES(10, 60, new Item(8015), new Item(NATURE_RUNE, 2), new Item(WATER_RUNE, 4), new Item(EARTH_RUNE, 3));
		
		private final int componentId, level;
		private final Item[] runes;
		private final Item tab;
		
		private static final Tablet[] VALUES = values();
		private static final Map<Integer, Tablet> TABLET_MAP = new HashMap<Integer, Tablet>(VALUES.length);
		
		static {
			for (final Tablet tab : VALUES) {
				TABLET_MAP.put(tab.componentId, tab);
			}
		}
		
		Tablet(final int componentId, final int level, final Item tab, final Item... runes) {
			this.componentId = componentId;
			this.level = level;
			this.tab = tab;
			this.runes = runes;
		}
	}
	
	private static final class TabletCreation extends Action {

		private static final Animation ANIM = new Animation(4067);
		
		public TabletCreation(final Tablet tab, final int amount) {
			this.tab = tab;
			this.amount = amount;
		}
		
		private final Tablet tab;
		private int amount;
		
		@Override
		public boolean start() {
			return true;
		}

		@Override
		public boolean process() {
			return true;
		}

		@Override
		public int processWithDelay() {
			if (amount-- <= 0) {
				return -1;
			}
			if (player.getSkills().getLevel(SkillConstants.MAGIC) < tab.level) {
				player.sendMessage("You need a Magic level of at least " + tab.level + " to craft tablets.");
				return -1;
			}
			if (!check()) {
				return -1;
			}
			player.setAnimation(ANIM);
			WorldTasksManager.schedule(() -> {
				if (!check()) {
					return;
				}
				player.getInventory().deleteItem(1761, 1);
				for (final Item rune : tab.runes) {
					player.getInventory().deleteItem(rune);
				}
				player.getInventory().addItem(tab.tab);
			}, 4);
			return 5;
		}
		
		private final boolean check() {
			if (!player.getInventory().containsItem(1761, 1) || !player.getInventory().containsItems(tab.runes)) {
				final StringBuilder builder = new StringBuilder();
				for (final Item i : tab.runes) {
					if (i.getAmount() > 1) {
						builder.append(i.getAmount() + " " + i.getName().toLowerCase() + "s, ");
					} else {
						builder.append((Utils.startWithVowel(i.getName()) ? "an " : "a ") + i.getName().toLowerCase() + ", ");
					}
				}
				builder.delete(builder.length() - 2, builder.length());
				final String tabName = tab.toString().toLowerCase().replaceAll("_", " ");
				player.sendMessage("You need " + builder + " and some soft clay to craft " + ((Utils.startWithVowel(tabName) ? "an " : "a ")) + tabName + " tablet.");
				return false;
			}
			return true;
		}
		
	}
	
	@Override
	public void handleComponentClick(final Player player, final int interfaceId, final int componentId, final int slotId, final int itemId, final int optionId, final String option) {
		player.getInterfaceHandler().closeInterface(InterfacePosition.CENTRAL);
		final Tablet tab = Tablet.TABLET_MAP.get(componentId);
		if (tab == null) {
			return;
		}
		final int amount = optionId == 1 ? 1 : optionId == 2 ? 5 : Integer.MAX_VALUE;
		if (optionId < 4) {
			player.getActionManager().setAction(new TabletCreation(tab, amount));
			return;
		} else if (optionId == 4) {
			player.sendInputInt("How many tablets would you like to craft?", value -> player.getActionManager().setAction(new TabletCreation(tab, value)));
			return;
		}
	}

	@Override
	public int[] getInterfaceIds() {
		return new int[] { 79 };
	}

}
