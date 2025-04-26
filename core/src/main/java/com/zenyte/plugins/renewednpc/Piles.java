package com.zenyte.plugins.renewednpc;

import com.zenyte.game.content.treasuretrails.TreasureTrail;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Tommeh | 2-2-2019 | 22:57
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class Piles extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {
            if (TreasureTrail.talk(player, npc)) {
                return;
            }
            player.getDialogueManager().start(new Dialogue(player, npc) {

                @Override
                public void buildDialogue() {
                    npc("Hello. I can convert items to banknotes, for 50 coins per item. Just hand me the items you'd like me to convert.");
                    options(TITLE, "Who are you?", "Thanks.").onOptionOne(() -> setKey(5)).onOptionTwo(() -> setKey(8));
                    player(5, "Who are you?");
                    npc("I'm Piles. I lived in Draynor Village when I was young, where I saw three men working in the market, converting items to banknotes.");
                    npc("Their names were Niles, Miles and Giles. I'm trying to be like them, so I've changed my name and started this business here.");
                    player("Thanks.");
                }
            });
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.PILES };
    }
}
