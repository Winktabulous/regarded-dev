package com.zenyte.game.content.boss.bryophyta.plugins;

import com.near_reality.game.content.skills.woodcutting.AxeDefinition;
import com.zenyte.game.content.skills.woodcutting.AxeDefinitions;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnNPCAction;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.player.Player;
import it.unimi.dsi.fastutil.ints.IntArrayList;

/**
 * @author Tommeh | 17/05/2019 | 20:01
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
@SuppressWarnings("unused")
public class ToolOnGrowthlingAction implements ItemOnNPCAction {

    private static final Animation CUT_ANIM = new Animation(405);

    @Override
    public void handleItemOnNPCAction(Player player, Item item, int slot, NPC npc) {
        if (!npc.isDead() && npc.getHitpoints() <= 2) {
            player.setAnimation(CUT_ANIM);
            npc.sendDeath();
        } else {
            player.sendMessage("The growthling isn't weak enough yet.");
        }
    }

    @Override
    public Object[] getItems() {
        final IntArrayList list = new IntArrayList();
        for (final AxeDefinition def : AxeDefinitions.VALUES) {
            list.add(def.getItemId());
        }
        list.add(5329);
        list.add(7409);
        return list.toArray(new Object[0]);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { NpcId.GROWTHLING };
    }

}
