package com.zenyte.game.content.skills.construction.objects.combatroom;

import com.zenyte.game.content.skills.construction.Construction;
import com.zenyte.game.content.skills.construction.ObjectInteraction;
import com.zenyte.game.content.skills.construction.RoomReference;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.OptionDialogue;
import mgi.types.config.items.ItemDefinitions;

/**
 * @author Kris | 6. march 2018 : 19:20.28
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class WeaponsRack implements ObjectInteraction {

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.GLOVE_RACK, ObjectId.WEAPONS_RACK, ObjectId.WEAPONS_RACK_13383 };
    }

    @Override
    public void handleObjectAction(Player player, Construction construction, RoomReference reference, WorldObject object, int optionId, String option) {
        final int objectId = object.getId();
        player.getDialogueManager().start(new OptionDialogue(player, "Select an Option", new String[] { "Red boxing gloves.", "Blue boxing gloves.", objectId >= 13382 ? "A wooden sword." : null, objectId >= 13382 ? "A wooden shield." : null, objectId >= 13383 ? "A pugel stick." : null }, new Runnable[] { () -> add(player, 0), () -> add(player, 1), () -> add(player, 2), () -> add(player, 3), () -> add(player, 4) }));
    }

    private void add(final Player player, final int slot) {
        if (!player.getInventory().hasFreeSlots()) {
            player.sendMessage("You need some free inventory space to take this.");
            return;
        }
        final int id = slot == 0 ? 7671 : slot == 1 ? 7673 : slot == 2 ? 7675 : slot == 3 ? 7676 : 7679;
        player.getInventory().addItem(id, 1);
        player.sendMessage("You take " + ItemDefinitions.get(id).getName() + " from the rack.");
    }
}
