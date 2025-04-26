package com.zenyte.plugins.item;

import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.impl.misc.PetRock;
import com.zenyte.plugins.dialogue.followers.PetPetRockD;

/**
 * @author Kris | 27/01/2019 21:41
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class PetRockItem extends ItemPlugin {
    @Override
    public void handle() {
        bind("Interact", (player, item, slotId) -> player.getDialogueManager().start(new PetPetRockD(player)));
        bind("Drop", (player, item, slotId) -> {
            if (player.inArea("Edgeville")) {
                player.sendMessage("You can't drop this item in Edgeville.");
                return;
            }
            final PetRock npc = new PetRock(player, new Location(player.getLocation()));
            npc.spawn();
            WorldTasksManager.schedule(npc::finish, 100);
            player.getInventory().deleteItem(item);
        });
    }

    @Override
    public int[] getItems() {
        return new int[] {3695};
    }
}
