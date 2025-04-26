package com.zenyte.plugins.renewednpc;

import com.zenyte.game.content.follower.Follower;
import com.zenyte.game.content.follower.impl.BossPet;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;

import java.util.Objects;

/**
 * @author Kris | 24/04/2019 15:12
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ChambersOfXericBossPetNPC extends NPCPlugin {

    @Override
    public void handle() {
        bind("Metamorphosis", (player, npc) -> {
            if (!(npc instanceof Follower) || !Objects.equals(((Follower) npc).getOwner(), player)) {
                player.sendMessage("This is not your pet.");
                return;
            }
            if (player.getNumericAttribute("Metamorphic dust sprinkle").intValue() <= 0) {
                player.sendMessage("You haven't unlocked the metamorphosis ability yet.");
                return;
            }
            final int target = BossPet.metamorphosisMap.getOrDefault(npc.getId(), -1);
            if (target == -1) {
                player.sendMessage("Nothing interesting happens.");
                return;
            }
            npc.setTransformation(target);
            player.setPetId(target);
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.OLMLET_7520, NpcId.PUPPADILE_8201, NpcId.TEKTINY_8202, NpcId.VANGUARD_8203, NpcId.VASA_MINIRIO_8204, NpcId.VESPINA_8205 };
    }
}
