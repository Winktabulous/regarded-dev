package com.zenyte.plugins.itemonnpc;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnNPCAction;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.impl.slayer.DesertLizard;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 11. mai 2018 : 01:01:38
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class IceCoolerOnLizardAction implements ItemOnNPCAction {

    public static final Graphics GFX = new Graphics(456);

    public static final Animation ANIM = new Animation(2779);

    @Override
    public void handleItemOnNPCAction(final Player player, final Item item, final int slot, final NPC npc) {
        if (!(npc instanceof DesertLizard))
            return;
        npc.setGraphics(GFX);
        player.setAnimation(ANIM);
        player.lock(2);
        // prevent the npc from attacking the target whilst dying.
        npc.getCombat().setCombatDelay(3);
        if (npc.getHitpoints() <= 4) {
            ((DesertLizard) npc).kill(player);
        } else {
            player.sendMessage("The lizard isn't weak enough to be affected by the icy water.");
        }
        player.getInventory().deleteItem(DesertLizard.ICE_COOLER);
    }

    @Override
    public Object[] getItems() {
        return new Object[] { DesertLizard.ICE_COOLER.getId() };
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 458, 459, 460, 461, 462, 463 };
    }
}
