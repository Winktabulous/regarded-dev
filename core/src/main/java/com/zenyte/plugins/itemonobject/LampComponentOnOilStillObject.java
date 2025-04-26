package com.zenyte.plugins.itemonobject;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Tommeh | 03/05/2019 | 13:23
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class LampComponentOnOilStillObject implements ItemOnObjectAction {

    @Override
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        final int id = item.getId();
        if (id == 1939) {
            // swamp tar
            if (player.getBooleanAttribute("oil_still_has_tar")) {
                player.sendMessage("There is already lamp oil in the still.");
                return;
            }
            player.sendMessage("You refine some swamp tar into lamp oil.");
            player.getInventory().deleteItem(item.getId(), 1);
            player.addAttribute("oil_still_has_tar", 1);
        } else {
            if (!player.getBooleanAttribute("oil_still_has_tar")) {
                player.sendMessage("You cannot fill your lamp without having swamp tar in the oil still.");
                return;
            }
            final Item lamp = new Item(id == 4525 ? 4522 : id == 4535 ? 4537 : 4548);
            player.sendMessage("You put some oil in the " + item.getName().split(" ")[1].toLowerCase() + ".");
            player.getInventory().set(slot, lamp);
            player.addAttribute("oil_still_has_tar", 0);
        }
    }

    @Override
    public Object[] getItems() {
        return new Object[] { 1939, 4525, 4535, 4546 };
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 5908 };
    }
}
