package com.zenyte.plugins.object;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 26/01/2019 17:43
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class DarkAltar implements ObjectAction {

    private static final Animation PRAY_ANIM = new Animation(645);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.equals("Venerate")) {
            final Inventory inventory = player.getInventory();
            int count = 0;
            for (int i = 0; i < 28; i++) {
                final Item item = inventory.getItem(i);
                if (item == null || item.getId() != 13445)
                    continue;
                item.setId(13446);
                count++;
            }
            if (count == 0) {
                player.sendMessage("You haven\'t got any dense essence blocks.");
                return;
            }
            player.getSkills().addXp(SkillConstants.RUNECRAFTING, 2.5F * count);
            player.getPrayerManager().drainPrayerPoints(count);
            player.setAnimation(PRAY_ANIM);
            inventory.refreshAll();
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.DARK_ALTAR };
    }
}
