package com.zenyte.plugins.dialogue;

import com.zenyte.game.item.Item;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

import static com.zenyte.game.GameInterface.HAIRDRESSER;

/**
 * @author Tommeh | 16 mrt. 2018 : 18:08:29
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public class HairdresserD extends Dialogue {

	public static final Item PRICE = new Item(995, 1000);

	private final boolean talk;

	public HairdresserD(final Player player, final NPC npc, final boolean talk) {
		super(player, npc);
		this.talk = talk;
	}

	@Override
	public void buildDialogue() {
		final boolean male = player.getAppearance().isMale();
		if (talk) {
			npc("Good afternoon " + (male ? "sir." : "madam.") + " In need of a haircut are we? " + (male ? "Or<br>perhaps a shave?" : "") + " The service costs " + PRICE.getAmount() + " coins.");
			if (!player.getInventory().containsItem(PRICE)) {
				player("I don't have " + PRICE.getAmount() + " gold coins on me...");
				npc("Well, come back when you do. I'm not running a<br>charity here!");
			} else {
				options(TITLE, male ? new String[] { "I'd like a haircut please.", "I'd like a shave please.", "No thank you." } : new String[] { "I'd like a haircut please", "No thank you." }).onOptionOne(() -> {
					setKey(3);
				}).onOptionTwo(() -> {
					setKey(male ? 6 : 10);
				}).onOptionThree(() -> {
					setKey(10);
				});
				player(3, "I'd like a haircut please.");
				npc("Please select the hairstyle you would like from this<br> brochure. I'll even throw in a free recolour.").executeAction(() -> sendHairdresserInterface(true));
				player(6, "I'd like a shave please.");
				npc("Please select the facial hair you would like from this<br> brochure. I'll even throw in a free recolour.").executeAction(() -> sendHairdresserInterface(false));
				player(10, "No thank you.");
				npc("Very well. Come back if you change your mind.");
			}
		} else {
			if (!player.getInventory().containsItem(PRICE)) {
				player("I don't have " + PRICE.getAmount() + " gold coins on me...");
				npc("Well, come back when you do. I'm not running a<br>charity here!");
				return;
			}
			options("A haircut will cost " + PRICE.getAmount() + " coins.", male ? new String[] { "Change my hairstyle.", "Change my facial hair.", "Cancel." } : new String[] { "Change my hairstyle.", "Cancel." })
			.onOptionOne(() -> sendHairdresserInterface(true))
			.onOptionTwo(() -> { 
				if (male) {
					sendHairdresserInterface(false);
				}
				finish();
			})
			.onOptionThree(this::finish);
		}
	}

	private void sendHairdresserInterface(final boolean haircut) {
		player.getTemporaryAttributes().remove("SelectedHairStyle");
		player.getTemporaryAttributes().remove("SelectedHairColour");
		player.getTemporaryAttributes().remove("SelectedBeardStyle");
		player.getTemporaryAttributes().remove("SelectedBeardColour");
		player.getVarManager().sendVar(261, -1);
		player.getVarManager().sendVar(263, -1);
		player.getVarManager().sendBit(4146, haircut ? 1 : 2);
		if (haircut) {
			player.getVarManager().sendBit(3945, player.getAppearance().isMale() ? 0 : 1);
		}
		HAIRDRESSER.open(player);
		player.getPacketDispatcher().sendComponentSettings(82, 2, 0, 23, AccessMask.CLICK_OP1);
		player.getPacketDispatcher().sendComponentSettings(82, 8, 0, 24, AccessMask.CLICK_OP1);
		player.getPacketDispatcher().sendComponentText(82, 9, "CONFIRM - (" + PRICE.getAmount() + " coins)");
		player.getTemporaryAttributes().put("HairdresserNPCId", npcId);
	}

}
