package com.zenyte.plugins.renewednpc;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.treasuretrails.TreasureTrail;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

import java.util.ArrayList;
import java.util.OptionalInt;

/**
 * @author Kris | 26/11/2018 19:58
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ToolLeprechaun extends NPCPlugin {

    @Override
    public void handle() {
        bind("Exchange", (player, npc) -> GameInterface.FARMING_STORAGE.open(player));
        bind("Talk-to", (player, npc) -> {
            if (TreasureTrail.talk(player, npc)) {
                return;
            }
            player.getDialogueManager().start(new Dialogue(player, npc) {

                @Override
                public void buildDialogue() {
                    npc("Ah, 'tis a foine day to be sure! Were yez wantin' me to store yer tools, or maybe ye might " + "be wantin' yer stuff back from me?");
                    appendOptions(OptionalInt.empty());
                    player(25, "What can you store?");
                    npc("We'll hold onto yer rake, yer seed dibber, yer spade, yer secateurs, yer waterin' can and " + "yer trowel - but mind it's not one of them fancy trowels only archaeologists use!");
                    npc("We'll take a few buckets off yer hands too, and even yer compost, supercompost an' " + "ultracompost! Also plant sure vials.");
                    npc("Aside from that, if ye hands me yer farming produce, I can mebbe change it into banknotes for ye.");
                    npc("So... do ye want to be using the store?");
                    appendOptions(OptionalInt.of(1));
                    player(50, "What do you do with the tools you're storing?");
                    npc("We leprechauns have a shed where we keep 'em. It's a magic shed, so ye can get yer items " + "back from any of us leprechauns whenever ye want. Saves ye havin' to carry loads of " + "stuff around the country!");
                    npc("So... do ye want to be using the store?");
                    appendOptions(OptionalInt.of(2));
                    player(75, "No thanks, I'll keep hold of my stuff.");
                    npc("Ye must be dafter than ye look if ye likes luggin' yer tools everywhere ye goes!");
                }

                private void appendOptions(final OptionalInt excludedOption) {
                    final ArrayList<Dialogue.DialogueOption> options = new ArrayList<DialogueOption>();
                    options.add(new DialogueOption("Yes please.", () -> GameInterface.FARMING_STORAGE.open(player)));
                    options.add(new DialogueOption("What can you store?", key(25)));
                    options.add(new DialogueOption("What do you do with the tools you're storing?", key(50)));
                    options.add(new DialogueOption("No thanks, I'll keep hold of my stuff.", key(75)));
                    excludedOption.ifPresent(options::remove);
                    options("What would you like to say?", options.toArray(new DialogueOption[0]));
                }
            });
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.TOOL_LEPRECHAUN, NpcId.TOOL_LEPRECHAUN_757, NpcId.TOOL_LEPRECHAUN_7757 };
    }
}
