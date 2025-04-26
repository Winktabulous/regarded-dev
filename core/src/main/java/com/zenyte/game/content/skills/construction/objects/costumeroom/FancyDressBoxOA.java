package com.zenyte.game.content.skills.construction.objects.costumeroom;

import com.zenyte.game.content.skills.construction.Construction;
import com.zenyte.game.content.skills.construction.ObjectInteraction;
import com.zenyte.game.content.skills.construction.RoomReference;
import com.zenyte.game.content.skills.construction.costume.FancyDressData;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 3. march 2018 : 18:11.41
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class FancyDressBoxOA implements ObjectInteraction, ItemOnObjectAction {

    private static final Animation ANIM = new Animation(834);

    @Override
    public Object[] getObjects() {
        return new Object[] { 18772, ObjectId.FANCY_DRESS_BOX_18773, 18774, ObjectId.FANCY_DRESS_BOX_18775, ObjectId.FANCY_DRESS_BOX_18776, ObjectId.FANCY_DRESS_BOX_18777 };
    }

    @Override
    public void handleObjectAction(Player player, Construction construction, RoomReference reference, WorldObject object, int optionId, String option) {
        if (option.equals("open")) {
            World.spawnObject(new WorldObject(object.getId() + 1, object.getType(), object.getRotation(), object));
        } else if (option.equals("close")) {
            World.spawnObject(new WorldObject(object.getId() - 1, object.getType(), object.getRotation(), object));
        } else if (option.equals("search")) {
            if (player.getCurrentHouse() != construction) {
                player.sendMessage("Only the owner of the house can use the fancy dress box.");
                return;
            }
            construction.getFancyDressBox().open();
        }
    }

    @Override
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        if (FancyDressData.MAP.get(item.getId()) == null) {
            player.sendMessage("You can't put this in the fancy dress box.");
            return;
        }
        final int id = object.getId();
        if (id == 18772 || id == 18774 || id == 18776) {
            player.sendMessage("You need to open the box first.");
            return;
        }
        if (player.getConstruction().getFancyDressBox().addSet(item.getId())) {
            player.setAnimation(ANIM);
            return;
        }
    }

    @Override
    public Object[] getItems() {
        return null;
    }
}
