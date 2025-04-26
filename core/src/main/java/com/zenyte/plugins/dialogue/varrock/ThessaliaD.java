package com.zenyte.plugins.dialogue.varrock;

import com.zenyte.game.item.Item;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

import static com.zenyte.game.GameInterface.THESSALIA_MAKEOVER;

/**
 * @author Tommeh | 16 mrt. 2018 : 19:54:11
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public class ThessaliaD extends Dialogue {

	public static final Item PRICE = new Item(995, 500);

	private final boolean talk;

	public ThessaliaD(final Player player, final NPC npc, final boolean talk) {
		super(player, npc);
		this.talk = talk;
	}

	@Override
	public void buildDialogue() {
		final boolean enoughCoins = player.getInventory().containsItem(PRICE);
		if (talk) {
			npc("Do you want to buy any fine clothes?");
			options(TITLE, "What have you got?", "No, thank you.").onOptionOne(() -> setKey(10)).onOptionTwo(() -> setKey(5));
			player(5, "No, thank you.");
			npc("Well, please return if you change your mind.");
			player(10, "What have you got?");
			npc("Well, I have a number of fine pieces of clothing on sale<br>or, if you prefer, I can offer you an exclusive, total-<br>clothing makeover?");
			options(TITLE, "Tell me more about this makeover.", "I'd just like to buy some clothes.")
					.onOptionOne(() -> setKey(15)).onOptionTwo(() -> {
						player.openShop("Thessalia's Fine Clothes");
						finish();
					});
			player(15, "Tell me more about this make-over.");
			npc("Certainly!");
			npc("Here at Thessalia's fine clothing boutique, we offer a<br>unique service where we will totally revamp your outfit<br>to your choosing.");
			npc("It costs only " + PRICE.getAmount()
					+ " coins. Tired of always wearing the<br>same old outfit, day in, day out? This is the service for<br> you!");
			npc("So what do you say? Interested? We can change either<br>your top or your legwear!");
			options(TITLE, "I'd like to change my top please.", "I'd like to change my legwear please.",
					"I'd just like to buy some clothes.", "No, thank you.").onOptionOne(() -> setKey(25)).onOptionTwo(() -> setKey(30))
							.onOptionThree(() -> {
								player.openShop("Thessalia's Fine Clothes");
								finish();
							}).onOptionFour(() -> setKey(35));
			npc(25, "Just select what style and colour you would like from<br>this catalogue.").executeAction(() -> {
				if (!enoughCoins) {
					setKey(26);
				} else {
					sendThessaliaMakeoverInterface(true);
				}
			});
			player("I don't have " + PRICE.getAmount() + " coins on me...");
			npc("That's ok! Just come back when you do have!");
			npc(30, "Just select what style and colour you would like from<br>this catalogue.").executeAction(() -> {
				if (!enoughCoins) {
					setKey(31);
				} else {
					sendThessaliaMakeoverInterface(true);
				}
			});
			player("I don't have " + PRICE.getAmount() + " coins on me...");
			npc("That's ok! Just come back when you do have!");
			player(35, "No, thank you.");
			npc("Well, please return if you change your mind.");

		} else {
			if (!enoughCoins) {
				npc("A makeover costs " + PRICE.getAmount() + " coins.");
			} else {
				options("A makeover costs " + PRICE.getAmount() + " coins.", "Change your top.", "Change your leggings.", "Cancel.")
						.onOptionOne(() -> {
							sendThessaliaMakeoverInterface(true);
							finish();
						}).onOptionTwo(() -> {
							sendThessaliaMakeoverInterface(false);
							finish();
						});
			}
		}
	}

	private void sendThessaliaMakeoverInterface(final boolean topwear) {
		player.getTemporaryAttributes().remove("SelectedBodyStyle");
		player.getTemporaryAttributes().remove("SelectedBodyColour");
		player.getTemporaryAttributes().remove("SelectedLegsStyle");
		player.getTemporaryAttributes().remove("SelectedLegsColour");
		player.getTemporaryAttributes().remove("SelectedArmStyle");
		for (int i = 261; i <= 263; i++) {
			player.getVarManager().sendVar(i, -1);
		}
		player.getVarManager().sendBit(3944, player.getAppearance().isMale() ? 0 : 1);
		player.getVarManager().sendBit(3945, topwear ? 0 : 1);
		THESSALIA_MAKEOVER.open(player);
		if (topwear) {
			player.getPacketDispatcher().sendComponentSettings(591, 3, 0, 13, AccessMask.CLICK_OP1);
			player.getPacketDispatcher().sendComponentSettings(591, 5, 0, 11, AccessMask.CLICK_OP1);
		} else {
			player.getPacketDispatcher().sendComponentSettings(591, 7, 0, 10, AccessMask.CLICK_OP1);
		}
		player.getTemporaryAttributes().put("ThessaliaNPCId", npcId);
		finish();
	}

}
