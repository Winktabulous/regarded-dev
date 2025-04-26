package com.zenyte.plugins.dialogue;

import com.zenyte.game.GameConstants;
import com.zenyte.game.GameInterface;
import com.zenyte.game.content.partyroom.FaladorPartyRoom;
import com.zenyte.game.content.skills.magic.spells.teleports.TeleportCollection;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.bank.BankPinSettingsInterface;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Tommeh | 27 mei 2018 | 15:15:57
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class BankerD extends Dialogue {
	public BankerD(Player player, NPC npc) {
		super(player, npc);
	}

	@Override
	public void buildDialogue() {
		final var partyRoom = FaladorPartyRoom.getPartyRoom();
		final var frequency = partyRoom.getAnnouncementFrequency();
		final var partyTeleport = frequency > 0 && partyRoom.getVariables().isAnnouncements();

		npc("Good day, how may I help you?");
		if (partyTeleport) {
			options(TITLE,
				"I'd like to access my bank account, please.",
//				"I'd like to check my PIN settings.",
				"I'd like to collect items.",
				"What is this place?",
				"Can you teleport me to the Party room?")
				.onOptionOne(() -> GameInterface.BANK.open(player))
//				.onOptionTwo(() -> new BankPinSettingsInterface().open(player))
				.onOptionThree(() -> GameInterface.GRAND_EXCHANGE_COLLECTION_BOX.open(player))
				.onOptionFour(() -> setKey(5))
				.onOptionFive(() -> setKey(25));
			npc(25, "Certainly.")
				.executeAction(() -> TeleportCollection.FALADOR_PARTY_ROOM.teleport(player));
		}
		else {
			options(TITLE,
				"I'd like to access my bank account, please.",
//				"I'd like to check my PIN settings.",
				"I'd like to collect items.",
				"What is this place?")
				.onOptionOne(() -> GameInterface.BANK.open(player))
//				.onOptionTwo(() -> new BankPinSettingsInterface().open(player))
				.onOptionThree(() -> GameInterface.GRAND_EXCHANGE_COLLECTION_BOX.open(player))
				.onOptionFour(() -> setKey(5));
		}
		player(5, "What is this place?");
		npc("This is a branch of the Bank of " + GameConstants.SERVER_NAME + ". We have<br>branches in many towns.");
		player("And what do you do?");
		npc("We will look after your items and money for you.<br>Leave your valuables with us if you want to keep them safe.");
	}
}
