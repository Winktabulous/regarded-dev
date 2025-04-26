package com.zenyte.game.content.achievementdiary.plugins.item;

import com.zenyte.game.content.achievementdiary.plugins.DiaryItem;
import com.zenyte.game.content.skills.magic.spells.teleports.TeleportCollection;
import com.zenyte.plugins.dialogue.OptionDialogue;

/**
 * @author Tommeh | 22-4-2019 | 17:52
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class WildernessSword extends DiaryItem {

    @Override
    public void handle() {
        bind("Teleport", (player, item, slotId) -> {
            player.getDialogueManager().start(new OptionDialogue(player, "Teleport to Level 47 Wilderness?",
                            new String[] { "Yes.", "No." },
                            new Runnable[] { () -> {
                                int limit = item.getId() == 13110 ? 1 : -1;
                                int usedTeleports = player.getVariables().getFountainOfRuneTeleports();
                                if (limit != -1 && usedTeleports >= limit) {
                                    player.sendMessage("You have used up your daily teleports for today.");
                                    return;
                                }
                                if (limit != -1) {
                                    player.getVariables().setFountainOfRuneTeleports(usedTeleports + 1);
                                    player.getTemporaryAttributes().put("fountain of rune restricted teleport", true);
                                }
                                TeleportCollection.WILDERNESS_SWORD_FOUNTAIN_OF_RUNE.teleport(player);
                            }, null }));
        });
    }

    @Override
    public int[] getItems() {
        return new int[] { 13108, 13109, 13110, 13111 };
    }
}
