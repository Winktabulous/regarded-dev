package com.zenyte.plugins.renewednpc;

import com.zenyte.game.content.treasuretrails.TreasureTrail;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.dialogue.ProspectorPercyD;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class ProspectorPercyAction extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", new OptionHandler() {

            @Override
            public void handle(final Player player, final NPC npc) {
                if (!TreasureTrail.talk(player, npc)) {
                    player.getDialogueManager().start(new ProspectorPercyD(player, npc.getId()));
                }
            }
        });
        bind("Trade", new OptionHandler() {

            @Override
            public void handle(final Player player, final NPC npc) {
                player.openShop("Prospector Percy's Nugget Shop");
            }
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.PROSPECTOR_PERCY };
    }
}
