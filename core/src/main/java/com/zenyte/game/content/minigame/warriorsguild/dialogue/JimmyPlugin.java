package com.zenyte.game.content.minigame.warriorsguild.dialogue;

import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 16. dets 2017 : 7:28.36
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class JimmyPlugin extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {
            player.getDialogueManager().start(new Dialogue(player, npc) {

                @Override
                public void buildDialogue() {
                    npc("'Ello there.");
                    options();
                    player(10, "Tell me about this room?");
                    npc("Well... s'like thish...");
                    npc("Thish here'sh a shtore room right?");
                    player("A store room you mean?");
                    npc("That'sh what I shaid! *HIC* A shtore room.... Now " + "technic'ly shpeaking, I should be outshide guarding it...");
                    player("But you just nipped in to have a quick drink?");
                    npc("Yep... and to practish.");
                    player("Practish? I mean.. practise what?");
                    npc("KegAle balancin. I'm the besht.");
                    options();
                    player(30, "Tell me how to balance kegs?");
                    npc("Yer very very shtrange. But.... you pick the keg up, " + "and balance it on yer head, then you pick another keg " + "up and put that on top. S'really very eashy.");
                    player("Eashy?");
                    npc("Yesh. Eashy.");
                    npc("But you couldn't ever balansh ash many ash meee!");
                    player("That sounds like a challenge, I'll show you!");
                    options();
                    player(50, "May I claim my tokens please?");
                    npc("Well... err.. ish not offishal or anyfin... but I got the " + "ledger of tokensh 'ere. I'll jus' err write it in!");
                    player("Won't they know?");
                    npc("Nah... hic.... I'm a wizsh at copyin' signaturesh! Jus' " + "ashk an offishal mem'er of shtaff like Shloane fer yer " + "tokensh.");
                    plain("The rather drunk Jimmy scribbles the tokens you've earned from " + "KegAle Balancing in the Ledger so that you can claim them from an " + "official member of training staff.");
                    player(70, "Bye!");
                    npc("Shure you wouldn't like an ickle drinkie fore yer go?");
                    player("No thanks, got things to do, people to see, tokens to earn...");
                }

                private void options() {
                    options(TITLE, "Tell me about this room.", "Tell me how to balance kegs.", "May I claim my tokens please?", "Bye!").onOptionOne(() -> setKey(10)).onOptionTwo(() -> setKey(30)).onOptionThree(() -> setKey(50)).onOptionFour(() -> setKey(70));
                }
            });
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.JIMMY };
    }
}
