package com.zenyte.plugins.renewednpc;

import com.zenyte.game.GameInterface;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Setting;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import mgi.types.config.enums.Enums;

import java.util.List;

/**
 * @author Kris | 26/11/2018 19:32
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Diango extends NPCPlugin {
    @Override
    public void handle() {
        bind("Redeem-code", (player, npc) -> player.sendInputString("Please enter your code.", value -> {
            if (value.equalsIgnoreCase("osrsrf2014")) {
                if (player.getBooleanSetting(Setting.UNLOCKED_RUNEFEST_HOME_TELEPORT_ANIMATION)) {
                    player.sendMessage("You've already redeemed that code.");
                    return;
                }
                player.sendMessage("You have unlocked: " + Colour.MAROON + "RuneFest 2014 home teleport animation!");
                player.sendMessage("Right-click on the Home Teleport spell to restore the default animation.");
                player.getSettings().setSetting(Setting.UNLOCKED_RUNEFEST_HOME_TELEPORT_ANIMATION, 1);
                player.getSettings().setSetting(Setting.USING_RUNEFEST_TELEPORT_ANIMATION, 1);
            }
        }));
        bind("Holiday-items", (player, npc) -> {
            final List<Integer> trackedHolidayItems = player.getTrackedHolidayItems();
            for (final Int2IntMap.Entry entry : Enums.DIANGO_ITEM_RETRIEVAL.getValues().int2IntEntrySet()) {
                if (!trackedHolidayItems.contains(entry.getIntValue()) || player.containsItem(entry.getIntValue())) {
                    continue;
                }
                GameInterface.DIANGO_ITEM_RETRIEVAL.open(player);
                return;
            }
            player.getDialogueManager().start(new Dialogue(player, npc) {
                @Override
                public void buildDialogue() {
                    npc("You have no holiday items to reclaim from me as of right now.");
                }
            });
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] {1308};
    }
}
