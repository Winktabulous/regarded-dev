package com.zenyte.game.content.treasuretrails.npcs.mimic;

import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 27/11/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MimicPlugin extends NPCPlugin {
    @Override
    public void handle() {
        bind("Challenge", new OptionHandler() {
            @Override
            public void click(final Player player, final NPC npc, final NPCOption option) {
                execute(player, npc);
            }
            @Override
            public void handle(Player player, NPC npc) {
                if (!(npc instanceof Mimic)) {
                    return;
                }
                final Mimic mimic = (Mimic) npc;
                if (!mimic.isOwner(player)) {
                    player.sendMessage("This is not your Mimic.");
                    return;
                }
                player.getBossTimer().startTracking("Mimic");
                npc.setTransformation(Mimic.ATTACKABLE_MIMIC);
            }
            @Override
            public void execute(final Player player, final NPC npc) {
                player.stopAll();
                player.setFaceEntity(npc);
                handle(player, npc);
            }
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] {Mimic.CHALLENGE_MIMIC};
    }
}
