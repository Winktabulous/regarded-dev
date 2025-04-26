package com.zenyte.plugins.equipment.equip;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 21/03/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class AncientWyvernShieldEquipPlugin implements EquipPlugin {

    private static final Animation anim = new Animation(3996);
    private static final Graphics gfx = new Graphics(1395, 15, 96);

    @Override
    public boolean handle(final Player player, final Item item, final int slotId, final int equipmentSlot) {
        if (slotId == -1)
            return true;
        player.setGraphics(gfx);
        player.setAnimation(anim);
        return true;
    }

    @Override
    public int[] getItems() {
        return new int[] {ItemId.ANCIENT_WYVERN_SHIELD, ItemId.ANCIENT_WYVERN_SHIELD_21634 };
    }

}
