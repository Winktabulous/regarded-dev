package com.zenyte.plugins.dialogue;

import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

import static com.zenyte.game.GameInterface.GRAND_EXCHANGE_HISTORY;

/**
 * @author Tommeh | 20 mei 2018 | 17:49:40
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class GrandExchangeClerkD extends Dialogue {

	public GrandExchangeClerkD(Player player, NPC npc) {
		super(player, npc);
	}

	@Override
	public void buildDialogue() {
		npc("Welcome to the Grand Exchange.<br><br>Would you like to trade now, or exchange item sets?");
		options(TITLE, "How do I use the Grand Exchange?", "I'd like to set up trade offers please.", "Can you help me with item sets?", "Show me my trade history.", "I'm fine, thanks.")
			.onOptionOne(() -> setKey(5))
			.onOptionTwo(() -> setKey(15))
			.onOptionThree(() -> setKey(20))
			.onOptionFour(() -> setKey(25))
			.onOptionFive(() -> setKey(30));
		player(5, "How do I use the Grand Exchange?");
		npc("My colleague and I can let you set up trade offers. You can offer to Sell items or Buy items.");
		npc("When you want to sell something, you give us the items<br>and tell us how much money you want for them.");
		npc("We'll look for someone who wants to buy those items at your price, and we'll perform the trade. You can then collect the cash here, or at any bank.");
		npc("When you want to buy something, you tell us what you want, and give us the cash you're willing to spend on it.");
		npc("We'll look for someone who's selling those items at your price, and we'll perform the trade. You can then collect the items here, or at any bank, along with any left-over cash.");
		npc("Sometimes it takes a while to find a matching trade offer. If you change your mind, we'll let you cancel your trade offer, and we'll return your unused items and cash.");
		npc("That's all the essential information you need to get started. Would you like to trade now, or exchange item sets?");
		options(TITLE, "I'd like to set up trade offers please.", "Can you help me with item sets?", "Show me my trade history.", "I'm fine, thanks.")
			.onOptionOne(() -> setKey(15))
			.onOptionTwo(() -> setKey(20))
		    .onOptionThree(() -> setKey(25))
		    .onOptionFour(() -> setKey(30));
		player(15, "I'd like to set up trade offers please.").executeAction(() -> player.getGrandExchange().openOffersInterface());
		player(20, "Can you help me with item sets?").executeAction(() -> player.getGrandExchange().openItemSetsInterface());
		player(25, "Show me my trade history.").executeAction(() -> GRAND_EXCHANGE_HISTORY.open(player));
		player(30, "I'm fine, thanks.");
	}

}
