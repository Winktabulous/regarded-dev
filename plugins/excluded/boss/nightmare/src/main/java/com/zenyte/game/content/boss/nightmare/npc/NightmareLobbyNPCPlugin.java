package com.zenyte.game.content.boss.nightmare.npc;

import com.zenyte.game.content.boss.nightmare.NightmareLobbyNPC;
import com.zenyte.game.content.boss.nightmare.area.NightmareBossArea;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

import static com.zenyte.GameToggles.NIGHTMARE_SHURA_PREREQ;

public class NightmareLobbyNPCPlugin extends NPCPlugin {

	@Override
	public void handle() {
		bind("Disturb", (player, npc) -> {
			if (NIGHTMARE_SHURA_PREREQ && player.getVarManager().getBitValue(10137) == 0) {
				player.getDialogueManager().start(new Dialogue(player, 9459) {
					@Override
					public void buildDialogue() {
						npc("Hey! Don't just go poking her without knowing what you're up against. Come talk to me first.");
					}
				});
				return;
			}

			if (player.getAttributes().containsKey("Ignore nightmare warning")) {
				NightmareBossArea.enterFight(player);
				return;
			}

			player.getDialogueManager().start(new Dialogue(player) {
				@Override
				public void buildDialogue() {
					plain("You are about to begin an encounter with the Nightmare. Dying during this encounter will not be considered as safe death. Are you sure you wish to begin?");
					options("Are you sure you wish to begin?", new DialogueOption("Yes.", () -> NightmareBossArea.enterFight(player)), new DialogueOption("Yes, and don't ask again.", () -> {
						player.getAttributes().put("Ignore nightmare warning", true);
						NightmareBossArea.enterFight(player);
					}), new DialogueOption("No."));
				}
			});
		});
		bind("Inspect", (player, npc) -> player.getDialogueManager().start(new Dialogue(player) {
			@Override
			public void buildDialogue() {
				plain(NightmareBossArea.buildFirstInspectMessage() + "<br>" + NightmareBossArea.buildSecondInspectMessage());
			}
		}));
	}

	@Override
	public int[] getNPCs() {
		return new int[]{NightmareLobbyNPC.SLEEPING, NightmareLobbyNPC.AWAKENING, NightmareLobbyNPC.FIRST_PHASE, NightmareLobbyNPC.SECOND_PHASE, NightmareLobbyNPC.THIRD_PHASE};
	}

}
