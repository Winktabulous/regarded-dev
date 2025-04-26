package com.zenyte.plugins.object;

import com.zenyte.game.item.Item;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.ItemChat;

/**
 * @author Kris | 09/10/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MarshyJungleVine implements ObjectAction {

    private static final Animation search = new Animation(2094);

    @Override
    public int getDelay() {
        return 1;
    }

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.equalsIgnoreCase("Search")) {
            player.getActionManager().setAction(new Action() {

                @Override
                public boolean start() {
                    if (!player.getInventory().hasFreeSlots()) {
                        player.sendFilteredMessage("You need some free inventory space to search the vine.");
                        return false;
                    }
                    player.setAnimation(search);
                    delay(11);
                    return process();
                }

                @Override
                public boolean process() {
                    return World.exists(object);
                }

                @Override
                public int processWithDelay() {
                    if (Utils.random(1) == 0) {
                        final Item item = new Item(1526);
                        player.getInventory().addOrDrop(item);
                        final WorldObject obj = new WorldObject(object);
                        obj.setId(2576);
                        player.getDialogueManager().start(new ItemChat(player, item, "You find a herb."));
                        World.spawnObject(obj);
                        WorldTasksManager.schedule(() -> World.spawnObject(object), 30);
                        return -1;
                    }
                    player.setAnimation(search);
                    return 10;
                }
            });
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.MARSHY_JUNGLE_VINE };
    }
}
