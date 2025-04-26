package com.zenyte.game.content.boss.grotesqueguardians.plugins;

import com.zenyte.game.content.boss.grotesqueguardians.boss.Dusk;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnNPCAction;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.impl.slayer.Gargoyle;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 02/08/2019 | 14:35
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class ItemOnGrotesqueGuardianAction implements ItemOnNPCAction {
    
    private static final Animation ANIM = new Animation(401);
    
    @Override
    public void handleItemOnNPCAction(final Player player, final Item item, final int slot, final NPC npc) {
        //player.setAnimation(ANIM);
        //player.lock(1);
        if (npc.getHitpoints() <= 9) {
            if (npc instanceof Dusk && ((Dusk) npc).isPerformingPrisonAttack()) {
                player.sendMessage("You cannot finish off Dusk during his special attack.");
                return;
            }
            npc.getTemporaryAttributes().put("used_rock_hammer", true);
            npc.sendDeath();
        } else {
            player.sendMessage("Dawn isn't weak enough to be affected by the rock hammer.");
        }
    }
    
    @Override
    public Object[] getItems() {
        return new Object[]{Gargoyle.ROCK_HAMMER.getId(), Gargoyle.GRANITE_HAMMER.getId(), 21754};
    }
    
    @Override
    public Object[] getObjects() {
        return new Object[]{"Dawn", "Dusk"};
    }
    
}
