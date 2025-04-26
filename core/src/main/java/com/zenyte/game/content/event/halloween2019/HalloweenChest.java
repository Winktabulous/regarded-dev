package com.zenyte.game.content.event.halloween2019;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.ItemChat;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import mgi.custom.halloween.HalloweenObject;

/**
 * @author Kris | 05/11/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class HalloweenChest implements ObjectAction {
    private static final Location[] options = new Location[] {new Location(1752, 4718, 0), new Location(1749, 4708, 0), new Location(1735, 4725, 0), new Location(1751, 4731, 0), new Location(1766, 4715, 0)};
    private static final Animation animation = new Animation(832);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (object.isLocked()) {
            return;
        }
        if (option.equalsIgnoreCase("Open")) {
            if (!player.getInventory().hasFreeSlots()) {
                player.sendMessage("You need some free inventory space to do this.");
                return;
            }
            player.lock(1);
            player.setAnimation(animation);
            object.setLocked(true);
            int bitpacked = player.getAttributeOrDefault("halloween chest positioning", -1);
            if (bitpacked == -1) {
                final IntArrayList ops = new IntArrayList();
                for (int i = 0; i < options.length; i++) {
                    ops.add(i);
                }
                int first = ops.removeInt(Utils.random(ops.size() - 1));
                int second = ops.removeInt(Utils.random(ops.size() - 1));
                player.addAttribute("halloween chest positioning", first | (second << 5));
            }
            final int firstIndex = bitpacked & 31;
            final int secondIndex = (bitpacked >> 5) & 31;
            final Location first = options[firstIndex >= options.length ? 0 : firstIndex];
            final Location second = options[secondIndex >= options.length ? 1 : secondIndex];
            WorldTasksManager.schedule(() -> {
                if (object.matches(first) && !player.containsItem(ItemId.HUMAN_BONES)) {
                    player.getInventory().addOrDrop(new Item(ItemId.HUMAN_BONES));
                    player.getDialogueManager().start(new ItemChat(player, new Item(ItemId.HUMAN_BONES), "You find some human remains in the chest."));
                } else if (object.matches(second) && !player.containsItem(ItemId.GHOSTSPEAK_AMULET)) {
                    player.getInventory().addOrDrop(new Item(ItemId.GHOSTSPEAK_AMULET));
                    player.getDialogueManager().start(new ItemChat(player, new Item(ItemId.GHOSTSPEAK_AMULET), "You find an odd-looking amulet in the chest."));
                } else {
                    player.sendFilteredMessage("You find nothing in this chest.");
                }
                final WorldObject obj = new WorldObject(object);
                obj.setId(HalloweenObject.OPENED_CHEST.getRepackedObject());
                World.spawnObject(obj);
                WorldTasksManager.schedule(() -> {
                    object.setLocked(false);
                    World.spawnObject(object);
                }, 2);
            });
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {HalloweenObject.CHEST.getRepackedObject()};
    }
}
