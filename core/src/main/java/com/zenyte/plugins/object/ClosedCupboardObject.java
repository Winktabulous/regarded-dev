package com.zenyte.plugins.object;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.PlainChat;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;

/**
 * @author Corey
 * @since 15/06/2020
 */
public class ClosedCupboardObject implements ObjectAction {
    public static final Int2IntOpenHashMap map = new Int2IntOpenHashMap();
    private static final Object2ObjectOpenHashMap<WorldObject, WorldObject> activeDrawers = new Object2ObjectOpenHashMap<>();
    private static final Animation openAnimation = new Animation(536);
    private static final Animation closeAnimation = new Animation(535);

    static {
        map.put(ObjectId.CUPBOARD_2612, ObjectId.CUPBOARD_2613);
    }

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (option.equalsIgnoreCase("Open")) {
            player.setAnimation(openAnimation);
            player.lock(1);
            player.sendMessage("You open the cupboard.");
            swapObject(object);
        } else if (option.equalsIgnoreCase("Close") || option.equalsIgnoreCase("Shut")) {
            player.setAnimation(closeAnimation);
            player.lock(1);
            player.sendMessage("You close the cupboard.");
            swapObject(object);
        } else if (option.equalsIgnoreCase("Search")) {
            if (object.getPosition().getPositionHash() == new Location(3096, 3269, 1).getPositionHash()) {
                // morgan house in Draynor
                if (!player.getInventory().hasFreeSlots()) {
                    player.getDialogueManager().start(new PlainChat(player, "The cupboard contains garlic, but you " +
                            "don't<br><br>have room to hold it at the moment."));
                    return;
                }
                if (player.getInventory().containsItem(ItemId.GARLIC)) {
                    player.sendMessage("You take a clove of garlic.");
                } else {
                    player.sendMessage("The cupboard contains garlic. You take a clove.");
                }
                player.getInventory().addItem(new Item(ItemId.GARLIC));
                return;
            }
            player.sendMessage("You find nothing.");
        }
    }

    @Override
    public Object[] getObjects() {
        final IntOpenHashSet set = new IntOpenHashSet();
        set.addAll(map.keySet());
        set.addAll(map.values());
        return set.toArray();
    }

    private WorldObject getObject(@NotNull final WorldObject current) {
        WorldObject obj = activeDrawers.get(current);
        if (obj == null) {
            obj = new WorldObject(current);
            obj.setId(map.get(obj.getId()));
        }
        return obj;
    }

    protected void swapObject(@NotNull final WorldObject object) {
        object.setLocked(true);
        final WorldObject obj = getObject(object);
        WorldTasksManager.schedule(() -> {
            World.spawnObject(obj);
            if (activeDrawers.remove(object) == null) {
                activeDrawers.put(obj, object);
                WorldTasksManager.schedule(() -> {
                    final WorldObject matchingObject = activeDrawers.remove(obj);
                    if (matchingObject != object) {
                        return;
                    }
                    World.spawnObject(matchingObject);
                }, 100);
            }
            object.setLocked(false);
        });
    }
}
