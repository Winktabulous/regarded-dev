package com.zenyte.game.content.skills.construction.objects.costumeroom;

import com.zenyte.game.content.skills.construction.Construction;
import com.zenyte.game.content.skills.construction.ObjectInteraction;
import com.zenyte.game.content.skills.construction.RoomReference;
import com.zenyte.game.content.skills.construction.costume.ToyBoxData;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 27. veebr 2018 : 19:39.27
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class ToyBoxOA implements ObjectInteraction, ItemOnObjectAction {

    private static final Animation ANIM = new Animation(834);

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.TOY_BOX, ObjectId.TOY_BOX_18799, ObjectId.TOY_BOX_18800, ObjectId.TOY_BOX_18801, ObjectId.TOY_BOX_18802, ObjectId.TOY_BOX_18803 };
    }

    @Override
    public void handleObjectAction(Player player, Construction construction, RoomReference reference, WorldObject object, int optionId, String option) {
        if (option.equals("open")) {
            World.spawnObject(new WorldObject(object.getId() + 1, object.getType(), object.getRotation(), object));
        } else if (option.equals("close")) {
            World.spawnObject(new WorldObject(object.getId() - 1, object.getType(), object.getRotation(), object));
        } else if (option.equals("search")) {
            if (player.getCurrentHouse() != construction) {
                player.sendMessage("Only the owner of the house can use the toy box.");
                return;
            }
            construction.getToyBox().open(0);
        }
    }

    @Override
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        if (ToyBoxData.MAP.get(item.getId()) == null) {
            player.sendMessage("You can't put this in the toy box.");
            return;
        }
        final int id = object.getId();
        if (id == 18798 || id == 18800 || id == 18802) {
            player.sendMessage("You need to open the toy box first.");
            return;
        }
        if (player.getConstruction().getToyBox().addSet(item.getId())) {
            player.setAnimation(ANIM);
            return;
        }
    }

    @Override
    public Object[] getItems() {
        return null;
    }
}
