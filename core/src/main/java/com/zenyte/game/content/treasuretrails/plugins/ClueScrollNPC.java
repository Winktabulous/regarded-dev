package com.zenyte.game.content.treasuretrails.plugins;

import com.zenyte.game.content.skills.farming.FarmingPatch;
import com.zenyte.game.content.treasuretrails.TreasureTrail;
import com.zenyte.game.content.treasuretrails.clues.Anagram;
import com.zenyte.game.content.treasuretrails.clues.CipherClue;
import com.zenyte.game.content.treasuretrails.clues.CrypticClue;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.dialogue.impl.NPCChat;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 07/04/2019 22:07
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ClueScrollNPC extends NPCPlugin {

    private static final String[] genericResponses = new String[] { "Good morning.", "Hello.", "How do you do?", "Hello, %s.", "Good morning, %s." };

    @Override
    public void handle() {
        bind("Talk-to", this::talk);
        bind("Talk", this::talk);
    }

    private final void talk(@NotNull final Player player, @NotNull final NPC npc) {
        if (!TreasureTrail.talk(player, npc)) {
            if (npc.getId() == 5735) {
                // Immenizz - Puro-Puro Impling
                player.getDialogueManager().start(new Dialogue(player, npc) {

                    @Override
                    public void buildDialogue() {
                        player("Hello. So, what is this place?");
                        npc("Immenizz", "This is my home, mundane human! What do you have in your pockets? Something tasty?");
                        player("Stay out of my pockets! I don't have anything that you want.");
                        npc("Immenizz", "Ah, but do you have anything that *you* want?");
                        player("Of course I do!");
                        npc("Immenizz", "Then you have something that implings want.");
                        player("Eh?");
                        npc("Immenizz", "We want things you people want. They are tasty to us! The more you want them, the tastier they are!");
                        player("So, you collect things that humans want? Interesting... So, what would happen if I caught an impling in a butterfly net?");
                        npc("Immenizz", "Don't do that! That would be cruel. But chase us, yes! That is good. " + "Implings are not easy to catch. Especially ones with really tasty food.");
                        player("So, some of these implings have things that I will really want? Hmm, maybe it would be worth my while trying to catch some.");
                    }
                });
            } else {
                player.getDialogueManager().start(new NPCChat(player, npc.getId(), String.format(Utils.random(genericResponses), player.getName())));
            }
        }
    }

    @Override
    public int[] getNPCs() {
        final IntOpenHashSet set = new IntOpenHashSet(CrypticClue.npcMap.keySet());
        set.addAll(Anagram.npcMap.keySet());
        set.addAll(CipherClue.npcMap.keySet());
        set.removeAll(new IntOpenHashSet(new int[] { NpcId.WYSON_THE_GARDENER, NpcId.DONIE, NpcId.PILES, NpcId.SQUIRE_VETERAN, NpcId.KAMFREENA, NpcId.DOMINIC_ONION, NpcId.GHOMMAL, NpcId.HAIRDRESSER, NpcId.HANS, NpcId.KEY_MASTER, NpcId.MONK_OF_ENTRANA, NpcId.MONK_OF_ENTRANA_1166, NpcId.MONK_OF_ENTRANA_1167, NpcId.MONK_OF_ENTRANA_1168, NpcId.MONK_OF_ENTRANA_1169, NpcId.TURAEL, NpcId.MAZCHNA, NpcId.VANNAKA, NpcId.CHAELDAR, NpcId.DURADEL, 490, NpcId.NIEVE, NpcId.CAPTAIN_TOBIAS, NpcId.SEAMAN_LORRIS, NpcId.SEAMAN_THRESNOR, 5034, NpcId.OZIACH, NpcId.ELLIS, NpcId.SBOTT, NpcId.TOOL_LEPRECHAUN, NpcId.TOOL_LEPRECHAUN_757, NpcId.TOOL_LEPRECHAUN_7757, NpcId.WIZARD_CROMPERTY, NpcId.WIZARD_CROMPERTY_8481, NpcId.BRIMSTAIL_11431, NpcId.COOK_4626, NpcId.AABLA, NpcId.SABREEN, NpcId.SURGEON_GENERAL_TAFANI, NpcId.JARAAH, 311, NpcId.OTTO_GODBLESSED, 1328, 1329, 1330, 1331, 1332, 1333, 1334, 2979, NpcId.MAWNIS_BUROWGAR, NpcId.WIZARD_CROMPERTY, NpcId.WIZARD_CROMPERTY, NpcId.PROSPECTOR_PERCY, NpcId.PARTY_PETE }));
        set.removeAll(FarmingPatch.getPatchSetByGardeners().keySet());
        set.remove(NpcId.ABBOT_LANGLEY);
        set.remove(NpcId.CANDLE_MAKER);
        return set.toIntArray();
    }
}
