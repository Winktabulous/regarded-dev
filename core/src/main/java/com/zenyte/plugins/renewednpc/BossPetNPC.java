package com.zenyte.plugins.renewednpc;

import com.zenyte.game.content.follower.Follower;
import com.zenyte.game.content.follower.impl.BossPet;
import com.zenyte.game.content.follower.impl.MiscPet;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.utils.IntArray;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;

import java.util.Objects;

/**
 * @author Kris | 24/04/2019 15:01
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class BossPetNPC extends NPCPlugin {
    @Override
    public void handle() {
        bind("Metamorphosis", (player, npc) -> {
            if (!(npc instanceof Follower) || !Objects.equals(((Follower) npc).getOwner(), player)) {
                player.sendMessage("This is not your pet.");
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
        final IntOpenHashSet set = new IntOpenHashSet(BossPet.BOSS_PETS_BY_PET_NPC_ID.keySet());
        set.removeAll(new IntArrayList(IntArray.of(7520, 8201, 8202, 8203, 8204, 8205, NpcId.SRARACHA_2144, NpcId.SRARACHA_11159, NpcId.SRARACHA_11160, NpcId.MUPHIN, NpcId.MUPHIN_12006, NpcId.MUPHIN_12007)));
        for (final MiscPet misc : MiscPet.VALUES) {
            if (misc.toString().startsWith("CHAMELEON")) {
                set.add(misc.petId());
            }
        }
        return set.toIntArray();
    }
}
