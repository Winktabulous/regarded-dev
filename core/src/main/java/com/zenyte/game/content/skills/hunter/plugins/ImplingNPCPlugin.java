package com.zenyte.game.content.skills.hunter.plugins;

import com.zenyte.game.content.skills.hunter.actions.CatchImplingAction;
import com.zenyte.game.content.skills.hunter.node.Impling;
import com.zenyte.game.content.skills.hunter.npc.ImplingNPC;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;

import java.util.Objects;

/**
 * @author Kris | 27/11/2018 11:30
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ImplingNPCPlugin extends NPCPlugin {
    @Override
    public void handle() {
        bind("Catch", new OptionHandler() {
            @Override
            public void handle(Player player, NPC npc) {
                final Impling impling = Objects.requireNonNull(Impling.get(npc.getId()));
                player.getActionManager().setAction(new CatchImplingAction(impling, (ImplingNPC) npc));
            }
            @Override
            public void click(final Player player, final NPC npc, final NPCOption option) {
                execute(player, npc);
            }
            @Override
            public void execute(final Player player, final NPC npc) {
                player.stopAll();
                handle(player, npc);
            }
        });
    }

    @Override
    public int[] getNPCs() {
        return Impling.implings.keySet().toIntArray();
    }
}
