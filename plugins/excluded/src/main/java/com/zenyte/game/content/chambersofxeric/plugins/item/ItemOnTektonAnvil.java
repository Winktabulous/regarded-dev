package com.zenyte.game.content.chambersofxeric.plugins.item;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 04/09/2019 06:57
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ItemOnTektonAnvil implements ItemOnObjectAction {

    /**
     * The object id of the large anvil the tekton smiths against.
     */
    private static final int TEKTONS_ANVIL_OBJECT = 29867;

    @Override
    public void handleItemOnObjectAction(final Player player, final Item item, final int slot, final WorldObject object) {
        player.sendMessage("You're not going to be able to do anything useful with such a weird anvil.");
    }

    @Override
    public Object[] getItems() {
        return null;
    }

    @Override
    public Object[] getObjects() {
        return new Object[]{
                TEKTONS_ANVIL_OBJECT
        };
    }
}
