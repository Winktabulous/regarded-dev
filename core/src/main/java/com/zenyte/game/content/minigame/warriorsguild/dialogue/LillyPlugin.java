package com.zenyte.game.content.minigame.warriorsguild.dialogue;

import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 16. dets 2017 : 5:29.24
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class LillyPlugin extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {
            player.getDialogueManager().start(new Dialogue(player, npc) {

                @Override
                public void buildDialogue() {
                    npc("Uh..... hi... didn't see you there. Can.... I help?");
                    player("Umm... do you sell potions?");
                    npc("Erm... yes. When I'm not drinking them.");
                    options(TITLE, "I'd like to see what you have for sale.", "That's a pretty wall hanging.", "Bye!").onOptionOne(() -> setKey(10)).onOptionTwo(() -> setKey(30)).onOptionThree(() -> setKey(50));
                    player(10, "I'd like to see what you have for sale.");
                    npc("Of course...").executeAction(() -> player.openShop("Warrior Guild Potion Shop"));
                    player(30, "That's a pretty wall hanging.");
                    npc("Do you think so? I made it myself.");
                    player("Really? Is that why there's all this cloth and dye around?");
                    npc("Yes, it's a hobby of mine when I'm.... relaxing.");
                    player(50, "Bye!");
                    npc("Have fun and come back soon!");
                }
            });
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.LILLY };
    }
}
