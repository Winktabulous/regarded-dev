package com.zenyte.game.content.skills.smithing;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.BossDropItem;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.plugins.dialogue.ItemChat;
import com.zenyte.plugins.dialogue.PlainChat;

/**
 * @author Tommeh | 25 aug. 2018 | 17:55:40
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public class SpiritShieldCreationAction extends Action {
	public static final Item BLESSED_SPIRIT_SHIELD = new Item(12831);
	private final BossDropItem shield;
	private int ticks;

	public SpiritShieldCreationAction(BossDropItem shield) {
		this.shield = shield;
	}

	@Override
	public boolean start() {
		if (!player.getInventory().containsItem(Smithing.HAMMER)) {
			player.getDialogueManager().start(new PlainChat(player, "You need to have a hammer to do this."));
			return false;
		}
		if (!player.getInventory().containsItem(BLESSED_SPIRIT_SHIELD)) {
			player.getDialogueManager().start(new PlainChat(player, "You need to have a blessed spirit shield to attach the sigil onto."));
			return false;
		}
		if (player.getSkills().getLevelForXp(SkillConstants.PRAYER) < 90) {
			player.getDialogueManager().start(new PlainChat(player, "You need to have a Prayer level of at least 90 to do this."));
			return false;
		}
		if (player.getSkills().getLevel(SkillConstants.SMITHING) < 85) {
			player.getDialogueManager().start(new PlainChat(player, "You need to have a Smithing level of at least 85 to do this."));
			return false;
		}
		for (final Item item : shield.getMaterials()) {
			if (!player.getInventory().containsItem(item)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean process() {
		return true;
	}

	@Override
	public int processWithDelay() {
		switch (ticks++) {
		case 0: 
		case 5: 
			player.setAnimation(Smithing.ANIMATION);
			break;
		case 9: 
			player.getDialogueManager().start(new ItemChat(player, shield.getItem(), "You successfully combine the " + shield.getMaterials()[0].getName() + " with the shield."));
			player.getInventory().deleteItemsIfContains(shield.getMaterials(), () -> {
				player.getInventory().addItem(shield.getItem());
				player.getSkills().addXp(SkillConstants.SMITHING, 1800);
			});
			return -1;
		}
		return 0;
	}
}
