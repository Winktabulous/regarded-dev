package com.zenyte.plugins.object;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.ItemChat;

/**
 * @author Kris | 06/04/2019 20:40
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class LumbridgeLogsAxeObject implements ObjectAction {

    private static final Item axe = new Item(1351);

    private static final SoundEffect sound = new SoundEffect(2733, 0, 0);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.equalsIgnoreCase("Take-axe")) {
            if (!player.getInventory().hasFreeSlots()) {
                player.getDialogueManager().start(new ItemChat(player, axe, "You need some free space to take the axe."));
                return;
            }
            final WorldObject obj = new WorldObject(object);
            obj.setId(5582);
            World.spawnTemporaryObject(obj, object, 250);
            player.getInventory().addItem(axe);
            player.sendSound(sound);
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.LOGS, ObjectId.LOGS_32536 };
    }
}
