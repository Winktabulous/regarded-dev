package com.zenyte.plugins.item;

import com.zenyte.game.content.achievementdiary.DiaryReward;
import com.zenyte.game.content.achievementdiary.DiaryUtil;
import com.zenyte.game.content.skills.magic.spells.teleports.TeleportCollection;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.plugins.dialogue.PlainChat;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 25. aug 2018 : 22:42:26
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class RoyalSeedPod extends ItemPlugin {

	@Override
	public void handle() {
		bind("Commune", (player, item, slotId) -> {
			if (player.getNumericAttribute("Royal seed pod configuration").intValue() == 0) {
				TeleportCollection.ROYAL_SEED_POD_TELEPORT_STRONGHOLD.teleport(player);
			} else {
				TeleportCollection.ROYAL_SEED_POD_TELEPORT_HOME.teleport(player);
			}
		});
		bind("Configure", (player, item, container, slotId) -> player.getDialogueManager().start(new Dialogue(player) {
			@Override
			public void buildDialogue() {
				if (!DiaryUtil.eligibleFor(DiaryReward.WILDERNESS_SWORD3, player)
						|| !DiaryUtil.eligibleFor(DiaryReward.WILDERNESS_SWORD2, player)
						|| !DiaryUtil.eligibleFor(DiaryReward.WILDERNESS_SWORD1, player)) {
					plain("You need to have completed the easy, medium and hard Wilderness diaries to configure the Royal seed pod.");
					return;
				}
				options("Select destination for commune",
						new DialogueOption("Tree Gnome Stronghold.", () -> {
							player.addAttribute("Royal seed pod configuration", 0);
							informConfiguration(player);
						}),
						new DialogueOption("Edgeville.", () -> {
							player.addAttribute("Royal seed pod configuration", 1);
							informConfiguration(player);
						}),
						new DialogueOption("Cancel."));
			}
		}));
	}

	private final void informConfiguration(@NotNull Player player) {
		player.getDialogueManager().finish();
		player.getDialogueManager().start(new PlainChat(player,
				"Your royal seed pod configuration has been changed to teleport you to " +
						(player.getNumericAttribute("Royal seed pod configuration").intValue() == 0 ? "Tree Gnome Stronghold." : "Edgeville.")));
	}

	@Override
	public int[] getItems() {
		return new int[] { 19564 };
	}

}
