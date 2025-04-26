package com.zenyte.game.content.partyroom;

import com.zenyte.game.GameInterface;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 25/12/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class PartyRoomChestPlugin implements ObjectAction, ItemOnObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (!player.inArea(FaladorPartyRoom.class)) {
            return;
        }
        switch(option) {
            case "Open":
                if (object.isLocked()) {
                    return;
                }
                player.setAnimation(new Animation(832));
                player.sendSound(new SoundEffect(52));
                WorldTasksManager.schedule(() -> {
                    final WorldObject obj = new WorldObject(object);
                    obj.setId(2418);
                    World.spawnObject(obj);
                });
                return;
            case "Shut":
                if (object.isLocked()) {
                    return;
                }
                if (!FaladorPartyRoom.getPartyRoom().getVariables().isChestCloseable()) {
                    player.sendMessage("The chest may not currently be closed.");
                    return;
                }
                player.setAnimation(new Animation(832));
                player.sendSound(new SoundEffect(51));
                WorldTasksManager.schedule(() -> {
                    final WorldObject obj = new WorldObject(object);
                    obj.setId(26193);
                    World.spawnObject(obj);
                });
                return;
            case "Deposit":
                GameInterface.PARTY_DROP_CHEST.open(player);
        }
    }

    @Override
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        if (object.getId() == ObjectId.CHEST_26193) {
            player.sendFilteredMessage("Nothing interesting happens.");
            return;
        }
        if (!player.inArea(FaladorPartyRoom.class)) {
            return;
        }
        GameInterface.PARTY_DROP_CHEST.open(player);
    }

    @Override
    public Object[] getItems() {
        return null;
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.CHEST_2418, ObjectId.CHEST_26193 };
    }
}
