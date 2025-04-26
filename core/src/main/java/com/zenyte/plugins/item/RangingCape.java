package com.zenyte.plugins.item;

import com.zenyte.game.model.item.SkillcapePerk;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 16/03/2019 02:52
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class RangingCape extends ItemPlugin {
    @Override
    public void handle() {
        bind("Commune", (player, item, slotId) -> player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                final boolean isGatheringJunk = player.getAttributes().containsKey("avasDeviceRetrieve");
                if (isGatheringJunk) {
                    plain("The undead chicken can protect some of your ammunition while you're ranging, and will also" +
                            " gather random metal items for you.");
                    options("Ask it to stop gathering junk?", new DialogueOption("Yes", () -> {
                        player.getAttributes().remove("avasDeviceRetrieve");
                        setKey(205);
                    }), new DialogueOption("No"));
                    plain(205, "You somehow communicate your message to the undead chicken. Henceforth it will no " +
                            "longer gather up random metal items while you've got it equipped.");
                } else {
                    plain("The undead chicken understands that you currently don't want it to accumulate random metal" +
                            " items while you've got it equipped.");
                    options("Ask it to start gathering junk?", new DialogueOption("Yes", () -> {
                        player.getAttributes().put("avasDeviceRetrieve", true);
                        setKey(205);
                    }), new DialogueOption("No"));
                    plain(205, "You somehow communicate your message to the undead chicken. Henceforth it will gather" +
                            " up random metal items while you've got it equipped.");
                }
            }
        }));
    }

    @Override
    public int[] getItems() {
        return SkillcapePerk.RANGED.getSkillCapes();
    }
}
