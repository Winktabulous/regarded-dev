package com.zenyte.plugins.object;

import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.CastleWarsTable;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class CastlewarsTableAction implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (!player.inArea("Castle Wars")) {
            player.sendMessage("You aren\'t in a castle wars game!");
            return;
        }
        if (!player.getInventory().hasFreeSlots()) {
            player.sendMessage("Not enough space in your inventory.");
        }
        final CastleWarsTable table = CastleWarsTable.getData(object.getId());
        final int slotsNeeded = option.equals("Take-5") ? 5 : 1;
        player.faceObject(object);
        player.setAnimation(Animation.GRAB);
        player.getInventory().addItem(table.getLoot().getId(), Math.min(player.getInventory().getFreeSlots(), slotsNeeded));
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.TABLE_4458, ObjectId.TABLE_4459, ObjectId.TABLE_4460, ObjectId.TABLE_4461, ObjectId.TABLE_4462, ObjectId.TABLE_4463, ObjectId.TABLE_4464 };
    }
}
