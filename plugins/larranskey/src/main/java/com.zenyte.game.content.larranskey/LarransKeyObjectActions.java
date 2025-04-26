package com.zenyte.game.content.larranskey;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.Ladder;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

public class LarransKeyObjectActions implements ObjectAction {

    private static final int UDI_LARRANS_CHEST_OBJECT_ID = 34829;
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        int id = object.getId();

        if (option.equalsIgnoreCase("check")) {
            if (id == LarransKey.LARRANS_CHEST_SMALL_OBJECT_ID) {
                LarransKey.sendCheckMessage(player, false);
            }
            if (id == LarransKey.LARRANS_CHEST_LARGE_OBJECT_ID || id == UDI_LARRANS_CHEST_OBJECT_ID) {
                LarransKey.sendCheckMessage(player, true);
            }
        }

        if (option.equalsIgnoreCase("unlock")) {
            if (id == LarransKey.LARRANS_CHEST_SMALL_OBJECT_ID) {
                LarransKey.openChest(player, object);
            }
            if (id == LarransKey.LARRANS_CHEST_LARGE_OBJECT_ID || id == UDI_LARRANS_CHEST_OBJECT_ID) {
                LarransKey.openChest(player, object);
            }
        }

    }

    @Override
    public Object[] getObjects() {
        return new Object[] {
                UDI_LARRANS_CHEST_OBJECT_ID,
                LarransKey.LARRANS_CHEST_SMALL_OBJECT_ID,
                LarransKey.LARRANS_CHEST_LARGE_OBJECT_ID// SMALL CHEST
        };
    }
}