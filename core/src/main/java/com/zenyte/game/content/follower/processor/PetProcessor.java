package com.zenyte.game.content.follower.processor;

import com.zenyte.game.content.follower.impl.BossPet;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;
import it.unimi.dsi.fastutil.ints.IntSet;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Tommeh | 22-11-2018 | 19:11
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class PetProcessor extends DropProcessor {
    @Override
    public void attach() {
        for (final BossPet pet : BossPet.VALUES) {
            if (pet.getBossIds() != null && pet.getBossIds()[0] == -1) {
                continue;
            }
            appendDrop(new DisplayedDrop(pet.getItemId(), 1, 1, pet.getRarity(null, pet.getBossIds()[0]), (player, id) -> ArrayUtils.contains(pet.getBossIds(), id), pet.getBossIds()));
        }
    }

    @Override
    public void onDeath(final NPC npc, final Player killer) {
        final BossPet pet = BossPet.getByBossNPC(npc.getId());
        if (pet == null) {
            return;
        }
        pet.roll(killer, pet.getRarity(killer, npc.getId()));
    }

    @Override
    public int[] ids() {
        final IntSet set = BossPet.BOSS_PETS_BY_BOSS_NPC_ID.keySet();
        set.remove(ChaosFanaticProcessor.NPC_ID); //remove chaos fanatic since he has his own pet processor
        return set.toIntArray();
    }
}
