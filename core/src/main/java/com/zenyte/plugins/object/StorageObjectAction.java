package com.zenyte.plugins.object;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import it.unimi.dsi.fastutil.ints.IntArrayList;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class StorageObjectAction implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        //if(option.toLowerCase().equals("open"))
    }

    @Override
    public Object[] getObjects() {
        final IntArrayList list = new IntArrayList();
        list.add(25387);
        list.add(25388);

        return list.toArray(new Object[list.size()]);
    }
}
