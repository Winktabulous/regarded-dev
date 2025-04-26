package com.zenyte.game.content.treasuretrails.plugins;

import com.zenyte.game.content.treasuretrails.ClueItem;
import com.zenyte.game.content.treasuretrails.TreasureTrail;
import com.zenyte.game.content.treasuretrails.clues.CharlieTask;
import com.zenyte.game.content.treasuretrails.clues.Clue;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnNPCAction;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.impl.NPCChat;

import java.util.List;

/**
 * @author Kris | 04/01/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class CharlieNPC implements ItemOnNPCAction {

    @Override
    public void handleItemOnNPCAction(final Player player, final Item item, final int slot, final NPC npc) {
        final List<String> list = TreasureTrail.getCluesList(item);
        assert list != null;
        assert list.size() >= 1;
        final String constantName = list.get(0);
        final Clue clueScroll = TreasureTrail.getClues().get(constantName);
        if (!(clueScroll instanceof CharlieTask)) {
            player.sendMessage("Nothing interesting happens.");
            return;
        }
        npc.setInteractingWith(player);
        final int stage = item.getNumericAttribute("Charlie Stage").intValue();
        if (stage == 0) {
            item.setAttribute("Charlie Stage", 1);
            player.getDialogueManager().start(new NPCChat(player, npc.getId(), "Ah, just what I was looking for. I " + "have a challenge for you. If you complete it, I'll give you something."));
        } else if (stage == 1) {
            player.getDialogueManager().start(new NPCChat(player, npc.getId(), "You haven't completed the challenge " + "yet."));
        } else if (stage == 2) {
            TreasureTrail.continueCharlieTask(player, item, slot);
        }
    }

    @Override
    public Object[] getItems() {
        return new Object[] { ClueItem.BEGINNER.getClue() };
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 5209 };
    }
}
