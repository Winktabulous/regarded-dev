package com.zenyte.plugins.itemonobject;

import com.zenyte.game.content.multicannon.DwarfMultiCannon;
import com.zenyte.game.content.multicannon.DwarfMultiCannonType;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Christopher
 * @since 1/23/2020
 */
@SuppressWarnings("unused")
public class CannonLoadAction implements ItemOnObjectAction {

    @Override
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        if (object.equals(DwarfMultiCannon.placedCannons.get(player.getUsername()))) {
            player.getDwarfMulticannon().loadCannon();
        } else {
            player.sendMessage("This is not your cannon.");
        }
    }

    @Override
    public Object[] getItems() {
        return new Object[] { ItemId.GRANITE_CANNONBALL, ItemId.CANNONBALL };
    }

    @Override
    public Object[] getObjects() {
        DwarfMultiCannonType type1 = DwarfMultiCannonType.REGULAR;
        DwarfMultiCannonType type2 = DwarfMultiCannonType.ORNAMENT;
        return new Object[] {
                type1.getBaseLoc(), type1.getStandLoc(), type1.getBarrelsLoc(), type1.getCannonLoc(), type1.getBrokenCannonLoc(),
                type2.getBaseLoc(), type2.getStandLoc(), type2.getBarrelsLoc(), type2.getCannonLoc(), type2.getBrokenCannonLoc(),
        };
    }

}
