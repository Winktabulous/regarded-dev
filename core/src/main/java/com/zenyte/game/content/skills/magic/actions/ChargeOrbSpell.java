package com.zenyte.game.content.skills.magic.actions;

import com.zenyte.game.content.achievementdiary.diaries.KandarinDiary;
import com.zenyte.game.content.achievementdiary.diaries.WildernessDiary;
import com.zenyte.game.content.skills.magic.SpellState;
import com.zenyte.game.content.skills.magic.spells.MagicSpell;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.dialogue.PlainChat;

/**
 * @author Tommeh | 9 jan. 2018 : 19:30:59
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public class ChargeOrbSpell extends Action {
	private static final Animation ANIMATION = new Animation(726);


	public enum ChargeOrbSpellData {
		CHARGE_EARTH_ORB(70, 40, new Graphics(151), new Item(575)), CHARGE_WATER_ORB(66, 36, new Graphics(149), new Item(571)), CHARGE_AIR_ORB(76, 50, new Graphics(150), new Item(573)), CHARGE_FIRE_ORB(73, 47, new Graphics(152), new Item(569));
		private int level;
		private final int spellId;
		private final double xp;
		private final Item product;
		private Item[] materials;
		public static final ChargeOrbSpellData[] VALUES = values();
		private final Graphics graphics;

		ChargeOrbSpellData(final double xp, final int spellId, final Graphics graphics, final Item product) {
			this.xp = xp;
			this.spellId = spellId;
			this.graphics = graphics;
			this.product = product;
		}

		public int getLevel() {
			return level;
		}

		public int getSpellId() {
			return spellId;
		}

		public double getXp() {
			return xp;
		}

		public Item getProduct() {
			return product;
		}

		public Item[] getMaterials() {
			return materials;
		}

		public Graphics getGraphics() {
			return graphics;
		}
	}

	private final MagicSpell spell;
	private final ChargeOrbSpellData data;
	private final int amount;
	private int cycle;

	public ChargeOrbSpell(final ChargeOrbSpellData data, final int amount, final MagicSpell spell) {
		this.data = data;
		this.amount = amount;
		this.spell = spell;
	}

	private boolean check(final Player player) {
		if (player.isDead() || player.isFinished()) {
			return false;
		}
        return cycle < amount;
    }

	@Override
	public boolean start() {
		if (!player.getInventory().containsItem(567, 1)) {
			player.getDialogueManager().start(new PlainChat(player, "You must be holding an orb to enchant it."));
			return false;
		}
		return check(player);
	}

	@Override
	public boolean process() {
		return check(player);
	}

	@Override
	public int processWithDelay() {
		final SpellState state = new SpellState(player, spell.getLevel(), spell.getRunes());
		if (!state.check()) {
			return -1;
		}
		state.remove();
		if (data.equals(ChargeOrbSpellData.CHARGE_EARTH_ORB)) {
			player.getAchievementDiaries().update(WildernessDiary.CHARGE_AN_EARTH_ORB);
		} else if (data.equals(ChargeOrbSpellData.CHARGE_AIR_ORB)) {
			player.getAchievementDiaries().update(WildernessDiary.CHARGE_AIR_ORB);
		} else if (data.equals(ChargeOrbSpellData.CHARGE_WATER_ORB)) {
			player.getAchievementDiaries().update(KandarinDiary.CHARGE_WATER_ORB);
		}
		player.setAnimation(ANIMATION);
		player.setGraphics(data.getGraphics());
		spell.addXp(player, data.getXp());
		player.getInventory().addItem(new Item(data.getProduct().getId()));
		cycle++;
		return 4;
	}
}
