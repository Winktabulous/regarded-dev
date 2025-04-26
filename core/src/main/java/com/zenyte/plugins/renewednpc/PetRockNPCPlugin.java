package com.zenyte.plugins.renewednpc;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.npc.impl.misc.PetRock;

/**
 * @author Kris | 27/01/2019 22:08
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class PetRockNPCPlugin extends NPCPlugin {

    @Override
    public void handle() {
        bind("Take", (player, npc) -> {
            if (!(npc instanceof PetRock)) {
                return;
            }
            final PetRock rock = (PetRock) npc;
            if (!player.getUsername().equals(rock.getOwner())) {
                player.sendMessage("You can't take someone else's rock!");
                return;
            }
            if (!player.getInventory().hasFreeSlots()) {
                player.sendMessage("You need some more free space to take the rock.");
                return;
            }
            rock.finish();
            player.getInventory().addItem(new Item(3695, 1));
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.PET_ROCK };
    }
}
