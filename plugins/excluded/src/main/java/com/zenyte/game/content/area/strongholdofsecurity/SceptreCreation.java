package com.zenyte.game.content.area.strongholdofsecurity;

import com.zenyte.game.content.achievementdiary.DiaryReward;
import com.zenyte.game.content.achievementdiary.DiaryUtil;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.model.item.PairedItemOnItemPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.plugins.dialogue.ItemChat;
import com.zenyte.plugins.dialogue.PlainChat;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Kris | 5. sept 2018 : 01:49:55
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>
 */
public class SceptreCreation implements PairedItemOnItemPlugin {
	private static final int TOP_OF_SCEPTRE = 9010;
	private static final int BOTTOM_OF_SCEPTRE = 9011;
	private static final int RIGHT_SKULL_HALF = 9007;
	private static final int LEFT_SKULL_HALF = 9008;
	private static final int RUNED_SCEPTRE = 9012;
	private static final int STRANGE_SKULL = 9009;
	private static final int SKULL_SCEPTRE = 9013;
	private static final int IMBUED_SKULL_SCEPTRE = 21276;
	private static final ItemPair SCEPTRE_HALVES = new ItemPair(TOP_OF_SCEPTRE, BOTTOM_OF_SCEPTRE);
	private static final ItemPair SKULL_HALVES = new ItemPair(RIGHT_SKULL_HALF, LEFT_SKULL_HALF);
	private static final ItemPair SCEPTRE_SKULL_HALVES = new ItemPair(RUNED_SCEPTRE, STRANGE_SKULL);
	private static final ItemPair[] RECHARGE_PAIRS = new ItemPair[] {new ItemPair(IMBUED_SKULL_SCEPTRE, TOP_OF_SCEPTRE), new ItemPair(IMBUED_SKULL_SCEPTRE, BOTTOM_OF_SCEPTRE), new ItemPair(IMBUED_SKULL_SCEPTRE, RIGHT_SKULL_HALF), new ItemPair(IMBUED_SKULL_SCEPTRE, LEFT_SKULL_HALF), new ItemPair(IMBUED_SKULL_SCEPTRE, RUNED_SCEPTRE), new ItemPair(IMBUED_SKULL_SCEPTRE, STRANGE_SKULL)};
	private final ItemPair[] concatenatedPairs = concatenate(RECHARGE_PAIRS, SCEPTRE_HALVES, SKULL_HALVES, SCEPTRE_SKULL_HALVES);

	@Override
	public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
		final Inventory inventory = player.getInventory();
		final ItemOnItemAction.ItemPair pair = getMatchingPair(from, to, concatenatedPairs);
		final int maxCharges = DiaryUtil.eligibleFor(DiaryReward.VARROCK_ARMOUR4, player) ? 13 : DiaryUtil.eligibleFor(DiaryReward.VARROCK_ARMOUR3, player) ? 11 : DiaryUtil.eligibleFor(DiaryReward.VARROCK_ARMOUR2, player) ? 9 : DiaryUtil.eligibleFor(DiaryReward.VARROCK_ARMOUR1, player) ? 7 : 5;
		if (ArrayUtils.contains(RECHARGE_PAIRS, pair)) {
			final int fromId = from.getId();
			final Item sceptre = fromId == IMBUED_SKULL_SCEPTRE ? from : to;
			if (sceptre.getCharges() >= maxCharges) {
				player.getDialogueManager().start(new PlainChat(player, "Your Skull Sceptre can\'t contain any more charges."));
				return;
			}
			final Item piece = sceptre == from ? to : from;
			inventory.deleteItem(piece);
			final int pieceId = piece.getId();
			final int chargesToAdd = pieceId == RUNED_SCEPTRE || pieceId == STRANGE_SKULL ? 6 : 3;
			sceptre.setCharges(Math.min(maxCharges, sceptre.getCharges() + chargesToAdd));
			player.getDialogueManager().start(new ItemChat(player, piece, "You charge the sceptre with a small sceptre piece."));
			return;
		}
		inventory.deleteItem(from);
		inventory.deleteItem(to);
		if (pair == SCEPTRE_HALVES) {
			final Item runedSceptre = new Item(RUNED_SCEPTRE);
			inventory.addItem(runedSceptre);
			player.getDialogueManager().start(new ItemChat(player, runedSceptre, "The two halves of the Sceptre fit perfectly. The Sceptre<br>appears to be designed to have something on top."));
		} else if (pair == SKULL_HALVES) {
			final Item strangeSkull = new Item(STRANGE_SKULL);
			inventory.addItem(strangeSkull);
			player.getDialogueManager().start(new ItemChat(player, strangeSkull, "The two halves of the skull fit perfectly, they appear to<br>have a fixing point, perhaps they are to be mounted on<br>something?"));
		} else if (pair == SCEPTRE_SKULL_HALVES) {
			final Item sceptre = new Item(SKULL_SCEPTRE);
			if (player.containsItem(sceptre) || player.containsItem(new Item(IMBUED_SKULL_SCEPTRE))) {
				player.sendMessage("You can only create one Skull Sceptre at a time.");
				return;
			}
			sceptre.setCharges(5);
			inventory.addItem(sceptre);
			player.getDialogueManager().start(new ItemChat(player, sceptre, "The skull fits perfectly atop the Sceptre. You feel there<br>is great magical power at work here, and that the<br>sceptre has 5 charges."));
		}
	}

	@Override
	public ItemPair[] getMatchingPairs() {
		final ArrayList<ItemOnItemAction.ItemPair> pairs = new ArrayList<ItemPair>();
		pairs.add(SCEPTRE_HALVES);
		pairs.add(SKULL_HALVES);
		pairs.add(SCEPTRE_SKULL_HALVES);
		pairs.addAll(Arrays.asList(RECHARGE_PAIRS));
		return pairs.toArray(new ItemPair[pairs.size()]);
	}
}
