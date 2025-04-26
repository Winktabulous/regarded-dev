package com.zenyte.game.content.skills.hunter.plugins;

import com.zenyte.game.content.skills.hunter.object.Birdhouse;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 25/06/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MagicOrbOnBirdhouse implements ItemOnObjectAction {

    @Override
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        final Birdhouse birdhouse = player.getHunter().findBirdhouse(object.getId()).orElseThrow(RuntimeException::new);
        final long fillTime = birdhouse.getFillTime();
        if (fillTime != 0 && fillTime != Long.MAX_VALUE) {
            birdhouse.setFillTime(1);
        }
    }

    @Override
    public Object[] getItems() {
        return new Object[] { ItemId.MAGICAL_ORB_A };
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 30565, 30566, 30567, 30568 };
    }
}
