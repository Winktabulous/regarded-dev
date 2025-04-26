package com.zenyte.plugins.renewednpc;

import com.zenyte.game.content.treasuretrails.TreasureTrail;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.dialogue.DuelArenaHealerD;

/**
 * @author Kris | 26/11/2018 18:45
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Healer extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {
            if (TreasureTrail.talk(player, npc)) {
                return;
            }
            player.getDialogueManager().start(new DuelArenaHealerD(player, npc, false));
        });
        bind("Heal", (player, npc) -> player.getDialogueManager().start(new DuelArenaHealerD(player, npc, true)));
    }

    public static void heal(final Player player, final NPC npc, final boolean heal) {
        player.getDialogueManager().start(new DuelArenaHealerD(player, npc, heal));
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.AABLA, NpcId.SABREEN, NpcId.SURGEON_GENERAL_TAFANI, NpcId.JARAAH };
    }
}
