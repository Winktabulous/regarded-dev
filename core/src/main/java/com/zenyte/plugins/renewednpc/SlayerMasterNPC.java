package com.zenyte.plugins.renewednpc;

import com.zenyte.game.content.skills.slayer.SlayerMaster;
import com.zenyte.game.content.skills.slayer.dialogue.SlayerMasterAssignmentD;
import com.zenyte.game.content.skills.slayer.dialogue.SlayerMasterD;
import com.zenyte.game.content.skills.slayer.dialogue.TuraelAssignmentD;
import com.zenyte.game.content.skills.slayer.dialogue.TuraelD;
import com.zenyte.game.content.treasuretrails.TreasureTrail;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Analytics;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 25/11/2018 16:27
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SlayerMasterNPC extends NPCPlugin {

    @Override
    public void handle() {
        bind("Rewards", (player, npc) -> {
            player.getSlayer().openInterface();
            Analytics.flagInteraction(player, Analytics.InteractionType.SLAYER_MASTER);
        });
        bind("Talk-to", (Player player, NPC npc) -> {
            if (TreasureTrail.talk(player, npc)) {
                return;
            }
            Analytics.flagInteraction(player, Analytics.InteractionType.SLAYER_MASTER);
            if (npc.getId() == SlayerMaster.TURAEL.getNpcId()) {
                player.getDialogueManager().start(new TuraelD(player, npc));
            } else {
                player.getDialogueManager().start(new SlayerMasterD(player, npc));
            }
        });
        bind("Assignment", (player, npc) -> {
            Analytics.flagInteraction(player, Analytics.InteractionType.SLAYER_MASTER);
            if (npc.getId() == SlayerMaster.TURAEL.getNpcId()) {
                player.getDialogueManager().start(new TuraelAssignmentD(player, npc));
            } else {
                player.getDialogueManager().start(new SlayerMasterAssignmentD(player, npc));
            }
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.TURAEL, NpcId.MAZCHNA, NpcId.VANNAKA, NpcId.CHAELDAR, NpcId.DURADEL, 490, NpcId.NIEVE, NpcId.KONAR_QUO_MATEN };
    }
}
