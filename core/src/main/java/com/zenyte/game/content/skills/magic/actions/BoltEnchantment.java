package com.zenyte.game.content.skills.magic.actions;

import com.zenyte.game.content.skills.magic.Magic;
import com.zenyte.game.content.skills.magic.SpellState;
import com.zenyte.game.content.skills.magic.spells.MagicSpell;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.plugins.dialogue.PlainChat;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tommeh | 16 dec. 2017 : 23:22:49
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 * profile</a>}
 */
public class BoltEnchantment extends Action {
	private static final Graphics GRAPHICS = new Graphics(759);
	private static final Animation ANIMATION = new Animation(4462);
	private final BoltEnchantmentData data;


	public enum BoltEnchantmentData {
		OPAL_BOLT(4, 0.9, new Item(9236), new Item(879), new Item(Magic.COSMIC_RUNE), new Item(Magic.AIR_RUNE, 2)), OPAL_DRAGON_BOLT(4, 0.9, new Item(21932), new Item(21955), new Item(Magic.COSMIC_RUNE), new Item(Magic.AIR_RUNE, 2)), JADE_BOLT(14, 1.9, new Item(9237), new Item(9335), new Item(Magic.COSMIC_RUNE), new Item(Magic.EARTH_RUNE, 2)), JADE_DRAGON_BOLT(14, 1.9, new Item(21934), new Item(21957), new Item(Magic.COSMIC_RUNE), new Item(Magic.EARTH_RUNE, 2)), PEARL_BOLT(24, 2.9, new Item(9238), new Item(880), new Item(Magic.COSMIC_RUNE), new Item(Magic.WATER_RUNE, 2)), PEARL_DRAGON_BOLT(24, 2.9, new Item(21936), new Item(21959), new Item(Magic.COSMIC_RUNE), new Item(Magic.WATER_RUNE, 2)), TOPAZ_BOLT(29, 3.3, new Item(9239), new Item(9336), new Item(Magic.COSMIC_RUNE), new Item(Magic.FIRE_RUNE, 2)), TOPAZ_DRAGON_BOLT(29, 3.3, new Item(21938), new Item(21961), new Item(Magic.COSMIC_RUNE), new Item(Magic.FIRE_RUNE, 2)), SAPPHIRE_BOLT(7, 1.7, new Item(9240), new Item(9337), new Item(Magic.COSMIC_RUNE), new Item(Magic.MIND_RUNE), new Item(Magic.WATER_RUNE)), SAPPHIRE_DRAGON_BOLT(7, 1.7, new Item(21940), new Item(21963), new Item(Magic.COSMIC_RUNE), new Item(Magic.MIND_RUNE), new Item(Magic.WATER_RUNE)), EMERALD_BOLT(27, 3.7, new Item(9241), new Item(9338), new Item(Magic.COSMIC_RUNE), new Item(Magic.NATURE_RUNE), new Item(Magic.AIR_RUNE, 3)), EMERALD_DRAGON_BOLT(27, 3.7, new Item(21942), new Item(21965), new Item(Magic.COSMIC_RUNE), new Item(Magic.NATURE_RUNE), new Item(Magic.AIR_RUNE, 3)), RUBY_BOLT(49, 5.9, new Item(9242), new Item(9339), new Item(Magic.COSMIC_RUNE), new Item(Magic.BLOOD_RUNE, 2), new Item(Magic.FIRE_RUNE, 5)), RUBY_DRAGON_BOLT(49, 5.9, new Item(21944), new Item(21967), new Item(Magic.COSMIC_RUNE), new Item(Magic.BLOOD_RUNE, 2), new Item(Magic.FIRE_RUNE, 5)), DIAMOND_BOLT(57, 6.7, new Item(9243), new Item(9340), new Item(Magic.COSMIC_RUNE), new Item(Magic.LAW_RUNE, 2), new Item(Magic.EARTH_RUNE, 10)), DIAMOND_DRAGON_BOLT(57, 6.7, new Item(21946), new Item(21969), new Item(Magic.COSMIC_RUNE), new Item(Magic.LAW_RUNE, 2), new Item(Magic.EARTH_RUNE, 10)), DRAGONSTONE_BOLT(68, 7.8, new Item(9244), new Item(9341), new Item(Magic.COSMIC_RUNE), new Item(Magic.SOUL_RUNE, 1), new Item(Magic.EARTH_RUNE, 15)), DRAGONSTONE_DRAGON_BOLT(68, 7.8, new Item(21948), new Item(21971), new Item(Magic.COSMIC_RUNE), new Item(Magic.SOUL_RUNE, 1), new Item(Magic.EARTH_RUNE, 15)), ONYX_BOLT(4, 8.7, new Item(9245), new Item(9342), new Item(Magic.COSMIC_RUNE), new Item(Magic.DEATH_RUNE, 1), new Item(Magic.FIRE_RUNE, 20)), ONYX_DRAGON_BOLT(4, 8.7, new Item(21950), new Item(21973), new Item(Magic.COSMIC_RUNE), new Item(Magic.DEATH_RUNE, 1), new Item(Magic.FIRE_RUNE, 20));
		private final int level;
		private final double xp;
		private final Item product;
		private final Item bolt;
		private final Item[] runes;
		public static final BoltEnchantmentData[] VALUES = values();

		BoltEnchantmentData(final int level, final double xp, final Item product, final Item bolt, final Item... runes) {
			this.level = level;
			this.xp = xp;
			this.product = product;
			this.bolt = bolt;
			this.runes = runes;
		}

		public static List<BoltEnchantmentData> getData(final Player player, final boolean skipRunes) {
			final ArrayList<BoltEnchantment.BoltEnchantmentData> list = new ArrayList<BoltEnchantmentData>();
			for (final BoltEnchantment.BoltEnchantmentData data : BoltEnchantmentData.VALUES) {
				final SpellState state = new SpellState(player, data.getLevel(), data.getRunes());
				if ((!skipRunes && !state.check(false)) || !player.getInventory().containsItem(data.getBolt())) {
					continue;
				}
				list.add(data);
			}
			return list;
		}

		public int getLevel() {
			return level;
		}

		public double getXp() {
			return xp;
		}

		public Item getProduct() {
			return product;
		}

		public Item getBolt() {
			return bolt;
		}

		public Item[] getRunes() {
			return runes;
		}
	}

	private final int amount;
	private int cycle;
	private final MagicSpell spell;
	private SpellState state;

	public BoltEnchantment(final MagicSpell spell, final BoltEnchantmentData data, final int amount) {
		this.spell = spell;
		this.data = data;
		this.amount = amount;
	}

	public boolean check() {
		if (player.isDead() || player.isFinished()) {
			return false;
		}
		if (player.getSkills().getLevel(SkillConstants.MAGIC) < data.getLevel()) {
			player.getDialogueManager().start(new PlainChat(player, "You need to have a Magic level of " + data.getLevel() + " to enchant " + data.getBolt().getName().toLowerCase() + "."));
			return false;
		}
		if (!player.getInventory().hasFreeSlots() && !player.getInventory().containsItem(data.getProduct().getId(), 1)) {
			player.sendMessage("You need some more free inventory space to do this.");
			return false;
		}
		state = new SpellState(player, data.getLevel(), data.getRunes());
		if (!state.check(false) || !player.getInventory().containsItem(data.getBolt())) {
			StringBuilder builder = new StringBuilder();
			for (final Item rune : data.getRunes()) {
				builder.append(rune.getAmount());
				builder.append(" ");
				builder.append(rune.getName().toLowerCase().replaceAll(" rune", ""));
				builder.append(", ");
			}
			String message = builder.toString().replaceAll(", $", "");
			builder = new StringBuilder(message);
			final int index = message.lastIndexOf(", ");
			if (index >= 0) {
				builder.replace(message.lastIndexOf(", "), message.lastIndexOf(", ") + 1, " and");
			}
			player.sendMessage("You need at least " + builder + " runes to do that.");
			return false;
		}
		return cycle < amount;
	}

	@Override
	public boolean start() {
		return check();
	}

	@Override
	public boolean process() {
		return check();
	}

	@Override
	public int processWithDelay() {
		int amount = player.getInventory().getAmountOf(data.getBolt().getId());
		if (amount >= 10) {
			amount = 10;
		}
		player.setAnimation(ANIMATION);
		player.setGraphics(GRAPHICS);
		spell.addXp(player, data.getXp() * amount);
		player.getInventory().deleteItem(new Item(data.getBolt().getId(), amount));
		state.remove();
		player.getInventory().addOrDrop(new Item(data.getProduct().getId(), amount));
		player.sendFilteredMessage("The magic of the runes coaxes out the true nature of the gem tips.");
		cycle++;
		return 2;
	}

	@Override
	public void stop() {
	}
}
