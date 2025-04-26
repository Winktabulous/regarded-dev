package com.zenyte.plugins.itemonobject;

import com.zenyte.game.content.minigame.warriorsguild.magicalanimator.MagicalAnimator;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

import java.util.ArrayList;
import java.util.List;

import static com.zenyte.game.content.minigame.warriorsguild.magicalanimator.MagicalAnimator.ARMOUR_SETS;

/**
 * @author Kris | 18. dets 2017 : 3:09.27
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class MagicalAnimatorAction implements ItemOnObjectAction {

    @Override
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        MagicalAnimator.handleAnimator(player, item, object);
    }

    @Override
    public Object[] getItems() {
        final List<Object> list = new ArrayList<Object>();
        for (int[] sets : ARMOUR_SETS) {
            for (int i : sets) {
                list.add(i);
            }
        }
        return list.toArray(new Object[list.size()]);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.MAGICAL_ANIMATOR };
    }
}
