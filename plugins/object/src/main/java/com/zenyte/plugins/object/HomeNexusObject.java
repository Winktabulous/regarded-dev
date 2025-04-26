package com.zenyte.plugins.object;

import com.near_reality.cache.interfaces.teleports.Category;
import com.near_reality.cache.interfaces.teleports.TeleportsList;
import com.zenyte.game.content.skills.afk.AfkBarrierDialogue;
import com.zenyte.game.content.skills.afk.AfkSkilling;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;


public class HomeNexusObject implements ObjectAction {
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {

        if(option.equalsIgnoreCase("Previous-teleport")) {
            if(player.getTeleportsManager().getPreviousDestination() == null) {
                player.sendMessage("You don't have a previous teleport.");
                return;
            }
            player.getTeleportsManager().attemptTeleport(player.getTeleportsManager().getPreviousDestination());
            return;
        }
        final Category category = TeleportsList.getCategories().get("training teleports");
        player.getVarManager().sendVar(261, category.getId());
        player.getTeleportsManager().setSelectedCategory(category);
        player.getTeleportsManager().attemptOpen();
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {50082};
    }
}
