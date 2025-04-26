package com.zenyte.game.content.skills.magic.spells.lunar;

import com.google.common.collect.ImmutableMap;
import com.zenyte.game.content.achievementdiary.DiaryReward;
import com.zenyte.game.content.achievementdiary.DiaryUtil;
import com.zenyte.game.content.skills.magic.Spellbook;
import com.zenyte.game.content.skills.magic.spells.DefaultSpell;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.Inventory;

/**
 * @author Kris | 17. veebr 2018 : 19:19.55
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class RechargeDragonstone implements DefaultSpell {

	private static final Animation ANIM = new Animation(712);
	private static final Graphics GFX = new Graphics(282, 0, 92);
	private static final SoundEffect sound = new SoundEffect(139);
	
	private static final ImmutableMap<Integer, Integer> JEWELLERY = ImmutableMap.<Integer, Integer>builder()
			.put(11113, 11105).put(11111, 11105).put(11109, 11105).put(11107, 11105).put(11126, 11118)
			.put(11124, 11118).put(11122, 11118).put(11120, 11118).put(1704, 1712).put(1706, 1712)
			.put(1708, 1712).put(1710, 1712).put(10362, 10354).put(10360, 10354).put(10358, 10354)
			.put(10356, 10354).build();
	
	@Override
	public int getDelay() {
		return 2000;
	}

	@Override
	public boolean spellEffect(final Player player, final int optionId, final String option) {
		if (!DiaryUtil.eligibleFor(DiaryReward.FREMENNIK_SEA_BOOTS3, player)) {
			player.sendMessage("You need to complete the fremennik hard diaries first in order to use this spell.");
			return false;
		}
		if (!containsJewellery(player)) {
			player.sendMessage("You have no dragonstone items capable of being charged.");
			return false;
		}
		player.setAnimation(ANIM);
		player.setGraphics(GFX);
		player.sendSound(sound);
		final Inventory inventory = player.getInventory();
		for (int i = 0; i < 28; i++) {
			final Item item = inventory.getItem(i);
			if (item == null) {
				continue;
			}
			final Integer response = JEWELLERY.get(item.getId());
			if (response == null) {
				continue;
			}
			item.setId(response);
		}
		player.getInventory().refreshAll();
		this.addXp(player, 97.5);
		player.sendMessage("You recharge all your dragonstone jewellery.");
		return true;
	}
	
	private final boolean containsJewellery(final Player player) {
		final Inventory inventory = player.getInventory();
		for (int i = 0; i < 28; i++) {
			final Item item = inventory.getItem(i);
			if (item == null) {
				continue;
			}
			if (JEWELLERY.get(item.getId()) != null) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public Spellbook getSpellbook() {
		return Spellbook.LUNAR;
	}
	
}
