package com.zenyte.plugins.object;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.UnmodifiableItem;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Toxins;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.entity.player.variables.TickVariable;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntLists;
import mgi.utilities.CollectionUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Kris | 15/09/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SinisterChest implements ObjectAction {
    private static final int OPENED_CHEST = 3368;
    private static final List<Item> loot = Collections.unmodifiableList(Arrays.asList(new UnmodifiableItem(Item.notedId(ItemId.GRIMY_RANARR_WEED), 3), new UnmodifiableItem(Item.notedId(ItemId.GRIMY_HARRALANDER), 2), new UnmodifiableItem(Item.notedId(ItemId.GRIMY_IRIT_LEAF)), new UnmodifiableItem(Item.notedId(ItemId.GRIMY_AVANTOE)), new UnmodifiableItem(Item.notedId(ItemId.GRIMY_KWUARM)), new UnmodifiableItem(Item.notedId(ItemId.GRIMY_TORSTOL))));
    private static final IntList protectiveAntipoisons = IntLists.unmodifiable(new IntArrayList(new int[] {ItemId.ANTIPOISON1, ItemId.ANTIPOISON2, ItemId.ANTIPOISON3, ItemId.ANTIPOISON4, ItemId.ANTIPOISON_MIX1, ItemId.ANTIPOISON_MIX2, ItemId.SUPERANTIPOISON1, ItemId.SUPERANTIPOISON2, ItemId.SUPERANTIPOISON3, ItemId.SUPERANTIPOISON4, ItemId.ANTIPOISON_SUPERMIX1, ItemId.ANTIPOISON_SUPERMIX2, ItemId.ANTIVENOM1, ItemId.ANTIVENOM2, ItemId.ANTIVENOM3, ItemId.ANTIVENOM4, ItemId.ANTIVENOM1_12919, ItemId.ANTIVENOM2_12917, ItemId.ANTIVENOM3_12915, ItemId.ANTIVENOM4_12913, ItemId.ANTIDOTE1, ItemId.ANTIDOTE2, ItemId.ANTIDOTE3, ItemId.ANTIDOTE4, ItemId.ANTIDOTE1_5958, ItemId.ANTIDOTE2_5956, ItemId.ANTIDOTE3_5954, ItemId.ANTIDOTE4_5952, ItemId.ANTIDOTE_MIX1, ItemId.ANTIDOTE_MIX2}));

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        final Inventory inventory = player.getInventory();
        if (!inventory.containsItem(ItemId.SINISTER_KEY, 1)) {
            player.sendMessage("The chest is locked.");
            return;
        }
        player.lock(10);
        inventory.deleteItem(ItemId.SINISTER_KEY, 1);
        object.setLocked(true);
        player.sendFilteredMessage("You unlock the chest with your key...");
        WorldTasksManager.schedule(() -> {
            World.spawnObject(new WorldObject(OPENED_CHEST, object.getType(), object.getRotation(), object));
            player.sendFilteredMessage("A foul gas seeps from the chest.");
            if (player.getVariables().getTime(TickVariable.POISON_IMMUNITY) > 0 || CollectionUtils.findMatching(inventory.getContainer().getItems().values(), value -> value != null && protectiveAntipoisons.contains(value.getId())) != null) {
                player.sendFilteredMessage("Luckily your poison antidote saves you from the harm.");
            } else if (CombatUtilities.isWearingSerpentineHelmet(player)) {
                player.sendFilteredMessage("Luckily your serpentine helmet saves you from the harm.");
            } else {
                final Toxins toxins = player.getToxins();
                if (!toxins.isPoisoned()) {
                    player.sendMessage("You have been poisoned!");
                }
                toxins.applyToxin(Toxins.ToxinType.POISON, 4);
            }
            player.sendFilteredMessage("You find a lot of herbs in the chest...");
            for (final Item item : loot) {
                inventory.addOrDrop(item);
            }
            player.lock(1);
            WorldTasksManager.schedule(() -> {
                object.setLocked(false);
                World.spawnObject(object);
            }, 3);
        }, 1);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {ObjectId.CLOSED_CHEST_377};
    }
}
